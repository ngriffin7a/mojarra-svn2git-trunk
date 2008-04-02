/*
 * $Id: Graphic_ImageTag.java,v 1.5 2003/04/29 20:52:06 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Graphic_ImageTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.FacesTag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;


/**
 *
 * @version $Id: Graphic_ImageTag.java,v 1.5 2003/04/29 20:52:06 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Graphic_ImageTag extends FacesTag 
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

    protected String url = null;


// Relationship Instance Variables

//
// Constructors and Initializers    
//

public Graphic_ImageTag()
{
    super();
}

//
// Class methods
//

    public String getUrl() {
        return url;
    }

    public void setUrl(String newUrl) {
        url = newUrl;
    }

// 
// Accessors
//

//
// General Methods
//

    public String getLocalRendererType() { return "Image"; }
    public String getComponentType() { return "Graphic"; }

    public UIComponent createComponent() {
        return (new UIGraphic());
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIGraphic graphic = (UIGraphic) component;
	
	if (null == graphic.getURL()) {
	    graphic.setURL(getUrl());
	}
        if ((usemap != null) &&
            (component.getAttribute("usemap") == null)) {
            component.setAttribute("usemap", getUsemap());
        }
        if ((ismap != null) &&
            (component.getAttribute("ismap") == null)) {
            component.setAttribute("ismap", getIsmap());
        }
    }
//
// Methods from TagSupport
// 


} // end of class Graphic_ImageTag
