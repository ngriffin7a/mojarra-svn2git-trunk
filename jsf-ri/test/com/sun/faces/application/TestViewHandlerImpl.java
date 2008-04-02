/* 
 * $Id: TestViewHandlerImpl.java,v 1.3 2003/10/02 06:50:08 jvisvanathan Exp $ 
 */ 


/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


// TestViewHandlerImpl.java 


package com.sun.faces.application; 


import org.apache.cactus.WebRequest; 
import org.apache.cactus.JspTestCase; 


import org.mozilla.util.Assert; 
import org.mozilla.util.ParameterCheck; 


import javax.faces.FacesException; 
import javax.faces.FactoryFinder; 
import javax.faces.context.FacesContext; 
import javax.faces.context.FacesContextFactory; 
import javax.faces.lifecycle.Lifecycle; 
import javax.faces.component.UIComponentBase;
import javax.faces.validator.Validator; 
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import com.sun.faces.JspFacesTestCase; 
import com.sun.faces.FileOutputResponseWrapper; 
import com.sun.faces.util.Util; 
import com.sun.faces.CompareFiles; 

import com.sun.faces.TestBean;
import com.sun.faces.application.ViewHandlerImpl;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;

import java.io.IOException; 
import java.util.Iterator; 
import java.util.ArrayList; 
import java.util.Map;

import javax.servlet.jsp.PageContext; 


/** 
 * 
 * <B>TestViewHandlerImpl</B> is a class ... 
 * 
 * <B>Lifetime And Scope</B> <P> 
 * 
 * @version $Id: TestViewHandlerImpl.java,v 1.3 2003/10/02 06:50:08 jvisvanathan Exp $ 
 * 
 * @see Blah 
 * @see Bloo 
 * 
 */ 


public class TestViewHandlerImpl extends JspFacesTestCase 
{ 
// 
// Protected Constants 
// 


public static final String TEST_URI = "/greeting.jsp"; 

public String getExpectedOutputFilename() { 
    return "TestViewHandlerImpl_correct"; 
} 


public static final String ignore[] = {
    "    <form id=\"helloForm\" method=\"post\" action=\"/test/faces/greeting.jsp;jsessionid=99D4FBE19D2B3AB5804FB24B9D0DBCF7\">"
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


    public TestViewHandlerImpl() { 
        super("TestViewHandlerImpl"); 
    } 


    public TestViewHandlerImpl(String name) { 
        super(name); 
    } 


// 
// Class methods 
// 


// 
// General Methods 
// 



public void beginRender(WebRequest theRequest) 
{ 
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null); 
} 


public void testRender() 
{ 
    boolean result = false; 
    UIComponentBase root = null; 
    String value = null; 
    UIViewRoot newView = new UIViewRoot();
    newView.setViewId(TEST_URI);
    getFacesContext().setViewRoot(newView);

    try { 
        ViewHandlerImpl viewHandler = new ViewHandlerImpl(); 
        viewHandler.renderView(getFacesContext(), 
			       getFacesContext().getViewRoot()); 
    } catch (IOException e) { 
        System.out.println("ViewHandler IOException:"+e); 
    } catch (FacesException fe) { 
        System.out.println("ViewHandler FacesException: "+fe); 
    }

    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));

    assertTrue(verifyExpectedOutput()); 
} 

public void testTransient()
{
    
    // precreate tree and set it in session and make sure the tree is
    // restored from session.
    getFacesContext().setViewRoot(null);
    UIViewRoot root = new UIViewRoot();
    root.setViewId(TEST_URI);
    
    UIForm basicForm = new UIForm();
    basicForm.setId("basicForm");
    UIInput userName = new UIInput();
    
    userName.setId("userName");
    userName.setTransient(true);
    root.getChildren().add(basicForm);
    basicForm.getChildren().add(userName);
    
    UIPanel panel1 = new UIPanel();
    panel1.setId("panel1");
    basicForm.getChildren().add(panel1);
    
    UIInput userName1 = new UIInput();
    userName1.setId("userName1");
    userName1.setTransient(true);
    panel1.getChildren().add(userName1);
    
    UIInput userName2 = new UIInput();
    userName2.setId("userName2");
    panel1.getChildren().add(userName2);
    
    UIInput userName3 = new UIInput();
    userName3.setTransient(true);
    panel1.getFacets().put("userName3", userName3);
    
    UIInput userName4 = new UIInput();
    panel1.getFacets().put("userName4",userName4);
    
    HttpSession session = (HttpSession) 
        getFacesContext().getExternalContext().getSession(false);
    session.setAttribute(TEST_URI, root);
    
    getFacesContext().setViewRoot(root);

    ViewHandlerImpl viewHandler = new ViewHandlerImpl(); 
    viewHandler.getStateManager().saveSerializedView(getFacesContext());
   
    // make sure that the transient property is not persisted.
    basicForm = (UIForm)(getFacesContext().getViewRoot()).findComponent("basicForm");
    assertTrue(basicForm != null);
    
    userName = (UIInput)basicForm.findComponent("userName");
    assertTrue(userName == null);
    
    panel1 = (UIPanel)basicForm.findComponent("panel1");
    assertTrue(panel1 != null);
    
    userName1 = (UIInput)panel1.findComponent("userName1");
    assertTrue(userName1 == null);
    
    userName2 = (UIInput)panel1.findComponent("userName2");
    assertTrue(userName2 != null);
    
    // make sure facets work correctly when marked transient.
    Map facetList = panel1.getFacets();
    assertTrue(!(facetList.containsKey("userName3")));
    assertTrue(facetList.containsKey("userName4"));
}


} // end of class TestViewHandlerImpl 
