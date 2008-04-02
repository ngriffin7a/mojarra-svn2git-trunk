/*
 * $Id: Util.java,v 1.101 2003/10/11 04:53:03 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Util.java

package com.sun.faces.util;

import com.sun.faces.RIConstants;
import com.sun.faces.application.MessageResourcesImpl;
import com.sun.faces.el.impl.ExpressionEvaluator;
import com.sun.faces.el.impl.ExpressionEvaluatorImpl;
import com.sun.faces.renderkit.RenderKitImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.Message;
import javax.faces.application.MessageResources;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.model.SelectItem;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import javax.faces.render.ResponseStateManager;
import javax.faces.application.StateManager;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.el.impl.JspVariableResolver;

/**
 *
 *  <B>Util</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Util.java,v 1.101 2003/10/11 04:53:03 horwat Exp $ 
 */

public class Util extends Object
{
    //
    // Private/Protected Constants
    //
    // Log instance for this class
    protected static Log log = LogFactory.getLog(Util.class);
    
    /**
     * The parser implementation for handling JSP EL expressions.
     */ 
    private static final ExpressionEvaluator JSP_EXPRESSION_EVALUATOR =
        new ExpressionEvaluatorImpl(RIConstants.JSP_EL_PARSER);
    
    /**
     * The parser implementation for handling Faces RE expressions.
     */ 
    private static final ExpressionEvaluator FACES_EXPRESSION_EVALUATOR =
        new ExpressionEvaluatorImpl(RIConstants.FACES_RE_PARSER);

    // README - make sure to add the message identifier constant
    // (ex: Util.CONVERSION_ERROR_MESSAGE_ID) and the number of substitution
    // parameters to test/com/sun/faces/util/TestUtil_messages (see comment there).
 
    /**
     * The message identifier of the {@link Message} to be created as
     * a result of type conversion error.
     */
    public static final String CONVERSION_ERROR_MESSAGE_ID =
        "com.sun.faces.TYPECONVERSION_ERROR";
    
    /**
     * The message identifier of the {@link Message} to be created if
     * there is model update failure.
     */
    public static final String MODEL_UPDATE_ERROR_MESSAGE_ID =
        "com.sun.faces.MODELUPDATE_ERROR";

    public static final String FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID = 
	"com.sun.faces.FACES_CONTEXT_CONSTRUCTION_ERROR";

    public static final String NULL_COMPONENT_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_COMPONENT_ERROR";

    public static final String NULL_REQUEST_VIEW_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_REQUEST_VIEW_ERROR";

    public static final String NULL_RESPONSE_VIEW_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_RESPONSE_VIEW_ERROR";

    public static final String REQUEST_VIEW_ALREADY_SET_ERROR_MESSAGE_ID = 
	"com.sun.faces.REQUEST_VIEW_ALREADY_SET_ERROR";
    
    public static final String NULL_MESSAGE_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_MESSAGE_ERROR";
    
    public static final String NULL_PARAMETERS_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_PARAMETERS_ERROR";
    
    public static final String NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID = 
	"com.sun.faces.NAMED_OBJECT_NOT_FOUND_ERROR";
    
    public static final String NULL_RESPONSE_STREAM_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_RESPONSE_STREAM_ERROR";

    public static final String NULL_RESPONSE_WRITER_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_RESPONSE_WRITER_ERROR";

    public static final String NULL_EVENT_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_EVENT_ERROR";

    public static final String NULL_HANDLER_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_HANDLER_ERROR";

    public static final String NULL_CONTEXT_ERROR_MESSAGE_ID = 
	"com.sun.faces.NULL_CONTEXT_ERROR";

    public static final String NULL_LOCALE_ERROR_MESSAGE_ID =
        "com.sun.faces.NULL_LOCALE_ERROR";

    public static final String SUPPORTS_COMPONENT_ERROR_MESSAGE_ID =
        "com.sun.faces.SUPPORTS_COMPONENT_ERROR";

    public static final String MISSING_RESOURCE_ERROR_MESSAGE_ID =
        "com.sun.faces.MISSING_RESOURCE_ERROR";

