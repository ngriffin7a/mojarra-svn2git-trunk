/*
 * $Id: ViewTagTestCase.java,v 1.1 2004/01/31 02:19:30 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.jsptest;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.htmlunit.AbstractTestCase;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Locale;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.faces.component.NamingContainer;


/**
 * <p>Test Case for JSP Interoperability.</p>
 */

public class ViewTagTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ViewTagTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------- Overall Test Methods


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
        return (new TestSuite(ViewTagTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    public void testLocaleOnViewTag() throws Exception {
	HtmlForm form;
	HtmlSubmitInput submit;
	HtmlAnchor link;
	HtmlTextInput input;
        HtmlPage page;

	page = getPage("/faces/viewLocale.jsp");
	form = getFormById(page, "form");
        submit = (HtmlSubmitInput)
	    form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                "button");
	
	// press the button
	page = (HtmlPage) submit.click();
	ResourceBundle messages = ResourceBundle.getBundle("javax.faces.Messages",
							   Locale.FRENCH);
	String message = messages.getString("javax.faces.validator.RequiredValidator.FAILED");
	assertTrue(-1 != page.asText().indexOf(message));
	
    }
}
