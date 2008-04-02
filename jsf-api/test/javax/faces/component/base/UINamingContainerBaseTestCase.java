/*
 * $Id: UINamingContainerBaseTestCase.java,v 1.2 2003/07/26 17:55:23 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UINamingContainerBase}.</p>
 */

public class UINamingContainerBaseTestCase extends UIComponentBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UINamingContainerBaseTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UINamingContainerBase();
        expectedId = null;
        expectedRendererType = null;
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UINamingContainerBaseTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Negative tests for findComponent()
    public void testFindComponentNegative() {

	UINamingContainerBase
	    containerOne = new UINamingContainerBase(),
	    containerTwo = new UINamingContainerBase(),
	    containerThree = new UINamingContainerBase();
	UIComponent 
	    leafOne = new TestComponent(),
	    leafTwo = new TestComponent(),
	    leafThree = new TestComponent();

	containerOne.setId("containerOne");
	containerTwo.setId("containerTwo");
	containerThree.setId("containerThree");
	
	leafOne.setId("leafOne");
	leafTwo.setId("leafTwo");
	leafThree.setId("leafThree");

	component.getChildren().add(containerOne);
	containerOne.getChildren().add(leafOne);
	containerOne.getChildren().add(containerTwo);
	
	containerTwo.getChildren().add(leafTwo);
	containerTwo.getChildren().add(containerThree);

	containerThree.getChildren().add(leafThree);

	// try invalid expressions
	boolean exceptionThrown = false;
	try {
	    component.findComponent(".");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Expected IllegalArgumentException", exceptionThrown);

	exceptionThrown = false;
	try {
	    component.findComponent("containerOne.");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Expected IllegalArgumentException", exceptionThrown);
	
	exceptionThrown = false;
	try {
	    component.findComponent("containerOne.containerTwo.");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Expected IllegalArgumentException", exceptionThrown);

	exceptionThrown = false;
	try {
	    component.findComponent("containerOne.containerTwo.containerThree.");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Expected IllegalArgumentException", exceptionThrown);

	// Throw a non-NamingContainer in the middle
	exceptionThrown = false;
	try {
	    component.findComponent("containerOne.leafOne.containerTwo");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Expected IllegalArgumentException", exceptionThrown);

	exceptionThrown = false;
	try {
	    component.findComponent("containerTwo.containerOne");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Unexpected IllegalArgumentException", !exceptionThrown);

	exceptionThrown = false;
	try {
	    component.findComponent("containerOne.containerTwo.leafThree");
	}
	catch (IllegalArgumentException e) {
	    exceptionThrown = true;
	}
	assertTrue("Unexpected IllegalArgumentException", !exceptionThrown);


    }


    // Positive tests for findComponent()
    public void testFindComponentPositive() {

	UINamingContainerBase
	    containerOne = new UINamingContainerBase(),
	    containerTwo = new UINamingContainerBase(),
	    containerThree = new UINamingContainerBase();
	UIComponent 
	    leafOne = new TestComponent(),
	    leafTwo = new TestComponent(),
	    leafThree = new TestComponent();

	containerOne.setId("containerOne");
	containerTwo.setId("containerTwo");
	containerThree.setId("containerThree");
	
	leafOne.setId("leafOne");
	leafTwo.setId("leafTwo");
	leafThree.setId("leafThree");

	component.getChildren().add(containerOne);
	containerOne.getChildren().add(leafOne);
	containerOne.getChildren().add(containerTwo);
	
	containerTwo.getChildren().add(leafTwo);
	containerTwo.getChildren().add(containerThree);

	containerThree.getChildren().add(leafThree);

	// Test the NamingContainers are found
	assertEquals("find containerOne", containerOne,
		     component.findComponent("containerOne"));
	assertEquals("find containerTwo", containerTwo,
		     component.findComponent("containerOne.containerTwo"));
	assertEquals("find containerThree", containerThree,
		     component.findComponent("containerOne.containerTwo.containerThree"));

	// Test the leaves are found
	assertEquals("find leafOne", leafOne,
		     component.findComponent("containerOne.leafOne"));
	assertEquals("find leafTwo", leafTwo,
		     component.findComponent("containerOne.containerTwo.leafTwo"));
	assertEquals("find leafThree", leafThree,
		     component.findComponent("containerOne.containerTwo.containerThree.leafThree"));

    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine UINamingContainerBase instance
    public void testPristine() {

        super.testPristine();
        UINamingContainerBase namingContainer =
            (UINamingContainerBase) component;

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UINamingContainerBase namingContainer =
            (UINamingContainerBase) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UINamingContainerBase namingContainer =
            (UINamingContainerBase) component;

    }



}
