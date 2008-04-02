/*
 * $Id: ConverterTestCase.java,v 1.1 2004/12/02 18:42:25 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.jsptest;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;

import javax.faces.component.NamingContainer;

/**
 * <p>Test that invalid values don't cause valueChangeEvents to occur.</p>
 */

public class ConverterTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ConverterTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------- Overall Test Methods


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ConverterTestCase.class));
    }

    // ------------------------------------------------- Individual Test Methods
    public void testConverter() throws Exception {
	HtmlPage page = getPage("/faces/converter03.jsp");
	List list;
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlTextInput.class); 

	// set the initial value to be 1 for all input fields
	((HtmlTextInput)list.get(0)).setValueAttribute("1111111111");
	((HtmlTextInput)list.get(1)).setValueAttribute("1111111111");
	((HtmlTextInput)list.get(2)).setValueAttribute("1111111111");
	((HtmlTextInput)list.get(3)).setValueAttribute("1111111111");
	((HtmlTextInput)list.get(4)).setValueAttribute("99");
	((HtmlTextInput)list.get(5)).setValueAttribute("4");
	((HtmlTextInput)list.get(6)).setValueAttribute("12");
	((HtmlTextInput)list.get(7)).setValueAttribute("7");
	((HtmlTextInput)list.get(8)).setValueAttribute("10");

	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();
	assertTrue(-1 != page.asText().indexOf("text1 was converted to Object"));
	assertTrue(-1 != page.asText().indexOf("text1 was converted to String"));
	assertTrue(-1 != page.asText().indexOf("text2 was converted to Object"));
	assertTrue(-1 != page.asText().indexOf("text2 was converted to String"));
	assertTrue(-1 != page.asText().indexOf("text3 was converted to Object"));
	assertTrue(-1 != page.asText().indexOf("text3 was converted to String"));
	assertTrue(-1 != page.asText().indexOf("text4 was converted to Object"));
	assertTrue(-1 != page.asText().indexOf("text4 was converted to String"));
	assertTrue(-1 != page.asText().indexOf("99 Conversion error occurred"));
	assertTrue(-1 != page.asText().indexOf("4.0"));
	assertTrue(-1 != page.asText().indexOf("12.0"));
	assertTrue(-1 != page.asText().indexOf("7.0"));
	assertTrue(-1 != page.asText().indexOf("10"));
    }
}