    public static final String MISSING_CLASS_ERROR_MESSAGE_ID =
        "com.sun.faces.MISSING_CLASS_ERROR";
    public static final String COMPONENT_NOT_FOUND_ERROR_MESSAGE_ID =
        "com.sun.faces.COMPONENT_NOT_FOUND_ERROR";
    public static final String LIFECYCLE_ID_ALREADY_ADDED_ID =
        "com.sun.faces.LIFECYCLE_ID_ALREADY_ADDED";

    public static final String LIFECYCLE_ID_NOT_FOUND_ERROR_MESSAGE_ID =
        "com.sun.faces.LIFECYCLE_ID_NOT_FOUND";

    public static final String PHASE_ID_OUT_OF_BOUNDS_ERROR_MESSAGE_ID =
        "com.sun.faces.PHASE_ID_OUT_OF_BOUNDS";

    public static final String CANT_CREATE_LIFECYCLE_ERROR_MESSAGE_ID =
        "com.sun.faces.CANT_CREATE_LIFECYCLE_ERROR";

    public static final String ILLEGAL_MODEL_REFERENCE_ID =
        "com.sun.faces.ILLEGAL_MODEL_REFERENCE";
    
     public static final String ATTRIBUTE_NOT_SUPORTED_ERROR_MESSAGE_ID =
         "com.sun.faces.ATTRIBUTE_NOT_SUPORTED";

   public static final String FILE_NOT_FOUND_ERROR_MESSAGE_ID =
         "com.sun.faces.FILE_NOT_FOUND";

   public static final String CANT_PARSE_FILE_ERROR_MESSAGE_ID =
         "com.sun.faces.CANT_PARSE_FILE";

   public static final String CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID =
         "com.sun.faces.CANT_INSTANTIATE_CLASS";

   public static final String ILLEGAL_CHARACTERS_ERROR_MESSAGE_ID =
         "com.sun.faces.ILLEGAL_CHARACTERS_ERROR";

   public static final String NOT_NESTED_IN_FACES_TAG_ERROR_MESSAGE_ID =
         "com.sun.faces.NOT_NESTED_IN_FACES_TAG_ERROR";

   public static final String NULL_BODY_CONTENT_ERROR_MESSAGE_ID =
         "com.sun.faces.NULL_BODY_CONTENT_ERROR";

   public static final String SAVING_STATE_ERROR_MESSAGE_ID =
         "com.sun.faces.SAVING_STATE_ERROR";

   public static final String RENDERER_NOT_FOUND_ERROR_MESSAGE_ID =
         "com.sun.faces.RENDERER_NOT_FOUND";

   public static final String MAXIMUM_EVENTS_REACHED_ERROR_MESSAGE_ID =
         "com.sun.faces.MAXIMUM_EVENTS_REACHED";

   public static final String NO_ACTION_FROM_ACTIONREF_ERROR_MESSAGE_ID = 
        "com.sun.faces.NO_ACTION_FROM_ACTIONREF";

   public static final String NULL_CONFIGURATION_ERROR_MESSAGE_ID = 
        "com.sun.faces.NULL_CONFIGURATION";

   public static final String ERROR_OPENING_FILE_ERROR_MESSAGE_ID = 
        "com.sun.faces.ERROR_OPENING_FILE";

   public static final String ERROR_REGISTERING_DTD_ERROR_MESSAGE_ID = 
        "com.sun.faces.ERROR_REGISTERING_DTD";

   public static final String INVALID_INIT_PARAM_ERROR_MESSAGE_ID =  
        "com.sun.faces.INVALID_INIT_PARAM";

   public static final String ERROR_SETTING_BEAN_PROPERTY_ERROR_MESSAGE_ID = 
        "com.sun.faces.ERROR_SETTING_BEAN_PROPERTY";

   public static final String ERROR_GETTING_VALUE_BINDING_ERROR_MESSAGE_ID = 
        "com.sun.faces.ERROR_GETTING_VALUE_BINDING";

   public static final String ERROR_GETTING_VALUEREF_VALUE_ERROR_MESSAGE_ID = 
        "com.sun.faces.ERROR_GETTING_VALUEREF_VALUE";

   public static final String CANT_INTROSPECT_CLASS_ERROR_MESSAGE_ID = 
        "com.sun.faces.CANT_INTROSPECT_CLASS";

