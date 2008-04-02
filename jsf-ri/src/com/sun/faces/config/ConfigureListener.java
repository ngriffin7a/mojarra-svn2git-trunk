/*
 * $Id: ConfigureListener.java,v 1.2 2004/01/27 21:04:08 eburns Exp $
 */
/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package com.sun.faces.config;

import com.sun.faces.RIConstants;
import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.application.ConfigNavigationCase;
import com.sun.faces.application.ViewHandlerImpl;
import com.sun.faces.config.beans.ApplicationBean;
import com.sun.faces.config.beans.ComponentBean;
import com.sun.faces.config.beans.ConverterBean;
import com.sun.faces.config.beans.FacesConfigBean;
import com.sun.faces.config.beans.FactoryBean;
import com.sun.faces.config.beans.LifecycleBean;
import com.sun.faces.config.beans.ListEntriesBean;
import com.sun.faces.config.beans.ListEntryBean;
import com.sun.faces.config.beans.LocaleConfigBean;
import com.sun.faces.config.beans.ManagedBeanBean;
import com.sun.faces.config.beans.ManagedPropertyBean;
import com.sun.faces.config.beans.MapEntriesBean;
import com.sun.faces.config.beans.MapEntryBean;
import com.sun.faces.config.beans.NavigationCaseBean;
import com.sun.faces.config.beans.NavigationRuleBean;
import com.sun.faces.config.beans.RendererBean;
import com.sun.faces.config.beans.RenderKitBean;
import com.sun.faces.config.beans.ValidatorBean;
import com.sun.faces.config.rules.FacesConfigRuleSet;
import com.sun.faces.util.Util;

import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.webapp.FacesServlet;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * <p>Parse all relevant JavaServer Faces configuration resources, and
 * configure the Reference Implementation runtime environment.</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong>:  The configuration of the
 * <code>Application</code> instance presumes that the implementation class
 * is <code>ApplicationImpl</code> or a subclass thereof.</p>
 */
public final class ConfigureListener implements ServletContextListener {


    // -------------------------------------------------------- Static Variables


    /**
     * <p>The set of <code>ClassLoader</code> instances that have
     * already been configured by this <code>ConfigureListener</code>.</p>
     */
    private static Set loaders = new HashSet();


    /**
     * <p>The <code>Log</code> instance for this class.</p>
     */
    private static Log log = LogFactory.getLog(ConfigureListener.class);


    // ------------------------------------------ ServletContextListener Methods


