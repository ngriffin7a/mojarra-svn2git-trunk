/*
 * $Id: PathTestCase.java,v 1.1 2005/07/25 18:34:21 rajprem Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.pathtest;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


public class PathTestCase extends AbstractTestCase {

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

    public PathTestCase(String name) {
        super(name);
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {
        super.setUp();
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(PathTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    /**
     * Verify that if an initial request comes to a FacesSevlet
     * mapped using a prefix path, that the client is redirected
     * to the context root.
     * Additionally verify that after request to a particular view
     * has been made, if a subsequent request is made to the
     * prefix path, context root redirection occurs.
     */
    public void testVerifyPathBehavior() throws Exception {
        final String welcomePage = "WELCOMEPAGE";
        HtmlPage page = getPage("/faces");
        WebResponse response = page.getWebResponse();
        assertTrue(welcomePage.equals(response.getContentAsString().trim()));
        
        // Ok, now get a page
        HtmlPage textPage = getHtmlPage("/faces/hello.jsp");
        response = textPage.getWebResponse();
        assertTrue("/hello.jsp PASSED".equals(
            response.getContentAsString().trim()));

        page = getPage("/faces");
        response = page.getWebResponse();
        assertTrue("WELCOMEPAGE".equals(response.getContentAsString().trim()));
    }


    protected HtmlPage getHtmlPage(String path) throws Exception {
        HtmlPage page = (HtmlPage) client.getPage(getURL(path));

        return (page);

    }


} // end of class PathTestCase

