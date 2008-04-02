/*
 * $Id: TextareaRenderer.java,v 1.5 2004/01/31 00:00:47 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TextareaRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.util.Util;



import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 *  <B>TextareaRenderer</B> is a class that handles the rendering of the input
 *  TextAreaTag
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextareaRenderer.java,v 1.5 2004/01/31 00:00:47 craigmcc Exp $
 * 
 */

public class TextareaRenderer extends HtmlBasicInputRenderer {
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

    public TextareaRenderer() {
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

    protected void getEndTextToRender(FacesContext context, UIComponent component,
            String currentValue) throws IOException {
                
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert(writer != null );	

	writer.startElement("textarea", component);
	writeIdAttributeIfNecessary(context, writer, component);
	writer.writeAttribute("name", component.getClientId(context), "clientId");
        // Deal with "rows" specially since it is not passthru on <h:dataTable/>
        Object rows = component.getAttributes().get("rows");
        if (rows != null) {
            if (!(rows instanceof String)) {
                rows = rows.toString();
            }
            writer.writeAttribute("rows", rows, "rows");
        }

        Util.renderPassThruAttributes(writer, component);
        Util.renderBooleanPassThruAttributes(writer, component);

        // render default text specified
        if ( currentValue != null ) {
	    writer.writeText(currentValue, "value");
        }

	writer.endElement("textarea");
    }

} // end of class TextareaRenderer
