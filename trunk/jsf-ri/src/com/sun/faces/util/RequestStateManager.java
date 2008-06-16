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

package com.sun.faces.util;

import javax.faces.context.FacesContext;

import com.sun.faces.RIConstants;

/**
 * <p>
 * This helper class is for managing per-request state within the
 * the implementation.
 * </p>
 *
 * <p>
 * Mojarra stores several request scoped attributes during its processing.
 * Each time an attribute is added or removed, the action of doing so results
 * in the ServletRequestAttributeListener Mojarra uses for ManagedBean tracking
 * to be triggered.  Additionally, any other ServletRequestAttributeListeners
 * will be invoked.  Since these attributes are, for all intents and purposes,
 * private, reducing the footprint of their usage is ideal.
 * </p>
 *
 * <p>
 * To that end, this class should be used to for all interactions with implementation
 * specific per-request attributes.  There may be cases where an attribute must
 * be in the request instead of the FacesContext attributes map.  For those
 * cases call the appropriate method containing the boolean parameter,
 * <code>useFacesContext</code> with a value of <code>false</code>.
 * </p>
 */
public class RequestStateManager {

    /**
     * Attribute for storing any content within a page that is defined
     * after the closing f:view.
     */
    public static final String AFTER_VIEW_CONTENT =
          RIConstants.FACES_PREFIX + "AFTER_VIEW_CONTENT";

    /**
     * Attribute describing the current ELResolver chain type (either JSP
     * or Faces)
     */
    public static final String EL_RESOLVER_CHAIN_TYPE_NAME =
          RIConstants.FACES_PREFIX + "ELResolverChainType";

    /**
     * Attribute indicating the current component being processed.
     * This will be used when generating bytecode for custom converters.
     */
    public static final String TARGET_COMPONENT_ATTRIBUTE_NAME =
          RIConstants.FACES_PREFIX + "ComponentForValue";

    /**
     * Attribute to lookup the ExternalContextImpl.  This is used by the
     * API to delegate to when a 1.1 ExternalContext implementation decorates
     * the 1.2 implementation.
     */
    public static final String EXTERNALCONTEXT_IMPL_ATTR_NAME =
          RIConstants.FACES_PREFIX + "ExternalContextImpl";

    /**
     * Attribute to lookup the FacesContextImpl.  This is used by the
     * API to delegate to when a 1.1 ExternalContext implementation decorates
     * the 1.2 implementation.
     */
    public static final String FACESCONTEXT_IMPL_ATTR_NAME =
          RIConstants.FACES_PREFIX + "FacesContextImpl";

    /**
     * Attribute defining the {@link javax.faces.render.RenderKit} being used
     * for this request.  Storing it in the request reduces the overhead of
     * querying the RenderKitFactory multiple times.
     */
    public static final String RENDER_KIT_IMPL_REQ =
          RIConstants.FACES_PREFIX + "renderKitImplForRequest";

    /**
     * Attribute containing a Set of clientIds that have messages
     * pending display when rendering response.  Any messages that haven't
     * been displayed will be written to the log.
     */
    public static final String CLIENT_ID_MESSAGES_NOT_DISPLAYED =
          RIConstants.FACES_PREFIX + "clientIdMessagesNotDisplayed";

    /**
     * This attribute is used by the StateMangaer during restore view.
     * The values are stored in the request for later use.
     */
    public static final String LOGICAL_VIEW_MAP =
          RIConstants.FACES_PREFIX + "logicalViewMap";

    /**
     * This attribute is used by the StateMangaer during restore view.
     * The values are stored in the request for later use.
     */
    public static final String ACTUAL_VIEW_MAP =
          RIConstants.FACES_PREFIX + "actualViewMap";

    /**
     * This attribute is used by the loadBundle tag for tracking views/subviews
     * within the logical view (this is only used when 1.1 compatibility is
     * enabled).
     */
    public static final String VIEWTAG_STACK_ATTR_NAME =
          RIConstants.FACES_PREFIX + "taglib.jsf_core.VIEWTAG_STACK";

    /**
     * Attribute to store the {@link javax.faces.webapp.FacesServlet} path of
     * the original request.
     */
    public static final String INVOCATION_PATH =
          RIConstants.FACES_PREFIX + "INVOCATION_PATH";

    /**
     * This attribute protects against infinite loops on expressions that
     * touch a custom legacy VariableResolver that delegates to its parent
     * VariableResolver.
     */
    public static final String REENTRANT_GUARD =
          RIConstants.FACES_PREFIX + "LegacyVariableResolver";

    /**
     * Leveraged by the RequestStateManager to allow deprecated ResponseStateManager
     * methods to continue to work if called.
     */
    public static final String FACES_VIEW_STATE =
          "com.sun.faces.FACES_VIEW_STATE";

