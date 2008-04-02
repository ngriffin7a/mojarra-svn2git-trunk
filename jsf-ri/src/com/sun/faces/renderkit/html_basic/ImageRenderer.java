/*
 * $Id: ImageRenderer.java,v 1.22 2003/11/09 05:11:05 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ImageRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *  <B>ImageRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ImageRenderer.java,v 1.22 2003/11/09 05:11:05 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ImageRenderer extends HtmlBasicRenderer {
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

    public ImageRenderer() {
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
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
        ResponseWriter writer = null;
	String styleClass = null;
        
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );
        
	writer.startElement("img", component);
	writer.writeAttribute("id", component.getClientId(context), "clientId");
	writer.writeAttribute("src", src(context, component), "value");

        Util.renderPassThruAttributes(writer, component);
        Util.renderBooleanPassThruAttributes(writer, component);
	if (null != (styleClass = (String) 
		     component.getAttributes().get("styleClass"))) {
	    writer.writeAttribute("class", styleClass, "styleClass");
	}
	String altText = (String)component.getAttributes().get("alt");
	if (altText == null) {
	    writer.writeAttribute("alt", "", "alt");
	}
	writer.endElement("img");
    }

    private String src(FacesContext context, UIComponent component) {

        String value = (String)((UIGraphic)component).getValue();
        if (value == null) {
            value = "";
        }

        if (value == "") {
            try {
                value = getKeyAndLookupInBundle(context, component, "key");
            } catch (java.util.MissingResourceException e) {
                // Do nothing since the absence of a resource is not an
                // error.
            }
        }
                              
        StringBuffer sb = new StringBuffer();
        if (value.startsWith("/")) {
            sb.append(context.getExternalContext().getRequestContextPath());
        }
        sb.append(value);
        return (sb.toString());
    }
    
    // The testcase for this class is TestRenderers_2.java 

} // end of class ImageRenderer