    public void contextInitialized(ServletContextEvent sce) {
        // Prepare local variables we will need
        Digester digester = null;
        FacesConfigBean fcb = new FacesConfigBean();
	ServletContext context = sce.getServletContext();        
        URL url = null;
        if (log.isDebugEnabled()) {
            log.debug("contextInitialized(" + context.getServletContextName()
                      + ")");
        }
        
	// Ensure that we initialize a particular application ony once
	if (initialized(context)) {
	    return;
	}

        // Step 0, parse obtain the url-pattern information
        // for the FacesServlet.  This information is passed
        // onto the ConfigParser for later use.
        WebXmlParser webXmlParser = new WebXmlParser(context);
        List mappings = webXmlParser.getFacesServletMappings();

        // Step 1, configure a Digester instance we can use
        try {
            digester = digester();
        } catch (MalformedURLException e) {
            throw new FacesException(e); // PENDING - add message
        }

        // Step 2, parse the RI configuration resource
        try {
            url = Util.getCurrentLoader(this).getResource
                (RIConstants.JSF_RI_CONFIG);
            parse(digester, url, fcb);
        } catch (Exception e) {
            String message = Util.getExceptionMessage
                (Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID,
                 new Object[] { url.toExternalForm() });
            log.warn(message, e);
            throw new FacesException(message, e);
        }

        // Step 3, parse the Standard HTML RenderKit
        try {
            url = Util.getCurrentLoader(this).getResource
                (RIConstants.JSF_RI_STANDARD);
            parse(digester, url, fcb);
        } catch (Exception e) {
            String message = Util.getExceptionMessage
                (Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID,
                 new Object[] { url.toExternalForm() });
            log.warn(message, e);
            throw new FacesException(message, e);
        }

	// Step 4, parse any "/META-INF/faces-config.xml" resources
	Enumeration resources;
	try {
	    resources = Util.getCurrentLoader(this).getResources
		("META-INF/faces-config.xml");
	} catch (IOException e) {
            String message = Util.getExceptionMessage
                (Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID,
                 new Object[] { "/META-INF/faces-config.xml" });
            log.warn(message, e);
            throw new FacesException(message, e);
	}
	while (resources.hasMoreElements()) {
	    url = (URL) resources.nextElement();
	    try {
		parse(digester, url, fcb);
	    } catch (Exception e) {
		String message = Util.getExceptionMessage
		    (Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID,
		     new Object[] { url.toExternalForm() });
		log.warn(message, e);
		throw new FacesException(message, e);
	    }
	}

	// Step 5, parse any context-relative resources specified in
	// the web application deployment descriptor
	String paths =
	    context.getInitParameter(FacesServlet.CONFIG_FILES_ATTR);
	if (paths != null) {
	    paths = paths.trim();
	    String path;
	    while (paths.length() > 0) {

		// Identify the next resource path to load
		int comma = paths.indexOf(",");
		if (comma >= 0) {
		    path = paths.substring(0, comma).trim();
		    paths = paths.substring(comma + 1).trim();
		} else {
		    path = paths.trim();
		    paths = "";
		}
		if (path.length() < 1) {
		    break;
		}

		try {
		    url = context.getResource(path);
		    parse(digester, url, fcb);
		} catch (Exception e) {
		    String message = Util.getExceptionMessage
			(Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID,
			 new Object[] { path });
		    log.warn(message, e);
		    throw new FacesException(message, e);
		}
	    }
	}

	// Step 6, parse "/WEB-INF/faces-config.xml" if it exists
	try {
	    url = context.getResource("/WEB-INF/faces-config.xml");
	    if (url != null) {
		parse(digester, url, fcb);
	    }
	} catch (Exception e) {
            String message = Util.getExceptionMessage
                (Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID,
                 new Object[] { url.toExternalForm() });
            log.warn(message, e);
            throw new FacesException(message, e);
	}

        // Step 7, use the accumulated configuration beans to configure the RI
        try {
            configure(context, fcb, mappings);
        } catch (FacesException e) {
            throw e;
        } catch (Exception e) {
            throw new FacesException(e);
        }

        context.setAttribute(RIConstants.CONFIG_ATTR, new Boolean(true)); 

    }

