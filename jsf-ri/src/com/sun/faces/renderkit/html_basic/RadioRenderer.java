/*
 * $Id: RadioRenderer.java,v 1.15 2002/05/29 20:44:01 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RadioRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import javax.faces.component.UIComponent;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>RadioRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: RadioRenderer.java,v 1.15 2002/05/29 20:44:01 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class RadioRenderer extends Renderer {
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
    public AttributeDescriptor getAttributeDescriptor(
        UIComponent component, String name) {
        return null;
    }

    public AttributeDescriptor getAttributeDescriptor(
        String componentType, String name) {
        return null;
    }

    public Iterator getAttributeNames(UIComponent component) {
        return null;
    }

    public Iterator getAttributeNames(String componentType) {
        return null;
    }

    public boolean supportsComponentType(UIComponent c) {
        return false;
    }

    public boolean supportsComponentType(String componentType) {
        return false;
    }

    public void decode(FacesContext context, UIComponent component) {

    }

    public void encodeBegin(FacesContext context, UIComponent component) {

    }

    public void encodeChildren(FacesContext context, UIComponent component) {

    }

    public void encodeEnd(FacesContext context, UIComponent component) {

    }

} // end of class RadioRenderer
