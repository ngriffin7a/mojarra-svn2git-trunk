/*
 * $Id: Util.java,v 1.212 2007/04/27 22:01:06 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// Util.java

package com.sun.faces.util;

import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.RenderKitImpl;

import javax.el.ELResolver;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.convert.Converter;
import javax.faces.event.AbortProcessingException;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletContext;
import java.beans.FeatureDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * <B>Util</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Util.java,v 1.212 2007/04/27 22:01:06 ofung Exp $
 */

public class Util {

    
    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    // README - make sure to add the message identifier constant
    // (ex: Util.CONVERSION_ERROR_MESSAGE_ID) and the number of substitution
    // parameters to test/com/sun/faces/util/TestUtil_messages (see comment there).
 
    // Loggers
    public static final String RENDERKIT_LOGGER = ".renderkit";
    public static final String TAGLIB_LOGGER = ".taglib";
    public static final String APPLICATION_LOGGER = ".application";
    public static final String CONTEXT_LOGGER = ".context";
    public static final String CONFIG_LOGGER = ".config";
    public static final String LIFECYCLE_LOGGER = ".lifecycle";
    public static final String TIMING_LOGGER = ".timing";

    /**
     * Flag that, when true, enables special behavior in the RI to enable
     * unit testing.
     */
    private static boolean unitTestModeEnabled = false;

    /**
     * Flag that enables/disables the core TLV.
     */
    private static boolean coreTLVEnabled = true;

    /**
     * Flag that enables/disables the html TLV.
     */
    private static boolean htmlTLVEnabled = true;
    
    private static final Map<String,Pattern> patternCache = 
          new LRUMap<String,Pattern>(15);
    
     /**
     * <p>The <code>request</code> scoped attribute to store the
     * {@link javax.faces.webapp.FacesServlet} path of the original
     * request.</p>
     */
    private static final String INVOCATION_PATH =
        RIConstants.FACES_PREFIX + "INVOCATION_PATH";

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    private Util() {
        throw new IllegalStateException();
    }

//
// Class methods
//

    /**
     * <p>Factory method for creating the varius JSF listener
     *  instances that may be referenced by <code>type</code>
     *  or <code>binding</code>.</p>
     * <p>If <code>binding</code> is not <code>null</code>
     * and the evaluation result is not <code>null</code> return
     * that instance.  Otherwise try to instantiate an instances
     * based on <code>type</code>.</p>
     * 
     * @param type the <code>Listener</code> type
     * @param binding a <code>ValueExpression</code> which resolves
     *  to a <code>Listener</code> instance
     * @return a <code>Listener</code> instance based off the provided
     *  <code>type</code> and <binding>
     */
    public static Object getListenerInstance(ValueExpression type,
                                             ValueExpression binding) {

        FacesContext faces = FacesContext.getCurrentInstance();
        Object instance = null;
        if (faces == null) {
            return null;
        }
        if (binding != null) {
            instance = binding.getValue(faces.getELContext());
        }
        if (instance == null && type != null) {
            try {
                instance = ReflectionUtils.newInstance(((String) type.getValue(faces.getELContext())));
            } catch (Exception e) {
                throw new AbortProcessingException(e.getMessage(), e);
            }

            if (binding != null) {
                binding.setValue(faces.getELContext(), instance);
            }
        }

        return instance;

    }

    public static void setUnitTestModeEnabled(boolean enabled) {
        unitTestModeEnabled = enabled;
    }

    public static boolean isUnitTestModeEnabled() {
        return unitTestModeEnabled;
    }

    public static void setCoreTLVActive(boolean active) {
        coreTLVEnabled = active;
    }

    public static boolean isCoreTLVActive() {
        return coreTLVEnabled;
    }

    public static void setHtmlTLVActive(boolean active) {
        htmlTLVEnabled = active;
    }

    public static boolean isHtmlTLVActive() {
        return htmlTLVEnabled;
    }


