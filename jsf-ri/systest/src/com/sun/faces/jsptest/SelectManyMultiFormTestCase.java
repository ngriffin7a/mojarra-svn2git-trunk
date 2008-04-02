/*
 * $Id: SelectManyMultiFormTestCase.java,v 1.6 2006/03/29 22:38:47 rlubke Exp $
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

package com.sun.faces.jsptest;


import javax.faces.component.NamingContainer;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/** <p>Test Case for JSP Interoperability.</p> */

public class SelectManyMultiFormTestCase extends AbstractTestCase {

    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public SelectManyMultiFormTestCase(String name) {

        super(name);

    }

    // ---------------------------------------------------------- Public Methods


    /** Return the tests included in this test suite. */
    public static Test suite() {

        return (new TestSuite(SelectManyMultiFormTestCase.class));

    }


    /** Set up instance variables required by this test case. */
    public void setUp() throws Exception {

        super.setUp();

    }


    /** Tear down instance variables required by this test case. */
    public void tearDown() {

        super.tearDown();

    }

    // ------------------------------------------------- Individual Test Methods


    public void testMultiForm() throws Exception {

        HtmlForm form;
        HtmlSubmitInput submit;
        HtmlAnchor link;
        HtmlTextInput input;
        HtmlPage page;

        page = getPage("/faces/standard/selectmany01.jsp");
        // verify that the model tier is as expected
        assertTrue(-1 !=
                   page.asText().indexOf("Current model value: 1, 2, ,"));
        form = getFormById(page, "form2");
        submit = (HtmlSubmitInput)
              form.getInputByName("form2" + NamingContainer.SEPARATOR_CHAR +
                                  "doNotModify");

        // press button1
        page = (HtmlPage) submit.click();
        // verify that submitting the form does not change the model tier
        assertTrue(-1 !=
                   page.asText().indexOf("Current model value: 1, 2, ,"));

    }

}
