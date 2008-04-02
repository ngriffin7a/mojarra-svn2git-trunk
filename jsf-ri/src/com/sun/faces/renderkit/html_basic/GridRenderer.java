/*
 * $Id: GridRenderer.java,v 1.3 2002/09/11 20:02:23 edburns Exp $
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

import com.sun.faces.util.Util;
/**
 *
 *  Render a <code>UIPanel</code> component in the proposed "Grid" style.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: GridRenderer.java,v 1.3 2002/09/11 20:02:23 edburns Exp $
 *  
 */

public class GridRenderer extends HtmlBasicRenderer {
    
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

    public GridRenderer() {
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

        String footerClass = (String) component.getAttribute("footerClass");
        String headerClass = (String) component.getAttribute("headerClass");
        int columns = getColumns(component);
        String columnClasses[] = getColumnClasses(component);
        int columnStyle = 0;
        int columnStyles = columnClasses.length;
        String rowClasses[] = getRowClasses(component);
        int rowStyle = 0;
        int rowStyles = rowClasses.length;
        int count = component.getChildCount();
        int first = ((headerClass == null) ? 0 : 1);
        int last = ((footerClass == null) ? (count - 1) : (count - 2));
        boolean open = false;
        ResponseWriter writer = context.getResponseWriter();
        if (headerClass != null) {
            writer.write("<tr><td class=\"");
            writer.write(headerClass);
            writer.write("\" colspan=\"");
            writer.write("" + columns);
            writer.write("\">");
            encodeRecursive(context, component.getChild(0));
            writer.write("</td></tr>\n");
        }
        for (int i = first; i <= last; i++) {
            if (((i - first) % columns) == 0) {
                if (open) {
                    writer.write("</tr>\n");
                    open = false;
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
                open = true;
                columnStyle = 0;
            }
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
            UIComponent child = component.getChild(i);
            encodeRecursive(context, child);
            writer.write("</td>\n");
        }
        if (open) {
            writer.write("</tr>\n");
        }
        if (footerClass != null) {
            writer.write("<tr><td class=\"");
            writer.write(footerClass);
            writer.write("\" colspan=\"");
            writer.write("" + columns);
            writer.write("\">");
            encodeRecursive(context, component.getChild(count - 1));
            writer.write("</td></tr>\n");
        }
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

       if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        // Render the ending of this panel
        ResponseWriter writer = context.getResponseWriter();
        writer.write("</table>\n");
    }

    /**
     * Renders nested children of panel by invoking the encode methods
     * on the components. This handles components nested inside
     * panel_group, panel_grid tags.
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
     * Returns number of columns of the grid converting the value
     * specified to int if necessary.
     */
    private int getColumns(UIComponent component) {
        int count;
        Object value = component.getAttribute("columns");
        if ((value != null) && (value instanceof Integer)) {
            count = ((Integer) value).intValue();
        } else {
            count = 2;
        }
        if (count < 1) {
            count = 1;
        }
        return (count);
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
