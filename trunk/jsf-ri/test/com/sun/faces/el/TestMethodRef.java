/*
 * $Id: TestMethodRef.java,v 1.7 2005/05/06 22:02:07 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestMethodRef.java
package com.sun.faces.el;

import com.sun.faces.ServletFacesTestCase;

import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.ReferenceSyntaxException;

/**
 * <B>TestMethodRef </B> is a class ... <p/><B>Lifetime And Scope </B>
 * <P>
 * 
 * @version $Id: TestMethodRef.java,v 1.7 2005/05/06 22:02:07 edburns Exp $
 */

public class TestMethodRef extends ServletFacesTestCase
{

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

    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public TestMethodRef()
    {
        super("TestMethodRef");
    }

    public TestMethodRef(String name)
    {
        super(name);
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    protected MethodBinding create(String ref, Class[] params) throws Exception
    {
        return (getFacesContext().getApplication().createMethodBinding(ref, params));
    }
    
    public void testNullReference() throws Exception
    {
        try
        {
            create(null, null);
            fail();
        }
        catch (NullPointerException npe) {}
        catch (Exception e) { fail("Should have thrown an NPE"); };
    }
    
    public void testInvalidMethod() throws Exception
    {
        try
        {
            create("#{foo > 1}", null);
            fail();
        }
        catch (ReferenceSyntaxException rse) {}
        catch (Exception e) { fail("Should have thrown a ReferenceSyntaxException"); }
    }
    
    public void testLiteralReference() throws Exception
    {
        try
        {
            create("some.method", null);
            fail();
        }
        catch (ReferenceSyntaxException rse) {}
        catch (Exception e) { fail("Should have thrown a ReferenceSyntaxException"); }
    }

    public void testInvalidTrailing() throws Exception
    {
        MethodBinding mb = this.create(
                "#{NewCustomerFormHandler.redLectroidsMmmm}", new Class[0]);

        boolean exceptionThrown = false;
        try
        {
            mb.invoke(getFacesContext(), new Object[0]);
        }
        catch (MethodNotFoundException e)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        mb = this.create("#{nonexistentBean.redLectroidsMmmm}", new Class[0]);
        
        exceptionThrown = false;
        try
        {
            mb.invoke(getFacesContext(), new Object[0]);
        }
        catch (MethodNotFoundException e)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

} // end of class TestMethodRef