    public void contextDestroyed(ServletContextEvent sce) {  

        ServletContext context = sce.getServletContext();
        if (log.isDebugEnabled()) {
            log.debug("contextDestroyed(" + context.getServletContextName()
                      + ")");
        }

        // Release any allocated application resources
        context.removeAttribute(RIConstants.CONFIG_ATTR);

        // Release the initialization mark on this web application
        release(context);

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the implementation-specific <code>Application</code>
     * instance for this application.  You must <strong>NOT</strong>
     * call this method prior to configuring the appropriate
     * <code>ApplicationFactory</code> class.</p>
     */
    private ApplicationImpl application() {

	ApplicationFactory afactory = (ApplicationFactory)
	    FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
	return ((ApplicationImpl) afactory.getApplication());

    }


    /**
     * <p>Configure the JavaServer Faces reference implementation based on
     * the accumulated configuration beans.</p>
     *
     * @param context <code>ServletContext</code> for this web application
     * @param config <code>FacesConfigBean</code> that is the root of the
     *  tree of configuration information
     */
    private void configure(ServletContext context, FacesConfigBean config,
			   List mappings) throws Exception {
        configure(config.getFactory());
        configure(config.getLifecycle());

	configure(config.getApplication(), mappings);
	configure(config.getComponents());
	configure(config.getConvertersByClass());
	configure(config.getConvertersById());
	configure(config.getManagedBeans());
	configure(config.getNavigationRules());
	configure(config.getRenderKits());
	configure(config.getValidators());

    }


    /**
     * <p>Configure the application objects for this application.</p>
     *
     * @param config <code>ApplicationBean</code> that contains our
     *  configuration information
     * @param mappings List of mappings for <code>FacesServlet</code>
     */
    private void configure(ApplicationBean config, List mappings)
	throws Exception {

	if (config == null) {
	    return;
	}
	ApplicationImpl application = application();
	Object instance;
	String value;
	String values[];

	// Configure scalar properties

	configure(config.getLocaleConfig());

	value = config.getMessageBundle();
	if (value != null) {
	    if (log.isTraceEnabled()) {
		log.trace("setMessageBundle(" + value + ")");
	    }
	    application.setMessageBundle(value);
	}

	value = config.getDefaultRenderKitId();
	if (value != null) {
	    if (log.isTraceEnabled()) {
		log.trace("setDefaultRenderKitId(" + value + ")");
	    }
	    application.setDefaultRenderKitId(value);
	}

	// Configure chains of handlers

	values = config.getActionListeners();
	if ((values != null) && (values.length > 0)) {
	    for (int i = 0; i < values.length; i++) {
		if (log.isTraceEnabled()) {
		    log.trace("setActionListener(" + values[i] + ")");
		}
		instance = Util.createInstance
		    (values[i], ActionListener.class,
		     application.getActionListener());
		if (instance != null) {
		    application.setActionListener((ActionListener) instance);
		}
	    }
	}

	values = config.getNavigationHandlers();
	if ((values != null) && (values.length > 0)) {
	    for (int i = 0; i < values.length; i++) {
		if (log.isTraceEnabled()) {
		    log.trace("setNavigationHandler(" + values[i] + ")");
		}
		instance = Util.createInstance
		    (values[i], NavigationHandler.class,
		     application.getNavigationHandler());
		if (instance != null) {
		    application.setNavigationHandler
			((NavigationHandler) instance);
		}
	    }
	}

	values = config.getPropertyResolvers();
	if ((values != null) && (values.length > 0)) {
	    for (int i = 0; i < values.length; i++) {
		if (log.isTraceEnabled()) {
		    log.trace("setPropertyResolver(" + values[i] + ")");
		}
		instance = Util.createInstance
		    (values[i], PropertyResolver.class,
		     application.getPropertyResolver());
		if (instance != null) {
		    application.setPropertyResolver
			((PropertyResolver) instance);
		}
	    }
	}

	values = config.getStateManagers();
	if ((values != null) && (values.length > 0)) {
	    for (int i = 0; i < values.length; i++) {
		if (log.isTraceEnabled()) {
		    log.trace("setStateManager(" + values[i] + ")");
		}
		instance = Util.createInstance
		    (values[i], StateManager.class,
		     application.getStateManager());
		if (instance != null) {
		    application.setStateManager
			((StateManager) instance);
		}
	    }
	}

	values = config.getVariableResolvers();
	if ((values != null) && (values.length > 0)) {
	    for (int i = 0; i < values.length; i++) {
		if (log.isTraceEnabled()) {
		    log.trace("setVariableResolver(" + values[i] + ")");
		}
		instance = Util.createInstance
		    (values[i], VariableResolver.class,
		     application.getVariableResolver());
		if (instance != null) {
		    application.setVariableResolver
			((VariableResolver) instance);
		}
	    }
	}

	values = config.getViewHandlers();
	if ((values != null) && (values.length > 0)) {
	    for (int i = 0; i < values.length; i++) {
		if (log.isTraceEnabled()) {
		    log.trace("setViewHandler(" + values[i] + ")");
		}
		instance = Util.createInstance
		    (values[i], ViewHandler.class,
		     application.getViewHandler());
		if (instance instanceof ViewHandlerImpl) {
		    ((ViewHandlerImpl) instance).setFacesMapping(mappings);
		}
		if (instance != null) {
		    application.setViewHandler
			((ViewHandler) instance);
		}
	    }
	}

    }


    /**
     * <p>Configure all registered components.</p>
     *
     * @param config Array of <code>ComponentBean</code> that contains
     *  our configuration information
     */
    private void configure(ComponentBean config[]) throws Exception {

	if (config == null) {
	    return;
	}
	ApplicationImpl application = application();

	for (int i = 0; i < config.length; i++) {
	    if (log.isTraceEnabled()) {
		log.trace("addComponent(" +
			  config[i].getComponentType() + "," +
			  config[i].getComponentClass() + ")");
	    }
	    application.addComponent(config[i].getComponentType(),
				     config[i].getComponentClass());
	}

    }

    private static Class primitiveClassesToConvert[] = {
	java.lang.Boolean.TYPE,
	java.lang.Byte.TYPE,
	java.lang.Character.TYPE,
	java.lang.Double.TYPE,
	java.lang.Float.TYPE,
	java.lang.Integer.TYPE,
	java.lang.Long.TYPE,
	java.lang.Short.TYPE
    };

    private static String convertersForPrimitives[] = {
	"javax.faces.convert.BooleanConverter",
	"javax.faces.convert.ByteConverter",
	"javax.faces.convert.CharacterConverter",
	"javax.faces.convert.DoubleConverter",
	"javax.faces.convert.FloatConverter",
	"javax.faces.convert.IntegerConverter",
	"javax.faces.convert.LongConverter",
	"javax.faces.convert.ShortConverter"
    };

    

    /**
     * <p>Configure all registered converters.</p>
     *
     * @param config Array of <code>ConverterBean</code> that contains
     *  our configuration information
     */
    private void configure(ConverterBean config[]) throws Exception {
	int i = 0, len = 0;
	ApplicationImpl application = application();

	// at a minimum, configure the primitive converters
	for (i = 0,len = primitiveClassesToConvert.length;i < len;i++){
	    if (log.isTraceEnabled()) {
		log.trace("addConverterByClass(" +
			  primitiveClassesToConvert[i].toString() +  "," + 
			  convertersForPrimitives[i].toString() + ")");
	    }
	    application.addConverter(primitiveClassesToConvert[i],
				     convertersForPrimitives[i]);
	}
	    
	if (config == null) {
	    return;
	}

	for (i = 0, len = config.length; i < len; i++) {
	    if (config[i].getConverterId() != null) {
		if (log.isTraceEnabled()) {
		    log.trace("addConverterById(" +
			      config[i].getConverterId() + "," +
			      config[i].getConverterClass() + ")");
		}
		application.addConverter(config[i].getConverterId(),
					 config[i].getConverterClass());
	    } else {
		if (log.isTraceEnabled()) {
		    log.trace("addConverterByClass(" +
			      config[i].getConverterForClass() + "," +
			      config[i].getConverterClass() + ")");
		}
		Class clazz = Util.getCurrentLoader(this).loadClass
		    (config[i].getConverterForClass());
		application.addConverter(clazz,
					 config[i].getConverterClass());
	    }
	}

    }


    /**
     * <p>Configure the object factories for this application.</p>
     *
     * @param config <code>FactoryBean</code> that contains our
     *  configuration information
     */
    private void configure(FactoryBean config) throws Exception {

        if (config == null) {
            return;
        }
        String value;

        value = config.getApplicationFactory();
        if (value != null) {
            if (log.isTraceEnabled()) {
                log.trace("setApplicationFactory(" + value + ")");
            }
            FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
                                     value);
        }

        value = config.getFacesContextFactory();
        if (value != null) {
            if (log.isTraceEnabled()) {
                log.trace("setFacesContextFactory(" + value + ")");
            }
            FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
                                     value);
        }

