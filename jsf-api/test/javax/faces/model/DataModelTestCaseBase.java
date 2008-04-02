/*
 * $Id: DataModelTestCaseBase.java,v 1.4 2003/10/15 20:16:02 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.model;


import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.beanutils.PropertyUtils;


/**
 * <p>Abstract base class for {@link DataModel} tests.</p>
 */

public abstract class DataModelTestCaseBase extends TestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public DataModelTestCaseBase(String name) {

        super(name);

    }


    // ------------------------------------------------------ Instance Variables


    // The array of beans we will be wrapping (must be initialized before setUp)
    protected TestBean beans[] = new TestBean[0];


    // The DataModel we are testing
    protected DataModel model = null;


    // ---------------------------------------------------- Overall Test Methods


    // Configure the properties of the beans we will be wrapping
    protected void configure() {

        for (int i = 0; i < beans.length; i++) {
            TestBean bean = beans[i];
            bean.setBooleanProperty((i % 2) == 0);
            bean.setBooleanSecond(!bean.getBooleanProperty());
            bean.setByteProperty((byte) i);
            bean.setDoubleProperty(((double) i) * 100.0);
            bean.setFloatProperty(((float) i) * ((float) 10.0));
            bean.setIntProperty(1000 * i);
            bean.setLongProperty((long) 10000 * (long) i);
            bean.setStringProperty("This is string " + i);
        }

    }


    // Set up instance variables required by this test case.
    public void setUp() throws Exception {

        // Subclasses must create "beans", call "configure()", create "model"
        super.setUp();

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(DataModelTestCaseBase.class));

    }


    // Tear down instance variables required by ths test case
    public void tearDown() throws Exception {

        super.tearDown();
        beans = null;
        model = null;

    }


    // ------------------------------------------------- Individual Test Methods


    // Test invalid arguments to listener methods
    public void testInvalidListeners() throws Exception {

        try {
            model.addDataModelListener(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }

        try {
            model.removeDataModelListener(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }


    }


    // Test positioning to all rows in ascending order
    public void testPositionAscending() throws Exception {

        StringBuffer sb = new StringBuffer();
        model.addDataModelListener(new TestListener());
        TestListener.trace(null);

        int n = model.getRowCount();
        for (int i = 0; i < n; i++) {
            checkRow(i);
            sb.append("/" + i);
        }
        assertEquals(sb.toString(), TestListener.trace());

    }


    // Test positioning to all rows in descending order
    public void testPositionDescending() throws Exception {

        StringBuffer sb = new StringBuffer();
        model.addDataModelListener(new TestListener());
        TestListener.trace(null);

        int n = model.getRowCount();
        for (int i = (n - 1); i >= 0; i--) {
            checkRow(i);
            sb.append("/" + i);
        }
        assertEquals(sb.toString(), TestListener.trace());

    }


    // Test a pristine DataModel instance
    public void testPristine() throws Exception {

        // Unopened instance
        assertNotNull("beans exists", beans);
        assertNotNull("model exists", model);

        // Correct row count
        assertEquals("correct row count", beans.length, model.getRowCount());

    }


    // Test removing listener
    public void testRemoveListener() throws Exception {

        TestListener listener = new TestListener();
        TestListener.trace(null);
        model.addDataModelListener(listener);
        model.setRowIndex(0);
        model.setRowIndex(0); // No movement so no event
        model.setRowIndex(-1);
        model.removeDataModelListener(listener);
        model.setRowIndex(0);
        assertEquals("/0/-1", TestListener.trace());

    }


    // Test the ability to update through the Map returned by getRowData()
    public void testRowData() throws Exception {

        // Retrieve the row data for row zero
        model.setRowIndex(0);
        Object data = model.getRowData();
        assertNotNull(data);

        // Modify several property values
        TestBean bean = beans[0];
        bean.setBooleanProperty(!bean.getBooleanProperty());
        if (data instanceof Map) {
            ((Map) data).put("booleanProperty",
                             bean.getBooleanProperty() ?
                             Boolean.TRUE : Boolean.FALSE);
        } else {
            PropertyUtils.setSimpleProperty(data, "booleanProperty",
                                            bean.getBooleanProperty() ?
                                            Boolean.TRUE : Boolean.FALSE);
        }
        bean.setIntProperty(bean.getIntProperty() + 5);
        if (data instanceof Map) {
            ((Map) data).put("intProperty",
                             new Integer(bean.getIntProperty()));
        } else {
            PropertyUtils.setSimpleProperty(data, "intProperty",
                                            new Integer(bean.getIntProperty()));
        }
        bean.setStringProperty(bean.getStringProperty() + "XYZ");
        if (data instanceof Map) {
            ((Map) data).put("stringProperty",
                             bean.getStringProperty() + "XYZ");
        } else {
            PropertyUtils.setSimpleProperty(data, "stringProperty",
                                            bean.getStringProperty());
        }

        // Ensure that all the modifications flowed through to beans[0]
        assertEquals(bean.getBooleanProperty(),
                     beans[0].getBooleanProperty());
        assertEquals(bean.isBooleanSecond(),
                     beans[0].isBooleanSecond());
        assertEquals(bean.getByteProperty(),
                     beans[0].getByteProperty());
        assertEquals(bean.getDoubleProperty(),
                     beans[0].getDoubleProperty(), 0.005);
        assertEquals(bean.getFloatProperty(),
                     beans[0].getFloatProperty(), (float) 0.005);
        assertEquals(bean.getIntProperty(),
                     beans[0].getIntProperty());
        assertEquals(bean.getLongProperty(),
                     beans[0].getLongProperty());
        assertEquals(bean.getStringProperty(),
                     beans[0].getStringProperty());

    }


    // Test row index manipulations
    public void testRowIndex() throws Exception {

        assertEquals("correct row index", -1, model.getRowIndex());

        // Positive setRowIndex() tests
        model.setRowIndex(0);
        model.setRowIndex(-1);

        // Negative setRowIndex() tests
        try {
            model.setRowIndex(-2);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }
        try {
            model.setRowIndex(model.getRowCount());
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

    }


    // ------------------------------------------------------- Protected Methods


    protected TestBean data() throws Exception {

        Object data = model.getRowData();
        assertNotNull(data);
        assertTrue(data instanceof TestBean);
        return ((TestBean) data);

    }


    protected void checkRow(int i) throws Exception {

        model.setRowIndex(i);
        String prompt = "Row " + i + " property ";
        TestBean bean = data();
        assertNotNull("Row " + i + " data", bean);
        assertEquals(prompt + "booleanProperty",
                     beans[i].getBooleanProperty(),
                     bean.getBooleanProperty());
        assertEquals(prompt + "booleanSecond",
                     beans[i].isBooleanSecond(),
                     bean.isBooleanSecond());
        assertEquals(prompt + "byteProperty",
                     beans[i].getByteProperty(),
                     bean.getByteProperty());
        assertEquals(prompt + "doubleProperty",
                     "" + beans[i].getDoubleProperty(),
                     "" + bean.getDoubleProperty());
        assertEquals(prompt + "floatProperty",
                     "" + beans[i].getFloatProperty(),
                     "" + bean.getFloatProperty());
        assertEquals(prompt + "intProperty",
                     beans[i].getIntProperty(),
                     bean.getIntProperty());
        assertEquals(prompt + "longProperty",
                     beans[i].getLongProperty(),
                     bean.getLongProperty());
        assertEquals(prompt + "nullProperty",
                     beans[i].getNullProperty(),
                     bean.getNullProperty());
        assertEquals(prompt + "readOnlyProperty",
                     beans[i].getReadOnlyProperty(),
                     bean.getReadOnlyProperty());
        assertEquals(prompt + "shortProperty",
                     beans[i].getShortProperty(),
                     bean.getShortProperty());
        assertEquals(prompt + "stringProperty",
                     beans[i].getStringProperty(),
                     bean.getStringProperty());
        assertEquals(prompt + "writeOnlyProperty",
                     beans[i].getWriteOnlyPropertyValue(),
                     bean.getWriteOnlyPropertyValue());

    }


}
