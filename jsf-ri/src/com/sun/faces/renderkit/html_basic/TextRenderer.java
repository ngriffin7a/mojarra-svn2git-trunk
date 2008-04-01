/*
 * $Id: TextRenderer.java,v 1.12 2001/12/20 22:26:40 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TextRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.beans.PropertyDescriptor;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.WOutput;
import javax.faces.WComponent;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>TextRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextRenderer.java,v 1.12 2001/12/20 22:26:40 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TextRenderer extends Object implements Renderer
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

    public TextRenderer()
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


    public void renderStart(RenderContext rc, WComponent c )
        throws IOException, FacesException { 

        ParameterCheck.nonNull(rc);
        ParameterCheck.nonNull(c);

        WOutput label = null;
        if ( supportsType(c)) {
            label = (WOutput) c;
        } else {
            throw new FacesException("Invalid component type. Expected WOutput");
        }
        String text = (String) label.getValue(rc);
        if ( text != null ) { 
            OutputMethod outputMethod = rc.getOutputMethod();
            Assert.assert_it(outputMethod != null );
            StringBuffer out = new StringBuffer();
            out.append(text);
            outputMethod.writeText(out.toString());
            outputMethod.flush();
        }
        return;
    }

    public void renderChildren(RenderContext rc, WComponent c) 
            throws IOException {
        return;
    }

    public void renderComplete(RenderContext rc, WComponent c) 
            throws IOException,FacesException {
        return;
    }

    public boolean supportsType(String componentType) {
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals(Constants.REF_WOUTPUT)) {
            supports = true;
        }
        return supports;
    }
    
    public boolean supportsType(WComponent c) {
        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof WOutput ) {
            supports = true;
        }
        return supports;
    }


} // end of class TextRenderer
