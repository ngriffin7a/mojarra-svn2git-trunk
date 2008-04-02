/*
 * $Id: Output_DateTag.java,v 1.9 2003/07/09 19:04:24 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Output_DateTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;

/**
 *
 * @version $Id: Output_DateTag.java,v 1.9 2003/07/09 19:04:24 rlubke Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Output_DateTag extends com.sun.faces.taglib.FacesTag
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

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public Output_DateTag()
{
    super();
}

//
// Class methods
//

// 
// Accessors
//


//
// General Methods
//
    public String getLocalRendererType() { return "Date"; }
    public String getComponentType() { return "Output"; }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
        UIOutput uiOutput = (UIOutput)component;
	// if component has non null value, do not call setValue().
        if (null != getValue()) {
	    uiOutput.setValue(getValue());
	}
    }


//
// Methods from TagSupport
//
    


} // end of class Output_DateTag
