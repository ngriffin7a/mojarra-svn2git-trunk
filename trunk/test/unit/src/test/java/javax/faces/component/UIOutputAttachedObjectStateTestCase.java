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
package javax.faces.component;

import com.sun.faces.mock.MockExternalContext;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.sun.faces.mock.MockFacesContext;
import com.sun.faces.mock.MockHttpServletRequest;
import com.sun.faces.mock.MockHttpServletResponse;
import com.sun.faces.mock.MockLifecycle;
import com.sun.faces.mock.MockServletContext;
import java.lang.reflect.Method;
import javax.faces.FactoryFinder;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.Converter;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class UIOutputAttachedObjectStateTestCase extends TestCase {

    private MockFacesContext facesContext = null;
    private MockServletContext servletContext;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    public UIOutputAttachedObjectStateTestCase(String arg0) {
        super(arg0);
    }

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIOutputAttachedObjectStateTestCase.class));
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        facesContext = new MockFacesContext();
        facesContext = new MockFacesContext();

        servletContext = new MockServletContext();
        servletContext.addInitParameter("appParamName", "appParamValue");
        servletContext.setAttribute("appScopeName", "appScopeValue");
        request = new MockHttpServletRequest(null);
        request.setAttribute("reqScopeName", "reqScopeValue");
        response = new MockHttpServletResponse();

        // Create something to stand-in as the InitFacesContext
        new MockFacesContext(new MockExternalContext(servletContext, request, response),
                new MockLifecycle());

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        FactoryFinder.releaseFactories();
        Method reInitializeFactoryManager = FactoryFinder.class.getDeclaredMethod("reInitializeFactoryManager", (Class<?>[]) null);
        reInitializeFactoryManager.setAccessible(true);
        reInitializeFactoryManager.invoke(null, (Object[]) null);
    }

    // ------------------------------------------------------------ Test Methods
    public void testConverterState() {
        UIOutput output = new UIOutput();
        DateTimeConverter converter = new DateTimeConverter();
        converter.setPattern("MM-dd-yy");
        output.setConverter(converter);
        output.markInitialState();
        assertTrue(output.initialStateMarked());
        assertTrue(converter.initialStateMarked());

        Object result = output.saveState(facesContext);
        // initial state has been marked an no changes
        // have occurred, we should have null state.
        assertNull(result);

        // setup the scenario again, but this time,
        // update the converter pattern.
        output = new UIOutput();
        converter = new DateTimeConverter();
        converter.setPattern("MM-dd-yy");
        output.setConverter(converter);
        output.markInitialState();
        assertTrue(output.initialStateMarked());
        assertTrue(converter.initialStateMarked());

        // now tweak the converter
        converter.setPattern("dd-MM-yy");
        result = output.saveState(facesContext);
        assertTrue(result instanceof Object[]);
        Object[] state = (Object[]) result;

        // state should have a lenght of 2.  The first element
        // is the state from UIComponentBase, where the second
        // is the converter state.  The first element in this
        // case should be null
        assertTrue(state.length == 2);
        assertTrue(state[0] == null);
        assertTrue(state[1] != null);

        output = new UIOutput();
        converter = new DateTimeConverter();
        output.setConverter(converter);

        // now validate what we've restored
        // first, ensure converter is null.  This will
        // be the case when initialState has been marked
        // for the component.
        output.restoreState(facesContext, state);
        assertTrue(output.getConverter() != null);
        assertTrue("dd-MM-yy".equals(converter.getPattern()));

        // now validate the case where UIOutput has some event
        // that adds a converter *after* initial state has been
        // marked.  This will cause the component to save full
        // state.
        output = new UIOutput();
        output.markInitialState();
        output.setConverter(converter);
        assertTrue(!output.initialStateMarked());
        assertTrue(!converter.initialStateMarked());

        result = output.saveState(facesContext);
        assertNotNull(result);

        // this time, both elements in the state array will not
        // be null.  If we call retoreState() on a new component instance
        // without setting a converter, we should have a new DateTimeConverter
        // *with* the expected pattern.
        assertTrue(result instanceof Object[]);
        state = (Object[]) result;
        assertTrue(state.length == 2);
        assertTrue(state[1] instanceof StateHolderSaver);
        output = new UIOutput();
        assertNull(output.getConverter());
        output.restoreState(facesContext, state);
        Converter c = output.getConverter();
        assertNotNull(c);
        assertTrue(c instanceof DateTimeConverter);
        converter = (DateTimeConverter) c;
        assertTrue("dd-MM-yy".equals(converter.getPattern()));
    }
}
