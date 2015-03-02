/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.servlet30.customlifecycle;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Replace the LifecycleFactory with an impl that knows about a new LifecycleId
 * that is passed to the app as a ServletContext init parameter. Make sure that
 * a phaseListener still gets called with a replaced lifecycle id.</p>
 */
public class ReplaceLifecycleIT {

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
    public void testReplaceLifecycle() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/test.jsp");
        String pageText = page.asText();
        assertTrue(-1 != pageText.indexOf("beforePhase"));
        // Ensure the phaseListener is only called once.
        assertTrue(!pageText.matches("(?s).*beforePhase.*beforePhase.*"));

    }

    @Test
    public void testAlternateLifecycle() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "alternate/test2.jsp");
        assertTrue(-1 != page.asText().indexOf("beforePhase"));
        assertTrue(-1 != page.asText().indexOf("AlternateLifecycle"));
        page = webClient.getPage(webUrl + "faces/test2.jsp");
        assertTrue(-1 != page.asText().indexOf("beforePhase"));
        assertTrue(-1 != page.asText().indexOf("NewLifecycle"));
    }
}
