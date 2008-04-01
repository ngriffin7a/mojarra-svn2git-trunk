/*
 * $Id: OptionListRenderer.java,v 1.2 2001/12/20 22:26:40 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// OptionListRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.beans.PropertyDescriptor;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.WComponent;
import javax.faces.WSelectOne;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>OptionListRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: OptionListRenderer.java,v 1.2 2001/12/20 22:26:40 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class OptionListRenderer extends Object implements Renderer {
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

    public OptionListRenderer() {
        super();
        // ParameterCheck.nonNull();
        this.init();
    }

    protected void init() {
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
        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof WSelectOne ) {
            supports = true;
        }
        return supports;
    }

    public boolean supportsType(String componentType) {
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals(Constants.REF_WSELECTONE)) {
            supports = true;
        }
        return supports;
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

        ParameterCheck.nonNull(rc);
        ParameterCheck.nonNull(c);
 
        WSelectOne wSelectOne = null;
        if ( supportsType(c)) {
            wSelectOne = (WSelectOne) c;
        } else {
            throw new FacesException("Invalid component type. " +
                      "Expected WSelectOne");
        }
        OutputMethod outputMethod = rc.getOutputMethod();
        Assert.assert_it(outputMethod != null );
	String selectedValue = (String) wSelectOne.getSelectedValue(rc);

        StringBuffer output = new StringBuffer();
        output.append("<SELECT NAME=\"");
        output.append(wSelectOne.getAttribute(rc, "name"));
        output.append("\">");
        outputMethod.writeText(output.toString());
        outputMethod.flush();
    }

    public void renderChildren(RenderContext rc, WComponent c) 
        throws IOException {
        return;
    }

    public void renderComplete(RenderContext rc, WComponent c) 
            throws IOException,FacesException {
        ParameterCheck.nonNull(rc);
        ParameterCheck.nonNull(c);
	
        WSelectOne wSelectOne = null;
        if ( supportsType(c)) {
            wSelectOne = (WSelectOne) c;
        } else {
            throw new FacesException("Invalid component type. " +
				     "Expected WSelectOne");
        }
        OutputMethod outputMethod = rc.getOutputMethod();
        Assert.assert_it(outputMethod != null );
	String selectedValue = (String) wSelectOne.getSelectedValue(rc);
	
        StringBuffer output = new StringBuffer();
        output.append("</SELECT>");
        outputMethod.writeText(output.toString());
        outputMethod.flush();

    }

    public boolean getCanRenderChildren(RenderContext rc, WComponent c) {
        return false;
    }


} // end of class OptionListRenderer