   public static final String CANT_CONVERT_VALUE_ERROR_MESSAGE_ID = 
        "com.sun.faces.CANT_CONVERT_VALUE";

   public static final String INVALID_SCOPE_LIFESPAN_ERROR_MESSAGE_ID =
        "com.sun.faces.INVALID_SCOPE_LIFESPAN";

   public static final String CONVERTER_NOT_FOUND_ERROR_MESSAGE_ID =
        "com.sun.faces.CONVERTER_NOT_FOUND_ERROR";

   public static final String VALIDATOR_NOT_FOUND_ERROR_MESSAGE_ID =
        "com.sun.faces.VALIDATOR_NOT_FOUND_ERROR";

   public static final String CANT_LOAD_CLASS_ERROR_MESSAGE_ID =
         "com.sun.faces.CANT_INSTANTIATE_CLASS";

   public static final String ENCODING_ERROR_MESSAGE_ID =
         "com.sun.faces.ENCODING_ERROR";
    
    public static final String ILLEGAL_IDENTIFIER_LVALUE_MODE_ID =
        "com.sun.faces.ILLEGAL_IDENTIFIER_LVALUE_MODE";

    public static final String VALIDATION_ID_ERROR_ID =
        "com.sun.faces.VALIDATION_ID_ERROR";

    public static final String VALIDATION_EL_ERROR_ID =
        "com.sun.faces.VALIDATION_EL_ERROR";

    public static final String VALIDATION_COMMAND_ERROR_ID =
        "com.sun.faces.VALIDATION_COMMAND_ERROR";

   public static final String CONTENT_TYPE_ERROR_MESSAGE_ID =
         "com.sun.faces.CONTENT_TYPE_ERROR";
    
   public static final String COMPONENT_NOT_FOUND_IN_VIEW_WARNING_ID =
         "com.sun.faces.COMPONENT_NOT_FOUND_IN_VIEW_WARNING";
    
   public static final String ILLEGAL_ATTEMPT_SETTING_VIEWHANDLER_ID = 
         "com.sun.faces.ILLEGAL_ATTEMPT_SETTING_VIEWHANDLER";
    
   public static final String INVALID_MESSAGE_SEVERITY_IN_CONFIG_ID =
         "com.sun.faces.INVALID_MESSAGE_SEVERITY_IN_CONFIG";

   public static final String CANT_CLOSE_INPUT_STREAM_ID =
         "com.sun.faces.CANT_CLOSE_INPUT_STREAM";
    
   public static final String DUPLICATE_COMPONENT_ID_ERROR_ID = 
         "com.sun.faces.DUPLICATE_COMPONENT_ID_ERROR";       
    
   public static final String FACES_SERVLET_MAPPING_CANNOT_BE_DETERMINED_ID =
         "com.sun.faces.FACES_SERVLET_MAPPING_CANNOT_BE_DETERMINED";
    
   public static final String ILLEGAL_VIEW_ID_ID =
         "com.sun.faces.ILLEGAL_VIEW_ID";
    

// README - make sure to add the message identifier constant
// (ex: Util.CONVERSION_ERROR_MESSAGE_ID) and the number of substitution
// parameters to test/com/sun/faces/util/TestUtil_messages (see comment there).

//
// Class Variables
//

    /**

    * This array contains attributes that have a boolean value in JSP,
    * but have have no value in HTML.  For example "disabled" or
    * "readonly". <P>

    * @see renderBooleanPassthruAttributes

    */

    private static String booleanPassthruAttributes[] = {
	"disabled",
	"readonly",
        "ismap"
    };
	
