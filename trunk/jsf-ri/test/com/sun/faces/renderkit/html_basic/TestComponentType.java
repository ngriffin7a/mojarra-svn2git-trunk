/*
 * $Id: TestComponentType.java,v 1.5 2003/08/21 14:18:22 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestComponentType.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.FacesException;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectMany;
import javax.faces.component.base.UISelectManyBase;
import javax.faces.component.base.UIOutputBase;

import java.util.Iterator;


import java.io.IOException;

import org.apache.cactus.ServletTestCase;
import junit.framework.TestCase;

/**
 *
 *  <B>TestComponentType.java</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestComponentType.java,v 1.5 2003/08/21 14:18:22 rlubke Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

    public class TestComponentType extends TestCase // ServletTestCase
{
//
// Protected Constants
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

    public TestComponentType() {super("TestComponentType.java");}
    public TestComponentType(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//

    public void testComponentTypeCheck() {

        MenuRenderer mr = new MenuRenderer();

        // case 1: UISelectMany component
        
        UISelectMany many = new UISelectManyBase();
        String multipleText = mr.getMultipleText(many);
        assertTrue(multipleText.equals(" multiple "));

        // case 2: UISelectMany subclass component

        MyComponent myC = new MyComponent();
        multipleText = mr.getMultipleText(myC);
        assertTrue(multipleText.equals(" multiple "));

        // case 3: UIOutput component

        UIOutput output = new UIOutputBase();
        multipleText = mr.getMultipleText(output);
        assertTrue(!multipleText.equals(" multiple "));
        assertTrue(multipleText.equals(""));
    }


    public class MyComponent extends UISelectManyBase {
    }


} // end of class TestComponentType
