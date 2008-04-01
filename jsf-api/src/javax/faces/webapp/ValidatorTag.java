/*
 * $Id: ValidatorTag.java,v 1.1 2002/07/16 23:48:37 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.faces.component.UIComponent;
import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * <p>Tag implementation that creates an {@link Validator} instance
 * and registers it on the {@link UIComponent} associated with our most
 * immediate surrounding instance of a tag whose implementation class
 * is a subclass of {@link FacesTag}.  This tag creates no output to the
 * page currently being created.</p>
 *
 * <p>FIXME - should this class be in jsf-api, or just a spec requirement
 * to provide such a tag with a well known name?</p>
 */

public final class ValidatorTag extends TagSupport {


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The fully qualified class name of the {@link Validator}
     * instance to be created.</p>
     */
    private String className = null;


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the fully qualified class name of the
     * {@link Validator} instance to be created.
     */
    public String getClassName() {

        return (this.className);

    }


    /**
     * <p>Set the fully qualified class name of the
     * {@link Validator} instance to be created.
     *
     * @param className The new class name
     */
    public void setClassName(String className) {

        this.className = className;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Create a new instance of the specified {@link Validator}
     * class, and register it with the {@link UIComponent} instance associated
     * with our most immediately surrounding {@link FacesTag} instance.
     *
     * @exception JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        try {

            // Create a new instance of the specified class
            ClassLoader classLoader =
                Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = this.getClass().getClassLoader();
            }
            Class clazz = classLoader.loadClass(className);
            Validator validator = (Validator) clazz.newInstance();

            // Register this instance with the appropriate component
            Tag tag = getParent();
            while ((tag != null) && !(tag instanceof FacesTag)) {
                tag = tag.getParent();
            }
            if (tag == null) { // FIXME - i18n
                throw new JspException("Not nested in a FacesTag");
            }
            ((FacesTag) tag).getComponent().addValidator(validator);
            return (SKIP_BODY);

        } catch (Exception e) {

            throw new JspException(e);

        }

    }


    /**
     * <p>Release references to any acquired resources.
     */
    public void release() {

        this.className = null;

    }


}
