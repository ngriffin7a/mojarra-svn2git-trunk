/*
 * $Id: JspFacesTestCase.java,v 1.1 2005/10/18 16:41:33 edburns Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// JspFacesTestCase.java

package com.sun.faces.cactus;

import org.apache.cactus.JspTestCase;

import javax.faces.context.FacesContext;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import java.util.Iterator;

/**
 * <B>JspFacesTestCase</B> is a base class that leverages
 * FacesTestCaseService to add Faces specific behavior to that provided
 * by cactus.  This class just delegates all method calls to
 * facesService.
 *
 * @version $Id: JspFacesTestCase.java,v 1.1 2005/10/18 16:41:33 edburns Exp $
 * @see	#facesService
 */

public abstract class JspFacesTestCase extends JspTestCase
    implements FacesTestCase {

//
// Protected Constants
//

    public static final String ENTER_CALLED = FacesTestCaseService.ENTER_CALLED;
    public static final String EXIT_CALLED = FacesTestCaseService.EXIT_CALLED;
    public static final String EMPTY = FacesTestCaseService.EMPTY;


//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

/*

* This is the thing you use to get the facesContext and other
* Faces Objects.

*/

    protected FacesTestCaseService facesService = null;

//
// Constructors and Initializers    
//

    public JspFacesTestCase() {
        super("JspFacesTestCase");
        init();
    }


    public JspFacesTestCase(String name) {
        super(name);
        init();
    }


    protected void init() {
        facesService = new FacesTestCaseService(this);
    }

//
// Class methods
//

//
// Methods from FacesTestCase
//

    public ServletConfig getConfig() {
        return config;
    }


    public HttpServletRequest getRequest() {
        return request;
    }


    public HttpServletResponse getResponse() {
        return response;
    }


    public PageContext getPageContext() {
        return pageContext;
    }


    public boolean sendResponseToFile() {
        return false;
    }


    public String getExpectedOutputFilename() {
        return null;
    }


    public String[] getLinesToIgnore() {
        return null;
    }

//
// General Methods
//


    public void setUp() {
        facesService.setUp();
    }


    public void tearDown() {
        facesService.tearDown();
    }


    public FacesContext getFacesContext() {
        return facesService.getFacesContext();
    }


    public boolean verifyExpectedOutput() {
        return facesService.verifyExpectedOutput();
    }

    public void verifyEqualsContractPositive(Object x, Object y, Object z) {
        facesService.verifyEqualsContractPositive(x, y, z);
    }

    public boolean verifyExpectedStringInOutput(String str) {
        return facesService.verifyExpectedStringInOutput(str);
    }


    public boolean sendWriterToFile() {
        return false;
    }


    public boolean isMember(String toTest, String[] set) {
        return facesService.isMember(toTest, set);
    }


    public boolean isSubset(String[] subset, Iterator superset) {
        return facesService.isSubset(subset, superset);
    }


    public boolean requestsHaveSameAttributeSet(HttpServletRequest request1,
                                                HttpServletRequest request2) {
        return facesService.requestsHaveSameAttributeSet(request1, request2);
    }


    public void loadFromInitParam(String paramValue) {
        facesService.loadFromInitParam(paramValue);
    }
    
    private String testRootDir = null;
    public String getTestRootDir() {
        return testRootDir;
    }
    
    public void setTestRootDir(String rootDir) {
        this.testRootDir = rootDir;
    }

} // end of class JspFacesTestCase
