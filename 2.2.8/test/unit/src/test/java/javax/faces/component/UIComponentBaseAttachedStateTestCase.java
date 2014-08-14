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
import com.sun.faces.mock.MockFacesContext;
import com.sun.faces.mock.MockHttpServletRequest;
import com.sun.faces.mock.MockHttpServletResponse;
import com.sun.faces.mock.MockLifecycle;
import com.sun.faces.mock.MockServletContext;
import java.lang.reflect.Method;
import java.util.HashMap;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesListener;
import javax.faces.event.ValueChangeListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.faces.FactoryFinder;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class UIComponentBaseAttachedStateTestCase extends TestCase {

    private UIComponentBase component;
    private MockFacesContext facesContext = null;
    private MockServletContext servletContext;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    public UIComponentBaseAttachedStateTestCase(String arg0) {
        super(arg0);
    }

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIComponentBaseAttachedStateTestCase.class));
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        component = new UIOutput();
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

    public void testAttachedObjectsSet() throws Exception {
        Set<ValueChangeListener> returnedAttachedObjects = null,
                attachedObjects = new HashSet<ValueChangeListener>();
        ValueChangeListener toAdd = new ValueChangeListenerTestImpl();
        attachedObjects.add(toAdd);
        toAdd = new ValueChangeListenerTestImpl();
        attachedObjects.add(toAdd);
        toAdd = new ValueChangeListenerTestImpl();
        attachedObjects.add(toAdd);
        Object result = UIComponentBase.saveAttachedState(facesContext, attachedObjects);
        returnedAttachedObjects = (Set<ValueChangeListener>) UIComponentBase.restoreAttachedState(facesContext, result);
    }

    public void testAttachedObjectsStack() throws Exception {
        Stack<ValueChangeListener> returnedAttachedObjects = null,
                attachedObjects = new Stack<ValueChangeListener>();
        ValueChangeListener toAdd = new ValueChangeListenerTestImpl();
        attachedObjects.add(toAdd);
        toAdd = new ValueChangeListenerTestImpl();
        attachedObjects.add(toAdd);
        toAdd = new ValueChangeListenerTestImpl();
        attachedObjects.add(toAdd);
        Object result = UIComponentBase.saveAttachedState(facesContext, attachedObjects);
        returnedAttachedObjects = (Stack<ValueChangeListener>) UIComponentBase.restoreAttachedState(facesContext, result);
    }

    public void testAttachedObjectsMap() throws Exception {
        Map<String, ValueChangeListener> returnedAttachedObjects = null,
                attachedObjects = new HashMap<String, ValueChangeListener>();
        ValueChangeListener toAdd = new ValueChangeListenerTestImpl();
        attachedObjects.put("one", toAdd);
        toAdd = new ValueChangeListenerTestImpl();
        attachedObjects.put("two", toAdd);
        toAdd = new ValueChangeListenerTestImpl();
        attachedObjects.put("three", toAdd);
        Object result = UIComponentBase.saveAttachedState(facesContext, attachedObjects);
        returnedAttachedObjects = (Map<String, ValueChangeListener>) UIComponentBase.restoreAttachedState(facesContext, result);
    }

    // Regression test for bug #907
    public void testAttachedObjectsCount() throws Exception {
        Set<ValueChangeListener> returnedAttachedObjects = null,
                attachedObjects = new HashSet<ValueChangeListener>();
        ValueChangeListener toAdd = new ValueChangeListenerTestImpl();
        attachedObjects.add(toAdd);
        toAdd = new ValueChangeListenerTestImpl();
        attachedObjects.add(toAdd);
        toAdd = new ValueChangeListenerTestImpl();
        attachedObjects.add(toAdd);
        Object result = UIComponentBase.saveAttachedState(facesContext, attachedObjects);
        returnedAttachedObjects = (Set<ValueChangeListener>) UIComponentBase.restoreAttachedState(facesContext, result);
        int firstSize = returnedAttachedObjects.size();
        returnedAttachedObjects = (Set<ValueChangeListener>) UIComponentBase.restoreAttachedState(facesContext, result);
        int secondSize = returnedAttachedObjects.size();
        assertEquals(firstSize, secondSize);
    }

    public void testFacesListenerState() {
        UIComponent component = new UIOutput();
        TestFacesListener listener = new TestFacesListener();
        listener.setValue("initial");
        component.addFacesListener(listener);
        component.markInitialState();
        assertTrue(component.initialStateMarked());
        assertTrue(listener.initialStateMarked());

        Object state = component.saveState(facesContext);
        assertNull(state);

        component = new UIOutput();
        listener = new TestFacesListener();
        component.addFacesListener(listener);
        listener.setValue("initial");
        component.markInitialState();
        listener.setValue("newvalue");
        state = component.saveState(facesContext);
        assertNotNull(state);

        // verify that state is applied to existing Listener instances.
        component = new UIOutput();
        listener = new TestFacesListener();
        component.addFacesListener(listener);
        listener.setValue("newinitial");
        component.restoreState(facesContext, state);
        assertTrue("newvalue".equals(listener.getValue()));

        // verify listeners are overwritten when using full state saving
        component = new UIOutput();
        listener = new TestFacesListener();
        component.addFacesListener(listener);
        listener.setValue("initial");
        state = component.saveState(facesContext);
        assertNotNull(state);
        listener.setValue("postsave");

        component.restoreState(facesContext, state);
        TestFacesListener l = (TestFacesListener) component.getFacesListeners(TestFacesListener.class)[0];
        assertTrue(l != listener);
        assertTrue("initial".equals(l.getValue()));
    }

    public void testTransientListenersState() {
        UIComponent output = new UIOutput();
        output.markInitialState();
        TestFacesListener l1 = new TestFacesListener();
        TestFacesListener l2 = new TestFacesListener();
        TestFacesListener l3 = new TestFacesListener();
        TestFacesListener l4 = new TestFacesListener();
        l1.setValue("l1");
        l2.setValue("l2");
        l3.setValue("l3");
        l4.setValue("l4");
        l2.setTransient(true);
        l4.setTransient(true);

        output.addFacesListener(l1);
        output.addFacesListener(l2);
        output.addFacesListener(l3);
        output.addFacesListener(l4);

        Object state = output.saveState(facesContext);
        assertNotNull(state);
        output = new UIOutput();
        output.restoreState(facesContext, state);
        FacesListener[] listeners = output.getFacesListeners(TestFacesListener.class);
        assertTrue(listeners.length == 2);
        assertEquals("l1", ((TestFacesListener) listeners[0]).getValue());
        assertEquals("l3", ((TestFacesListener) listeners[1]).getValue());

        output = new UIOutput();
        output.markInitialState();
        output.addFacesListener(l2);
        state = output.saveState(facesContext);
        assertNotNull(state);
        output = new UIOutput();
        output.restoreState(facesContext, state);
        listeners = output.getFacesListeners(TestFacesListener.class);
        assertTrue(listeners.length == 0);
    }

    public void testTransientListenersState2() {
        UIComponent output = new UIOutput();
        TestFacesListener l1 = new TestFacesListener();
        TestFacesListener l2 = new TestFacesListener();
        TestFacesListener l3 = new TestFacesListener();
        TestFacesListener l4 = new TestFacesListener();
        l1.setValue("l1");
        l2.setValue("l2");
        l3.setValue("l3");
        l4.setValue("l4");
        l2.setTransient(true);
        l4.setTransient(true);

        output.addFacesListener(l1);
        output.addFacesListener(l2);
        output.addFacesListener(l3);
        output.addFacesListener(l4);

        Object state = output.saveState(facesContext);
        assertNotNull(state);
        output = new UIOutput();
        output.restoreState(facesContext, state);
        FacesListener[] listeners = output.getFacesListeners(TestFacesListener.class);
        assertTrue(listeners.length == 2);
        assertEquals("l1", ((TestFacesListener) listeners[0]).getValue());
        assertEquals("l3", ((TestFacesListener) listeners[1]).getValue());

        output = new UIOutput();
        output.addFacesListener(l2);
        state = output.saveState(facesContext);
        assertNotNull(state);
        output = new UIOutput();
        output.restoreState(facesContext, state);
        listeners = output.getFacesListeners(TestFacesListener.class);
        assertTrue(listeners.length == 0);
    }

    // ---------------------------------------------------------- Nested Classes
    public static final class TestFacesListener implements FacesListener, PartialStateHolder {

        private boolean initialState;
        private String value;
        private boolean trans;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            clearInitialState();
            this.value = value;
        }

        @Override
        public void markInitialState() {
            initialState = true;
        }

        @Override
        public boolean initialStateMarked() {
            return initialState;
        }

        @Override
        public void clearInitialState() {
            initialState = false;
        }

        @Override
        public Object saveState(FacesContext context) {
            return ((!initialState) ? new Object[]{value} : null);
        }

        @Override
        public void restoreState(FacesContext context, Object state) {
            if (state != null) {
                Object[] values = (Object[]) state;
                value = (String) values[0];
            }
        }

        @Override
        public boolean isTransient() {
            return trans;
        }

        @Override
        public void setTransient(boolean trans) {
            this.trans = trans;
        }
    }
}
