package com.sun.faces.systest;

import java.net.URL;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


public class ViewExpiredExceptionTestCase extends AbstractTestCase {

    public ViewExpiredExceptionTestCase(String name) {
        super(name);
    }

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
        return (new TestSuite(InvalidMappingTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }
    
    
    // ------------------------------------------------------------ Test Methods
    
    public void testViewExpiredExceptionErrorPage() throws Exception {
        WebClient client = new WebClient();
        client.setThrowExceptionOnFailingStatusCode(false);
        client.setTimeout(0);
       
        HtmlPage page = (HtmlPage) client.getPage("/faces/test.jsp", client);

        HtmlSubmitInput submit = (HtmlSubmitInput) 
            getInputContainingGivenId(page, "submit");

        Thread.sleep(65000);

        HtmlPage errorPage = (HtmlPage) submit.click();
        assertTrue(errorPage.asText().indexOf("Error page invoked") >= 0);
    }
}
