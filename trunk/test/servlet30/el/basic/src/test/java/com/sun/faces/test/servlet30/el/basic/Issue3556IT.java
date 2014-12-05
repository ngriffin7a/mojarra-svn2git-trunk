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
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
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
package com.sun.faces.test.servlet30.el.basic;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class Issue3556IT {

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

    @Test
    public void testResource1en() throws Exception {
        webClient.addRequestHeader("Accept-Language", "en");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource1.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource1.gif"));
        assertTrue(image.getSrcAttribute().contains("?loc=en"));
    }

    @Test
    public void testResource1de() throws Exception {
        webClient.addRequestHeader("Accept-Language", "de");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource1.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource1.gif"));
        assertTrue(image.getSrcAttribute().contains("?loc=de"));
    }

    @Test
    public void testResource1fr() throws Exception {
        webClient.addRequestHeader("Accept-Language", "fr");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource1.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource1.gif"));
        assertTrue(image.getSrcAttribute().contains("?loc=fr"));
    }

    @Test
    public void testResource1ja() throws Exception {
        webClient.addRequestHeader("Accept-Language", "ja");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource1.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource1.gif"));
        assertTrue(image.getSrcAttribute().contains("?loc=en"));
    }
    @Test
    public void testResource2en() throws Exception {
        webClient.addRequestHeader("Accept-Language", "en");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource2.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource2.gif"));
        assertTrue(image.getSrcAttribute().contains("?ln=resource2&loc=en"));
    }

    @Test
    public void testResource2de() throws Exception {
        webClient.addRequestHeader("Accept-Language", "de");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource2.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource2.gif"));
        assertTrue(image.getSrcAttribute().contains("?ln=resource2&loc=de"));
    }

    @Test
    public void testResource2fr() throws Exception {
        webClient.addRequestHeader("Accept-Language", "fr");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource2.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource2.gif"));
        assertTrue(image.getSrcAttribute().contains("?ln=resource2&loc=fr"));
    }

    @Test
    public void testResource2ja() throws Exception {
        webClient.addRequestHeader("Accept-Language", "ja");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource2.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource2.gif"));
        assertTrue(image.getSrcAttribute().contains("?ln=resource2&loc=en"));
    }
    
    @Test
    public void testResource3en() throws Exception {
        webClient.addRequestHeader("Accept-Language", "en");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource3.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource3.gif"));
    }
    
    @Test
    public void testResource3de() throws Exception {
        webClient.addRequestHeader("Accept-Language", "de");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource3.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource3.gif"));
    }
    
    @Test
    public void testResource3fr() throws Exception {
        webClient.addRequestHeader("Accept-Language", "fr");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource3.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource3.gif"));
    }
    
    @Test
    public void testResource3ja() throws Exception {
        webClient.addRequestHeader("Accept-Language", "ja");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource3.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource3.gif"));
    }
    
    @Test
    public void testResource4en() throws Exception {
        webClient.addRequestHeader("Accept-Language", "en");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource4.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource4.gif"));
        assertTrue(image.getSrcAttribute().contains("ln=resource4"));
    }
    
    @Test
    public void testResource4de() throws Exception {
        webClient.addRequestHeader("Accept-Language", "de");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource4.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource4.gif"));
        assertTrue(image.getSrcAttribute().contains("ln=resource4"));
    }
    
    @Test
    public void testResource4fr() throws Exception {
        webClient.addRequestHeader("Accept-Language", "fr");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource4.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource4.gif"));
        assertTrue(image.getSrcAttribute().contains("ln=resource4"));
    }
    
    @Test
    public void testResource4ja() throws Exception {
        webClient.addRequestHeader("Accept-Language", "ja");
        HtmlPage page = webClient.getPage(webUrl + "faces/resource4.jsp");
        HtmlImage image = page.getHtmlElementById("image");
        assertTrue(image.getSrcAttribute().contains("javax.faces.resource/resource4.gif"));
        assertTrue(image.getSrcAttribute().contains("ln=resource4"));
    }
}
