/*
 * $Id: ApplicationImpl.java,v 1.66 2005/08/09 17:38:25 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import com.sun.faces.el.FacesResourceBundleELResolver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContextListener;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ListELResolver;
import javax.el.ResourceBundleELResolver;
import javax.el.MapELResolver;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;

import com.sun.faces.el.FacesCompositeELResolver;
import com.sun.faces.el.ImplicitObjectELResolver;
import com.sun.faces.el.ManagedBeanELResolver;
import com.sun.faces.el.PropertyResolverChainWrapper;
import com.sun.faces.el.PropertyResolverImpl;
import com.sun.faces.el.ScopedAttributeELResolver;
import com.sun.faces.el.VariableResolverChainWrapper;
import com.sun.faces.el.VariableResolverImpl;
import com.sun.faces.util.Util;

import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * <p><strong>Application</strong> represents a per-web-application
 * singleton object where applications based on JavaServer Faces (or
 * implementations wishing to provide extended functionality) can
 * register application-wide singletons that provide functionality
 * required by JavaServer Faces.
 */
public class ApplicationImpl extends Application {

    // Log instance for this class
    private static Logger logger = Util.getLogger(Util.FACES_LOGGER 
            + Util.APPLICATION_LOGGER);

    private static final ELContextListener[] EMPTY_EL_CTX_LIST_ARRAY = { };

    // Relationship Instance Variables

    private ApplicationAssociate associate = null;

    private ActionListener actionListener = null;
    private NavigationHandler navigationHandler = null;
    private PropertyResolver propertyResolver = null;
    private VariableResolver variableResolver = null;
    private ViewHandler viewHandler = null;
    private StateManager stateManager = null;
    //
    // This map stores reference expression | value binding instance
    // mappings.
    //
    
    //
    // These three maps store store "identifier" | "class name"
    // mappings.
    //
    private Map componentMap = null;
    private Map converterIdMap = null;
    private Map converterTypeMap = null;
    private Map validatorMap = null;
    private String messageBundle = null;

    private ArrayList elContextListeners = null;
    private ArrayList elResolvers = null;
    private CompositeELResolver compositeELResolver = null;

