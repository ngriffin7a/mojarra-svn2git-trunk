/*
 * $Id: TestUtil.java,v 1.7 2003/05/15 22:25:51 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestUtil.java

package com.sun.faces.util;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.RIConstants;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

import javax.servlet.ServletContext;

/**
 *
 *  <B>TestUtil</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestUtil.java,v 1.7 2003/05/15 22:25:51 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestUtil extends ServletFacesTestCase
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

    public TestUtil() {super("TestUtil");}
    public TestUtil(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//

    public void testRenderPassthruAttributes() {
	UIInput input = new UIInput();
	input.setComponentId("testRenderPassthruAttributes");
	input.setAttribute("notPresent", "notPresent");
	input.setAttribute("onblur", "javascript:f.blur()");
	input.setAttribute("onchange", "javascript:h.change()");
	String result = Util.renderPassthruAttributes(getFacesContext(),input);
	String expectedResult = " onblur=\"javascript:f.blur()\" onchange=\"javascript:h.change()\" ";
	assertTrue(result.equals(expectedResult));

	// verify no passthru attributes returns empty string
	input.setAttribute("onblur", null);
	input.setAttribute("onchange", null);
	result = Util.renderPassthruAttributes(getFacesContext(), input);
	assertTrue(0 == result.length());
    }

    public void testRenderBooleanPassthruAttributes() {
	UIInput input = new UIInput();
	input.setComponentId("testBooleanRenderPassthruAttributes");
	input.setAttribute("disabled", "true");
	input.setAttribute("readonly", "false");
	String result = Util.renderBooleanPassthruAttributes(getFacesContext(),
							     input);
	String expectedResult = " disabled ";
	assertTrue(result.equals(expectedResult));

	// verify no passthru attributes returns empty string
	input.setAttribute("disabled", null);
	input.setAttribute("readonly", null);
	result = Util.renderBooleanPassthruAttributes(getFacesContext(), 
						      input);
	assertTrue(0 == result.length());
    }

    public void testVerifyRequiredClasses() {
	ServletContext servletContext = (ServletContext)getFacesContext().getExternalContext().getContext();
	servletContext.removeAttribute(RIConstants.HAS_REQUIRED_CLASSES_ATTR);
	try {
	    Util.verifyRequiredClasses(getFacesContext());
	}
	catch (Throwable e) {
	    assertTrue(false);
	}

	try {
	    Util.verifyRequiredClasses(getFacesContext());
	}
	catch (Throwable e) {
	    assertTrue(false);
	}
    }

    /**
     * This method tests the <code>Util.getSessionMap</code> method.
     */
    public void testGetSessionMap() {
        // Test with null FacesContext
	//
	Map sessionMap = Util.getSessionMap(null);
	assertTrue(sessionMap != null);

	// Test with FacesContext
	//
	sessionMap = Util.getSessionMap(getFacesContext());
	assertTrue(sessionMap != null);

	// Test with no session
	//
	session.invalidate();
	sessionMap = Util.getSessionMap(getFacesContext());
	assertTrue(sessionMap != null);
    }

	
} // end of class TestUtil
