/*
 * $Id: ConvertDateTimeTag.java,v 1.27 2007/04/25 04:07:00 rlubke Exp $
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

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.el.ELUtils;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.servlet.jsp.JspException;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * <p>ConvertDateTimeTag is a ConverterTag implementation for
 * javax.faces.convert.DateTimeConverter</p>
 *
 * @version $Id: ConvertDateTimeTag.java,v 1.27 2007/04/25 04:07:00 rlubke Exp $
 */

public class ConvertDateTimeTag extends AbstractConverterTag {

    private static final long serialVersionUID = -5815655767093677438L;
    private static ValueExpression CONVERTER_ID_EXPR = null;

     private static final Logger LOGGER = FacesLogger.TAGLIB.getLogger();

    //
    // Instance Variables
    //

    private ValueExpression dateStyleExpression;
    private ValueExpression localeExpression;
    private ValueExpression patternExpression;
    private ValueExpression timeStyleExpression;
    private ValueExpression timeZoneExpression;
    private ValueExpression typeExpression;

    private String dateStyle;
    private Locale locale;
    private String pattern;
    private String timeStyle;
    private TimeZone timeZone;
    private String type;// Log instance for this class


    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //
    public ConvertDateTimeTag() {
        super();
        init();
    }


    public void release() {
        super.release();
        init();
    }


    private void init() {
        dateStyle = "default";
        dateStyleExpression = null;
        locale = null;
        localeExpression = null;
        pattern = null;
        patternExpression = null;
        timeStyle = "default";
        timeStyleExpression = null;
        timeZone = null;
        timeZoneExpression = null;
        type = "date";
        typeExpression = null;
        if (CONVERTER_ID_EXPR == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            ExpressionFactory factory = context.getApplication().
                    getExpressionFactory();
            CONVERTER_ID_EXPR = factory.createValueExpression(
                    context.getELContext(),"javax.faces.DateTime",String.class);
        }
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    public void setDateStyle(ValueExpression dateStyle) {
        this.dateStyleExpression = dateStyle;
    }


    public void setLocale(ValueExpression locale) {
        this.localeExpression = locale;
    }


    public void setPattern(ValueExpression pattern) {
        this.patternExpression = pattern;
    }


    public void setTimeStyle(ValueExpression timeStyle) {
        this.timeStyleExpression = timeStyle;
    }


    public void setTimeZone(ValueExpression timeZone) {
        this.timeZoneExpression = timeZone;
    }


    public void setType(ValueExpression type) {
        this.typeExpression = type;
    }

    public int doStartTag() throws JspException {
        super.setConverterId(CONVERTER_ID_EXPR);
        return super.doStartTag();
    }

    //
    // Methods from ConverterTag
    //

    protected Converter createConverter() throws JspException {

        DateTimeConverter result = (DateTimeConverter) super.createConverter();
        assert (null != result);

        evaluateExpressions();
        result.setDateStyle(dateStyle);
        result.setLocale(locale);
        result.setPattern(pattern);
        result.setTimeStyle(timeStyle);
        result.setTimeZone(timeZone);
        result.setType(type);

        return result;
    }


    /* Evaluates expressions as necessary */
    private void evaluateExpressions() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();

        if (dateStyleExpression != null) {
            dateStyle = (String)
            ELUtils.evaluateValueExpression(dateStyleExpression, elContext);
        }
        if (patternExpression != null) {
            pattern = (String)
            ELUtils.evaluateValueExpression(patternExpression, elContext);
        }
        if (timeStyleExpression != null) {
            timeStyle = (String)
            ELUtils.evaluateValueExpression(timeStyleExpression, elContext);
        }
        if (typeExpression != null) {
            type = (String)
            ELUtils.evaluateValueExpression(typeExpression, elContext);
        } else {
            if (timeStyleExpression != null) {
                if (dateStyleExpression != null) {
                    type = "both";
                } else {
                    type = "time";
                }
            } else {
                type = "date";
            }
        }
        if (localeExpression != null) {
            if (localeExpression.isLiteralText()) {
                locale = getLocale(localeExpression.getExpressionString());
            } else {
                Object loc = ELUtils.evaluateValueExpression(localeExpression,
                                                          elContext);
                if (loc != null) {
                    if (loc instanceof String) {
                        locale = getLocale((String) loc);
                    } else if (loc instanceof Locale) {
                        locale = (Locale) loc;
                    } else {
                        Object[] params = {
                            "locale",
                            "java.lang.String or java.util.Locale",
                            loc.getClass().getName()
                        };
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE,
                                       "jsf.core.tags.eval_result_not_expected_type",
                                       params);
                        }
                        throw new FacesException(
                            MessageUtils.getExceptionMessageString(
                                MessageUtils.EVAL_ATTR_UNEXPECTED_TYPE, params));
                    }
                } else {
                    locale = facesContext.getViewRoot().getLocale();
                }
            }
        }
        if (timeZoneExpression != null) {
            if (timeZoneExpression.isLiteralText()) {
                timeZone =
                TimeZone.getTimeZone(
                    timeZoneExpression.getExpressionString());
            } else {
                Object tz = ELUtils.evaluateValueExpression(timeZoneExpression,
                                                         elContext);
                if (tz != null) {
                    if (tz instanceof String) {
                        timeZone = TimeZone.getTimeZone((String) tz);
                    } else if (tz instanceof TimeZone) {
                        timeZone = (TimeZone) tz;
                    } else {
                        Object[] params = {
                            "timeZone",
                            "java.lang.String or java.util.TimeZone",
                            tz.getClass().getName()
                        };
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE,
                                       "jsf.core.tags.eval_result_not_expected_type",
                                       params);
                        }
                        throw new FacesException(
                            MessageUtils.getExceptionMessageString(
                                MessageUtils.EVAL_ATTR_UNEXPECTED_TYPE, params));
                    }
                }
            }
        }
    }

    protected static Locale getLocale(String string) {
        if (string == null) {
            return Locale.getDefault();
        }

        if (string.length() > 2) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING,
                           "jsf.core.taglib.convertdatetime.invalid_local_value",
                           string);
            }
        } else {
            String[] langs = Locale.getISOLanguages();
            Arrays.sort(langs);
            if (Arrays.binarySearch(langs, string) < 0) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING,
                               "jsf.core.taglib.convertdatetime.invalid_language",
                               string);
                }
            }
        }

        return new Locale(string, "");
    }
} // end of class ConvertDateTimeTag