    /**
     * Leveraged by the RequestStateManager to allow deprecated ResponseStateManager
     * methods to continue to work if called.
     */
    public static final String FACES_VIEW_STRUCTURE =
          "com.sun.faces.FACES_VIEW_STRUCTURE";

    /**
     * Leveraged by ResourceHandlerImpl to denote whether or not a request
     * is a resource request.  A <code>Boolean</code> value will be assoicated
     * with this key.
     */
    public static final String RESOURCE_REQUEST =
          "com.sun.faces.RESOURCE_REQUEST";


    // ---------------------------------------------------------- Public Methods


    /**
     * Calls through to {@link #get(javax.faces.context.FacesContext, String, boolean)}
     * passing <code>true</code> for the <code>useFacesContext</code> param.
     *
     * @see #get(javax.faces.context.FacesContext, String, boolean)
     */
    public static Object get(FacesContext ctx, String key) {

        return get(ctx, key, true);

    }


    /**
     * @param ctx the <code>FacesContext</code> for the current request
     * @param key the key for the value
     * @param useFacesContext if <code>true</code> then the value will
     *  be retrienved from the FacesContext attributes map, otherwise it will be
     *  retrieved from the ExternalContext request map.
     * @return the value associated with the specified key.
     */
    public static Object get(FacesContext ctx,
                             String key,
                             boolean useFacesContext) {

        if (ctx == null || key == null) {
            return null;
        }

        return ((useFacesContext)
                ? ctx.getAttributes().get(key)
                : ctx.getExternalContext().getRequestMap().get(key));

    }


    /**
     * Calls through to {@link #set(javax.faces.context.FacesContext, String, Object, boolean)}
     * passing <code>true</code> for the <code>useFacesContext</code> param.
     *
     * @see #set(javax.faces.context.FacesContext, String, Object, boolean)
     */
    public static void set(FacesContext ctx, String key, Object value) {

        set(ctx, key, value, true);

    }
    

    /**
     * <p>
     * Adds the specified key and value to the Map stored in the request.
     * If <code>value</code> is <code>null</code>, that key/value pair will
     * be removed from the Map.
     * </p>
     *
     * @param ctx the <code>FacesContext</code> for the current request
     * @param key the key for the value
     * @param value the value to store
     * @param useFacesContext if <code>true</code> then the key/value pair will
     *  be stored in the FacesContext attrigbutes map, otherwise it they will be
     *  stored in the ExternalContext request map
     */
    public static void set(FacesContext ctx,
                           String key,
                           Object value,
                           boolean useFacesContext) {

        if (ctx == null || key == null) {
            return;
        }
        if (value == null) {
            remove(ctx, key, useFacesContext);
        }

        if (useFacesContext) {
            ctx.getAttributes().put(key, value);
        } else {
            ctx.getExternalContext().getRequestMap().put(key, value);
        }

    }


    /**
     * Calls through to {@link #remove(javax.faces.context.FacesContext, String, boolean)}
     * passing <code>true</code> for the <code>useFacesContext</code> param.
     *
     * @see #remove(javax.faces.context.FacesContext, String, boolean)
     */
    public static Object remove(FacesContext ctx, String key) {

        return remove(ctx, key, true);

    }


    /**
     * <p>
     * Remove the value associated with the specified key.
     * </p>
     *
     * @param ctx the <code>FacesContext</code> for the current request
     * @param key the key for the value
     * @param useFacesContext if <code>true</code> then the key/value pair will
     *  be removed from the FacesContext attrigbutes map, otherwise it they will
     *  be removed from the ExternalContext request map
     * @return the value previous associated with the specified key, if any
     */
    public static Object remove(FacesContext ctx,
                                String key,
                                boolean useFacesContext) {

        if (ctx == null || key == null) {
            return null;
        }

        return ((useFacesContext)
                ? ctx.getAttributes().remove(key)
                : ctx.getExternalContext().getRequestMap().remove(key));

    }


    /**
     * Calls through to {@link #containsKey(javax.faces.context.FacesContext, String, boolean)}
     * passing <code>true</code> for the <code>useFacesContext</code> param.
     *
     * @see #containsKey(javax.faces.context.FacesContext, String, boolean)
     */
    public static boolean containsKey(FacesContext ctx, String key) {

        return containsKey(ctx, key, true);

    }


    /**
     * @param ctx the <code>FacesContext</code> for the current request
     * @param key the key for the value
     * @param useFacesContext if <code>true</code> then the key will be checked
     *  in the FacesContext attrigbutes map, otherwise it it will be checked in
     *  the ExternalContext request map
     * @return true if the specified key exists in the Map
     */
    public static boolean containsKey(FacesContext ctx, String key, boolean useFacesContext) {

        return !(ctx == null || key == null) && ((useFacesContext)
                                                 ? ctx.getAttributes().containsKey(key)
                                                 : ctx.getExternalContext()
                                                       .getRequestMap()
                                                       .containsKey(key));

    }

}
