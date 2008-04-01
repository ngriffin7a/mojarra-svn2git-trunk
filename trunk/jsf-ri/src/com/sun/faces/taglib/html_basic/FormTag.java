/*
 * $Id: FormTag.java,v 1.24 2002/02/05 18:57:03 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FormTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.FacesTag;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.UIForm;
import javax.faces.UIComponent;

import javax.servlet.jsp.JspException;

/**
 *
 *  <B>FormTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FormTag.java,v 1.24 2002/02/05 18:57:03 edburns Exp $
 * @author Jayashri Visvanathan
 * 
 *
 */

public class FormTag extends FacesTag
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
    private String formListener = null;
    private String navigationMapId = null;
    
    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public FormTag()
    {
        super();
    }
    
    //
    // Class methods
    //

    //
    // General Methods
    //

    /**
     * Returns the value of the navigationMapId attribute
     *
     * @return String value of navigationMapId attribute
     */
    public String getNavigationMapId() {
        return this.navigationMapId;
    }

    /**
     * Sets NavigationMapId attribute
     * @param navMap_id value of navigationMapId attribute 
     */
    public void setNavigationMapId(String navMap_id) {
        this.navigationMapId = navMap_id;
    }

    /**
     * Returns the value of formListener attribute
     *
     * @return String value of formListener attribute
     */
    public String getFormListener() {
        return this.formListener;
    }

    /**
     * Sets formListener attribute
     * @param form_listener value of formListener attribute
     */
    public void setFormListener(String form_listener) {
        this.formListener = form_listener;
    }

//
// Methods from FacesTag
//

    public UIComponent newComponentInstance() {
        return new UIForm();
    }

    public void setAttributes(UIComponent comp) {
	ParameterCheck.nonNull(comp);
	Assert.assert_it(comp instanceof UIForm);
        // set render independent attributes
        // make sure that the model object is registered
        if ( getModel() != null ) {
            ((UIForm)comp).setModelReference(getModel());
        }   
    }

    public String getRendererType() {
	return "FormRenderer";
    }

    public void addListeners(UIComponent comp) throws JspException {
	ParameterCheck.nonNull(comp);
	Assert.assert_it(comp instanceof UIForm);
	UIForm form = (UIForm) comp;
        try {
            form.addFormListener(formListener);    
            if ( navigationMapId != null ) {
                form.setNavigationMapId(navigationMapId);
            }    
        } catch (FacesException fe) {
            throw new JspException("Listener + " + formListener +
				   " does not implement formListener interface or doesn't exist" );
        }    
    }

    /**
     * Tag cleanup method.
     */
    public void release() {

        super.release();

        formListener = null;
        navigationMapId = null;
    }

} // end of class FormTag
