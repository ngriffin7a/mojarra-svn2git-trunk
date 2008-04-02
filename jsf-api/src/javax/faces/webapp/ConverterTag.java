/*
 * $Id: ConverterTag.java,v 1.19 2005/12/05 16:43:04 edburns Exp $
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

package javax.faces.webapp;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;






/**
 * <p><strong>ConverterTag</strong> is a base class for all JSP custom actions
 * that create and register a <code>Converter</code> instance on the
 * {@link ValueHolder} associated with our most immediate
 * surrounding instance of a tag whose implementation class is a subclass
 * of {@link UIComponentTag}.  To avoid creating duplicate instances when
 * a page is redisplayed, creation and registration of a {@link Converter}
 * occurs <strong>only</strong> if the corresponding {@link UIComponent} was
 * created (by the owning {@link UIComponentTag}) during the execution of the
 * current page.</p>
 *
 * <p>This class may be used directly to implement a generic converter
 * registration tag (based on the converter-id specified by the
 * <code>converterId</code> attribute), or as a base class for tag
 * instances that support specific {@link Converter} subclasses.  This
 * <code>converterId</code> attribute must refer to one of the well
 * known converter-ids, or a custom converter-id as defined in a
 * <code>faces-config.xml</code> file.</p>
 *
 * <p>Subclasses of this class must implement the
 * <code>createConverter()</code> method, which creates and returns a
 * {@link Converter} instance.  Any configuration properties that specify
 * behavior of this {@link Converter} must have been set by the
 * <code>createConverter()</code> method.  Generally, this occurs
 * by copying corresponding attribute values on the tag instance.</p>
 *
 * <p>This tag creates no output to the page currently being created.  It
 * is used solely for the side effect of {@link Converter} creation.</p>
 *
 * @deprecated This has been partially replaced by {@link
 * ConverterELTag}.  The remainder of the functionality, namely, the
 * binding facility and the implementation of the {@link
 * #createConverter} method, is now an implementation detail.
 */

public class ConverterTag extends TagSupport {


    // ---------------------------------------------------------- Static Members


    private static final long serialVersionUID = -5909792518081427720L;


    // -------------------------------------------------------------- Attributes

    /**
     * <p>The identifier of the {@link Converter} instance to be created.</p>
     */
    private String converterId = null;
    
    /**
     * <p>The {@link ValueExpression} that evaluates to an object that 
     * implements {@link Converter}.</p>
     */
    private String binding = null;

    /**
     * <p>Set the identifer of the {@link Converter} instance to be created.
     *
     * @param converterId The identifier of the converter instance to be
     * created.
     */
    public void setConverterId(String converterId) {

        this.converterId = converterId;

    }

    /**
     * <p>Set the expression that will be used to create a {@link ValueExpression}
     * that references a backing bean property of the {@link Converter} instance to 
     * be created.</p>
     *
     * @param binding The new expression
     *
     * @throws JspException if a JSP error occurs
     */
    public void setBinding(String binding) 
        throws JspException {
        if (binding!= null && !UIComponentTag.isValueReference(binding)) {
            // PENDING i18n
            throw new JspException("Invalid Expression:"+binding);
        }
        this.binding = binding;
    }
    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Create a new instance of the specified {@link Converter}
     * class, and register it with the {@link UIComponent} instance associated
     * with our most immediately surrounding {@link UIComponentTag} instance, if
     * the {@link UIComponent} instance was created by this execution of the
     * containing JSP page.</p>
     *
     * @throws JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        Converter converter = null;
        
        // Locate our parent UIComponentTag
        UIComponentClassicTagBase tag =
            UIComponentClassicTagBase.getParentUIComponentClassicTagBase(pageContext);
        if (tag == null) { // PENDING - i18n
            throw new JspException("Not nested in a UIComponentTag Error for tag with handler class:"+
                    this.getClass().getName());
        }

        // Nothing to do unless this tag created a component
        if (!tag.getCreated()) {
            return (SKIP_BODY);
        }

        UIComponent component = tag.getComponentInstance();
        if (component == null) {            
            //PENDING i18n
            throw new JspException("Can't create Component from tag.");
        }
        if (!(component instanceof ValueHolder)) {
            //PENDING i18n
            throw new JspException("Not nested in a tag of proper type. Error for tag with handler class:"+
                    this.getClass().getName());
        }
        
        converter = createConverter();
        
        if (converter == null) {
            String converterError = null;
            if (binding != null) {
                converterError = binding;
            }
            if (converterId != null) {
                if (converterError != null) {
                    converterError += " or " + converterId;
                } else {
                    converterError = converterId;
                }
            }

            // PENDING i18n
            throw new JspException("Can't create class of type:"+
                    "javax.faces.convert.Converter for:"+converterError);
        }

        ValueHolder vh = (ValueHolder)component;
        FacesContext context = FacesContext.getCurrentInstance();
        
        // Register an instance with the appropriate component
        vh.setConverter(converter);
        
        // Once the converter has been set, attempt to convert the
        // incoming "value"
        Object localValue = vh.getLocalValue();
        if (localValue instanceof String) {
            try {
                localValue = converter.getAsObject(context, (UIComponent)vh, (String) localValue);
                vh.setValue(localValue);
            }
            catch (ConverterException ce) {
                // PENDING - Ignore?  Throw an exception?  Set the local
                // value back to "null" and log a warning?
            }
        }        
  
        return (SKIP_BODY);

    }


    /**
     * <p>Release references to any acquired resources.
     */
    public void release() {

        this.converterId = null;

    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Create and return a new {@link Converter} to be registered
     * on our surrounding {@link UIComponent}.</p>
     *
     * @throws JspException if a new instance cannot be created
     */
    protected Converter createConverter()
        throws JspException {

        FacesContext context = FacesContext.getCurrentInstance();
        Converter converter = null;
        ValueExpression vb = null;
        
        // If "binding" is set, use it to create a converter instance.
        if (binding != null) {
            try {
		vb = 
                context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), binding, Object.class);
		if (vb != null) {
                    converter = (Converter)vb.getValue(context.getELContext());
                    if (converter != null) {
                        return converter;
                    }
                }
            } catch (Exception e) {
                throw new JspException(e);
            }
        }
        // If "converterId" is set, use it to create the converter
        // instance.  If "converterId" and "binding" are both set, store the 
        // converter instance in the value of the property represented by
        // the value binding expression.      
        if (converterId != null) {
            try {
                String converterIdVal = converterId;
                if (UIComponentTag.isValueReference(converterId)) {
                    ValueExpression idBinding =
                        context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), converterId, Object.class);
                    converterIdVal = (String) idBinding.getValue(context.getELContext());
                }
                converter = context.getApplication().createConverter(converterIdVal);
                if (converter != null) {
                    if (vb != null) {
                        vb.setValue(context.getELContext(), converter);
                    }
                }
            } catch (Exception e) {
                throw new JspException(e);
            }
        }
        return converter;
    }
}
