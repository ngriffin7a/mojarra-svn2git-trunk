/*
 * $Id: ApplicationConfigProcessor.java,v 1.3 2007/04/25 04:06:58 rlubke Exp $
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
 * Copyright 2007 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.config.processor;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ApplicationResourceBundle;
import com.sun.faces.el.ChainAwareVariableResolver;
import com.sun.faces.el.DummyPropertyResolverImpl;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.el.ELResolver;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.MessageFormat;

/**
 * <p>
 *  This <code>ConfigProcessor</code> handles all elements defined under
 *  <code>/faces-config/application</code>.
 * </p>
 */
public class ApplicationConfigProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    /**
     * <code>/faces-config/application</code>
     */
    private static final String APPLICATION =
         "application";

    /**
     * <code>/faces-config/application/action-listener</code>
     */
    private static final String ACTION_LISTENER
         = "action-listener";

    /**
     * <code>/faces-config/application/default-render-kit-id
     */
    private static final String DEFAULT_RENDERKIT_ID
         = "default-render-kit-id";

    /**
     * <code>/faces-config/application/message-bundle
     */
    private static final String MESSAGE_BUNDLE
         = "message-bundle";

    /**
     * <code>/faces-config/application/navigation-handler</code>
     */
    private static final String NAVIGATION_HANDLER
         = "navigation-handler";

    /**
     * <code>/faces-config/application/view-handler</code>
     */
    private static final String VIEW_HANDLER
         = "view-handler";

    /**
     * <code>/faces-config/application/state-manager</code>
     */
    private static final String STATE_MANAGER
         = "state-manager";

    /**
     * <code>/faces-config/application/el-resolver</code>
     */
    private static final String EL_RESOLVER
         = "el-resolver";

    /**
     * <code>/faces-config/application/property-resolver</code>
     */
    private static final String PROPERTY_RESOLVER
         = "property-resolver";

    /**
     * <code>/faces-config/application/variable-resolver</code>
     */
    private static final String VARIABLE_RESOLVER
         = "variable-resolver";

    /**
     * <code>/faces-config/application/locale-config</code>
     */
    private static final String LOCALE_CONFIG
         = "locale-config";

    /**
     * <code>/faces-config/application/locale-config/default-locale</code>
     */
    private static final String DEFAULT_LOCALE
         = "default-locale";

    /**
     * <code>/faces-config/application/locale-config/supported-locale</code>
     */
    private static final String SUPPORTED_LOCALE
         = "supported-locale";

    /**
     * <code>/faces-config/application/resource-bundle</code>
     */
    private static final String RESOURCE_BUNDLE
         = "resource-bundle";

    /**
     * <code>/faces-config/application/resource-bundle/base-name</code>
     */
    private static final String BASE_NAME
         = "base-name";

    /**
     * <code>/faces-config/application/resource-bundle/var</code>
     */
    private static final String VAR
         = "var";

    /**
     * <code>/faces-config/application/resource-bundle/description</code>
     */
    private static final String RES_DESCRIPTIONS
         = "description";

    /**
     * <code>/faces-config/application/resource-bundle/display-name</code>
     */
    private static final String RES_DISPLAY_NAMES
         = "display-name";

    // -------------------------------------------- Methods from ConfigProcessor


    /**
     * @see ConfigProcessor#process(org.w3c.dom.Document[])
     */
    public void process(Document[] documents)
    throws Exception {

        Application app = getApplication();

        for (int i = 0; i < documents.length; i++) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           MessageFormat.format("Processing application elements for document: ''{0}''",
                                                documents[i].getDocumentURI()));
            }
            String namespace =
                 documents[i].getDocumentElement().getNamespaceURI();
            NodeList applicationElements = documents[i].getDocumentElement()
                 .getElementsByTagNameNS(namespace, APPLICATION);
            if (applicationElements != null
                && applicationElements.getLength() > 0) {
                for (int a = 0, asize = applicationElements.getLength();
                     a < asize;
                     a++) {
                    Node appElement = applicationElements.item(a);
                    NodeList children = ((Element) appElement)
                         .getElementsByTagNameNS(namespace, "*");
                    if (children != null && children.getLength() > 0) {
                        for (int c = 0, csize = children.getLength();
                             c < csize;
                             c++) {
                            Node n = children.item(c);
                            if (MESSAGE_BUNDLE.equals(n.getLocalName())) {
                                setMessageBundle(app, n);
                            } else if (DEFAULT_RENDERKIT_ID.equals(n.getLocalName())) {
                                setDefaultRenderKitId(app, n);
                            } else if (ACTION_LISTENER.equals(n.getLocalName())) {
                                addActionListener(app, n);
                            } else if (NAVIGATION_HANDLER.equals(n.getLocalName())) {
                                setNavigationHandler(app, n);
                            } else if (VIEW_HANDLER.equals(n.getLocalName())) {
                                setViewHandler(app, n);
                            } else if (STATE_MANAGER.equals(n.getLocalName())) {
                                setStateManager(app, n);
                            } else if (EL_RESOLVER.equals(n.getLocalName())) {
                                addELResolver(app, n);
                            } else if (PROPERTY_RESOLVER.equals(n.getLocalName())) {
                                addPropertyResolver(app, n);
                            } else if (VARIABLE_RESOLVER.equals(n.getLocalName())) {
                                addVariableResolver(app, n);
                            } else if (DEFAULT_LOCALE.equals(n.getLocalName())) {
                                setDefaultLocale(app, n);
                            } else if (SUPPORTED_LOCALE.equals(n.getLocalName())) {
                                addSupportedLocale(app, n);
                            } else if (RESOURCE_BUNDLE.equals(n.getLocalName())) {
                                addResouceBundle(app, n);
                            }
                        }
                    }
                }
            }
        }
        invokeNext(documents);

    }

    // --------------------------------------------------------- Private Methods


    private void setMessageBundle(Application application,
                                  Node messageBundle) {

        if (messageBundle != null) {
            String bundle = getNodeText(messageBundle);
            if (bundle != null) {

                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               MessageFormat.format(
                                    "Calling Application.setMessageBundle({0})",
                                    bundle));
                }
                application.setMessageBundle(bundle);
            }
        }

    }


    private void setDefaultRenderKitId(Application application,
                                       Node defaultId) {

        if (defaultId != null) {
            String id = getNodeText(defaultId);
            if (id != null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               MessageFormat.format(
                                    "Calling Application.setDefaultRenderKitId({0})",
                                    id));
                }
                application.setDefaultRenderKitId(id);
            }
        }

    }


    private void addActionListener(Application application,
                                   Node actionListener) {

        if (actionListener != null) {

            String listener = getNodeText(actionListener);
            if (listener != null) {
                Object instance = Util.createInstance(listener,
                                                      ActionListener.class,
                                                      application.getActionListener());
                if (instance != null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                   MessageFormat.format(
                                        "Calling Application.setActionListeners({0})",
                                        listener));
                    }
                    application
                         .setActionListener((ActionListener) instance);
                }
            }
        }
    }


    private void setNavigationHandler(Application application,
                                      Node navigationHandler) {

        if (navigationHandler != null) {

            String handler = getNodeText(navigationHandler);
            if (handler != null) {
                Object instance = Util.createInstance(handler,
                                                      NavigationHandler.class,
                                                      application.getNavigationHandler());
                if (instance != null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                   MessageFormat.format(
                                        "Calling Application.setNavigationHandlers({0})",
                                        handler));
                    }
                    application
                         .setNavigationHandler((NavigationHandler) instance);
                }
            }
        }

    }


    private void setStateManager(Application application,
                                 Node stateManager) {

        if (stateManager != null) {
            String manager = getNodeText(stateManager);
            if (manager != null) {
                Object instance = Util.createInstance(manager,
                                                      StateManager.class,
                                                      application.getStateManager());
                if (instance != null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                   MessageFormat.format(
                                        "Calling Application.setStateManagers({0})",
                                        manager));
                    }
                    application.setStateManager((StateManager) instance);
                }
            }
        }

    }


    private void setViewHandler(Application application,
                                Node viewHandler) {

        if (viewHandler != null) {
            String handler = getNodeText(viewHandler);
            if (handler != null) {
                Object instance = Util.createInstance(handler,
                                                      ViewHandler.class,
                                                      application.getViewHandler());
                if (instance != null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                   MessageFormat.format(
                                        "Calling Application.setViewHandlers({0})",
                                        handler));
                    }
                    application.setViewHandler((ViewHandler) instance);
                }
            }
        }

    }


    private void addELResolver(Application application,
                               Node elResolver) {

        if (elResolver != null) {
            ApplicationAssociate associate = ApplicationAssociate
                 .getInstance(FacesContext
                      .getCurrentInstance().getExternalContext());
            if (associate != null) {
                List<ELResolver> resolvers = associate
                     .getELResolversFromFacesConfig();
                if (resolvers == null) {
                    //noinspection CollectionWithoutInitialCapacity
                    resolvers = new ArrayList<ELResolver>();
                    associate.setELResolversFromFacesConfig(resolvers);
                }
                String elResolverClass = getNodeText(elResolver);
                if (elResolverClass != null) {
                    Object elRes = Util.createInstance(elResolverClass);
                    if (elRes != null) {
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.log(Level.FINE,
                                       MessageFormat.format(
                                            "Adding ''{0}'' to ELResolver chain",
                                            elResolverClass));
                        }
                        resolvers.add((ELResolver) elRes);
                    }
                }
            }
        }

    }


    private void addPropertyResolver(Application application,
                                     Node propertyResolver) {

        if (propertyResolver != null) {
            ApplicationAssociate associate = ApplicationAssociate
                 .getInstance(FacesContext
                      .getCurrentInstance().getExternalContext());
            if (associate != null) {
                Object resolverImpl = associate.getLegacyPRChainHead();
                if (resolverImpl == null) {
                    resolverImpl = new DummyPropertyResolverImpl();
                }

                String resolver = getNodeText(propertyResolver);
                if (resolver != null) {
                    resolverImpl = Util.createInstance(resolver,
                                                       PropertyResolver.class,
                                                       resolverImpl);
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                   MessageFormat.format(
                                        "Adding ''{0}'' to PropertyResolver chain",
                                        resolverImpl));
                    }
                }
                if (resolverImpl != null) {
                    associate
                         .setLegacyPRChainHead((PropertyResolver) resolverImpl);
                }
            }
        }

    }


    private void addVariableResolver(Application application,
                                     Node variableResolver) {

        if (variableResolver != null) {
            ApplicationAssociate associate = ApplicationAssociate
                 .getInstance(FacesContext
                      .getCurrentInstance().getExternalContext());
            if (associate != null) {
                Object resolverImpl = associate.getLegacyVRChainHead();
                if (resolverImpl == null) {
                    resolverImpl = new ChainAwareVariableResolver();
                }
                String resolver = getNodeText(variableResolver);
                if (resolver != null) {
                    resolverImpl = Util.createInstance(resolver,
                                                       VariableResolver.class,
                                                       resolverImpl);
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                   MessageFormat.format(
                                        "Adding ''{0}'' to VariableResolver chain",
                                        resolverImpl));
                    }
                }
                if (resolverImpl != null) {
                    associate
                         .setLegacyVRChainHead((VariableResolver) resolverImpl);
                }
            }
        }

    }


    private void setDefaultLocale(Application application,
                                  Node defaultLocale) {
        if (defaultLocale != null) {
            String defLocale = getNodeText(defaultLocale);
            if (defLocale != null) {
                Locale def = Util.getLocaleFromString(defLocale);
                if (def != null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                   MessageFormat.format(
                                        "Setting default Locale to ''{0}''",
                                        defLocale));
                    }
                    application.setDefaultLocale(def);
                }
            }
        }
    }


    private void addSupportedLocale(Application application,
                                    Node supportedLocale) {

        if (supportedLocale != null) {
            Set<Locale> sLocales = getCurrentLocales(application);
            String locString = getNodeText(supportedLocale);
            if (locString != null) {
                Locale loc = Util.getLocaleFromString(locString);
                if (loc != null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                   MessageFormat.format(
                                        "Adding supported Locale ''{0}''",
                                        locString));
                    }
                    sLocales.add(loc);
                }
                application.setSupportedLocales(sLocales);
            }
        }

    }



    private void addResouceBundle(Application application,
                                  Node resourceBundle) {

        if (resourceBundle != null) {
            ApplicationAssociate associate = ApplicationAssociate
                 .getInstance(FacesContext
                      .getCurrentInstance().getExternalContext());
            NodeList children = resourceBundle.getChildNodes();
            if (children != null) {
                String baseName = null;
                String var = null;
                List<Node> descriptions = null;
                List<Node> displayNames = null;
                for (int i = 0, size = children.getLength(); i < size; i++) {
                    Node n = children.item(i);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        if (BASE_NAME.equals(n.getLocalName())) {
                            baseName = getNodeText(n);
                        } else if (VAR.equals(n.getLocalName())) {
                            var = getNodeText(n);
                        } else if (RES_DESCRIPTIONS.equals(n.getLocalName())) {
                            if (descriptions == null) {
                                descriptions = new ArrayList(2);
                            }
                            descriptions.add(n);
                        } else if (RES_DISPLAY_NAMES.equals(n.getLocalName())) {
                            if (displayNames == null) {
                                displayNames = new ArrayList(2);
                            }
                            displayNames.add(n);
                        }
                    }
                }
                if ((baseName != null) && (var != null)) {
                    associate.addResourceBundle(var,
                                                new ApplicationResourceBundle(
                                                     baseName,
                                                     getTextMap(displayNames),
                                                     getTextMap(descriptions)));
                }
            }
        }
    }


    private Set<Locale> getCurrentLocales(Application application) {

        //noinspection CollectionWithoutInitialCapacity
        Set<Locale> supportedLocales = new HashSet<Locale>();
        for (Iterator<Locale> i = application.getSupportedLocales();
             i.hasNext();) {
            supportedLocales.add(i.next());
        }
        return supportedLocales;

    }

}