    /**

    * This array contains attributes whose value is just rendered
    * straight to the content.  This array should only contain
    * attributes that require no interpretation by the Renderer.  If an
    * attribute requires interpretation by a Renderer, it should be
    * removed from this array.<P>

    * @see renderPassthruAttributes

    */
    private static String passthruAttributes[] = {
	"accesskey",
	"alt",
        "cols",
        "height",
	"lang",
	"longdesc",
	"maxlength",
	"onblur",
	"onchange",
	"onclick",
	"ondblclick",
	"onfocus",
	"onkeydown",
	"onkeypress",
	"onkeyup",
	"onload",
	"onmousedown",
	"onmousemove",
	"onmouseout",
	"onmouseover",
	"onmouseup",
	"onreset",
	"onselect",
	"onsubmit",
	"onunload",
        "rows",
	"size",
        "tabindex",
	//        "class", // PENDING(edburns): revisit this for JSFA105
        "title",
        "style",
        "width",
        "dir",
        "rules",
        "frame",
        "border",
        "cellspacing",
        "cellpadding",
        "summary",
        "bgcolor",
        "usemap",
        "enctype", 
        "acceptcharset", 
        "accept", 
        "target", 
        "onsubmit", 
        "onreset"
    };

private static long id = 0;

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

private Util()
{
    throw new IllegalStateException();
}

//
// Class methods
//
    public static Class loadClass(String name, 
				  Object fallbackClass) throws ClassNotFoundException {
	ClassLoader loader = Util.getCurrentLoader(fallbackClass);
	return loader.loadClass(name);
    }

