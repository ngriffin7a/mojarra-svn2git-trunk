/*
 * $Id: ResultSetRenderer.java,v 1.1 2003/02/14 20:36:18 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package components.renderkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.faces.FacesException;


import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIPanel;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.FactoryFinder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *  Render a <code>UIPanel</code> component in the proposed "List" style.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ResultSetRenderer.java,v 1.1 2003/02/14 20:36:18 eburns Exp $
 *  
 */

public class ResultSetRenderer extends BaseRenderer {
    
    //
    // Protected Constants
    //

    public static final String SCROLLER_COMPONENT = "ResultSetControls";

    public static final String NORTH = "NORTH";
    public static final String SOUTH = "SOUTH";
    public static final String EAST = "EAST";
    public static final String WEST = "WEST";
    public static final String BOTH = "BOTH";

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public ResultSetRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //

    public boolean supportsComponentType(String componentType) {
        if ( componentType == null ) {
            throw new NullPointerException();
        }    
        return (componentType.equals(UIPanel.TYPE));
    }

    public void decode(FacesContext context, UIComponent component)
        throws IOException {
	return;
    }

    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException();
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }

        String panelClass = (String) component.getAttribute("panelClass");
        
        // Render the beginning of this panel
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<table");
        if (panelClass != null) {
            writer.write(" class=\"");
            writer.write(panelClass);
            writer.write("\"");
        }
        writer.write(com.sun.faces.util.Util.renderPassthruAttributes(context, component));
        writer.write(">\n");
    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException();
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
	String controlsLocation = 
	    (String) component.getAttribute("scrollerControlsLocation");

	// attach our hack Facet on the initial render, but not on
	// postback
	ResultSetControls scroller = 
	    (ResultSetControls) component.getFacet(SCROLLER_COMPONENT);

	if (null == scroller) {
	    component.addFacet(SCROLLER_COMPONENT, scroller = 
			       newResultSetScroller(component,
						    (UIComponent) 
						    component.getChild(0)));
	}

        // Set up variables we will need
        // PENDING (visvan) is it possible to use hardcoded column headings
        // without using stylesheets ?
        String footerClass = (String) component.getAttribute("footerClass");
        String headerClass = (String) component.getAttribute("headerClass");
        String columnClasses[] = getColumnClasses(component);
        int columnStyle = 0;
        int columnStyles = columnClasses.length;
        String rowClasses[] = getRowClasses(component);
        int rowStyle = 0;
        int rowStyles = rowClasses.length;
        ResponseWriter writer = context.getResponseWriter();
	UIComponent facet = null;
	Iterator kids = null;

	// output the result set scroller, if necessary.
	if (null != controlsLocation &&
	    (controlsLocation.equalsIgnoreCase(NORTH) || 
	     controlsLocation.equalsIgnoreCase(BOTH))) {
	    scroller.encodeEnd(context);
	}

        // Process the table header (if any)
        if (null != (facet = component.getFacet("header"))) {
            writer.write("<tr>\n");
	    // If the header has kids, render them recursively
	    if (null != (kids = facet.getChildren())) {
		while (kids.hasNext()) {
		    UIComponent kid = (UIComponent) kids.next();
		    // write out the table header
		    if (null != headerClass) {
			writer.write("<th class=\"");
			writer.write(headerClass);
			writer.write("\">");
		    }
		    else {
			writer.write("<th>\n");
		    }
		    // encode the children
		    encodeRecursive(context, kid);
		    // write out the table footer
		    writer.write("</th>\n");
		}
	    }
	    else {
		// if the header has no kids, just render it
		facet.encodeBegin(context);
		if (facet.getRendersChildren()) {
		    facet.encodeChildren(context);
		}
		facet.encodeEnd(context);
            }
            writer.write("</tr>\n");
        }
	
	// Make sure we have only one child
	if (1 < component.getChildCount()) {
	    throw new IOException("ResultSetRenderer only prepared for one child");
	}
	
	UIComponent group = (UIComponent) component.getChild(0);
	String var = (String) group.getAttribute("var");
	
	Iterator rows = getIterator(context, scroller, group);
	while (rows.hasNext()) {
	    
	    // Start the next row to be rendered
	    Object row = rows.next(); // Model data from the list
	    if (var != null) {
		// set model bean in request scope. nested components
		// will use this to get their values.
		context.getServletRequest().setAttribute(var, row);
	    }
	    writer.write("<tr");
	    if (rowStyles > 0) {
		writer.write(" class=\"");
		writer.write(rowClasses[rowStyle++]);
		writer.write("\"");
		if (rowStyle >= rowStyles) {
		    rowStyle = 0;
		}
	    }
	    writer.write(">\n");
	    
	    // Process each column to be rendered
	    columnStyle = 0;
	    Iterator columns = group.getChildren();
	    // number of columns will equal the total number of elements
	    // in the iterator. No of rows will be equal to the number of
	    // rows in the list bean.
	    while (columns.hasNext()) {
		UIComponent column = (UIComponent) columns.next();
		writer.write("<td");
		if (columnStyles > 0) {
		    writer.write(" class=\"");
		    writer.write(columnClasses[columnStyle++]);
		    writer.write("\"");
		    if (columnStyle >= columnStyles) {
			columnStyle = 0;
		    }
		}
		writer.write(">");
		encodeRecursive(context, column);
		writer.write("</td>\n");
	    }
	    
	    // Finish the row that was just rendered
	    writer.write("</tr>\n");
	    if (var != null) {
		context.getServletRequest().removeAttribute(var);
	    }
	}

        // Process the table footer (if any)
        if (null != (facet = component.getFacet("footer"))) {
            writer.write("<tr>\n");
	    // If the footer has kids, render them recursively
	    if (null != (kids = facet.getChildren())) {
		while (kids.hasNext()) {
		    UIComponent kid = (UIComponent) kids.next();
		    // write out the table footer
		    if (null != footerClass) {
			writer.write("<td class=\"");
			writer.write(footerClass);
			writer.write("\">");
		    }
		    else {
			writer.write("<th>\n");
		    }
		    // encode the children
		    encodeRecursive(context, kid);
		    // write out the table footer
		    writer.write("</th>\n");
		}
	    }
	    else {
		// if the footer has no kids, just render it
		facet.encodeBegin(context);
		if (facet.getRendersChildren()) {
		    facet.encodeChildren(context);
		}
		facet.encodeEnd(context);
            }
            writer.write("</tr>\n");
        }
	
	if (null == controlsLocation ||
	    (controlsLocation.equalsIgnoreCase(SOUTH) || 
	     controlsLocation.equalsIgnoreCase(BOTH))) {
	    scroller.encodeEnd(context);
	}

    }

    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        // Render the ending of this panel
        ResponseWriter writer = context.getResponseWriter();
        writer.write("</table>\n");
    }


    /**
     * Renders nested children of panel by invoking the encode methods
     * on the components. This handles components nested inside
     * panel_data, panel_group tags.
     */
    private void encodeRecursive(FacesContext context, UIComponent component)
        throws IOException {
        
        component.encodeBegin(context);
        if (component.getRendersChildren()) {
            component.encodeChildren(context);
        } else {
            Iterator kids = component.getChildren();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                encodeRecursive(context, kid);
            }
        }
        component.encodeEnd(context);
    }

    /**
    * Returns an array of stylesheet classes to be applied to
    * each column in the list in the order specified. Every column may or
    * may not have a stylesheet
    */
    private String[] getColumnClasses(UIComponent component) {
        String values = (String) component.getAttribute("columnClasses");
        if (values == null) {
            return (new String[0]);
        }
        values = values.trim();
        ArrayList list = new ArrayList();
        while (values.length() > 0) {
            int comma = values.indexOf(",");
            if (comma >= 0) {
                list.add(values.substring(0, comma).trim());
                values = values.substring(comma + 1);
            } else {
                list.add(values.trim());
                values = "";
            }
        }
        String results[] = new String[list.size()];
        return ((String[]) list.toArray(results));
    }

    /**
     * Returns an iterator over data collection to be rendered. Each item
     * in the iterator will correspond to a row in the list.
     */
    private Iterator getIterator(FacesContext context, 
				 ResultSetControls scroller,
				 UIComponent group) {

        Object value = group.currentValue(context);
        if (value == null || (!(value instanceof List))) {
            return (Collections.EMPTY_LIST.iterator());
        } 
	// value is a List.  
	List list = (List) value;

	// return an Iterator corresponding to the rowsPerPage, and the
	// currentPage.
	int 
	    start = 0,
	    end = 0,
	    size = list.size(),
	    currentPage = scroller.getCurrentPage(),
	    rowsPerPage = scroller.getRowsPerPage();
	
	start = (currentPage - 1) * rowsPerPage;
	end = start + rowsPerPage;
	// handle the case where we're on the last page
	if (size < end) {
	    end = size;
	}
	return list.subList(start, end).iterator();
    }

   /**
    * Returns an array of stylesheet classes to be applied to
    * each row in the list in the order specified. Every row may or 
    * may not have a stylesheet
    */
    private String[] getRowClasses(UIComponent component) {
        String values = (String) component.getAttribute("rowClasses");
        if (values == null) {
            return (new String[0]);
        }
        values = values.trim();
        ArrayList list = new ArrayList();
        while (values.length() > 0) {
            int comma = values.indexOf(",");
            if (comma >= 0) {
                list.add(values.substring(0, comma).trim());
                values = values.substring(comma + 1);
            } else {
                list.add(values.trim());
                values = "";
            }
        }
        String results[] = new String[list.size()];
        return ((String[]) list.toArray(results));
    }

    //
    // Methods to be overridden in the subclass
    // 

    public ResultSetControls newResultSetScroller(UIComponent newPanel,
						  UIComponent newData) {
	return new ResultSetControls(newPanel, newData, this);
    }
}

