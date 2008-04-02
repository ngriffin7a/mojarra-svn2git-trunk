/*
 * $Id: ValidateLongRangeTag.java,v 1.10 2004/04/06 15:02:04 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ValidateLongRangeTag.java

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.util.Util;

import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;

/**
 * ValidateLongRangeTag is the tag handler class for
 * <code>validate_longrange</code> tag.
 */

public class ValidateLongRangeTag extends MaxMinValidatorTag {

//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables
    protected String maximum_ = null;
    protected long maximum = 0;
    protected String minimum_ = null;
    protected long minimum = 0;


// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public ValidateLongRangeTag() {
        super();
    }

//
// Class methods
//

//
// General Methods
//

    public void setMaximum(String newMaximum) {
        maximumSet = true;
        maximum_ = newMaximum;
    }


    public void setMinimum(String newMinimum) {
        minimumSet = true;
        minimum_ = newMinimum;
    }

    public int doStartTag() throws JspException {
        super.setValidatorId("javax.faces.LongRange");
	return super.doStartTag();
    }


// 
// Methods from ValidatorTag
//

    protected Validator createValidator() throws JspException {
        LongRangeValidator result = null;

        result = (LongRangeValidator) super.createValidator();
        Util.doAssert(null != result);

        evaluateExpressions();
        if (maximumSet) {
            result.setMaximum(maximum);
        }

        if (minimumSet) {
            result.setMinimum(minimum);
        }

        return result;
    }

/* Evaluates expressions as necessary */
    private void evaluateExpressions() throws JspException {

        if (minimum_ != null) {
            if (Util.isVBExpression(minimum_)) {
                Number numberObj = (Number) Util.evaluateVBExpression(minimum_);
                Util.doAssert(null != numberObj);
                minimum = numberObj.longValue();
            } else {
                minimum = new Long(minimum_).longValue();
            }
        }
        if (maximum_ != null) {
            if (Util.isVBExpression(maximum_)) {
                Number numberObj = (Number) Util.evaluateVBExpression(maximum_);
                Util.doAssert(null != numberObj);
                maximum = numberObj.longValue();
            } else {
                maximum = new Long(maximum_).longValue();
            }
        }
    }

} // end of class ValidateLongRangeTag
