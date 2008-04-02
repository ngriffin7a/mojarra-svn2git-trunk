/*
 * $Id: SecretRenderer.java,v 1.34 2002/09/17 20:07:58 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SecretRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>SecretRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SecretRenderer.java,v 1.34 2002/09/17 20:07:58 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SecretRenderer extends HtmlBasicRenderer {
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

    public SecretRenderer() {
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
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        return (componentType.equals(UIInput.TYPE));
    }

    public void encodeBegin(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

   protected void getEndTextToRender(FacesContext context, UIComponent component,
            String currentValue, StringBuffer buffer ) {
        String inputClass = null;
        
        String redisplay = (String)component.getAttribute("redisplay");
        if (redisplay == null || !redisplay.equals("true")) {
            currentValue = "";
        }

        buffer.append("<input type=\"password\"");
        buffer.append(" name=\"");
        buffer.append(component.getCompoundId());
        buffer.append("\"");

        // render default text specified
        if (currentValue != null) {
            buffer.append(" value=\"");
            buffer.append(currentValue);
            buffer.append("\"");
        }
        buffer.append(Util.renderPassthruAttributes(context, component));
        buffer.append(Util.renderBooleanPassthruAttributes(context, component));
	if (null != (inputClass = (String) 
		     component.getAttribute("inputClass"))) {
	    buffer.append(" class=\"" + inputClass + "\" ");
	}
	
        buffer.append(">");         
    }

} // end of class SecretRenderer
