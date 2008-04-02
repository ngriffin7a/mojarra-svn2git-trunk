/*
 * $Id: TestInvokeApplicationPhase.java,v 1.23 2004/02/26 20:34:28 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestInvokeApplicationPhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.ServletFacesTestCase;
import org.apache.cactus.WebRequest;

import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;

/**
 * <B>TestInvokeApplicationPhase</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestInvokeApplicationPhase.java,v 1.23 2004/02/26 20:34:28 eburns Exp $
 */

public class TestInvokeApplicationPhase extends ServletFacesTestCase {

//
// Protected Constants
//

    public static final String DID_COMMAND = "didCommand";
    public static final String DID_FORM = "didForm";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestInvokeApplicationPhase() {
        super("TestInvokeApplicationPhase");
    }


    public TestInvokeApplicationPhase(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//

    public void testInvokeNormal() {
    }


    public void testInvokeNoOp() {
        UIInput root = new UIInput();
        UIViewRoot page = new UIViewRoot();
        page.setViewId("default.xul");
        Phase invokeApplicationPhase = new InvokeApplicationPhase();
        getFacesContext().setViewRoot(page);

        invokeApplicationPhase.execute(getFacesContext());
        assertTrue(!(getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));
    }

} // end of class TestInvokeApplicationPhase
