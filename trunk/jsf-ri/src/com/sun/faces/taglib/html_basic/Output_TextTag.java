/*
 * $Id: Output_TextTag.java,v 1.39 2003/02/04 01:17:43 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Output_TextTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;

import com.sun.faces.taglib.FacesTag;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: Output_TextTag.java,v 1.39 2003/02/04 01:17:43 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Output_TextTag extends FacesTag
{
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

    private String converter = null;

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public Output_TextTag()
{
    super();
}

//
// Class methods
//

// 
// Accessors
//
    public void setConverter(String converter) {
        this.converter = converter;
    }

//
// General Methods
//

    public String getLocalRendererType() { return "Text"; }

    public UIComponent createComponent() {
        return (new UIOutput());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIOutput textOutput = (UIOutput) component;
	// if component has non null value, do not call setValue().
	if (null == textOutput.getValue()) {
	    textOutput.setValue(getValue());
	}
        if ((converter != null) &&
            (component.getAttribute(UIComponent.CONVERTER_ATTR) == null)) {
            component.setAttribute(UIComponent.CONVERTER_ATTR, converter);
        }
    }
    
//
// Methods from TagSupport
// 


} // end of class Output_TextTag
