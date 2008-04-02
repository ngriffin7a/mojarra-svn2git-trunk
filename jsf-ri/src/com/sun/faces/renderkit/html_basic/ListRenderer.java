/*
 * $Id: ListRenderer.java,v 1.2 2002/09/11 20:02:25 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.renderkit.html_basic;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.faces.FacesException;


import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import com.sun.faces.util.Util;
/**
 *
 *  Render a <code>UIPanel</code> component in the proposed "List" style.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ListRenderer.java,v 1.2 2002/09/11 20:02:25 edburns Exp $
 *  
 */

public class ListRenderer extends HtmlBasicRenderer {
    
    //
    // Protected Constants
    //

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

    public ListRenderer() {
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
            throw new NullPointerException(
                Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }    
        return (componentType.equals(UIPanel.TYPE));
    }

    public boolean decode(FacesContext context, UIComponent component)
        throws IOException {
	return true;
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
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
        writer.write(Util.renderPassthruAttributes(context, component));
        writer.write(">\n");
    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
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
        int count = component.getChildCount();
        int first = ((headerClass == null) ? 0 : 1);
        int last = ((footerClass == null) ? (count - 1) : (count - 2));
        ResponseWriter writer = context.getResponseWriter();

        // Process the table header (if any)
        if (headerClass != null) {
            writer.write("<tr>\n");
            Iterator kids = component.getChild(0).getChildren();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                writer.write("<td class=\"");
                writer.write(headerClass);
                writer.write("\">");
                encodeRecursive(context, kid);
                writer.write("</td>\n");
            }
            writer.write("</tr>\n");
        }

        // Process each grouping of data items to be processed
        for (int i = first; i <= last; i++) {

            UIComponent group = component.getChild(i);
            String var = (String) group.getAttribute("var");
            
            Iterator rows = getIterator(context, group);
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
        }

        // Process the table footer (if any)
        if (footerClass != null) {
            writer.write("<tr>\n");
            Iterator kids = component.getChild(count - 1).getChildren();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                writer.write("<td class=\"");
                writer.write(footerClass);
                writer.write("\">");
                encodeRecursive(context, kid);
                writer.write("</td>\n");
            }
            writer.write("</tr>\n");
        }

    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
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
    private Iterator getIterator(FacesContext context, UIComponent component) {

        // Process the current value of this component appropriately
        Object value = component.currentValue(context);
        if (value == null) {
            return (Collections.EMPTY_LIST.iterator());
        } else if (value instanceof Collection) {
            return (((Collection) value).iterator());
        } else if (value instanceof Iterator) {
            return ((Iterator) value);
        } else if (value instanceof Map) {
            return (((Map) value).entrySet().iterator());
        } else if (value.getClass().isArray()) {
            try {
                // Array of objects does not require copying
                return (Arrays.asList((Object[]) value).iterator());
            } catch (ClassCastException e) {
                // Make a copy for arrays of primitives
                int length = Array.getLength(value);
                ArrayList list = new ArrayList(length);
                for (int i = 0; i < length; i++) {
                    list.add(Array.get(value, i));
                }
                return (list.iterator());
            }
        } else {
            // Create a single-item list
            ArrayList list = new ArrayList(1);
            list.add(value);
            return (list.iterator());
        }
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
}
