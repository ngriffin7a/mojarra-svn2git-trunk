/*
 * $Id: TestBeanSubclass.java,v 1.3 2004/02/06 18:56:12 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;


/**
 * <p>Test JavaBean for managed object creation facility.</p>
 */

public class TestBeanSubclass extends AbstractTestBean {

    private String extraProperty = "Extra Property";


    public String getExtraProperty() {
        return this.extraProperty;
    }


    public void setExtraProperty(String extraProperty) {
        this.extraProperty = extraProperty;
    }

}
