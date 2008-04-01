/*
 * $Id: Output_TimeTag.java,v 1.2 2002/09/03 18:42:31 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Output_TimeTag.java

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
 * @version $Id: Output_TimeTag.java,v 1.2 2002/09/03 18:42:31 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Output_TimeTag extends com.sun.faces.taglib.FacesTag
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

public Output_TimeTag()
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
    public String getLocalRendererType() { return "TimeRenderer"; }

    public UIComponent createComponent() {
        return (new UIOutput());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	
        // if model property represented by this component has non null value, 
        // do not call setValue().
	if (null == component.currentValue(context) && null != getValue()) {
	    component.setValue(getValue());
	}
    }


//
// Methods from TagSupport
//
    


} // end of class Output_TimeTag
