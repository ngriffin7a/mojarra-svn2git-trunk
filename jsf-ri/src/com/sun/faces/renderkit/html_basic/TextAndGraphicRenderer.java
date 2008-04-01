/*
 * $Id: TextAndGraphicRenderer.java,v 1.7 2001/11/29 01:54:35 rogerk Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

// TextAndGraphicRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.beans.PropertyDescriptor;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.WComponent;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>TextAndGraphicRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextAndGraphicRenderer.java,v 1.7 2001/11/29 01:54:35 rogerk Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TextAndGraphicRenderer extends Object implements Renderer
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

public TextAndGraphicRenderer()
{
    super();
    // ParameterCheck.nonNull();
    this.init();
}

protected void init()
{
    // super.init();
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

public boolean supportsType(WComponent c) {
    return false;
}

public boolean supportsType(String componentType) {
    return false;
}
    public Iterator getSupportedAttributeNames(String componentType) throws FacesException {
        return null;
    }

    public Iterator getSupportedAttributes(String componentType) throws FacesException {
	return null;
    }

    public PropertyDescriptor getAttributeDescriptor(String attributeName)
	throws FacesException {
	return null;
    }


public void renderStart(RenderContext rc, WComponent c) 
    throws IOException, FacesException {
    return;
}

public void renderChildren(RenderContext rc, WComponent c) throws IOException {
    return;
}

public void renderComplete(RenderContext rc, WComponent c) throws IOException {
    return;
}

public boolean getCanRenderChildren(RenderContext rc, WComponent c) {
    return false;
}

} // end of class TextAndGraphicRenderer
