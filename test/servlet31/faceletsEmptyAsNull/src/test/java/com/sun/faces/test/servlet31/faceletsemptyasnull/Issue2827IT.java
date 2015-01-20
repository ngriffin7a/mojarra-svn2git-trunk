/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.sun.faces.test.servlet31.faceletsemptyasnull;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import static com.sun.faces.test.junit.JsfServerExclude.GLASSFISH_4_0;
import com.sun.faces.test.junit.JsfTest;
import com.sun.faces.test.junit.JsfTestRunner;
import static com.sun.faces.test.junit.JsfVersion.JSF_2_2_0;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JsfTestRunner.class)
public class Issue2827IT {

    private String webUrl;
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
    }

    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    /**
     * Test to verify empty as null.
     *
     * Note: this test is excluded on Tomcat because the included EL parser
     * requires a System property for this test to work, which would cause
     * problems with other tests. See
     * http://tomcat.apache.org/tomcat-7.0-doc/config/systemprops.html and look
     * for COERCE_TO_ZERO
     * 
     * @throws Exception
     */
    @JsfTest(value=JSF_2_2_0)
    @Test
    public void testValidateEmptyFields() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/index.xhtml");
        if (page.getWebResponse().getResponseHeaderValue("Server") == null
                || !page.getWebResponse().getResponseHeaderValue("Server").startsWith("Apache-Coyote")) {
            page = webClient.getPage(webUrl + "faces/verifyEmptyAsNull.xhtml");

            HtmlInput stringInput = page.getHtmlElementById("form:string");
            HtmlInput integerInput = page.getHtmlElementById("form:integer");

            assertNotNull(stringInput);
            assertNotNull(integerInput);
            assertEquals(stringInput.getValueAttribute(), "");
            assertEquals(integerInput.getValueAttribute(), "");

            String pageAsText = page.asText();
            assertTrue(pageAsText.contains("VC1 Fired: false"));
            assertTrue(pageAsText.contains("VC2 Fired: false"));
            assertTrue(pageAsText.contains("String model set with null: false"));
            assertTrue(pageAsText.contains("Integer model set with null: false"));

            HtmlSubmitInput submit = (HtmlSubmitInput) page.getHtmlElementById("form:command");
            assertNotNull(submit);

            stringInput.setValueAttribute("11");
            integerInput.setValueAttribute("11");

            page = (HtmlPage) submit.click();

            stringInput = page.getHtmlElementById("form:string");
            integerInput = page.getHtmlElementById("form:integer");

            assertNotNull(stringInput);
            assertNotNull(integerInput);
            assertEquals(stringInput.getValueAttribute(), "11");
            assertEquals(integerInput.getValueAttribute(), "11");

            pageAsText = page.asText();
            assertTrue(pageAsText.contains("VC1 Fired: true"));
            assertTrue(pageAsText.contains("VC2 Fired: true"));
            assertTrue(pageAsText.contains("String model set with null: false"));
            assertTrue(pageAsText.contains("Integer model set with null: false"));

            submit = (HtmlSubmitInput) page.getHtmlElementById("form:command");
            assertNotNull(submit);

            stringInput.setValueAttribute("");
            integerInput.setValueAttribute("");

            page = (HtmlPage) submit.click();

            stringInput = page.getHtmlElementById("form:string");
            integerInput = page.getHtmlElementById("form:integer");

            assertNotNull(stringInput);
            assertNotNull(integerInput);
            assertEquals(stringInput.getValueAttribute(), "");
            assertEquals(integerInput.getValueAttribute(), "");

            pageAsText = page.asText();
            assertTrue(pageAsText.contains("VC1 Fired: true"));
            assertTrue(pageAsText.contains("VC2 Fired: true"));
            assertTrue(pageAsText.contains("String model set with null: true"));
            assertTrue(pageAsText.contains("Integer model set with null: true"));

            submit = (HtmlSubmitInput) page.getHtmlElementById("form:command");
            assertNotNull(submit);

            stringInput.setValueAttribute("");
            integerInput.setValueAttribute("");

            page = (HtmlPage) submit.click();

            stringInput = page.getHtmlElementById("form:string");
            integerInput = page.getHtmlElementById("form:integer");

            assertNotNull(stringInput);
            assertNotNull(integerInput);
            assertEquals(stringInput.getValueAttribute(), "");
            assertEquals(integerInput.getValueAttribute(), "");

            pageAsText = page.asText();
            assertTrue(pageAsText.contains("VC1 Fired: false"));
            assertTrue(pageAsText.contains("VC2 Fired: false"));
            assertTrue(pageAsText.contains("String model set with null: true"));
            assertTrue(pageAsText.contains("Integer model set with null: true"));
        }
    }
}
