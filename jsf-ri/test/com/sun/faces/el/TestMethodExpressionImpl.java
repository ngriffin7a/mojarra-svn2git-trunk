/*
 * $Id: TestMethodExpressionImpl.java,v 1.4 2005/08/22 22:11:14 ofung Exp $
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

// TestMethodRef.java
package com.sun.faces.el;

import com.sun.faces.ServletFacesTestCase;

import javax.el.MethodExpression;
import javax.el.ELException;
import javax.el.MethodNotFoundException;
import javax.faces.el.PropertyNotFoundException;

/**
 * <B>TestMethodRef </B> is a class ... <p/><B>Lifetime And Scope </B>
 * <P>
 * 
 * @version $Id: TestMethodExpressionImpl.java,v 1.4 2005/08/22 22:11:14 ofung Exp $
 */

public class TestMethodExpressionImpl extends ServletFacesTestCase
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

    public TestMethodExpressionImpl()
    {
        super("TestMethodExpression");
    }

    public TestMethodExpressionImpl(String name)
    {
        super(name);
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    protected MethodExpression create(String ref, Class[] params) throws Exception
    {
        return (getFacesContext().getApplication().getExpressionFactory().
            createMethodExpression(getFacesContext().getELContext(),ref, null, params));
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
            create("${foo > 1}", null);
            fail();
        }
        catch (ELException ee) {
            fail("Should have thrown a NullPointerException"); 
        }
        catch (NullPointerException npe) { }
    }
    
    public void testLiteralReference() throws Exception
    {
        boolean exceptionThrown = false;
        try
        {
            create("some.method", null);
        }
        catch (NullPointerException ee) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    public void testInvalidTrailing() throws Exception
    {
        MethodExpression mb = this.create(
                "#{NewCustomerFormHandler.redLectroidsMmmm}", new Class[0]);

        boolean exceptionThrown = false;
        try
        {
            mb.invoke(getFacesContext().getELContext(), new Object[0]);
        }
        catch (MethodNotFoundException me)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        mb = this.create("#{nonexistentBean.redLectroidsMmmm}", new Class[0]);
       
        exceptionThrown = false;
        try
        {
            mb.invoke(getFacesContext().getELContext(), new Object[0]);
        }
        catch (PropertyNotFoundException ne)
        {
            exceptionThrown = true;
        }
        catch (ELException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

} // end of class TestMethodRef
