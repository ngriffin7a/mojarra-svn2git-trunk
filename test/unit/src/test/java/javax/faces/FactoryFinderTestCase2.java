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

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit tests for {@link UISelectBooleanBase}.</p>
 */
public class FactoryFinderTestCase2 extends TestCase {

    // ------------------------------------------------------------ Constructors
    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public FactoryFinderTestCase2(String name) {
        super(name);
    }
    
        public static String FACTORIES[][] = {
	{ FactoryFinder.APPLICATION_FACTORY, 
	  "com.sun.faces.mock.MockApplicationFactory"
	},
	{ FactoryFinder.EXTERNAL_CONTEXT_FACTORY, 
	  "com.sun.faces.mock.MockExternalContextFactory"
	},
	{ FactoryFinder.FACES_CONTEXT_FACTORY, 
	  "com.sun.faces.mock.MockFacesContextFactory"
	},
	{ FactoryFinder.LIFECYCLE_FACTORY, 
	  "com.sun.faces.mock.MockLifecycleFactory"
	},
	{ FactoryFinder.RENDER_KIT_FACTORY, 
	  "com.sun.faces.mock.MockRenderKitFactory"
	}
    };

    // ---------------------------------------------------- Overall Test Methods
    // Set up instance variables required by this test case.
    @Override
    public void setUp() throws Exception {
        super.setUp();
        for (int i = 0, len = FactoryFinderTestCase2.FACTORIES.length; i < len; i++) {
            System.getProperties().remove(FactoryFinderTestCase2.FACTORIES[i][0]);
        }
    }

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(FactoryFinderTestCase2.class));
    }

    // Tear down instance variables required by ths test case
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        FactoryFinder.releaseFactories();
        for (int i = 0, len = FactoryFinderTestCase2.FACTORIES.length; i < len; i++) {
            System.getProperties().remove(FactoryFinderTestCase2.FACTORIES[i][0]);
        }
    }

    // ------------------------------------------------- Individual Test Methods
    /**
     * <p>
     * In the absence of webapp faces-config.xml and META-INF/services, verify
     * that the overrides specified in the implementation faces-config.xml take
     * precedence.</p>
     * @throws java.lang.Exception
     */
    public void testJSFImplCase() throws Exception {
        Object factory = null;
        Class clazz = null;

        FactoryFinder.releaseFactories();
        int len, i = 0;

	// this testcase only simulates the "faces implementation
        // specific" part
        for (i = 0, len = FactoryFinderTestCase2.FACTORIES.length; i < len; i++) {
            FactoryFinder.setFactory(FactoryFinderTestCase2.FACTORIES[i][0],
                    FactoryFinderTestCase2.FACTORIES[i][1]);
        }

        for (i = 0, len = FactoryFinderTestCase2.FACTORIES.length; i < len; i++) {
            clazz = Class.forName(FactoryFinderTestCase2.FACTORIES[i][0]);
            factory = FactoryFinder.getFactory(FactoryFinderTestCase2.FACTORIES[i][0]);
            assertTrue("Factory for " + clazz.getName()
                    + " not of expected type.",
                    clazz.isAssignableFrom(factory.getClass()));
            clazz = Class.forName(FactoryFinderTestCase2.FACTORIES[i][1]);
            assertTrue("Factory " + FactoryFinderTestCase2.FACTORIES[i][1] + " not of expected type",
                    clazz.isAssignableFrom(factory.getClass()));
        }
    }
}
