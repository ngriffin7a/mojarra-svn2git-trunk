/*
 * $Id: TestSaveStateInPage.java,v 1.17 2003/09/13 12:58:53 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestSaveStateInPage.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.base.UIComponentBase;
import com.sun.faces.util.TreeStructure;
import com.sun.faces.application.ViewHandlerImpl;
import com.sun.faces.application.StateManagerImpl;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.base.UIFormBase;
import javax.faces.component.base.UIInputBase;
import javax.faces.component.base.UIPanelBase;
import javax.faces.component.base.UIViewRootBase;
import javax.faces.component.UIViewRoot;

import com.sun.faces.lifecycle.Phase;
import com.sun.faces.JspFacesTestCase;
import com.sun.faces.RIConstants;


/**
 *
 *  <B>TestSaveStateInPage</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestSaveStateInPage.java,v 1.17 2003/09/13 12:58:53 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestSaveStateInPage extends JspFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI = "/greeting.jsp";

public String getExpectedOutputFilename() {
    return "SaveState_correct";
}

public static final String ignore[] = {
   "<form id=\"helloForm\" method=\"post\" action=\"/test/faces/greeting.jsp;jsessionid=09AF72F7E5EA209865AFFAB72D0F7B33\">"
};
    
public String [] getLinesToIgnore() {
    return ignore;
}

public boolean sendResponseToFile() 
{
    return true;
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

    public TestSaveStateInPage() {
	super("TestRenderResponsePhase");
    }

    public TestSaveStateInPage(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//


public void beginSaveStateInPage(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
}

public void testSaveStateInPage()
{    

    boolean result = false;
    UIComponentBase root = null;
    String value = null;    
    Phase renderResponse = new RenderResponsePhase(Application.getCurrentInstance());   
    UIViewRoot page = new UIViewRootBase();
    page.setId("root");
    page.setViewId(TEST_URI);
    getFacesContext().setViewRoot(page);

    renderResponse.execute(getFacesContext());
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));

    assertTrue(verifyExpectedOutput());
}

public void testSaveStateInClient()
{
    // PENDING (visvan) add test case to make sure no state is saved when
    // root is marked transient.
    // precreate tree and set it in session and make sure the tree is
    // restored from session.
    getFacesContext().setViewRoot(null);
    UIViewRoot root = new UIViewRootBase();
    root.setViewId(TEST_URI);
    
    UIFormBase basicForm = new UIFormBase();
    basicForm.setId("basicForm");
    UIInputBase userName = new UIInputBase();
    
    userName.setId("userName");
    userName.setTransient(true);
    root.getChildren().add(basicForm);
    basicForm.getChildren().add(userName);
    
    UIPanelBase panel1 = new UIPanelBase();
    panel1.setId("panel1");
    basicForm.getChildren().add(panel1);
    
    UIInputBase userName1 = new UIInputBase();
    userName1.setId("userName1");
    userName1.setTransient(true);
    panel1.getChildren().add(userName1);
    
    UIInputBase userName2 = new UIInputBase();
    userName2.setId("userName2");
    panel1.getChildren().add(userName2);
    
    UIInputBase userName3 = new UIInputBase();
    userName3.setTransient(true);
    panel1.getFacets().put("userName3", userName3);
    
    UIInputBase userName4 = new UIInputBase();
    panel1.getFacets().put("userName4",userName4);
    
    getFacesContext().setViewRoot(root);

    ViewHandlerImpl viewHandler = new ViewHandlerImpl(); 
    StateManagerImpl stateManager = 
	(StateManagerImpl) viewHandler.getStateManager();
    TreeStructure structRoot = 
        new TreeStructure(((UIComponent)getFacesContext().getViewRoot()));
    stateManager.buildTreeStructureToSave(((UIComponent)root),
					  structRoot, true);
   
   // make sure restored tree structure is correct
   UIViewRootBase viewRoot = (UIViewRootBase)structRoot.createComponent();
   assertTrue(null != viewRoot);
   stateManager.restoreComponentTreeStructure(structRoot, ((UIComponent)viewRoot), true);
   
   UIComponent component = (UIComponent)viewRoot.getChildren().get(0);
   assertTrue(component instanceof UIFormBase);
   assertTrue(component.getId().equals("basicForm"));
   
   UIFormBase uiform = (UIFormBase) component;
   component = (UIComponent)uiform.getChildren().get(0);
   assertTrue(component instanceof UIPanelBase);
   assertTrue(component.getId().equals("panel1"));
   
   UIPanelBase uipanel = (UIPanelBase) component;
   component = (UIComponent)uipanel.getChildren().get(0);
   assertTrue(component instanceof UIInputBase);
   assertTrue(component.getId().equals("userName2"));
   
    // make sure that the transient property is not persisted as well as the
    // namespace is preserved.
    basicForm = (UIFormBase)viewRoot.findComponent("basicForm");
    assertTrue(basicForm != null);
    
    userName = (UIInputBase)basicForm.findComponent("userName");
    assertTrue(userName == null);
    
    panel1 = (UIPanelBase)basicForm.findComponent("panel1");
    assertTrue(panel1 != null);
    
    userName1 = (UIInputBase)panel1.findComponent("userName1");
    assertTrue(userName1 == null);
    
    userName2 = (UIInputBase)panel1.findComponent("userName2");
    assertTrue(userName2 != null);
    
    // make sure facets work correctly when marked transient.
    Map facetList = panel1.getFacets();
    assertTrue(!(facetList.containsKey("userName3")));
    assertTrue(facetList.containsKey("userName4"));
}


} // end of class TestRenderResponsePhase
