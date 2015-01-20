/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.servlet30.el;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Issue2561IT {

    private String webUrl;
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(60000);
    }

    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    @Test
    public void testConstructor() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/viewConstructor.xhtml");
        assertTrue(page.asText().indexOf("This is constructed") != -1);
    }

    @Test
    public void testPostConstruct() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/viewPostconstruct.xhtml");
        assertTrue(page.asText().indexOf("This is from the @PostConstruct") != -1);
    }

    @Test
    public void testNavigate() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/viewNavigate.xhtml");
        assertTrue(page.asText().indexOf("This is from the @PostConstruct") != -1);
        HtmlElement anchor = page.getHtmlElementById("form:submit");
        page = anchor.click();
        assertTrue(page.asText().indexOf("This is from the @PostConstruct") != -1);
    }

    @Test
    public void testNavigateAway() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/viewNavigateAway.xhtml");
        assertTrue(page.asText().indexOf("This is from the @PostConstruct") != -1);
        HtmlElement anchor = page.getHtmlElementById("form:submit");
        page = anchor.click();
        assertTrue(page.asText().indexOf("true") != -1);
    }

    @Test
    public void testInvalidatedSession() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/viewInvalidatedSession.xhtml");
        assertTrue(page.asText().indexOf("This is from the @PostConstruct") != -1);
        webClient.getPage(webUrl + "faces/viewInvalidatedPerform.xhtml");
        page = webClient.getPage(webUrl + "faces/viewInvalidatedVerify.xhtml");
        assertTrue(page.asText().indexOf("true") != -1);
    }
}
