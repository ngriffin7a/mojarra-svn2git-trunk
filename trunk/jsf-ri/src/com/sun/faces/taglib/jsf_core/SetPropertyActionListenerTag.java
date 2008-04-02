/*
 * $Id: SetPropertyActionListenerTag.java,v 1.1 2005/07/25 18:40:25 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionListener;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.sun.faces.util.Util;

import java.util.logging.Logger;
import javax.faces.component.ActionSource;



/**
 * <p>Tag implementation that creates a special {@link ActionListener} instance
 * and registers it on the {@link ActionSource} associated with our most
 * immediate surrounding instance of a tag whose implementation class
 * is a subclass of {@link UIComponentTag}.  This tag creates no output to
 * the page currently being created.</p>
 * <p/>
 * <p>The ActionListener instance created and installed by this tag has the 
 * following behavior and contract.</p>
 *
 * <ul>
 *
 * <li>Only create and register the <code>ActionListener</code> instance
 * the first time the component for this tag is created</li>
 *
 * <li>The "target" and "value" tag attributes are ValueExpression
 * instances and are stored unevaluated as instance variables of the
 * listener.</li>
 *
 * <li>When the listener executes, call getValue() on the "value"
 * ValueExpression.  Pass the result to a call to setValue() on the
 * "target" ValueExpression</li>
 *
 * </ul>
 *
 * <p>This tag creates no output to the page currently being created.  It
 * is used solely for the side effect of {@link ActionListener}
 * creation.</p>
 */

public class SetPropertyActionListenerTag extends TagSupport {


    // ------------------------------------------------------------- Attributes

    static final long serialVersionUID = 7966883942522780374L;
    private static final Logger logger = 
            Util.getLogger(Util.FACES_LOGGER + Util.TAGLIB_LOGGER);
    /**
     * <p>The target of the value attribute.</p>
     */
    private ValueExpression target = null;

    /**
     * <p>The value that is set into the target attribute.</p>
     */
    private ValueExpression value = null;

    /**
     * <p>Setter for the target attribute</p>
     *
     * @param target The new class name
     */
    public void setTarget(ValueExpression target) {

        this.target = target;

    }

    /*
     * <p>Setter for the value attribute</p>
     *
     * @param value The new value value expression
     *
     * @throws JspException if a JSP error occurs
     */
    public void setValue(ValueExpression value) {
	this.value = value;
    }

    // --------------------------------------------------------- Public Methods


    /**
     * <p>Create a new instance of the {@link ActionListener}
     * class, and register it with the {@link UIComponent} instance associated
     * with our most immediately surrounding {@link UIComponentTagBase}
     * instance.  The behavior of the {@link ActionListener} must conform 
     * to the class description.</p>
     *
     * @throws JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        ActionListener handler = null;

        // Locate our parent UIComponentTag
        UIComponentClassicTagBase tag =
            UIComponentELTag.getParentUIComponentClassicTagBase(pageContext);
        if (tag == null) {
            Object params [] = {this.getClass().getName()};
            throw new JspException(
                Util.getExceptionMessageString(
                    Util.NOT_NESTED_IN_FACES_TAG_ERROR_MESSAGE_ID, params));
        }
        
        // Nothing to do unless this tag created a component
        if (!tag.getCreated()) {
            return (SKIP_BODY);
        }
                
        FacesContext context = FacesContext.getCurrentInstance();

        ActionSource component = (ActionSource)tag.getComponentInstance();
        if (component == null) {
            throw new JspException(
                Util.getExceptionMessageString(Util.NULL_COMPONENT_ERROR_MESSAGE_ID));
        }
        if (!(component instanceof ActionSource)) {
            Object params [] = {this.getClass().getName()};
            throw new JspException(
                Util.getExceptionMessageString(
                    Util.NOT_NESTED_IN_TYPE_TAG_ERROR_MESSAGE_ID, params));
        }
        
        handler = new SetPropertyActionListenerImpl(target, value);
        component.addActionListener(handler);
                       
        return (SKIP_BODY);

    }


    /**
     * <p>Release references to any acquired resources.
     */
    public void release() {

        this.value = null;
        this.target = null;

    }
    


}
