/*
 * $Id: Input_SecretTag.java,v 1.3 2002/09/23 20:34:17 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Input_SecretTag.java

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

/**
 *
 * @version $Id: Input_SecretTag.java,v 1.3 2002/09/23 20:34:17 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Input_SecretTag extends Input_TextTag
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
    private String converter = null;

// Attribute Instance Variables

    protected String redisplay = null;

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public Input_SecretTag()
{
    super();
}

//
// Class methods
//
    public String getRedisplay() {
        return redisplay;
    }

    public void setRedisplay(String newRedisplay) {
        redisplay = newRedisplay;
    }
// 
// Accessors
//
    public void setConverter(String converter) {
        this.converter = converter;
    }

//
// General Methods
//

    public String getLocalRendererType() { return "SecretRenderer"; }

    public UIComponent createComponent() {
        return (new UIInput());
    }

    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        UIInput input = (UIInput) component;

        if (null == input.getAttribute("redisplay")) {
            input.setAttribute("redisplay", getRedisplay());
        }
        if ((converter != null) &&
            (component.getAttribute("converter") == null)) {
            component.setAttribute("converter", converter);
        }
    }


//
// Methods from TagSupport
// 


} // end of class Input_SecretTag
