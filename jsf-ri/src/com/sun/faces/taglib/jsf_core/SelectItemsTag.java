/*
 * $Id: SelectItemsTag.java,v 1.5 2004/02/04 23:42:09 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import com.sun.faces.util.Util;
import com.sun.faces.taglib.BaseComponentTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>selectitems</code> custom tag.
 */

public class SelectItemsTag extends BaseComponentTag
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
    public String getRendererType() { 
        return null; 
    }
    public String getComponentType() { 
        return "javax.faces.SelectItems"; 
    }

    //
    // Methods from BaseComponentTag
    //
    protected void setProperties(UIComponent component) {
	super.setProperties(component);
	UISelectItems selectItems = (UISelectItems) component;
	
        if (null != value) {
            if (isValueReference(value)) {
                component.setValueBinding("value",
                                          Util.getValueBinding(value));
             } else {
                 selectItems.setValue(value);
             }
	}
    }

} // end of class SelectItemsTag