        value = config.getLifecycleFactory();
        if (value != null) {
            if (log.isTraceEnabled()) {
                log.trace("setLifecycleFactory(" + value + ")");
            }
            FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
                                     value);
        }

        value = config.getRenderKitFactory();
        if (value != null) {
            if (log.isTraceEnabled()) {
                log.trace("setRenderKitFactory(" + value + ")");
            }
            FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
                                     value);
        }

    }


    /**
     * <p>Configure the lifecycle listeners for this application.</p>
     *
     * @param config <code>LifecycleBean</code> that contains our
     *  configuration information
     */
    private void configure(LifecycleBean config) throws Exception {

        if (config == null) {
            return;
        }
        String listeners[] = config.getPhaseListeners();
        LifecycleFactory factory = (LifecycleFactory)
            FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle =
            factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        for (int i = 0; i < listeners.length; i++) {
            if (log.isTraceEnabled()) {
                log.trace("addPhaseListener(" + listeners[i] + ")");
            }
            ClassLoader cl = Util.getCurrentLoader(this);
            Class clazz = cl.loadClass(listeners[i]);
            lifecycle.addPhaseListener((PhaseListener) clazz.newInstance());
        }

    }


    /**
     * <p>Configure the locale support for this application.</p>
     *
     *
     * @param config <code>LocaleConfigBean</code> that contains our
     *  configuration information
     */
    private void configure(LocaleConfigBean config) throws Exception {

        if (config == null) {
            return;
        }
	ApplicationImpl application = application();
	String value;
	String values[];

	value = config.getDefaultLocale();
	if (value != null) {
	    if (log.isTraceEnabled()) {
		if (log.isTraceEnabled()) {
		    log.trace("setDefaultLocale(" + value + ")");
		}
		application.setDefaultLocale
		    (Util.getLocaleFromString(value));
	    }
	}

	values = config.getSupportedLocales();
	if ((values != null) && (values.length > 0)) {
	    List locales = new ArrayList();
	    for (int i = 0; i < values.length; i++) {
		if (log.isTraceEnabled()) {
		    log.trace("addSupportedLocale(" + values[i] + ")");
		}
		locales.add(Util.getLocaleFromString(values[i]));
	    }
	    application.setSupportedLocales(locales);
	}

    }


    /**
     * <p>Configure all registered managed beans.</p>
     *
     * @param config Array of <code>ManagedBeanBean</code> that contains
     *  our configuration information
     */
    // PENDING - the code below is a start at converting new-style config beans
    // back to old style ones so we don't have to modify the functional code.
    // It is not clear that this is the lower-effort choice, however.
    private void configure(ManagedBeanBean config[]) throws Exception {
	if (config == null) {
	    return;
	}
	ApplicationImpl application = application();

	for (int i = 0; i < config.length; i++) {
	    if (log.isTraceEnabled()) {
		log.trace("addManagedBean(" +
			  config[i].getManagedBeanName() + "," +
			  config[i].getManagedBeanClass() + ")");
	    }
            ManagedBeanFactory mbf = new ManagedBeanFactory(config[i]);
            if (application instanceof ApplicationImpl) {
                ((ApplicationImpl) application).addManagedBeanFactory(config[i].getManagedBeanName(),
								      mbf);
            }
	}
    }


    /**
     * <p>Configure all registered navigation rules.</p>
     *
     * @param config Array of <code>NavigationRuleBean</code> that contains
     *  our configuration information
     */
    private void configure(NavigationRuleBean config[]) throws Exception {

	if (config == null) {
	    return;
	}
	ApplicationImpl application = application();

	for (int i = 0; i < config.length; i++) {
	    if (log.isTraceEnabled()) {
		log.trace("addNavigationRule(" +
			  config[i].getFromViewId() + ")");
	    }
	    NavigationCaseBean ncb[] = config[i].getNavigationCases();
	    for (int j = 0; j < ncb.length; j++) {
		if (log.isTraceEnabled()) {
		    log.trace("addNavigationCase(" +
			      ncb[j].getFromAction() + "," +
			      ncb[j].getFromOutcome() + "," +
			      ncb[j].isRedirect() + "," +
			      ncb[j].getToViewId() + ")");
		}
		ConfigNavigationCase cnc = new ConfigNavigationCase();
		if (config[i].getFromViewId() == null) {
		    cnc.setFromViewId("*");
		} else {
		    cnc.setFromViewId(config[i].getFromViewId());
		}
		cnc.setFromAction(ncb[j].getFromAction());
		String fromAction = ncb[j].getFromAction();
		if (fromAction == null) {
		    fromAction = "-";
		}
		cnc.setFromOutcome(ncb[j].getFromOutcome());
		String fromOutcome = ncb[j].getFromOutcome();
		if (fromOutcome == null) {
		    fromOutcome = "-";
		}
		cnc.setToViewId(ncb[j].getToViewId());
		String toViewId = ncb[j].getToViewId();
		if (toViewId == null) {
		    toViewId = "-";
		}
		cnc.setKey(cnc.getFromViewId() + fromAction + fromOutcome);
		if (ncb[j].isRedirect()) {
		    cnc.setRedirect("true");
		} else {
		    cnc.setRedirect(null);
		}
		application.addNavigationCase(cnc);
	    }
	}

    }


    /**
     * <p>Configure all registered renderers for this renderkit.</p>
     *
     * @param config Array of <code>RendererBean</code> that contains
     *  our configuration information
     * @param rk RenderKit to be configured
     */
    private void configure(RendererBean config[], RenderKit rk)
	throws Exception {

	if (config == null) {
	    return;
	}

	for (int i = 0; i < config.length; i++) {
	    if (log.isTraceEnabled()) {
		log.trace("addRenderer(" +
			  config[i].getComponentFamily() + "," +
			  config[i].getRendererType() + "," +
			  config[i].getRendererClass() + ")");
	    }
	    Renderer r = (Renderer)
		Util.getCurrentLoader(this).
		loadClass(config[i].getRendererClass()).
		newInstance();
	    rk.addRenderer(config[i].getComponentFamily(),
			   config[i].getRendererType(),
			   r);
	}

    }


    /**
     * <p>Configure all registered renderKits.</p>
     *
     * @param config Array of <code>RenderKitBean</code> that contains
     *  our configuration information
     */
    private void configure(RenderKitBean config[]) throws Exception {

	if (config == null) {
	    return;
	}
	RenderKitFactory rkFactory = (RenderKitFactory)
	    FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

	for (int i = 0; i < config.length; i++) {
	    RenderKit rk = rkFactory.getRenderKit
		(null, config[i].getRenderKitId());
	    if (rk == null) {
		if (log.isTraceEnabled()) {
		    log.trace("createRenderKit(" +
			      config[i].getRenderKitId() + "," +
			      config[i].getRenderKitClass() + ")");
		}
		if (config[i].getRenderKitClass() == null) {
		    throw new IllegalArgumentException // PENDING - i18n
			("No renderKitClass for renderKit " +
			 config[i].getRenderKitId());
		}
		rk = (RenderKit)
		    Util.getCurrentLoader(this).
		    loadClass(config[i].getRenderKitClass()).
		    newInstance();
		rkFactory.addRenderKit(config[i].getRenderKitId(), rk);
	    } else {
		if (log.isTraceEnabled()) {
		    log.trace("getRenderKit(" +
			      config[i].getRenderKitId() + ")");
		}
	    }
	    configure(config[i].getRenderers(), rk);
	}

    }


    /**
     * <p>Configure all registered validators.</p>
     *
     * @param config Array of <code>ValidatorBean</code> that contains
     *  our configuration information
     */
    private void configure(ValidatorBean config[]) throws Exception {

	if (config == null) {
	    return;
	}
	ApplicationImpl application = application();

	for (int i = 0; i < config.length; i++) {
	    if (log.isTraceEnabled()) {
		log.trace("addValidator(" +
			  config[i].getValidatorId() + "," +
			  config[i].getValidatorClass() + ")");
	    }
	    application.addValidator(config[i].getValidatorId(),
				     config[i].getValidatorClass());
	}

    }


    /**
     * <p>Configure and return a <code>Digester</code> instance suitable for
     * parsing the runtime configuration information we need.</p>
     *
     * @exception MalformedURLException if a URL cannot be formed correctly
     */
    private Digester digester() throws MalformedURLException {

        Digester digester = new Digester();

        // Configure basic properties
        digester.setNamespaceAware(false);
        digester.setUseContextClassLoader(true);
        digester.setValidating(true);

        // Configure parsing rules
        digester.addRuleSet(new FacesConfigRuleSet(false, false, true));

        // Configure preregistered entities
        URL url = this.getClass().getResource
            ("/com/sun/faces/web-facesconfig_1_0.dtd");
        digester.register
            ("-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.0//EN",
             url.toString());

        // Push an initial FacesConfigBean onto the stack
        digester.push(new FacesConfigBean());

        return (digester);

    }


    /**
     * <p>Return <code>true</code> if this web application has already
     * been initialized.  If it has not been initialized, also record
     * the initialization of this application until removed by a call to
     * <code>release()</code>.</p>
     *
     * @param context <code>ServletContext</code> for this web application
     */
    private boolean initialized(ServletContext context) {

        // Initialize at most once per web application class loader
        ClassLoader cl = Util.getCurrentLoader(this);
	synchronized(loaders) {
            if (!loaders.contains(cl)) {
	        loaders.add(cl);
		if (log.isTraceEnabled()) {
		    log.trace("Initializing this webapp");
		}
		return false;
	    } else {
		if (log.isTraceEnabled()) {
		    log.trace("Listener already completed for this webapp");
		}
	        return true;
	    }
	}

    }


    /**
     * <p>Parse the configuration resource at the specified URL, using
     * the specified <code>Digester</code> instance.</p>
     *
     * @param digester Digester to use for parsing
     * @param url URL of the configuration resource to be parsed
     * @param fcb FacesConfigBean to accumulate results
     *
     * @exception IOException if an input/output error occurs
     * @exception SAXException if an XML parsing error occurs
     */
    private void parse(Digester digester, URL url, FacesConfigBean fcb)
        throws IOException, SAXException {

        if (log.isDebugEnabled()) {
            log.debug("parse(" + url.toExternalForm() + ")");
        }

        URLConnection conn = null;
        InputStream stream = null;
        InputSource source = null;
        try {
            conn = url.openConnection();
            conn.setUseCaches(false);
            stream = conn.getInputStream();
            source = new InputSource(url.toExternalForm());
            source.setByteStream(stream);
            digester.clear();
            digester.push(fcb);
            digester.parse(source);
            stream.close();
            stream = null;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    ;
                }
            }
            stream = null;
        }

    }


    /**
     * <p>Release the mark that this web application has been initialized.</p>
     *
     * @param context <code>ServletContext</code> for this web application
     */
    private void release(ServletContext context) {

        ClassLoader cl = Util.getCurrentLoader(this);
        synchronized(loaders) {
            loaders.remove(cl);
        }

    }


} 
