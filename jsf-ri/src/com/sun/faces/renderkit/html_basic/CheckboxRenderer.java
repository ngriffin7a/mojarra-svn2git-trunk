/*
 * $Id: CheckboxRenderer.java,v 1.19 2002/04/16 23:27:30 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// CheckboxRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.beans.PropertyDescriptor;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.FacesContext;
import javax.faces.Renderer;
import javax.faces.UIComponent;
import javax.faces.UISelectBoolean;
import javax.faces.Constants;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>CheckboxRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: CheckboxRenderer.java,v 1.19 2002/04/16 23:27:30 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class CheckboxRenderer extends Object implements Renderer {
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

    public CheckboxRenderer() {
        super();
        // ParameterCheck.nonNull();
        this.init();
    }

    protected void init() {
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

    public boolean supportsComponentType(UIComponent c) {
        ParameterCheck.nonNull(c);
        boolean supports= false;
        if ( c instanceof UISelectBoolean ) {
            supports = true;
        }
        return supports;
    }

    public boolean supportsComponentType(String componentType) {
        ParameterCheck.nonNull(componentType);
        boolean supports = false;
        if ( componentType.equals(Constants.REF_UISELECTBOOLEAN)) {
            supports = true;
        }
        return supports;
    }

    public Iterator getSupportedAttributeNames(String componentType) throws FacesException {
        return null;
    }

    public Iterator getSupportedAttributes(String componentType) throws FacesException {
	return null;
    }

    public PropertyDescriptor getAttributeDescriptor(String attributeName)
	throws FacesException {
	return null;
    }


    public void renderStart(FacesContext fc, UIComponent c) 
        throws IOException, FacesException {

        ParameterCheck.nonNull(fc);
        ParameterCheck.nonNull(c);
 
        UISelectBoolean wSelectBoolean = null;
        if ( supportsComponentType(c)) {
            wSelectBoolean = (UISelectBoolean) c;
        } else {
            throw new FacesException("Invalid component type. " +
                      "Expected UISelectBoolean");
        }

        String cbId = wSelectBoolean.getId();
        Assert.assert_it(null != cbId);

        OutputMethod outputMethod = fc.getOutputMethod();
        Assert.assert_it(outputMethod != null );

        StringBuffer output = new StringBuffer();

        output.append("<INPUT TYPE=\"CHECKBOX\" ");
        output.append(" NAME=\"");
        output.append(cbId);
        output.append("\"");

        if (wSelectBoolean.getAttribute(fc, "checked") != null) {
            output.append(" CHECKED ");
        }

        // do not render the name and value of the checkbox.The
        // state of this checkbox will be tracked using hidden
        // field because HTML doesn't send the status of the check
        // box during form submissions if it is not selected.

        String hiddenFieldname = Constants.REF_HIDDENCHECKBOX + cbId;

        output.append(">");
        if (wSelectBoolean.getAttribute(fc, "label") != null) {
            output.append(" ");
            output.append(wSelectBoolean.getAttribute(fc, "label"));
        }

        // render a hiddenField to track the state of the checkbox
        output.append(" ");
        output.append ("<INPUT TYPE=\"HIDDEN\" NAME=\"");
        output.append ( hiddenFieldname );
        output.append("\"");
        output.append(" VALUE=\"");
        output.append("false");
        output.append ("\">");
 
        outputMethod.writeText(output.toString());

        outputMethod.flush();
    }

    public void renderChildren(FacesContext fc, UIComponent c) 
        throws IOException {
        return;
    }

    public void renderComplete(FacesContext fc, UIComponent c) 
            throws IOException,FacesException {
        return;
    }

    public boolean getCanRenderChildren(FacesContext fc, UIComponent c) {
        return false;
    }


} // end of class CheckboxRenderer
