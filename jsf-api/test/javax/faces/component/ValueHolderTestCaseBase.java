/*
 * $Id: ValueHolderTestCaseBase.java,v 1.12 2006/03/07 18:28:10 edburns Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.component;


import java.beans.PropertyDescriptor;
import java.util.WeakHashMap;
import java.lang.reflect.Field;
import java.util.Map;
import javax.faces.convert.Converter;
import javax.faces.convert.LongConverter;
import javax.faces.convert.NumberConverter;
import javax.faces.convert.ShortConverter;
import javax.faces.component.html.HtmlInputText;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link ValueHolder}.  Any test case for a
 * component class that implements {@link ValueHolder} should
 * extend this class.</p>
 */

public abstract class ValueHolderTestCaseBase extends UIComponentBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ValueHolderTestCaseBase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIOutput();
        expectedId = null;
        expectedRendererType = "Text";
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(ValueHolderTestCaseBase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods

    public void testAttributesTransparencyNonDeterministic() throws Exception {
	final int numThreads = 30;
	final Boolean outcomes[] = new Boolean[numThreads];
	Runnable runnables[] = new Runnable[numThreads];
        int i = 0;
        
        for (i = 0; i < outcomes.length; i++) {
            outcomes[i] = null;
        }

	for (i = 0; i < runnables.length; i++) {
	    runnables[i] = new Runnable() {
                public void run() {
                    int threadNum = 0;
                    try {
                        threadNum = Integer.valueOf(Thread.currentThread().
                                getName()).intValue();
                    } catch (NumberFormatException ex) {
                        fail("Expected thread name to be an integer");
                    }
                    // Even threadNums use HtmlInputText, odd use this component
                    boolean isEven = (threadNum % 2) == 0;
                    ValueHolder vh = null;
                    UIComponent newComp = null;
                    if (isEven) {
                        newComp = new HtmlInputText();
                        vh = (ValueHolder) newComp;
                    }
                    else {
                        try {
                            newComp = ValueHolderTestCaseBase.this.component.getClass().newInstance();
                            vh = (ValueHolder) newComp;
                                    
                        } catch (IllegalAccessException ex) {
                            fail("Can't instantiate class of " + ValueHolderTestCaseBase.this.component.getClass().getName());
                        } catch (InstantiationException ex) {
                            fail("Can't instantiate class of " + ValueHolderTestCaseBase.this.component.getClass().getName());
                        }
                    }
                    try {
                        boolean result = doTestAttributesTransparency(vh, newComp);
                        outcomes[threadNum] = new Boolean(result);
                    }
                    catch (Throwable e) {
                        outcomes[threadNum] = new Boolean(false);
                    }
                }
            };
	}
        clearDescriptors();
        Thread thread = null;
        for (i = 0; i < runnables.length; i++) {
            thread = new Thread(runnables[i], "" + i);
            thread.start();
        }
        
        // Keep polling the outcomes array until there are no nulls.
        boolean foundNull = false;
        while (!foundNull) {
            for (i = 0; i < outcomes.length; i++) {
                if (null != outcomes[i]) {
                    foundNull = true;
                }
            }
            Thread.currentThread().sleep(500);
        }
		    
        for (i = 0; i < outcomes.length; i++) {
            if (!outcomes[i].booleanValue()) {
                fail("Thread " + i + " failed");
            }
        }

    }
    
    private void clearDescriptors() throws Exception {
        Field descriptorsField = UIComponentBase.class.getDeclaredField("descriptors");
        descriptorsField.setAccessible(true);
        WeakHashMap<String,Map<String,PropertyDescriptor>> descriptors = 
                (WeakHashMap) descriptorsField.get(null);
        descriptors.clear();        
    }

    public void testAttributesTransparency() {
        super.testAttributesTransparency();
        ValueHolder vh = (ValueHolder) component;
        doTestAttributesTransparency(vh, component);
    }

    // Test attribute-property transparency
    public boolean doTestAttributesTransparency(ValueHolder vh, UIComponent newComp) {


        assertEquals(vh.getValue(),
                     (String) newComp.getAttributes().get("value"));
        vh.setValue("foo");
        assertEquals("foo", (String) newComp.getAttributes().get("value"));
        vh.setValue(null);
        assertNull((String) newComp.getAttributes().get("value"));
        newComp.getAttributes().put("value", "bar");
        assertEquals("bar", vh.getValue());
        newComp.getAttributes().put("value", null);
        assertNull(vh.getValue());

        assertEquals(vh.getConverter(),
                     (String) newComp.getAttributes().get("converter"));
        vh.setConverter(new LongConverter());
        assertNotNull((Converter) newComp.getAttributes().get("converter"));
        assertTrue(newComp.getAttributes().get("converter")
                   instanceof LongConverter);
        vh.setConverter(null);
        assertNull(newComp.getAttributes().get("converter"));
        newComp.getAttributes().put("converter", new ShortConverter());
        assertNotNull(vh.getConverter());
        assertTrue(vh.getConverter() instanceof ShortConverter);
        newComp.getAttributes().put("converter", null);
        assertNull(vh.getConverter());

        return true;
    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine ValueHolderBase instance
    public void testPristine() {

        super.testPristine();
        ValueHolder vh = (ValueHolder) component;

        // Validate properties
        assertNull("no value", vh.getValue());
        assertNull("no converter", vh.getConverter());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        ValueHolder vh = (ValueHolder) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        ValueHolder vh = (ValueHolder) component;

        // value
        vh.setValue("foo.bar");
        assertEquals("expected value",
                     "foo.bar", vh.getValue());
        vh.setValue(null);
        assertNull("erased value", vh.getValue());

        // converter
        vh.setConverter(new LongConverter());
        assertTrue("expected converter",
                   vh.getConverter() instanceof LongConverter);
        vh.setConverter(null);
        assertNull("erased converter", vh.getConverter());
    }


    // --------------------------------------------------------- Support Methods


    // Check that the properties of the NumberConverters are equal
    protected void checkNumberConverter(NumberConverter nc1,
                                        NumberConverter nc2) {

        assertEquals(nc1.getCurrencyCode(), nc2.getCurrencyCode());
        assertEquals(nc1.getCurrencySymbol(), nc2.getCurrencySymbol());
        assertEquals(nc1.isGroupingUsed(), nc2.isGroupingUsed());
        assertEquals(nc1.isIntegerOnly(), nc2.isIntegerOnly());
        assertEquals(nc1.getMaxFractionDigits(), nc2.getMaxFractionDigits());
        assertEquals(nc1.getMaxIntegerDigits(), nc2.getMaxIntegerDigits());
        assertEquals(nc1.getMinFractionDigits(), nc2.getMinFractionDigits());
        assertEquals(nc1.getMinIntegerDigits(), nc2.getMinIntegerDigits());
        assertEquals(nc1.getLocale(), nc2.getLocale());
        assertEquals(nc1.getPattern(), nc2.getPattern());
        assertEquals(nc1.getType(), nc2.getType());

    }


    // Check that the properties on the specified components are equal
    protected void checkProperties(UIComponent comp1, UIComponent comp2) {

        super.checkProperties(comp1, comp2);
        ValueHolder vh1 = (ValueHolder) comp1;
        ValueHolder vh2 = (ValueHolder) comp2;
        assertEquals(vh1.getValue(), vh2.getValue());
        checkNumberConverter((NumberConverter) vh1.getConverter(),
                             (NumberConverter) vh2.getConverter());
    }


    // Populate a pristine component to be used in state holder tests
    protected void populateComponent(UIComponent component) {

        super.populateComponent(component);
        ValueHolder vh = (ValueHolder) component;
        vh.setValue("component value");
        vh.setConverter(createNumberConverter());

    }


    // Create and configure a NumberConverter
    protected NumberConverter createNumberConverter() {

        NumberConverter nc = new NumberConverter();
        nc.setCurrencyCode("USD");
        nc.setCurrencySymbol("$");
        nc.setGroupingUsed(false);
        nc.setIntegerOnly(true);
        nc.setMaxFractionDigits(2);
        nc.setMaxIntegerDigits(10);
        nc.setMinFractionDigits(2);
        nc.setMinIntegerDigits(5);
        nc.setType("currency");
        return (nc);

    }


    protected void checkNumberConverters(NumberConverter nc1,
                               NumberConverter nc2) {

        assertNotNull(nc1);
        assertNotNull(nc2);
        assertEquals(nc1.getCurrencyCode(), nc2.getCurrencyCode());
        assertEquals(nc1.getCurrencySymbol(), nc2.getCurrencySymbol());
        assertEquals(nc1.isGroupingUsed(), nc2.isGroupingUsed());
        assertEquals(nc1.isIntegerOnly(), nc2.isIntegerOnly());
        assertEquals(nc1.getMaxFractionDigits(), nc2.getMaxFractionDigits());
        assertEquals(nc1.getMaxIntegerDigits(), nc2.getMaxIntegerDigits());
        assertEquals(nc1.getMinFractionDigits(), nc2.getMinFractionDigits());
        assertEquals(nc1.getMinIntegerDigits(), nc2.getMinIntegerDigits());
        assertEquals(nc1.getLocale(), nc2.getLocale());
        assertEquals(nc1.getPattern(), nc2.getPattern());
        assertEquals(nc1.getType(), nc2.getType());

    }


}
