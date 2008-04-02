/*
 * $Id: SelectItemsTag.java,v 1.8 2003/07/29 18:23:30 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import com.sun.faces.taglib.FacesTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>selectitems</code> custom tag.
 */

public class SelectItemsTag extends FacesTag
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

    public SelectItemsTag()
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
    public String getLocalRendererType() { 
        return null; 
    }
    public String getComponentType() { 
        return "SelectItems"; 
    }


    //
    // Methods from FacesTag
    //
    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
    }

} // end of class SelectItemsTag
