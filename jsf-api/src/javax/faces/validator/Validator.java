/*
 * $Id: Validator.java,v 1.5 2002/08/29 05:39:13 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import java.io.Serializable;
import java.util.Iterator;
import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;


/**
 * <p>A <strong>Validator</strong> is a class that can perform validation
 * (correctness checks) on a {@link UIComponent}.  Zero or more
 * <code>Validator</code>s can be associated with each {@link UIComponent}
 * in the request tree, and are called during the <em>Process
 * Validations Phase</em>.</p>
 *
 * <p>Individual <code>Validator</code>s should examine the component that
 * they are passed, and add {@link Message} instances to the
 * {@link FacesContext} for the current request, documenting
 * any failures to conform to the required rules.  In general, such
 * messages should be associated with the {@link UIComponent} on which
 * the validation failure occurred.</p>
 *
 * <p>For maximum generality, <code>Validator</code> instances should be
 * configurable based on attribute values associated with the
 * {@link UIComponent} being validated.  For example, a range check
 * validator might support configuration of the minimum and maximum values
 * to be used.  Each <code>Validator</code> should document the attributes
 * it cares about via the <code>getAttributeNames()</code> and
 * <code>getAttributeDescriptor()</code> methods, so that tools can construct
 * robust user interfaces for configuring them.</p>
 */

public interface Validator extends Serializable {


    /**
     * <p>Perform the correctness checks implemented by this
     * <code>Validator</code> against the specified {@link UIComponent}.
     * Add {@link Message}s describing any correctness violations to the
     * specified {@link FacesContext}.  If any violations are found:</p>
     * <ul>
     * <li>Add zero or more {@link Message}s to the specified
     *     {@link FacesContext}, specifying this {@link UIComponent} as
     *     associated with the message, describing the nature of the
     *     violation(s) encountered.</li>
     * <li>Call <code>setValid(false)</code> on the {@link UIComponent}.</li>
     * </ul>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent we are checking for correctness
     *
     * @return <code>true</code> if all validations performed by this
     *  method passed successfully, or <code>false</code> if one or more
     *  validations performed by this method failed
     *
     * @exception NullPointerException if any parameter is <code>null</code>
     */
    public boolean validate(FacesContext context, UIComponent component);


}
