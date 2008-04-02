/*
 * $Id: Output_ErrorsTag.java,v 1.13 2003/09/05 14:34:47 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.FacesTag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

/**
 *  <B>Output_ErrorsTag</B> is a convenience tag allowing content
 *  authors flexible error reporting.  See the tld for
 *  attribute description.
 */

public class Output_ErrorsTag extends FacesTag {

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

    protected String forValue = null;
    protected String color = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Output_ErrorsTag() {
        super();
    }

    //
    // Class methods
    //

    //
    // Accessors
    //

    public void setFor(String newForValue)
    {
        forValue = newForValue;
    }

    public void setColor(String newColor) {
        color = newColor;
    }

    //
    // General Methods
    //

    public String getRendererType() {
        return "Errors"; 
    }
    public String getComponentType() {
        return "Output"; 
    }

    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        UIOutput output = (UIOutput) component;
        if (null != forValue) {
            component.setAttribute("for", forValue);
        }
        if (null != color) {
            component.setAttribute("color", color);
        }
    }
    //
    // Methods from TagSupport
    //


} // end of class Output_ErrorsTag
