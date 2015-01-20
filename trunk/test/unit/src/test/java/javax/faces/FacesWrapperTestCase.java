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
package javax.faces;

import java.io.File;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;

/**
 * <p>
 * A unit test to make sure all classes implementing {@link FacesWrapper} are
 * actually wrapping all public and protected methods of the wrapped class. This
 * should help to keep the wrapper classes in synch with the wrapped classes.
 * </p>
 */
public class FacesWrapperTestCase extends TestCase {

    private static List<Class<?>> wrapperClasses;
    private static List<Class<?>> noWrapperClasses;
    private static List<Method> methodsToIgnore;
    private static final String JAVAX_FACES_PKG = "javax.faces.";

    /**
     * Perform class-level initialization for test - lookup for classes
     * implementing FacesWrapper.
     * @throws java.lang.Exception
     */
    @Override
    protected void setUp() throws Exception {
        if (wrapperClasses == null) {
            loadWrapperClasses();
            methodsToIgnore = new ArrayList<Method>();
            methodsToIgnore.add(Object.class.getMethod("toString", new Class[0]));
        }
    }

    /**
     * Unit test to assert wrapperClasses list was loaded (see {@link #setUp()}.
     */
    public void testWrapperClassesLoaded() {
        assertNotNull(wrapperClasses);
        assertTrue("no wrapper classes found!", !wrapperClasses.isEmpty());
    }

    /**
     * Unit test to assert there are no *Wrapper classes not implementing
     * FacesWrapper.
     */
    public void testWrapperClassesImplementFacesWrapper() {
        assertNotNull(noWrapperClasses);
        if (noWrapperClasses.size() > 0) {
            System.out.println("Wrapper classes not implementing javax.faces.FacesWrapper:");
            System.out.println(noWrapperClasses.toString());
        }
        assertTrue("Found wrapper classes not implementing FacesWrapper!", noWrapperClasses
                .isEmpty());
    }

    /**
     * The main goal of this TestSuite: unit test to assert all classes
     * implementing FacesWrapper do wrap all public and protected methods of the
     * wrapped class.
     */
    public void testWrapperClassWrapsPublicAndProtectedMethods() {
        for (Class<?> wrapper : wrapperClasses) {
            if (wrapper.isInterface()) {
                continue;
            }
            List<Method> wrapperMethods = getPublicAndProtectedMethods(wrapper);
            List<Method> methodsToWrap = getPublicAndProtectedMethods(wrapper.getSuperclass());

            System.out.println("verify " + wrapper.getName() + " is wrapping "
                    + wrapper.getSuperclass().getName() + " well");
            String msg = wrapper.getCanonicalName() + " does not wrap method: ";
            for (Method m : methodsToWrap) {
                if (isMethodContained(m, methodsToIgnore)) {
                    continue;
                }
                assertTrue(msg + m.toString(), isMethodContained(m, wrapperMethods));
            }
        }
    }

    // private methods
    /**
     * Returns true it the passed method is contained in the also passed list of
     * methods by also comparing matching parameters.
     *
     * @param m the method (from the wrapped class) to compare against.
     * @param wrapperMethods the list of methods of the wrapper class.
     */
    private boolean isMethodContained(Method m, List<Method> wrapperMethods) {
        String name = m.getName();
        Class<?>[] paramTypes = m.getParameterTypes();
        Class<?> returnType = m.getReturnType();
        for (Method wm : wrapperMethods) {
            if (name.equals(wm.getName()) && Arrays.equals(paramTypes, wm.getParameterTypes())
                    && returnType == wm.getReturnType()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Collect public and protected methods of a class.
     *
     * @param wrapper the class to find its methods.
     * @return list of found methods.
     */
    private List<Method> getPublicAndProtectedMethods(Class<?> wrapper) {
        List<Method> mList = new ArrayList<Method>();
        if (Object.class == wrapper) {
            return mList;
        }

        Method[] methods = wrapper.getDeclaredMethods();
        for (Method m : methods) {
            int mod = m.getModifiers();
            if (!Modifier.isStatic(mod) && (Modifier.isPublic(mod) || Modifier.isProtected(mod))) {
                mList.add(m);
            }
        }
        return mList;
    }

    /**
     * Collect the wrapper classes.
     */
    private void loadWrapperClasses() {
        wrapperClasses = new ArrayList<Class<?>>();
        noWrapperClasses = new ArrayList<Class<?>>();

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            URL res = cl.getResource("javax/faces/Messages.properties");
            File javaxFacesPackage = new File(res.getFile()).getParentFile();
            collectWrapperClasses(cl, JAVAX_FACES_PKG, javaxFacesPackage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Walk package tree for collecting wrapper classes.
     *
     * @param cl the ClassLoader.
     * @param pkg package name.
     * @param file current File (directory or file)
     * @throws Exception might throw ClassNotFoundException from class loading.
     */
    private void collectWrapperClasses(ClassLoader cl, String pkg, File file) throws Exception {
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                collectWrapperClasses(cl, pkg + f.getName() + ".", f);
            } else {
                addWrapperClassToWrapperClassesList(cl, pkg, f);
            }
        }
    }

    /**
     * Add classes that are assignable to FacesWrapper class to the
     * wrapperClasses list - and also add classes with a name ending on
     * "Wrapper" but being not assignable to FacesWrapper to the
     * noWrapperClasses list.
     *
     * @param cl the ClasslOader used to load the class.
     * @param pkg the name of the package working in.
     * @param f the File to analyse.
     * @throws Exception ClassLoader exceptions.
     */
    private void addWrapperClassToWrapperClassesList(ClassLoader cl, String pkg, File f)
            throws Exception {
        String name = f.getName();
        if (!name.endsWith(".class")) {
            return;
        }
        String className = pkg + name.substring(0, name.length() - 6);
        Class<?> c = cl.loadClass(className);
        Class<?> wrappedClass = c.getSuperclass();
        if (wrappedClass != null) {
            // we are not interested in interfaces extending FacesWrapper interface.
            // also skip classes implementing FacesWrapper but extend from Object (e.g. factories).
            if (FacesWrapper.class.isAssignableFrom(wrappedClass) || wrappedClass == Object.class) {
                return;
            }
        }
        if (FacesWrapper.class.isAssignableFrom(c)) {
            wrapperClasses.add(c);
        } else if (c != FacesWrapper.class && c.getName().endsWith("Wrapper")) {
            noWrapperClasses.add(c);
        }
    }
}