    public static Class loadClass(String name,
                                  Object fallbackClass)
        throws ClassNotFoundException {
        ClassLoader loader = Util.getCurrentLoader(fallbackClass);
        return Class.forName(name, false, loader);       
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
     * Verify the existence of all the factories needed by faces.  Create
     * and install the default RenderKit into the ServletContext. <P>
     *
     * @see javax.faces.FactoryFinder
     */

    public static void verifyFactoriesAndInitDefaultRenderKit(ServletContext context)
        throws FacesException {
        RenderKitFactory renderKitFactory = null;
        LifecycleFactory lifecycleFactory = null;
        FacesContextFactory facesContextFactory = null;
        ApplicationFactory applicationFactory = null;
        RenderKit defaultRenderKit = null;

        renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        assert (null != renderKitFactory);

        lifecycleFactory = (LifecycleFactory)
            FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        assert (null != lifecycleFactory);

        facesContextFactory = (FacesContextFactory)
            FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        assert (null != facesContextFactory);

        applicationFactory = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        assert (null != applicationFactory);

        defaultRenderKit =
            renderKitFactory.getRenderKit(null,
                                          RenderKitFactory.HTML_BASIC_RENDER_KIT);
        if (defaultRenderKit == null) {
            // create default renderkit if doesn't exist
            //
            defaultRenderKit = new RenderKitImpl();
            renderKitFactory.addRenderKit(
                RenderKitFactory.HTML_BASIC_RENDER_KIT,
                defaultRenderKit);
        }

        context.setAttribute(RIConstants.HTML_BASIC_RENDER_KIT,
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
     * <p/>
     * <p>The result of the check is saved in the ServletContext
     * attribute RIConstants.HAS_REQUIRED_CLASSES_ATTR.</p>
     * <p/>
     * <p>Algorithm:</p>
     * <p/>
     * <p>Check the ServletContext for the attribute, if found, and the
     * value is false, that means we've checked before, and we don't have
     * the classes, just throw FacesException.  If the value is true,
     * we've checked before and we have the classes, just return.</p>
     */

    public static void verifyRequiredClasses(FacesContext facesContext)
        throws FacesException {
        Map<String,Object> applicationMap = facesContext.getExternalContext()
            .getApplicationMap();
        Boolean result = null;
        String className = "javax.servlet.jsp.jstl.fmt.LocalizationContext";
        Object[] params = {className};

        // Have we checked before?
        if (null != (result = (Boolean)
            applicationMap.get(RIConstants.HAS_REQUIRED_CLASSES_ATTR))) {
            // yes, and the check failed.
            if (Boolean.FALSE.equals(result)) {
                throw new
                    FacesException(
                        MessageUtils.getExceptionMessageString(
                            MessageUtils.MISSING_CLASS_ERROR_MESSAGE_ID, params));
            } else {
                // yes, and the check passed.
                return;
            }
        }

        //
        // We've not checked before, so do the check now!
        //

        try {
            Util.loadClass(className, facesContext);
        } catch (ClassNotFoundException e) {
            applicationMap.put(RIConstants.HAS_REQUIRED_CLASSES_ATTR,
                               Boolean.FALSE);
            throw new FacesException(
                MessageUtils.getExceptionMessageString(MessageUtils.MISSING_CLASS_ERROR_MESSAGE_ID,
                                         params),
                e);
        }
        applicationMap.put(RIConstants.HAS_REQUIRED_CLASSES_ATTR, Boolean.TRUE);
    }
   
    
    /**
     * <p>If the FacesContext has a UIViewRoot, and this UIViewRoot has a Locale,
     * return it.  Otherwise return Locale.getDefault().
     */
    
    public static Locale getLocaleFromContextOrSystem(FacesContext context) {
        Locale result, temp = Locale.getDefault();
        UIViewRoot root = null;
        result = temp;
        if (null != context) {
            if (null != (root = context.getViewRoot())) {
                if (null == (result = root.getLocale())) {
                    result = temp;
                }
            }
        }
        return result;
    }


    public static Converter getConverterForClass(Class converterClass,
                                                 FacesContext context) {
        if (converterClass == null) {
            return null;
        }
        try {            
            Application application = context.getApplication();
            return (application.createConverter(converterClass));
        } catch (Exception e) {
            return (null);
        }
    }


    public static Converter getConverterForIdentifer(String converterId,
                                                     FacesContext context) {
        if (converterId == null) {
            return null;
        }
        try {            
            Application application = context.getApplication();
            return (application.createConverter(converterId));
        } catch (Exception e) {
            return (null);
        }
    }


    public static StateManager getStateManager(FacesContext context)
        throws FacesException {
        return (context.getApplication().getStateManager());
    }


    public static ViewHandler getViewHandler(FacesContext context)
        throws FacesException {
        // Get Application instance
        Application application = context.getApplication();
        assert (application != null);

        // Get the ViewHandler
        ViewHandler viewHandler = application.getViewHandler();
        assert (viewHandler != null);

        return viewHandler;
    }


    public static boolean componentIsDisabled(UIComponent component) {

        return (Boolean.valueOf(String.valueOf(component.getAttributes().get("disabled"))));

    }


    public static boolean componentIsDisabledOrReadonly(UIComponent component) {
        return Boolean.valueOf(String.valueOf(component.getAttributes().get("disabled"))) || Boolean.valueOf(String.valueOf(component.getAttributes().get("readonly")));
    }


    public static Object createInstance(String className) {
        return createInstance(className, null, null);
    }


    public static Object createInstance(String className,
                                        Class rootType,
                                        Object root) {
        Class clazz;
        Object returnObject = null;
        if (className != null) {
            try {
                clazz = Util.loadClass(className, returnObject);
                if (clazz != null) {
                    // Look for an adapter constructor if we've got
                    // an object to adapt
                    if ((rootType != null) && (root != null)) {

                        Constructor construct =
                              ReflectionUtils.lookupConstructor(
                                    clazz,
                                    rootType);
                        if (construct != null) {
                            returnObject = construct.newInstance(root);
                        } 
                    }
                }
                if (clazz != null && returnObject == null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.log(Level.FINE,
                                       "jsf.util.no.adapter.ctor.available",
                                       new Object[]{
                                             clazz.getName(),
                                             (rootType != null ? rootType.getName() : "null")
                                       });
                        }
                        returnObject = clazz.newInstance();
                }
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    Object[] params = new Object[1];
                    params[0] = className;
                    String msg = MessageUtils.getExceptionMessageString(
                          MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID,
                          params);
                    LOGGER.log(Level.SEVERE, msg, e);
                }
            }
        }
        return returnObject;
    }

    // W3C XML specification refers to IETF RFC 1766 for language code
    // structure, therefore the value for the xml:lang attribute should
    // be in the form of language or language-country or
    // language-country-variant.

    public static Locale getLocaleFromString(String localeStr)
        throws IllegalArgumentException {
        // length must be at least 2.
        if (null == localeStr || localeStr.length() < 2) {
            throw new IllegalArgumentException("Illegal locale String: " +
                                               localeStr);
        }

        Locale result = null;
        String
            lang = null,
            country = null,
            variant = null;
        char[] seps = {
            '-',
            '_'
        };
        int
            i = 0,
            j = 0;

        // to have a language, the length must be >= 2
        if ((localeStr.length() >= 2) &&
            (-1 == (i = indexOfSet(localeStr, seps, 0)))) {
            // we have only Language, no country or variant
            if (2 != localeStr.length()) {
                throw new
                    IllegalArgumentException("Illegal locale String: " +
                                             localeStr);
            }
            lang = localeStr.toLowerCase();
        }

        // we have a separator, it must be either '-' or '_'
        if (-1 != i) {
            lang = localeStr.substring(0, i);
            // look for the country sep.
            // to have a country, the length must be >= 5
            if ((localeStr.length() >= 5) &&
                (-1 == (j = indexOfSet(localeStr, seps, i + 1)))) {
                // no further separators, length must be 5
                if (5 != localeStr.length()) {
                    throw new
                        IllegalArgumentException("Illegal locale String: " +
                                                 localeStr);
                }
                country = localeStr.substring(i + 1);
            }
            if (-1 != j) {
                country = localeStr.substring(i + 1, j);
                // if we have enough separators for language, locale,
                // and variant, the length must be >= 8.
                if (localeStr.length() >= 8) {
                    variant = localeStr.substring(j + 1);
                } else {
                    throw new
                        IllegalArgumentException("Illegal locale String: " +
                                                 localeStr);
                }
            }
        }
        if (null != variant && null != country && null != lang) {
            result = new Locale(lang, country, variant);
        } else if (null != lang && null != country) {
            result = new Locale(lang, country);
        } else if (null != lang) {
            result = new Locale(lang, "");
        }
        return result;
    }


    /**
     * @return starting at <code>fromIndex</code>, the index of the
     *         first occurrence of any substring from <code>set</code> in
     *         <code>toSearch</code>, or -1 if no such match is found
     */

    public static int indexOfSet(String str, char[] set,
                                 int fromIndex) {
        int result = -1;
        char[] toSearch = str.toCharArray();
        for (int i = fromIndex, len = toSearch.length; i < len; i++) {
            for (int j = 0, innerLen = set.length; j < innerLen; j++) {
                if (toSearch[i] == set[j]) {
                    result = i;
                    break;
                }
            }
            if (-1 != result) {
                break;
            }
        }
        return result;
    }


    public static void parameterNonNull(Object param) throws FacesException {
        if (null == param) {
            throw new FacesException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "param"));
        }
    }


    public static void parameterNonEmpty(String param) throws FacesException {
        if (null == param || 0 == param.length()) {
            throw new FacesException(MessageUtils.getExceptionMessageString(MessageUtils.EMPTY_PARAMETER_ID));
        }
    }

    /**
     * <p>Leverage the Throwable.getStackTrace() method to produce a
     * String version of the stack trace, with a "\n" before each
     * line.</p>
     *
     * @return the String representation ofthe stack trace obtained by
     * calling getStackTrace() on the passed in exception.  If null is
     * passed in, we return the empty String.
     */ 

    public static String getStackTraceString(Throwable e) {
	if (null == e) {
	    return "";
	}
	
	StackTraceElement[] stacks = e.getStackTrace();
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < stacks.length; i++) {
	    sb.append(stacks[i].toString()).append('\n');
	}
	return sb.toString();
    }

    /**
     * <p>PRECONDITION: argument <code>response</code> is non-null and
     * has a method called <code>getContentType</code> that takes no
     * arguments and returns a String, with no side-effects.</p>
     *
     * <p>This method allows us to get the contentType in both the
     * servlet and portlet cases, without introducing a compile-time
     * dependency on the portlet api.</p>
     *
     */
    public static String getContentTypeFromResponse(Object response) {
        String result = null;
        if (null != response) {           

            try {
                Method method = ReflectionUtils.lookupMethod(
                      response.getClass(),
                      "getContentType",
                      RIConstants.EMPTY_CLASS_ARGS
                );
                if (null != method) {
                    Object obj =
                          method.invoke(response, RIConstants.EMPTY_METH_ARGS);
                    if (null != obj) {
                        result = obj.toString();
                    }
                }
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
        return result;
    }

    public static boolean prefixViewTraversal(FacesContext context,
					      UIComponent root,
					      TreeTraversalCallback action) throws FacesException {
	boolean keepGoing = false;
	if (keepGoing = action.takeActionOnNode(context, root)) {
	    Iterator<UIComponent> kids = root.getFacetsAndChildren();
	    while (kids.hasNext() && keepGoing) {
		keepGoing = prefixViewTraversal(context, 
						kids.next(), 
						action);
	    }
	}
	return keepGoing;
    }

    public static interface TreeTraversalCallback {
	public boolean takeActionOnNode(FacesContext context, 
					UIComponent curNode) throws FacesException;
    }
    
    public static FeatureDescriptor getFeatureDescriptor(String name, String
        displayName, String desc, boolean expert, boolean hidden, 
        boolean preferred, Object type, Boolean designTime) {
            
        FeatureDescriptor fd = new FeatureDescriptor();
        fd.setName(name);
        fd.setDisplayName(displayName);
        fd.setShortDescription(desc);
        fd.setExpert(expert);
        fd.setHidden(hidden);
        fd.setPreferred(preferred);
        fd.setValue(ELResolver.TYPE, type);
        fd.setValue(ELResolver.RESOLVABLE_AT_DESIGN_TIME, designTime);
        return fd;
    }
   

    /**
     * <p>A slightly more efficient version of 
     * <code>String.split()</code> which caches
     * the <code>Pattern</code>s in an LRUMap instead of
     * creating a new <code>Pattern</code> on each
     * invocation.</p>
     * @param toSplit the string to split
     * @param regex the regex used for splitting
     * @return the result of <code>Pattern.spit(String, int)</code>
     */
    public synchronized static String[] split(String toSplit, String regex) {
        Pattern pattern = patternCache.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            patternCache.put(regex, pattern);
        }
        return pattern.split(toSplit, 0);
    }


    /**
     * <p>Returns the URL pattern of the
     * {@link javax.faces.webapp.FacesServlet} that
     * is executing the current request.  If there are multiple
     * URL patterns, the value returned by
     * <code>HttpServletRequest.getServletPath()</code> and
     * <code>HttpServletRequest.getPathInfo()</code> is
     * used to determine which mapping to return.</p>
     * If no mapping can be determined, it most likely means
     * that this particular request wasn't dispatched through
     * the {@link javax.faces.webapp.FacesServlet}.
     *
     * @param context the {@link FacesContext} of the current request
     *
     * @return the URL pattern of the {@link javax.faces.webapp.FacesServlet}
     *         or <code>null</code> if no mapping can be determined
     *
     * @throws NullPointerException if <code>context</code> is null
     */
    public static String getFacesMapping(FacesContext context) {

        if (context == null) {
            String message = MessageUtils.getExceptionMessageString
                  (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context");
            throw new NullPointerException(message);
        }

        // Check for a previously stored mapping   
        ExternalContext extContext = context.getExternalContext();
        String mapping =
              (String) extContext.getRequestMap().get(INVOCATION_PATH);

        if (mapping == null) {
         
            String servletPath = null;
            String pathInfo = null;

            // first check for javax.servlet.forward.servlet_path
            // and javax.servlet.forward.path_info for non-null
            // values.  if either is non-null, use this
            // information to generate determine the mapping.

            servletPath = extContext.getRequestServletPath();
            pathInfo = extContext.getRequestPathInfo();

            mapping = getMappingForRequest(servletPath, pathInfo);
            if (mapping == null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               "jsf.faces_servlet_mapping_cannot_be_determined_error",
                               new Object[]{servletPath});
                }
            }
        }
        
        // if the FacesServlet is mapped to /* throw an 
        // Exception in order to prevent an endless 
        // RequestDispatcher loop
        if ("/*".equals(mapping)) {
            throw new FacesException(MessageUtils.getExceptionMessageString(
                  MessageUtils.FACES_SERVLET_MAPPING_INCORRECT_ID));
        }

        if (mapping != null) {
            extContext.getRequestMap().put(INVOCATION_PATH, mapping);
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE,
                       "URL pattern of the FacesServlet executing the current request "
                       + mapping);
        }
        return mapping;
    }

    /**
     * <p>Return the appropriate {@link javax.faces.webapp.FacesServlet} mapping
     * based on the servlet path of the current request.</p>
     *
     * @param servletPath the servlet path of the request
     * @param pathInfo    the path info of the request
     *
     * @return the appropriate mapping based on the current request
     *
     * @see HttpServletRequest#getServletPath()
     */
    private static String getMappingForRequest(String servletPath, String pathInfo) {

        if (servletPath == null) {
            return null;
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "servletPath " + servletPath);
            LOGGER.log(Level.FINE, "pathInfo " + pathInfo);
        }
        // If the path returned by HttpServletRequest.getServletPath()
        // returns a zero-length String, then the FacesServlet has
        // been mapped to '/*'.
        if (servletPath.length() == 0) {
            return "/*";
        }

        // presence of path info means we were invoked
        // using a prefix path mapping
        if (pathInfo != null) {
            return servletPath;
        } else if (servletPath.indexOf('.') < 0) {
            // if pathInfo is null and no '.' is present, assume the
            // FacesServlet was invoked using prefix path but without
            // any pathInfo - i.e. GET /contextroot/faces or
            // GET /contextroot/faces/
            return servletPath;
        } else {
            // Servlet invoked using extension mapping
            return servletPath.substring(servletPath.lastIndexOf('.'));
        }
    }
    
    
    /**
     * <p>Returns true if the provided <code>url-mapping</code> is
     * a prefix path mapping (starts with <code>/</code>).</p>
     *
     * @param mapping a <code>url-pattern</code>
     * @return true if the mapping starts with <code>/</code>
     */
    public static boolean isPrefixMapped(String mapping) {
        return (mapping.charAt(0) == '/');
    }


} // end of class Util
