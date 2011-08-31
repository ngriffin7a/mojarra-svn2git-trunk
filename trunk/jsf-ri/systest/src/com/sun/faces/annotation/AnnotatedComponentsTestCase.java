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

package com.sun.faces.annotation;

import java.util.List;
import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.faces.application.Application;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.FactoryFinder;
import javax.faces.validator.Validator;
import javax.faces.convert.Converter;
import javax.faces.component.UIComponent;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.NamedEventManager;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.mgbean.ManagedBeanInfo;
import com.sun.faces.mgbean.BeanBuilder;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import junit.framework.Test;
import junit.framework.TestSuite;


public class AnnotatedComponentsTestCase extends HtmlUnitFacesTestCase {


    // ------------------------------------------------------------ Constructors


    public AnnotatedComponentsTestCase() {
        super("AnnotatedComponentsTestCase");
    }


    public AnnotatedComponentsTestCase(String name) {
        super(name);
    }

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(AnnotatedComponentsTestCase.class));
    }


    // ------------------------------------------------------------ Test Methods


    public void testAnnotations() throws Exception {

        HtmlPage page = getPage("/faces/annotationtest.xhtml");
        List<HtmlSpan> output = new ArrayList<HtmlSpan>(1);
        getAllElementsOfGivenClass(page, output, HtmlSpan.class);
        assertTrue(output.size() == 1);
        HtmlSpan span = output.get(0);
        assertTrue(span.asText().contains("true"));
        assertTrue(page.asText().contains("AnnotatedPhaseListener: Hello World from env-entry!"));
        
    }
}
