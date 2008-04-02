/*
 * $Id: TestValidatorTags.java,v 1.10 2003/08/13 17:52:13 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestValidatorTags.java

package com.sun.faces.taglib.jsf_core;

import org.apache.cactus.WebRequest;

import org.mozilla.util.Assert;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIComponent;

import com.sun.faces.context.FacesContextImpl;
import com.sun.faces.JspFacesTestCase;
import com.sun.faces.RIConstants;
import com.sun.faces.context.FacesContextImpl;
import com.sun.faces.lifecycle.ApplyRequestValuesPhase;
import com.sun.faces.lifecycle.LifecycleImpl;
import com.sun.faces.lifecycle.Phase;
import com.sun.faces.lifecycle.ProcessValidationsPhase;
import com.sun.faces.lifecycle.RenderResponsePhase;


import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;

import java.util.Iterator;

/**
 *
 *  <B>TestValidatorTags</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestValidatorTags.java,v 1.10 2003/08/13 17:52:13 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestValidatorTags extends JspFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI = "/TestValidatorTags.jsp";
public static final String OUTOFBOUNDS1_ID = "outOfBounds1";
public static final String OUTOFBOUNDS1_VALUE = "3.1415";
public static final String INBOUNDS1_ID = "inBounds1";
public static final String INBOUNDS1_VALUE = "10.25";
public static final String OUTOFBOUNDS2_ID = "outOfBounds2";
public static final String OUTOFBOUNDS2_VALUE = "fox";
public static final String INBOUNDS2_ID = "inBounds2";
public static final String INBOUNDS2_VALUE = "alligator22";
public static final String OUTOFBOUNDS3_ID = "outOfBounds3";
public static final String OUTOFBOUNDS3_VALUE = "30000";
public static final String INBOUNDS3_ID = "inBounds3";
public static final String INBOUNDS3_VALUE = "1100";
public static final String REQUIRED1_ID = "required1";
public static final String REQUIRED1_VALUE = "required";
public static final String OUTOFBOUNDS4_ID = "outOfBounds4";
public static final String OUTOFBOUNDS4_VALUE = "aaa";
public static final String INBOUNDS4_ID = "inBounds4";
public static final String INBOUNDS4_VALUE = "ccc";
public static final String REQUIRED2_ID = "required2";
public static final String REQUIRED2_VALUE = "required";

public boolean sendResponseToFile() 
{
    return false;
}

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

    public TestValidatorTags() {
	super("TestValidatorTags");
    }

    public TestValidatorTags(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//


public void beginValidators(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
    theRequest.addParameter(OUTOFBOUNDS1_ID, OUTOFBOUNDS1_VALUE);
    theRequest.addParameter(INBOUNDS1_ID, INBOUNDS1_VALUE);
    theRequest.addParameter(OUTOFBOUNDS2_ID, OUTOFBOUNDS2_VALUE);
    theRequest.addParameter(INBOUNDS2_ID, INBOUNDS2_VALUE);
    theRequest.addParameter(OUTOFBOUNDS3_ID, OUTOFBOUNDS3_VALUE);
    theRequest.addParameter(INBOUNDS3_ID, INBOUNDS3_VALUE);
    theRequest.addParameter(REQUIRED1_ID, "");
    theRequest.addParameter(OUTOFBOUNDS4_ID, OUTOFBOUNDS4_VALUE);
    theRequest.addParameter(INBOUNDS4_ID, INBOUNDS4_VALUE);
    theRequest.addParameter(REQUIRED2_ID, "");
  
}

public void setUp() {
    RIConstants.IS_UNIT_TEST_MODE = true;
    super.setUp();
}

public void testValidators()
{
    System.setProperty(RIConstants.DISABLE_RENDERERS, "");
    // Verify the parmeters are as expected
    String paramVal = (String)(getFacesContext().getExternalContext().getRequestParameterMap()).get(OUTOFBOUNDS1_ID);
    assertTrue(OUTOFBOUNDS1_VALUE.equals(paramVal));
//    assertTrue(OUTOFBOUNDS1_VALUE.equals(getFacesContext().getServletRequest().getParameter(OUTOFBOUNDS1_ID)));

    boolean result = false;
    String value = null;
    LifecycleImpl lifecycle = new LifecycleImpl();
    Phase 
	renderResponse = new RenderResponsePhase(lifecycle),
	processValidations = new ProcessValidationsPhase(),
	applyRequestValues = new ApplyRequestValuesPhase();

    TreeFactory treeFactory = (TreeFactory)
         FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
    Assert.assert_it(treeFactory != null);
    Tree requestTree = treeFactory.getTree(getFacesContext(),
            TEST_URI );
    getFacesContext().setTree(requestTree);

    // This builds the tree, and usefaces saves it in the session
    renderResponse.execute(getFacesContext());
    assertTrue(!((FacesContextImpl)getFacesContext()).getRenderResponse() &&
        !((FacesContextImpl)getFacesContext()).getResponseComplete());

    // This causes the components to be set to valid
    applyRequestValues.execute(getFacesContext());
    assertTrue(!((FacesContextImpl)getFacesContext()).getRenderResponse() &&
        !((FacesContextImpl)getFacesContext()).getResponseComplete());
    
    // process the validations
    processValidations.execute(getFacesContext());
    // We know there are validation errors on the page
    assertTrue(((FacesContextImpl)getFacesContext()).getRenderResponse());

    // verify the messages have been added correctly.
    UIComponent comp = null;
    Iterator messages = null;
    
    assertTrue(null != (messages = getFacesContext().getMessages()));
    assertTrue(messages.hasNext());
    
    // check the messages for each component in the page
    assertTrue(null != 
	       (comp = 
		requestTree.getRoot().findComponent(OUTOFBOUNDS1_ID)));
    assertTrue(null != (messages = getFacesContext().getMessages(comp)));
    assertTrue(messages.hasNext());

    assertTrue(null != 
	       (comp = 
		requestTree.getRoot().findComponent(INBOUNDS1_ID)));
    assertTrue(null != (messages = getFacesContext().getMessages(comp)));
    assertTrue(!messages.hasNext());

    assertTrue(null != 
	       (comp = 
		requestTree.getRoot().findComponent(OUTOFBOUNDS2_ID)));
    assertTrue(null != (messages = getFacesContext().getMessages(comp)));
    assertTrue(messages.hasNext());

    assertTrue(null != 
	       (comp = 
		requestTree.getRoot().findComponent(INBOUNDS2_ID)));
    assertTrue(null != (messages = getFacesContext().getMessages(comp)));
    assertTrue(!messages.hasNext());

    assertTrue(null != 
	       (comp = 
		requestTree.getRoot().findComponent(OUTOFBOUNDS3_ID)));
    assertTrue(null != (messages = getFacesContext().getMessages(comp)));
    assertTrue(messages.hasNext());

    assertTrue(null != 
	       (comp = 
		requestTree.getRoot().findComponent(INBOUNDS3_ID)));
    assertTrue(null != (messages = getFacesContext().getMessages(comp)));
    assertTrue(!messages.hasNext());

    assertTrue(null != 
	       (comp = 
		requestTree.getRoot().findComponent(REQUIRED1_ID)));
    assertTrue(null != (messages = getFacesContext().getMessages(comp)));
    assertTrue(messages.hasNext());

    assertTrue(null != 
	       (comp = 
		requestTree.getRoot().findComponent(OUTOFBOUNDS4_ID)));
    assertTrue(null != (messages = getFacesContext().getMessages(comp)));
    assertTrue(messages.hasNext());

    assertTrue(null != 
	       (comp = 
		requestTree.getRoot().findComponent(INBOUNDS4_ID)));
    assertTrue(null != (messages = getFacesContext().getMessages(comp)));
    assertTrue(!messages.hasNext());

    assertTrue(null != 
	       (comp = 
		requestTree.getRoot().findComponent(REQUIRED2_ID)));
    assertTrue(null != (messages = getFacesContext().getMessages(comp)));
    assertTrue(messages.hasNext());

    RIConstants.IS_UNIT_TEST_MODE = false;
}


} // end of class TestValidatorTags
