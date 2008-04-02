/*
 * $Id: GroupRenderer.java,v 1.11 2003/11/03 21:44:03 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * Arbitrary grouping "renderer" that simply renders its children
 * recursively in the <code>encodeEnd()</code> method. 
 *
 * @version $Id: GroupRenderer.java,v 1.11 2003/11/03 21:44:03 eburns Exp $
 *  
 */
public class GroupRenderer extends HtmlBasicRenderer {

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

    public GroupRenderer() {
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

    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
	String 
	    style = (String) component.getAttributes().get("style"),
	    styleClass = (String) component.getAttributes().get("styleClass");
	if (null != styleClass || null != style) {
	    ResponseWriter writer = context.getResponseWriter();
	    
	    writer.startElement("span", component);
	    if (null != styleClass) {
		writer.writeAttribute("class", styleClass, "styleClass");
	    }
	    if (null != style) {
		writer.writeAttribute("style", style, "style");
	    }
	    writer.closeStartTag(component);
	}
    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            encodeRecursive(context, kid);
        }
	String 
	    style = (String) component.getAttributes().get("style"),
	    styleClass = (String) component.getAttributes().get("styleClass");
	if (null != styleClass || null != style) {
	    ResponseWriter writer = context.getResponseWriter();

	    if (null != styleClass || null != style) {
		writer.endElement("span");
	    }
	}

    }

    /**
     * Renders nested children of panel by invoking the encode methods
     * on the components. This handles components nested inside
     * panel_group.
     */
    private void encodeRecursive(FacesContext context, UIComponent component)
        throws IOException {
        component.encodeBegin(context);
        if (component.getRendersChildren()) {
            component.encodeChildren(context);
        } else {
            Iterator kids = component.getChildren().iterator();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                encodeRecursive(context, kid);
            }
        }
        component.encodeEnd(context);
    }


}
