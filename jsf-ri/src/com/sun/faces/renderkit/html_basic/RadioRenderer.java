/*
 * $Id: RadioRenderer.java,v 1.27 2002/08/21 19:26:03 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RadioRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.component.UIComponent;
import javax.faces.FacesException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.component.UISelectOne;
import javax.faces.component.SelectItem;

import com.sun.faces.util.Util;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConversionException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;


/**
 *
 *  <B>RadioRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: RadioRenderer.java,v 1.27 2002/08/21 19:26:03 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class RadioRenderer extends HtmlBasicRenderer {
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

    public RadioRenderer() {
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
        return (componentType.equals(UISelectOne.TYPE));
    }

    public void decode(FacesContext context, UIComponent component) 
           throws IOException {
        Object convertedValue = null;
        Class modelType = null;
        
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
       
        String compoundId = component.getCompoundId();
        Assert.assert_it(compoundId != null );
        
        String newValue = context.getServletRequest().getParameter(compoundId);
        // currently we assume the model type to be of type string or 
        // convertible to string and localised by the application.
        component.setValue(newValue);
        component.setValid(true);
    }

    public void encodeBegin(FacesContext context, UIComponent component) 
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException("context or component argument is null.");
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException 
    {
        String currentValue = null;
        ResponseWriter writer = null;
        UISelectOne uiSelectOne = null;
	String alignStr = null;
	String borderStr = null;
	boolean alignVertical = false;
	int border = 0;

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        Object currentObj = component.currentValue(context);
        if ( currentObj != null) {
            if (currentObj instanceof String) {
                currentValue = (String)currentObj;
            } else {
                currentValue = currentObj.toString();
            }
        }
        if (currentValue == null) {
            currentValue = "";
        }
      
        // cast component to UISelectOne.
        if ( supportsComponentType(component)) {
            uiSelectOne = (UISelectOne) component;
        }    

        Iterator items = Util.getSelectItems(context, uiSelectOne);
	SelectItem curItem = null;
        if ( items == null ) {
            return;
        }
        
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );
	
	if (null != (alignStr = (String) uiSelectOne.getAttribute("layout"))) {
	    alignVertical = alignStr.equalsIgnoreCase("vertical") ? 
		true : false;
	}
	if (null != (borderStr = (String) uiSelectOne.getAttribute("border"))){
	    try {
		border = Integer.valueOf(borderStr).intValue();
	    }
	    catch (Throwable e) {
		border = 0;
	    }
	}
	
	writer.write("<table border=\"" + border + "\">\n");
	if (!alignVertical) {
	    writer.write("\t<tr>\n");
	}
        // PENDING (visvan) handle nested labels
	while (items.hasNext()) {
	    curItem = (SelectItem) items.next();
	    if (alignVertical) {
		writer.write("\t<tr>\n");
	    }
            writer.write("<td><input type=\"radio\"");
            if (null != curItem.getValue() &&
		curItem.getValue().equals(currentValue)){
                writer.write(" checked");
            }
            writer.write(" name=\"");
            writer.write(uiSelectOne.getCompoundId());
            writer.write("\" value=\"");
            writer.write((String) curItem.getValue());
            writer.write("\"");
            // render HTML 4.0 attributes if any
            // PENDING (visvan) render HTML 4.0 attributes for each checkbox tag.
            // can't do this right now,  because SelectItem doesn't store
            // attributes, UISelectItem does.
           // writer.write(Util.renderPassthruAttributes(context, component));
	   // writer.write(Util.renderBooleanPassthruAttributes(context, component));
            writer.write(">");
            
            String itemLabel = curItem.getLabel();
            if (itemLabel != null) {
                writer.write(" ");
                writer.write(itemLabel);
            }
            writer.write("</td>\n");
	    if (alignVertical) {
		writer.write("\t<tr>\n");
	    }
        }

	if (!alignVertical) {
	    writer.write("\t</tr>\n");
	}
	writer.write("</table>");


    }

} // end of class RadioRenderer