    /**
     * Constructor
     */
    public ApplicationImpl() {
        super();
	associate = new ApplicationAssociate(this);
        componentMap = new HashMap();
        converterIdMap = new HashMap();
        converterTypeMap = new HashMap();
        validatorMap = new HashMap();

        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Created Application instance ");
        }
    }

    public void addELContextListener(ELContextListener listener) {
        if (listener != null) {
            if (elContextListeners == null) {
                elContextListeners = new ArrayList();
            }
            elContextListeners.add(listener);
        }
    }
    
    public void removeELContextListener(ELContextListener listener) {
        if (listener != null && elContextListeners != null) {
            elContextListeners.remove(listener);
        }
    }
    
    public ELContextListener [] getELContextListeners() {
        if (elContextListeners != null ) {
            return ((ELContextListener[])
                       elContextListeners.toArray(
                           new ELContextListener[elContextListeners.size()]));
        } else {
            return (EMPTY_EL_CTX_LIST_ARRAY);
        }
    }
   
    public ExpressionFactory getExpressionFactory() {
        return associate.getExpressionFactory();
    }

    public Object evaluateExpressionGet(FacesContext context, 
        String expression, Class expectedType) throws ELException {
        ValueExpression ve = 
          getExpressionFactory().createValueExpression(context.getELContext(), 
                expression,expectedType);     
        return (ve.getValue(context.getELContext()));
    }
    
    public UIComponent createComponent(ValueExpression componentExpression,
        FacesContext context, String componentType) throws FacesException {
        if (null == componentExpression || null == context ||
            null == componentType) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" componentExpression " + componentExpression +
                " context " + context + " componentType " + componentType;
            throw new NullPointerException(message);
        }

        Object result = null;
        boolean createOne = false;

        try {
            if (null != (result = 
                componentExpression.getValue(context.getELContext()))) {
                // if the result is not an instance of UIComponent
                createOne = (!(result instanceof UIComponent));
                // we have to create one.
            }
            if (null == result || createOne) {
                result = this.createComponent(componentType);
                componentExpression.setValue((context.getELContext()), result);
            }
        } catch (Exception ex) {
            throw new FacesException(ex);
        }

        return (UIComponent) result;    
    }

    public ELResolver getELResolver() {
        
        if (compositeELResolver != null) {
            return compositeELResolver;
        }
        compositeELResolver = 
            new FacesCompositeELResolver();    
        compositeELResolver.add(new ImplicitObjectELResolver());
       
        Iterator it = null;
        ArrayList resolvers = associate.geELResolversFromFacesConfig();
        // add ELResolvers from faces-config.xml
        if (resolvers != null) {
            it = resolvers.iterator();
            while (it.hasNext()) {
                compositeELResolver.add((ELResolver) it.next());
            }
        }
        // add legacy VariableResolvers if any.
        
        // wrap the head of the legacyVR in ELResolver and add it to the
        // compositeELResolver.
        if (associate.getLegacyVariableResolver() != null ) {
            compositeELResolver.add(new VariableResolverChainWrapper(
                    associate.getLegacyVariableResolver()));
        } else if (associate.getLegacyVRChainHead() != null) {
            compositeELResolver.add(new VariableResolverChainWrapper(
                    associate.getLegacyVRChainHead()));   
        }
        
        // add legacy PropertyResolvers if any
        if (associate.getLegacyPropertyResolver() != null ) {
            compositeELResolver.add(new PropertyResolverChainWrapper(
                    associate.getLegacyPropertyResolver()));
        } else if (associate.getLegacyPRChainHead() != null) {
            compositeELResolver.add(new PropertyResolverChainWrapper(
                    associate.getLegacyPRChainHead()));   
        }

        if (elResolvers != null) {
            it = elResolvers.iterator();
            while (it.hasNext()) {
                compositeELResolver.add((ELResolver) it.next());
            }
        }
        
        compositeELResolver.add(new ManagedBeanELResolver());
        compositeELResolver.add(new ResourceBundleELResolver());
        compositeELResolver.add(new FacesResourceBundleELResolver());
        compositeELResolver.add(new MapELResolver());
        compositeELResolver.add(new ListELResolver());
        compositeELResolver.add(new ArrayELResolver());
        compositeELResolver.add(new BeanELResolver());
        compositeELResolver.add(new ScopedAttributeELResolver());
        return compositeELResolver;
    }
    
    public void addELResolver(ELResolver resolver) {
        // Throw Illegal State Exception if  ELResolvers are added after 
        // application initialization has completed. 
        if (FacesContext.getCurrentInstance() != null) {
            throw new IllegalStateException(
                    Util.getExceptionMessageString(
                    Util.APPLICATION_INIT_COMPLETE_ERROR_ID));
        }
        if (elResolvers == null) {
            elResolvers = new ArrayList();
        }
        elResolvers.add(resolver);
    }
    
    public ArrayList getApplicationELResolvers() {
        return elResolvers;
    }
    
    public ActionListener getActionListener() {
        return actionListener;
    }


    public ViewHandler getViewHandler() {
        return viewHandler;
    }


    public void setViewHandler(ViewHandler handler) {
        if (handler == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" ViewHandler " + handler;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            if (associate.isResponseRendered()) {
                // at least one response has been rendered.
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE,
                        "jsf.illegal_attempt_setting_viewhandler_error");
                }
                throw new IllegalStateException(Util.getExceptionMessageString(
                    Util.ILLEGAL_ATTEMPT_SETTING_VIEWHANDLER_ID));
            }
            viewHandler = handler;
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE,"set ViewHandler Instance to " 
                        + viewHandler);
            }
        }
    }


    public StateManager getStateManager() {
        return stateManager;
    }


    public void setStateManager(StateManager manager) {
        if (manager == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " StateManager " + manager;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            if (associate.isResponseRendered()) {
                // at least one response has been rendered.
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE,
                        "jsf.illegal_attempt_setting_statemanager_error");
                }
                throw new IllegalStateException(Util.getExceptionMessageString(
                    Util.ILLEGAL_ATTEMPT_SETTING_STATEMANAGER_ID));
            }
            stateManager = manager;
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "set StateManager Instance to " 
                        + stateManager);
            }
        }
    }


    public void setActionListener(ActionListener listener) {
        if (listener == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" ActionListener " + listener;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            this.actionListener = listener;
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("set ActionListener Instance to " + actionListener);
        }
    }


    /**
     * Return the <code>NavigationHandler</code> instance
     * installed present in this application instance.  If
     * an instance does not exist, it will be created.
     */
    public NavigationHandler getNavigationHandler() {
        synchronized (this) {
            if (null == navigationHandler) {
                navigationHandler = new NavigationHandlerImpl();
            }
        }
        return navigationHandler;
    }


    /**
     * Set a <code>NavigationHandler</code> instance for this
     * application instance.
     *
     * @param handler The <code>NavigationHandler</code> instance.
     */
    public void setNavigationHandler(NavigationHandler handler) {
        if (handler == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" NavigationHandler " + handler;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            this.navigationHandler = handler;
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("set NavigationHandler Instance to "+ navigationHandler);
        }
    }


    public PropertyResolver getPropertyResolver() {
        synchronized (this) {
            if (null == propertyResolver) {
                propertyResolver = 
                    new PropertyResolverImpl(getELResolver());
            }
        }
        return propertyResolver;
    }
    
    public ResourceBundle getResourceBundle(FacesContext context, String var) {
        if (null == context || null == var) {
            throw new FacesException("context or var is null.");
        }
        return associate.getResourceBundle(context, var);
    }

    public void setPropertyResolver(PropertyResolver resolver) {
        // Throw Illegal State Exception if  a PropertyResolver is set after 
        // application initialization has completed. 
        if (FacesContext.getCurrentInstance() != null) {
            throw new IllegalStateException(
                    Util.getExceptionMessageString(
                    Util.APPLICATION_INIT_COMPLETE_ERROR_ID));
        }
        if (resolver == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" PropertyResolver " + resolver;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            associate.setLegacyPropertyResolver(resolver); 
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("set PropertyResolver Instance to " + propertyResolver);
        }
    }


    public MethodBinding createMethodBinding(String ref, Class params[]) {
        MethodExpression result = null;
        if (ref == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" ref " + ref;
            throw new NullPointerException(message);
        }
        if (!(ref.startsWith("#{") && ref.endsWith("}"))) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(" Expression " + ref +
                  " does not follow the syntax #{...}");
            }
            throw new ReferenceSyntaxException(ref);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            // return a MethodBinding that wraps a MethodExpression.
	    if (null == params) {
		params = new Class[0];
	    }
            result = 
                getExpressionFactory().
                    createMethodExpression(context.getELContext(), ref, null,
                    params);
        } catch (ELException elex) {
            throw new ReferenceSyntaxException(elex);
        }
        return (new MethodBindingMethodExpressionAdapter(result));
    }


    public ValueBinding createValueBinding(String ref)
        throws ReferenceSyntaxException {
        if (ref == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" ref " + ref;
            throw new NullPointerException(message);
        }
        ValueExpression result = null;
        FacesContext context = FacesContext.getCurrentInstance();
         // return a ValueBinding that wraps a ValueExpression.
         try {
             result= getExpressionFactory().
                     createValueExpression(context.getELContext(),ref,
                     Object.class);     
         } catch (ELException elex) {
            throw new ReferenceSyntaxException(elex);
         } 
         return (new ValueBindingValueExpressionAdapter(result));
    }


    public VariableResolver getVariableResolver() {
        synchronized (this) {
            if (null == variableResolver) {
                variableResolver = new VariableResolverImpl(getELResolver());
            }
        }
        return variableResolver;
    }


    public void setVariableResolver(VariableResolver resolver) {
        // Throw Illegal State Exception if VariableResolver is set after 
        // application initialization has completed. 
        if (FacesContext.getCurrentInstance() != null) {
            throw new IllegalStateException(
                    Util.getExceptionMessageString(
                    Util.APPLICATION_INIT_COMPLETE_ERROR_ID));
        }
        if (resolver == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" VariableResolver " + resolver;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            associate.setLegacyVariableResolver(resolver); 
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("set VariableResolver Instance to " + variableResolver);
        }
    }


    public void addComponent(String componentType, String componentClass) {
        if (componentType == null || componentClass == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" componentType " + componentType +
                " componentClass " + componentClass;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            componentMap.put(componentType, componentClass);
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("added component of type " + componentType +
                      " class " + componentClass);
        }
    }


    public UIComponent createComponent(String componentType)
        throws FacesException {
        if (componentType == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" componentType " + componentType;
            throw new NullPointerException(message);
        }
        UIComponent returnVal = null;
        try {
            returnVal = (UIComponent) newThing(componentType, componentMap);
        } catch (Exception ex) {     
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, 
                        "jsf.cannot_instantiate_component_error", ex);
            }
            throw new FacesException(ex);
        }
        if (returnVal == null) {
            Object[] params = {componentType};
            if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE, 
                            "jsf.cannot_instantiate_component_error", params);
            }
            throw new FacesException(Util.getExceptionMessageString(
                    Util.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }
        
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Created component " + componentType);
        }
        return returnVal;
    }


    public UIComponent createComponent(ValueBinding componentBinding,
                                       FacesContext context,
                                       String componentType)
        throws FacesException {
        if (null == componentBinding || null == context ||
            null == componentType) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" componentBinding " + componentBinding +
                " context " + context + " componentType " + componentType;
            throw new NullPointerException(message);
        }

        Object result = null;
        boolean createOne = false;
        try {
            if (null != (result = componentBinding.getValue(context))) {
                // if the result is not an instance of UIComponent
                createOne = (!(result instanceof UIComponent));
                // we have to create one.
            }
          
            if (null == result || createOne) {
                result = this.createComponent(componentType);
                componentBinding.setValue(context, result);
            }
        } catch (Exception ex) {
            throw new FacesException(ex);
        }
        return (UIComponent) result;
    }


    public Iterator getComponentTypes() {
        Iterator result = Collections.EMPTY_LIST.iterator();
        synchronized (this) {
            result = componentMap.keySet().iterator();
        }

        return result;
    }


    public void addConverter(String converterId, String converterClass) {
        if (converterId == null || converterClass == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " converterId " + converterId +
                " converterClass " + converterClass;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            converterIdMap.put(converterId, converterClass);
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("added converter of type " + converterId +
                      " and class " + converterClass);
        }
    }


    public void addConverter(Class targetClass, String converterClass) {
        if (targetClass == null || converterClass == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" targetClass " + targetClass +
                " converterClass " + converterClass;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            converterTypeMap.put(targetClass, converterClass);
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("added converter of class type " + converterClass);
        }
    }


    public Converter createConverter(String converterId) {
        if (converterId == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" converterId " + converterId;
            throw new NullPointerException(message);
        }
        Converter returnVal = (Converter) newThing(converterId, converterIdMap);
        if (returnVal == null) {
            Object[] params = {converterId};
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, 
                        "jsf.cannot_instantiate_converter_error", converterId);
            }
            throw new FacesException(Util.getExceptionMessageString(
                Util.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("created converter of type " + converterId);
        }
        return returnVal;
    }


    public Converter createConverter(Class targetClass) {
        if (targetClass == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" targetClass " + targetClass;
            throw new NullPointerException(message);
        }
        Converter returnVal = (Converter) newThing(targetClass,
                                                   converterTypeMap);
        if (returnVal != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Created converter of type " + 
                        returnVal.getClass().getName());
            }
            return returnVal;
        } 

        //Search for converters registered to interfaces implemented by
        //targetClass
        Class[] interfaces = targetClass.getInterfaces();
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; i++) {
                returnVal = createConverterBasedOnClass(interfaces[i]);
                if (returnVal != null) {
                   if (logger.isLoggable(Level.FINE)) {
                       logger.fine("Created converter of type " +
                                  returnVal.getClass().getName());
                    }
                    return returnVal;
                }
            }
        }

        //Search for converters registered to superclasses of targetClass
        Class superclass = targetClass.getSuperclass();
        if (superclass != null) {
            returnVal = (Converter) createConverterBasedOnClass(superclass);
            if (returnVal != null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Created converter of type " +
                              returnVal.getClass().getName());
                }
                return returnVal;
            }
        } 
        return returnVal;
    }

    protected Converter createConverterBasedOnClass(Class targetClass) {
        
        Converter returnVal = (Converter) newThing(targetClass,
                                                   converterTypeMap);
        if (returnVal != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Created converter of type " + 
                        returnVal.getClass().getName());
            }
            return returnVal;
        } 

        //Search for converters registered to interfaces implemented by
        //targetClass
        Class[] interfaces = targetClass.getInterfaces();
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; i++) {
                returnVal = createConverterBasedOnClass(interfaces[i]);
                if (returnVal != null) {
                   if (logger.isLoggable(Level.FINE)) {
                       logger.fine("Created converter of type " +
                                  returnVal.getClass().getName());
                    }
                    return returnVal;
                }
            }
        }

        //Search for converters registered to superclasses of targetClass
        Class superclass = targetClass.getSuperclass();
        if (superclass != null) {
            returnVal = (Converter) createConverterBasedOnClass(superclass);
            if (returnVal != null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Created converter of type " +
                              returnVal.getClass().getName());
                }
                return returnVal;
            }
        } 
        return returnVal;
    }

    public Iterator getConverterIds() {
        Iterator result = Collections.EMPTY_LIST.iterator();
        synchronized (this) {
            result = converterIdMap.keySet().iterator();
        }

        return result;
    }


    public Iterator getConverterTypes() {
        Iterator result = Collections.EMPTY_LIST.iterator();
        synchronized (this) {
            result = converterTypeMap.keySet().iterator();
        }
        return result;
    }


    ArrayList supportedLocales = null;


    public Iterator getSupportedLocales() {
        Iterator result = Collections.EMPTY_LIST.iterator();

        synchronized (this) {
            if (null != supportedLocales) {
                result = supportedLocales.iterator();
            }
        }
        return result;
    }


    public void setSupportedLocales(Collection newLocales) {
        if (null == newLocales) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" newLocales " + newLocales;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            supportedLocales = new ArrayList(newLocales);
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "set Supported Locales");
        }
    }


    protected Locale defaultLocale = null;


    public Locale getDefaultLocale() {
        return defaultLocale;
    }


    public void setDefaultLocale(Locale locale) {

        if (locale == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " locale " + locale;
            throw new NullPointerException(message);
        }

        synchronized (this) {
            defaultLocale = locale;
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, ("set defaultLocale " + defaultLocale));
        }

    }


    protected String defaultRenderKitId = null;


    public String getDefaultRenderKitId() {
        return defaultRenderKitId;
    }


    public void setDefaultRenderKitId(String renderKitId) {
        defaultRenderKitId = renderKitId;
    }


    public void addValidator(String validatorId, String validatorClass) {
        if (validatorId == null || validatorClass == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " validatorId " + validatorId + 
                " validatorClass " + validatorClass;
            throw new NullPointerException(message);
        }
        synchronized (this) {
            validatorMap.put(validatorId, validatorClass);
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("added validator of type " + validatorId +
                      " class " + validatorClass);
        }
    }


    public Validator createValidator(String validatorId) throws FacesException {
        if (validatorId == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message +" validatorId " + validatorId;
            throw new NullPointerException(message);
        }
        Validator returnVal = (Validator) newThing(validatorId, validatorMap);
        if (returnVal == null) {
            Object[] params = {validatorId};
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, 
                        "jsf.cannot_instantiate_validator_error", params);
            }
            throw new FacesException(Util.getExceptionMessageString(
                Util.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("created validator of type " + validatorId);
        }
        return returnVal;
    }


    public Iterator getValidatorIds() {
        Iterator result = Collections.EMPTY_LIST.iterator();
        synchronized (this) {
            result = validatorMap.keySet().iterator();
        }
        return result;
    }


    public void setMessageBundle(String messageBundle) {
        synchronized (this) {
            this.messageBundle = messageBundle;
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "set messageBundle " + messageBundle);
        }
    }


    synchronized public String getMessageBundle() {
        return messageBundle;
    }

    /**
     * <p>PRECONDITIONS: the values in the Map are either Strings
     * representing fully qualified java class names, or java.lang.Class
     * instances.</p>
     * <p>ALGORITHM: Look in the argument map for a value for the argument
     * key.  If found, if the value is instanceof String, assume the String
     * specifies a fully qualified java class name and obtain the
     * java.lang.Class instance for that String using Util.loadClass().
     * Replace the String instance in the argument map with the Class
     * instance.  If the value is instanceof Class, proceed.  Assert that the
     * value is either instanceof java.lang.Class or java.lang.String.</p>
     * <p>Now that you have a java.lang.class, call its newInstance and
     * return it as the result of this method.</p>
     *
     * @param key Used to look up the value in the <code>Map</code>.
     * @param map The <code>Map</code> that will be searched.
     * @return The new object instance.
     */
    protected Object newThing(Object key, Map map) {
        assert (key != null && map != null);
        assert (key instanceof String || key instanceof Class);

        Object result = null;
        Class clazz = null;
        Object value = null;
        synchronized (this) {
            value = map.get(key);
            if (value == null) {
                return null;
            }
            assert (value instanceof String || value instanceof Class);
            if (value instanceof String) {
                try {
                    clazz = Util.loadClass((String) value, value);
                    assert (clazz != null);
                    map.put(key, clazz);
                } catch (Throwable t) {
                    throw new FacesException(t.getMessage(), t);
                }
            } else {
                clazz = (Class) value;
            }
        }

        try {
            result = clazz.newInstance();
        } catch (Throwable t) {
            Object[] params = {clazz.getName()};
            throw new FacesException((Util.getExceptionMessageString(
                Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, params)), t);
        }
        return result;
    }
    
    ApplicationAssociate getAssociate() {
        return associate;
    }


    // The testcase for this class is
    // com.sun.faces.application.TestApplicationImpl.java

    // The testcase for this class is
    // com.sun.faces.application.TestApplicationImpl_Config.java
}
