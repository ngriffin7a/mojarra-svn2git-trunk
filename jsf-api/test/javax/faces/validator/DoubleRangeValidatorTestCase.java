/*
 * $Id: DoubleRangeValidatorTestCase.java,v 1.2 2005/05/20 14:49:57 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import java.util.Locale;

import javax.faces.component.UIInput;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Unit tests for {@link DoubleRangeValidator}.</p>
 */

public class DoubleRangeValidatorTestCase extends ValidatorTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public DoubleRangeValidatorTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(DoubleRangeValidatorTestCase.class));
    }


    // ------------------------------------------------- Individual Test Methods

    public void testLocaleHonored() {
	DoubleRangeValidator validator = new DoubleRangeValidator();
	validator.setMinimum(10.1);
	validator.setMaximum(20.1);
	boolean exceptionThrown = false;
	UIInput component = new UIInput();
	String message;
	Locale.setDefault(Locale.US);
	facesContext.getViewRoot().setLocale(Locale.US);
	
	try {
	    validator.validate(facesContext, component, "5.1");
	    fail("Exception not thrown");
	}
	catch (ValidatorException e) {
	    exceptionThrown = true;
	    message = e.getMessage();
	    assertTrue("message: \"" + message + "\" missing localized chars.",
		       -1 != message.indexOf("10.1"));
	    assertTrue("message: \"" + message + "\" missing localized chars.",
		       -1 != message.indexOf("20.1"));
	}
	assertTrue(exceptionThrown);

	exceptionThrown = false;
	Locale.setDefault(Locale.GERMAN);
	facesContext.getViewRoot().setLocale(Locale.GERMAN);

	try {
	    validator.validate(facesContext, component, "5");
	    fail("Exception not thrown");
	}
	catch (ValidatorException e) {
	    exceptionThrown = true;
	    message = e.getMessage();
	    assertTrue("message: \"" + message + "\" missing localized chars.",
		       -1 != message.indexOf("10,1"));
	    assertTrue("message: \"" + message + "\" missing localized chars.",
		       -1 != message.indexOf("20,1"));
	}
	assertTrue(exceptionThrown);

    }

    public void testHashCode() {
        DoubleRangeValidator validator1 = new DoubleRangeValidator();
        DoubleRangeValidator validator2 = new DoubleRangeValidator();

        validator1.setMinimum(10.0d);
        validator1.setMaximum(15.1d);
        validator2.setMinimum(10.0d);
        validator2.setMaximum(15.1d);

        assertTrue(validator1.hashCode() == validator2.hashCode());
        assertTrue(validator1.hashCode() == validator2.hashCode());

        validator2.setMaximum(15.2d);

        assertTrue(validator1.hashCode() != validator2.hashCode());

        validator1 = new DoubleRangeValidator();
        validator2 = new DoubleRangeValidator();

        validator1.setMinimum(10.0d);
        validator2.setMinimum(10.0d);

        assertTrue(validator1.hashCode() == validator2.hashCode());
        assertTrue(validator1.hashCode() == validator2.hashCode());

        validator1.setMinimum(11.0d);

        assertTrue(validator1.hashCode() != validator2.hashCode());

        validator1.setMinimum(10.0d);
        validator1.setMaximum(10.1d);

        assertTrue(validator1.hashCode() != validator2.hashCode());
    }

}
