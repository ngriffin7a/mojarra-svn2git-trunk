/*
 * ConverterPropertyEditor.java
 *
 * Created on August 10, 2006, 12:39 PM
 *
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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */
package com.sun.faces.application;

import java.beans.PropertyEditorSupport;
import java.util.Map;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;
import com.sun.faces.util.MessageFactory;
import com.sun.faces.util.FacesLogger;

/**
 * Abstract base for a {@link java.beans.PropertyEditor} that delegates to a
 * faces Converter that was registered by-type in a faces-config descriptor.
 * Concrete implementations (such as generated by
 * {@link ConverterPropertyEditorFactory}) will override
 * {@link #getTargetClass}. (This is based on the original
 * ConverterPropertyEditor code).
 */
public abstract class ConverterPropertyEditorBase extends PropertyEditorSupport {
    protected static final Logger logger = FacesLogger.APPLICATION.getLogger();
    // Name of the request scope attribute that will indicate the current
    // component being processed.
    public static final String TARGET_COMPONENT_ATTRIBUTE_NAME = RIConstants.FACES_PREFIX
        + "ComponentForValue";

    /**
     * Return the target class of the objects that are being edited. This is
     * used as a key to find the appropriate
     * {@link javax.faces.convert.Converter} from the Faces application.
     * 
     * @return the target class.
     */
    protected abstract Class<?> getTargetClass();

    /**
     * Return the {@link javax.faces.component.UIComponent} that is currently
     * being processed, so it can be passed on to the
     * {@link javax.faces.convert.Converter}. (Most basic converters use this
     * for creating and setting error messages, although they may also use
     * attributes of the component to customize the conversion). For now, do
     * this by looking for a request attribute keyed on
     * {@link TARGET_COMPONENT_ATTRIBUTE_NAME}.
     * 
     * @return the current component, or null.
     */
    protected UIComponent getComponent() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            Map<String, Object> requestMap = context.getExternalContext()
                .getRequestMap();
            return (UIComponent) requestMap
                .get(TARGET_COMPONENT_ATTRIBUTE_NAME);
        }
        return null;
    }

    /**
     * Convert the <code>textValue</code> to an object of type
     * {@link #getTargetClass} by delegating to a converter obtained from the
     * Faces application.
     */
    @Override
    public void setAsText(String textValue) throws IllegalArgumentException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (null == context) {
            // PENDING(edburns): I18N
            logger.warning("setAsText: Cannot find FacesContext.");
            throw new FacesException("Cannot find FacesContext.");
        }
        UIComponent component = getComponent();
        Class targetClass = getTargetClass();
        Converter converter = context.getApplication().createConverter(
            targetClass);
        if (null == converter) {
            // PENDING(edburns): I18N
            FacesException e = new FacesException(
                "Cannot create Converter to convert value " + textValue
                    + " to instance of target class " + targetClass.getName()
                    + '.');
            logger.warning("setAsText: no faces converter: " + e.getMessage());
            throw e;
        }
        try {
            setValue(converter.getAsObject(context, component, textValue));
        } catch (ConverterException ce) {
            logger.warning("setAsText: ConverterException: " + ce.getMessage());
            addConversionErrorMessage(context, component, ce, textValue);
        }
    }

    /**
     * Convert an object of type {@link #getTargetClass} to text by delegating
     * to a converter obtained from the Faces application.
     */
    @Override
    public String getAsText() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (null == context) {
            // PENDING(edburns): I18N
            logger.warning("getAsText: Cannot find FacesContext.");
            throw new FacesException("Cannot find FacesContext.");
        }
        UIComponent component = getComponent();
        Class targetClass = getTargetClass();
        Converter converter = context.getApplication().createConverter(
            targetClass);
        if (null == converter) {
            // PENDING(edburns): I18N
            throw new FacesException("Cannot create Converter to convert "
                + targetClass.getName() + " value " + getValue()
                + " to string.");
        }
        try {
            return converter.getAsString(context, component, getValue());
        } catch (ConverterException ce) {
            addConversionErrorMessage(context, component, ce, getValue());
            return super.getAsText();
        }
    }

    private void addConversionErrorMessage(FacesContext context,
        UIComponent component, ConverterException ce, Object value) {
        String converterMessageString = null;
        FacesMessage message;
        UIInput input;
        if (component instanceof UIInput) {
            input = (UIInput) component;
            converterMessageString = input.getConverterMessage();
            input.setValid(false);
        }
        if (null != converterMessageString) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                       converterMessageString,
                                       converterMessageString);
        } else {
            message = ce.getFacesMessage();
            if (message == null) {
                message = MessageFactory.getMessage(context,
                                                    UIInput.CONVERSION_MESSAGE_ID);
                if (message.getDetail() == null) {
                    message.setDetail(ce.getMessage());
                }
            }
        }       
        context.addMessage(component != null ? component.getClientId(context)
            : null, message);
    }
}
