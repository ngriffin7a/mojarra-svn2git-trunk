/*
 * $Id: FormRenderer.java,v 1.7 2001/11/21 22:32:39 visvan Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

// FormRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.WForm;
import javax.faces.WComponent;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>FormRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FormRenderer.java,v 1.7 2001/11/21 22:32:39 visvan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class FormRenderer extends Object implements Renderer
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
    public FormRenderer()
    {
        super();
        // ParameterCheck.nonNull();
        this.init();
    }

    protected void init()
    {
        // super.init();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //

    public Iterator getSupportedAttributeNames(String componentType) {
        return null;
    }

    public boolean supportsType(String componentType) {
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals("WForm")) {
            supports = true;
        }
        return supports;
    }

    public boolean supportsType(WComponent c) {

        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof WForm ) {
            supports = true;
        }
        return supports;
    }


    public void renderStart(RenderContext rc, WComponent c ) 
        throws IOException, FacesException {

        // render the form
        ParameterCheck.nonNull(rc);
        ParameterCheck.nonNull(c);

        OutputMethod outputMethod = rc.getOutputMethod();
        Assert.assert_it(outputMethod != null );
        
        WForm form = null; 
        if ( supportsType(c)) {
            form = (WForm) c;
        } else {
            throw new FacesException("Invalid component type. Expected WForm");
        }
        StringBuffer out = new StringBuffer();
        out.append("<FORM ");

        String form_name = (String) form.getAttribute(rc, "name");
        if (form_name != null) {
            out.append("NAME=\"" + form_name + "\"");
        }
        out.append(">");
        outputMethod.writeText(out.toString());
        outputMethod.flush();
    }

    public void renderChildren(RenderContext rc, 
            WComponent c) throws IOException {
        return;
    }

    public void renderEnd(RenderContext rc, WComponent c )
            throws IOException, FacesException {
        // render the form
        OutputMethod outputMethod = rc.getOutputMethod();
        StringBuffer out = new StringBuffer();
        out.append("</FORM>");
        outputMethod.writeText(out.toString());
        outputMethod.flush();
        return;
    }


} // end of class FormRenderer
