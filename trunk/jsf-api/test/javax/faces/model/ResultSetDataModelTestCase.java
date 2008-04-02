/*
 * $Id: ResultSetDataModelTestCase.java,v 1.1 2003/10/12 05:07:25 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.model;


import javax.faces.context.FacesContext;
import javax.faces.mock.MockResultSet;
import javax.faces.mock.MockResultSetMetaData;
import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link ArrayDataModel}.</p>
 */

public class ResultSetDataModelTestCase extends DataModelTestCaseBase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ResultSetDataModelTestCase(String name) {

        super(name);

    }


    // ------------------------------------------------------ Instance Variables


    // The ResultSet passed to our ResultSetDataModel
    protected MockResultSet result = null;


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() throws Exception {

        beans = new TestBean[5];
        for (int i = 0; i < beans.length; i++) {
            beans[i] = new TestBean();
        }
        configure();
        result = new MockResultSet(beans);
        model = new ResultSetDataModel(result);
        super.setUp();

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(ResultSetDataModelTestCase.class));

    }


    // ------------------------------------------------- Individual Test Methods


    // ------------------------------------------------------- Protected Methods


    protected TestBean data() throws Exception {

        Object data = model.getRowData();
        assertTrue(data instanceof ResultSetDataModel);
        assertTrue(data == model);
        TestBean bean = new TestBean();
        ResultSetDataModel rsdm = (ResultSetDataModel) model;

        bean.setBooleanProperty
            (((Boolean) rsdm.value("booleanProperty")).booleanValue());
        bean.setBooleanSecond
            (((Boolean) rsdm.value("booleanSecond")).booleanValue());
        bean.setByteProperty
            (((Byte) rsdm.value("byteProperty")).byteValue());
        bean.setDoubleProperty
            (((Double) rsdm.value("doubleProperty")).doubleValue());
        bean.setFloatProperty
            (((Float) rsdm.value("floatProperty")).floatValue());
        bean.setIntProperty
            (((Integer) rsdm.value("intProperty")).intValue());
        bean.setLongProperty
            (((Long) rsdm.value("longProperty")).longValue());
        bean.setNullProperty((String) rsdm.value("nullProperty"));
        bean.setShortProperty
            (((Short) rsdm.value("shortProperty")).shortValue());
        bean.setStringProperty((String) rsdm.value("stringProperty"));
        bean.setWriteOnlyProperty
            ((String) rsdm.value("writeOnlyPropertyValue"));

        return (bean);

    }


}
