/*
 * $Id: UIGraphicTestCase.java,v 1.6 2003/04/29 18:51:52 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.Iterator;
import javax.faces.event.FacesEvent;
import javax.faces.validator.Validator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Test case for the <strong>javax.faces.UIGraphic</strong>
 * concrete class.</p>
 */

public class UIGraphicTestCase extends UIComponentTestCase {


    // ----------------------------------------------------- Instance Variables


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIGraphicTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods
// This class is necessary since UIGraphic isn't a naming container by
// default.
private class UIGraphicNamingContainer extends UIGraphic implements NamingContainer {
    private UINamingContainer namingContainer = null;

    UIGraphicNamingContainer() {
	namingContainer = new UINamingContainer();
    }

    public void addComponentToNamespace(UIComponent namedComponent) {
	namingContainer.addComponentToNamespace(namedComponent);
    }
    public void removeComponentFromNamespace(UIComponent namedComponent) {
	namingContainer.removeComponentFromNamespace(namedComponent);
    } 
    public UIComponent findComponentInNamespace(String name) {
	return namingContainer.findComponentInNamespace(name);
    }

    public String generateClientId() {
	return namingContainer.generateClientId();
    }
}



    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        component = new UIGraphicNamingContainer();
        component.setComponentId("test");
        attributes = new String[0];
        rendererType = "Image";

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UIGraphicTestCase.class));

    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        component = null;

    }


    // ------------------------------------------------ Individual Test Methods


    /**
     * [3.1.7] Attribute/Property Transparency
     */
    public void testAttributePropertyTransparency() {

        super.testAttributePropertyTransparency();
        UIGraphic graphic = (UIGraphic) component;

        // graphicName
        assertNull("url1", graphic.getURL());
        assertNull("url2", graphic.getValue());
        graphic.setURL("foo");
        assertEquals("url3", "foo", graphic.getURL());
        assertEquals("url4", "foo",
                     (String) graphic.getValue());
        graphic.setValue("bar");
        assertEquals("url5", "bar", graphic.getURL());
        assertEquals("url6", "bar",
                     (String) graphic.getValue());
        graphic.setValue(null);
        assertNull("url7", graphic.getURL());
        assertNull("url8", graphic.getValue());

    }


}