    public static ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader =
	    Thread.currentThread().getContextClassLoader();
	if (loader == null) {
	    loader = fallbackClass.getClass().getClassLoader();
	}
	return loader;
    }

    /**
     * This method will be called before calling facesContext.addMessage, so 
     * message can be localized.
     * <p>Return the {@link MessageResources} instance for the message
     * resources defined by the JavaServer Faces Specification.
     */
    public static synchronized MessageResources getMessageResources() {
        MessageResources resources = null;
        ApplicationFactory aFactory = 
	    (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = aFactory.getApplication();
	resources = application.getMessageResources(MessageResources.FACES_IMPL_MESSAGES);
	
        return (resources);
    }

    /**

    * Called by the RI to get the IMPL_MESSAGES MessageResources
    * instance and get a message on it.  

    */

    public static synchronized String getExceptionMessage(String messageId,
							  Object params[]) {
	String result = null;
	MessageResourcesImpl resources = (MessageResourcesImpl)
	    Util.getMessageResources();

	// As an optimization, we could store the MessageResources
	// instance in the System Properties for subsequent calls to
	// getExceptionMessage().

	if (null != resources) {
	    Message message = resources.getMessage(messageId, params);
	    if (null != message) {
		result = message.getSummary();
	    }
	}

	if (null == result) {
	    result = "null MessageResources";
	}
	return result;
    }

    public static synchronized String getExceptionMessage(String messageId) {
	return Util.getExceptionMessage(messageId, null);
    }
    
    /**

    * Verify the existence of all the factories needed by faces.  Create
    * and install the default RenderKit into the ServletContext. <P>

    * @see javax.faces.FactoryFinder

    */

    public static void verifyFactoriesAndInitDefaultRenderKit(ServletContext context) throws FacesException {
	RenderKitFactory renderKitFactory = null;
	LifecycleFactory lifecycleFactory = null;	
	FacesContextFactory facesContextFactory = null;
	ApplicationFactory applicationFactory = null;
	RenderKit defaultRenderKit = null;

	renderKitFactory = (RenderKitFactory)
	    FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	Assert.assert_it(null != renderKitFactory);

	lifecycleFactory = (LifecycleFactory)
	    FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
	Assert.assert_it(null != lifecycleFactory);	

	facesContextFactory = (FacesContextFactory)
	    FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
	Assert.assert_it(null != facesContextFactory);

	applicationFactory = (ApplicationFactory)
	    FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
	Assert.assert_it(null != applicationFactory);

	defaultRenderKit = 
	    renderKitFactory.getRenderKit(RenderKitFactory.DEFAULT_RENDER_KIT);
	if (defaultRenderKit == null) {
	    // create default renderkit if doesn't exist
	    //
	    defaultRenderKit = new RenderKitImpl();
	    renderKitFactory.addRenderKit(RenderKitFactory.DEFAULT_RENDER_KIT,
	        defaultRenderKit);
	}
	
	context.setAttribute(RIConstants.DEFAULT_RENDER_KIT, 
			     defaultRenderKit);

	context.setAttribute(RIConstants.ONE_TIME_INITIALIZATION_ATTR,
			     RIConstants.ONE_TIME_INITIALIZATION_ATTR);
    }

    /**

    * <p>Verifies that the required classes are available on either the
    * ContextClassLoader, or the local ClassLoader.  Currently only
    * checks for the class
    * "javax.servlet.jsp.jstl.fmt.LocalizationContext", which is used
    * for Localization.</p>  

    * <p>The result of the check is saved in the ServletContext
    * attribute RIConstants.HAS_REQUIRED_CLASSES_ATTR.</p>

    * <p>Algorithm:</p>

    * <p>Check the ServletContext for the attribute, if found, and the
    * value is false, that means we've checked before, and we don't have
    * the classes, just throw FacesException.  If the value is true,
    * we've checked before and we have the classes, just return.</p>

    */

    public static void verifyRequiredClasses(FacesContext facesContext) throws FacesException {
        Map applicationMap = facesContext.getExternalContext().getApplicationMap();
	Boolean result = null;
	String className = "javax.servlet.jsp.jstl.fmt.LocalizationContext";
	Object [] params = {className};

	// Have we checked before?
	if (null != (result = (Boolean)
            applicationMap.get(RIConstants.HAS_REQUIRED_CLASSES_ATTR))) {
	    // yes, and the check failed.
	    if (Boolean.FALSE == result) {
		throw new 
		    FacesException(Util.getExceptionMessage(Util.MISSING_CLASS_ERROR_MESSAGE_ID, params));
	    }
	    else {
		// yes, and the check passed.
		return;
	    }
	}

	//
	// We've not checked before, so do the check now!
	// 

	try {
	    Util.loadClass(className, facesContext);
	}
	catch (ClassNotFoundException e) {
	    applicationMap.put(RIConstants.HAS_REQUIRED_CLASSES_ATTR, Boolean.FALSE);
	    throw new FacesException(Util.getExceptionMessage(Util.MISSING_CLASS_ERROR_MESSAGE_ID, params), e);
	}
	applicationMap.put(RIConstants.HAS_REQUIRED_CLASSES_ATTR, Boolean.TRUE);
    }

    /** 

    * Release the factories and remove the default RenderKit from the
    * ServletContext.

    */

    public static void releaseFactoriesAndDefaultRenderKit(ServletContext context) throws FacesException {
	FactoryFinder.releaseFactories();

	Assert.assert_it(null != 
		 context.getAttribute(RIConstants.DEFAULT_RENDER_KIT));
	context.removeAttribute(RIConstants.DEFAULT_RENDER_KIT);
	context.removeAttribute(RIConstants.CONFIG_ATTR);
    }
			 
    /**
     * <p>Return an Iterator over {@link SelectItemWrapper} instances representing the
     * available options for this component, assembled from the set of
     * {@link UISelectItem} and/or {@link UISelectItems} components that are
     * direct children of this component.  If there are no such children, a
     * zero-length array is returned.</p>
     *
     * @param context The {@link FacesContext} for the current request.
     * If null, the UISelectItems behavior will not work.
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public static Iterator getSelectItemWrappers(FacesContext context,
					  UIComponent component) {

        ArrayList list = new ArrayList();
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof UISelectItem) {
                Object value = ((UISelectItem)kid).currentValue(context);
                if ( value == null ) {
                    UISelectItem item = (UISelectItem) kid;
                    list.add(new SelectItemWrapper( kid,
                                        new SelectItem(item.getItemValue(),
                                        item.getItemLabel(),
                                        item.getItemDescription())));
                } else if ( value instanceof SelectItem){
                    list.add(new SelectItemWrapper(kid,
                            ((SelectItem)value)));
                } else {
                    throw new IllegalArgumentException(Util.getExceptionMessage(
                        Util.CONVERSION_ERROR_MESSAGE_ID));
                }
            } else if (kid instanceof UISelectItems && null != context) {
                Object value = ((UISelectItems)kid).currentValue(context);
                if (value instanceof SelectItem) {
                    SelectItem item = (SelectItem) value;
                    list.add(new SelectItemWrapper( kid, item));
                } else if (value instanceof SelectItem[]) {
                    SelectItem items[] = (SelectItem[]) value;
                    for (int i = 0; i < items.length; i++) {
                        list.add(new SelectItemWrapper(kid, items[i]));
                    }
                } else if (value instanceof Collection) {
                    Iterator elements = ((Collection) value).iterator();
                    while (elements.hasNext()) {
                        list.add(new SelectItemWrapper(kid, (SelectItem) elements.next()));
                    }
                } else if (value instanceof Map) {
                    Iterator keys = ((Map) value).keySet().iterator();
                    while (keys.hasNext()) {
                        Object key = keys.next();
                        if (key == null) {
                            continue;
                        }
                        Object val = ((Map) value).get(key);
                        if (val == null) {
                            continue;
                        }
                        list.add(new SelectItemWrapper( kid, 
                            new SelectItem(val.toString(), key.toString(),null)));
                    }
                } else {
                    throw new IllegalArgumentException(Util.getExceptionMessage(
                        Util.CONVERSION_ERROR_MESSAGE_ID));
                }
            }
        }
        return (list.iterator());

    }

    /**

    * Return a Locale instance using the following algorithm: <P>

     	<UL>

	<LI>

	If this component instance has an attribute named "bundle",
	interpret it as a model reference to a LocalizationContext
	instance accessible via FacesContext.getModelValue().

	</LI>

	<LI>

	If FacesContext.getModelValue() returns a LocalizationContext
	instance, return its Locale.

	</LI>

	<LI>

	If FacesContext.getModelValue() doesn't return a
	LocalizationContext, return the FacesContext's Locale.

	</LI>

	</UL>



    */

    public static Locale 
	getLocaleFromContextOrComponent(FacesContext context,
					UIComponent component) {
	Locale result = null;
	String bundleName = null, bundleAttr = "bundle";
	
	ParameterCheck.nonNull(context);
	ParameterCheck.nonNull(component);
	
	// verify our component has the proper attributes for bundle.
	if (null != (bundleName = (String)component.getAttributes().get(bundleAttr))){
	    // verify there is a Locale for this localizationContext
	    javax.servlet.jsp.jstl.fmt.LocalizationContext locCtx = null;
	    if (null != (locCtx = 
			 (javax.servlet.jsp.jstl.fmt.LocalizationContext) 
                         (Util.getValueBinding(bundleName)).getValue(context))) {
                result = locCtx.getLocale();
		Assert.assert_it(null != result);
	    }
	}
	if (null == result) {
	    result = context.getLocale();
	}

	return result;
    }


    /**
    * Render any boolean "passthru" attributes.  
    * <P>
    * @see passthruAttributes
    */
    public static void renderBooleanPassThruAttributes(ResponseWriter writer,
        UIComponent component) throws IOException {
	Assert.assert_it(null != writer);
	Assert.assert_it(null != component);

        int i = 0, len = booleanPassthruAttributes.length;
        Object value = null;
        boolean result;
        for (i = 0; i < len; i++) {
            value = component.getAttributes().get(booleanPassthruAttributes[i]);
            if (value != null) {
                if (value instanceof Boolean) {
                    result = ((Boolean) value).booleanValue();
                } else {
                    if (!(value instanceof String)) {
                        value = value.toString();
                    }
                    result = (new Boolean((String) value)).booleanValue();
                }
		//PENDING(rogerk) will revisit "null" param soon..
                if (result) {
                    // NOTE:  render things like readonly="readonly" here
                    writer.writeAttribute(booleanPassthruAttributes[i],
                                          booleanPassthruAttributes[i],
                                          booleanPassthruAttributes[i]);
                    // NOTE:  otherwise render nothing
                }
            }
	}
    }

    /**
    * Render any "passthru" attributes, where we simply just output the
    * raw name and value of the attribute.  This method is aware of the
    * set of HTML4 attributes that fall into this bucket.  Examples are
    * all the javascript attributes, alt, rows, cols, etc.  <P>
    * @see passthruAttributes
    */
    public static void renderPassThruAttributes(ResponseWriter writer,
        UIComponent component) throws IOException {
	Assert.assert_it(null != writer);
	Assert.assert_it(null != component);

        int i = 0, len = passthruAttributes.length;
	Object value = null;
	for (i = 0; i < len; i++) {
            value = component.getAttributes().get(passthruAttributes[i]);
	    if (value != null && shouldRenderAttribute(value)) {
                if (!(value instanceof String)) {
                    value = value.toString();
                }
		//PENDING(rogerk) will revisit "null" param soon..
		writer.writeAttribute(passthruAttributes[i], value,
                                      passthruAttributes[i]);
	    }
	}
    }

    /**
     * @return true if and only if the argument
     * <code>attributeVal</code> is an instance of a wrapper for a
     * primitive type and its value is equal to the default value for
     * that type as given in the spec.
     */

    private static boolean shouldRenderAttribute(Object attributeVal) {
	if (attributeVal instanceof Boolean && 
	    ((Boolean)attributeVal).booleanValue() == 
	    Boolean.FALSE.booleanValue()) {
	    return false;
	} 
	else if (attributeVal instanceof Integer && 
	    ((Integer)attributeVal).intValue() == Integer.MIN_VALUE) {
	    return false;
	}
	else if (attributeVal instanceof Double && 
	    ((Double)attributeVal).doubleValue() == Double.MIN_VALUE) {
	    return false;
	}
	else if (attributeVal instanceof Character && 
	    ((Character)attributeVal).charValue() == Character.MIN_VALUE) {
	    return false;
	}
	else if (attributeVal instanceof Float && 
	    ((Float)attributeVal).floatValue() == Float.MIN_VALUE) {
	    return false;
	}
	else if (attributeVal instanceof Short && 
	    ((Short)attributeVal).shortValue() == Short.MIN_VALUE) {
	    return false;
	}
	else if (attributeVal instanceof Byte && 
	    ((Byte)attributeVal).byteValue() == Byte.MIN_VALUE) {
	    return false;
	}
	else if (attributeVal instanceof Long && 
	    ((Long)attributeVal).longValue() == Long.MIN_VALUE) {
	    return false;
	}
	return true;
    }

	

    /**

    * @return src with all occurrences of "from" replaced with "to".

    */

    public static String replaceOccurrences(String src, 
					    String from,
					    String to) {
	// a little optimization: don't bother with strings that don't
	// have any occurrences to replace.
	if (-1 == src.indexOf(from)) {
	    return src;
	}
	StringBuffer result = new StringBuffer(src.length());
	StringTokenizer toker = new StringTokenizer(src, from, true);
	String curToken = null;
	while (toker.hasMoreTokens()) {
	    // if the current token is a delimiter, replace it with "to"
	    if ((curToken = toker.nextToken()).equals(from)) {
		result.append(to);
	    }
	    else {
		// it's not a delimiter, just output it.
		result.append(curToken);
	    }
	}
	
	
	return result.toString();
    }
    
    public static ValueBinding getValueBinding(String valueRef) {
        ApplicationFactory factory = (ApplicationFactory)
                FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = factory.getApplication();
        ValueBinding binding = application.getValueBinding(valueRef);
        return binding;
    }         

    /**
     * This method will return a <code>SessionMap</code> for the current
     * <code>FacesContext</code>.  If the <code>FacesContext</code> argument
     * is null, then one is determined by <code>FacesContext.getCurrentInstance()</code>.
     * The <code>SessionMap</code> will be created if it is null.
     * @param context the FacesContext
     * @return Map The <code>SessionMap</code>
     */
    public static Map getSessionMap(FacesContext context) {
        if (context == null) {
            context = FacesContext.getCurrentInstance();
        }
        return context.getExternalContext().getSessionMap();
    }

    public static Converter getConverterForClass(Class converterClass) {
        if (converterClass == null) {
            return null;
        }
        try {
	    ApplicationFactory aFactory = 
		(ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
	    Application application = aFactory.getApplication();
            return (application.createConverter(converterClass));
        } catch (Exception e) {
            return (null);
        }
    }

    public static Converter getConverterForIdentifer(String converterId) {
        if (converterId == null) {
            return null;
        }
        try {
	    ApplicationFactory aFactory = 
		(ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
	    Application application = aFactory.getApplication();
            return (application.createConverter(converterId));
        } catch (Exception e) {
            return (null);
        }
    }
    
    
    /**
     * <p>Return an {@link ExpressionEvaluator} instance using the specified parser.</p>
     * @param parser the parser to be used for this expressionEvaluator
     * @return an ExpressionEvaluator using the specified parser
     */
    public static ExpressionEvaluator getExpressionEvaluator(String parser) {       
        Assert.assert_it(parser != null);
        if (parser.equals(RIConstants.FACES_RE_PARSER)) 
            return FACES_EXPRESSION_EVALUATOR;
        else if (parser.equals(RIConstants.JSP_EL_PARSER))
            return JSP_EXPRESSION_EVALUATOR;
        else 
            return null;                      
    }


    /*
     * Evaluate the EL expression for the given attribute.
     * @throws JspException if an error occurs during evaluation
     */
    public static String evaluateElExpression(String expression, PageContext pageContext) 
                                              throws JspException {
        if (expression != null) {
            //PENDING: horwat: put in quick and dirty expression check.
            //this method will be called often so it needs to be efficient!
            if (isElExpression(expression)) {

                ExpressionInfo exprInfo = new ExpressionInfo();
                exprInfo.setExpressionString(expression);
                exprInfo.setExpectedType(String.class);
                exprInfo.setVariableResolver(new JspVariableResolver(pageContext));
                try {
                    expression = (String)
                        getExpressionEvaluator(RIConstants.JSP_EL_PARSER).evaluate(exprInfo);
                } catch (ElException ele) {
                    throw new JspException(ele.getMessage(), ele);
                }
            }
        }

        return expression;
    }

    /*
     * Determine whether String is an expression or not.
     */
    public static boolean isElExpression(String expression) {
        //check to see if attribute has an expression
        if ((expression.indexOf("${") != -1) &&
            (expression.indexOf("${") < expression.indexOf('}'))) {
            return true;
        }
        return false;
    }
    
    public static StateManager getStateManager(FacesContext context) 
            throws FacesException {
        return(getViewHandler(context).getStateManager());
    }
    
    public static ViewHandler getViewHandler(FacesContext context) 
            throws FacesException {
	// Get Application instance
	ApplicationFactory factory = (ApplicationFactory)
                FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = factory.getApplication();
	Assert.assert_it(application != null);
        
	// Get the ViewHandler
        ViewHandler viewHandler = application.getViewHandler();
        Assert.assert_it(viewHandler != null);
        
        return viewHandler;
    }

    public static ResponseStateManager getResponseStateManager(
            FacesContext context) throws FacesException {
	RenderKit renderKit = null;
	RenderKitFactory renderKitFactory = null;
	String renderKitId = null;
	ResponseStateManager result = null;

	renderKitFactory = (RenderKitFactory)
	    FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	Assert.assert_it(null != renderKitFactory);

	if (context.getViewRoot() == null || 
	    (renderKitId = context.getViewRoot().getRenderKitId()) == null) {
            renderKitId = RenderKitFactory.DEFAULT_RENDER_KIT;
	}
	Assert.assert_it(null != renderKitId);

	renderKit = renderKitFactory.getRenderKit(renderKitId);
	Assert.assert_it(null != renderKit);

	result = renderKit.getResponseStateManager();
        return result;
    }

    public static boolean componentIsDisabledOnReadonly(UIComponent component) {
	Object disabledOrReadonly = null;
	boolean result = false;
	if (null != (disabledOrReadonly = component.getAttributes().get("disabled"))){
	    if (disabledOrReadonly instanceof String) {
		result = ((String)disabledOrReadonly).equalsIgnoreCase("true");
	    }
	    else {
		result = disabledOrReadonly.equals(Boolean.TRUE);
	    }
	}
	if ((result == false) &&
	    null != (disabledOrReadonly = component.getAttributes().get("readonly"))){
	    if (disabledOrReadonly instanceof String) {
		result = ((String)disabledOrReadonly).equalsIgnoreCase("true");
	    }
	    else {
		result = disabledOrReadonly.equals(Boolean.TRUE);
	    }
	}
	    
	return result;
    }

    public static Object createInstance(String className) {
	Class clazz = null;
	Object returnObject = null;
	if (className != null) {
            try {
	        clazz = Util.loadClass(className, returnObject);
	        if (clazz != null) {
	            returnObject = clazz.newInstance();
	        }
	    } catch (Exception e) {
	        Object[] params = new Object[1];
	        params[0] = className;
	        String msg = Util.getExceptionMessage(
		    Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, params);
	        if (log.isErrorEnabled()) {
	            log.error(msg + ":" + className + ":exception:"+
		        e.getMessage());
                }
	    }
        }
	return returnObject;
    }
    
    //
    // General Methods
    //

} // end of class Util
