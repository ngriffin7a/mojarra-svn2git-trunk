/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
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
package javax.faces.lifecycle;

import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;

/**
 * <p class="changed_added_2_2">This class represents a client window,
 * which may be a browser tab, browser window, browser pop-up, portlet,
 * or anything else that can display a {@link
 * javax.faces.component.UIComponent} hierarchy rooted at a {@link
 * javax.faces.component.UIViewRoot}.</p>
 * 
 * <div class="changed_added_2_2">

 * <p>Modes of Operation</p>

 * <ul>

 * <p>none mode</p>

 * <p>The generation of <code>ClientWindow</code> is controlled by the
 * value of the <code>context-param</code> named by the value of {@link
 * #WINDOW_ID_MODE_PARAM_NAME}.  If this <code>context-param</code> is
 * not specified, or its value is "none", no <code>ClientWindow</code>
 * instances will be generated, and the entire feature is effectively
 * disabled for the entire application.</p>

 * <p>Other modes</p>

 * <p>For all other valid values of {@link
 * #WINDOW_ID_MODE_PARAM_NAME}, including custom values not explicitly covered
 * in this specification, the lifetime of a
 * <code>ClientWindow</code> starts on the first request made by a
 * particular client window (or tab, or pop-up, etc) to the JSF runtime
 * and persists as long as that window remains open or the session expires, 
 * whichever comes first.  A client window is
 * always associated with exactly one <code>UIViewRoot</code> instance
 * at a time, but may display many different <code>UIViewRoot</code>s
 * during its lifetime.</p>

 * <p>The <code>ClientWindow</code> instance is associated with the
 * incoming request during the {@link Lifecycle#attachWindow} method.
 * This method will cause a new instance of <code>ClientWindow</code> to
 * be created, assigned an id, and passed to {@link
 * javax.faces.context.ExternalContext#setClientWindow}.</p>

 * <p>During state saving, regardless of the window id mode, or state
 * saving mode, a hidden field must be written whose name, id and value
 * are given as specified in {@link
 * javax.faces.render.ResponseStateManager#WINDOW_ID_PARAM}. </p>

 * <p>url mode</p>

 * <p>If the value of the {@link #WINDOW_ID_MODE_PARAM_NAME} is "url",
 * without the quotes, the encoding of the <code>ClientWindow</code>
 * must be performed as follows, in addition to the hidden field already
 * described.  The runtime must ensure that any component that renders a
 * hyperlink that causes the user agent to send a GET request to the
 * Faces server when it is clicked has a query parameter with a name and
 * value specified in {@link ResponseStateManager#WINDOW_ID_URL_PARAM}.
 * This requirement is met by several of the "encode" methods on {@link
 * javax.faces.context.ExternalContext} See {@link
 * javax.faces.context.ExternalContext#encodeActionURL(java.lang.String)
 * } for details, including a special case where the windowId is not
 * appended even though url mode is enabled.</p>

 * </ul>
 
 * </div>
 * 
 * @since 2.2
 * 
 */

public abstract class ClientWindow {
    
    /**
     * <p class="changed_added_2_2">The context-param that controls the operation
     * of the <code>ClientWindow</code> feature.  Valid values are "none" and
     * "url", without the quotes.  If not specified, "none" is assumed.</p>
     *
     * @since 2.2
     */
    public static final String WINDOW_ID_MODE_PARAM_NAME =
          "javax.faces.WINDOW_ID_MODE";
    
    
    /**
     * <p class="changed_added_2_2">Return a String value that uniquely 
     * identifies this <code>ClientWindow</code>
     * within the scope of the current session.  See {@link #decode} for the
     * specification of how to derive this value.</p>
     * 
     * @since 2.2
     */
    
    public abstract String getId();
    
    /**
     * <p class="changed_added_2_2">The implementation is responsible
     * for examining the incoming request and extracting the value that must 
     * be returned from the {@link #getId} method.  If {@link #WINDOW_ID_MODE_PARAM_NAME}
     * is "none" this method must not be invoked.  If {@link #WINDOW_ID_MODE_PARAM_NAME}
     * is "url" the implementation must first look for a request parameter
     * under the name given by the value of {@link javax.faces.render.ResponseStateManager#WINDOW_ID_PARAM}.
     * If no value is found, look for a request parameter under the name given
     * by the value of {@link javax.faces.render.ResponseStateManager#WINDOW_ID_URL_PARAM}.
     * If no value is found, fabricate an id that uniquely identifies this
     * <code>ClientWindow</code> within the scope of the current session.  This
     * value must be encrypted with a key stored in the http session and made 
     * available to return from the {@link #getId} method.  The value must be
     * suitable for inclusion as a hidden field or query parameter.
     * If a value is found, decrypt it using the key from the session and 
     * make it available for return from {@link #getId}.</p>
     * 
     * @param context the {@link FacesContext} for this request.
     * 
     * @since 2.2
     */
    
    public abstract void decode(FacesContext context);
    
    private static final String PER_USE_CLIENT_WINDOW_URL_MODE_DISABLED_KEY = 
            "javax.faces.lifecycle.ClientWindowUrlModeEnablement";
    
    /**
     * <p class="changed_added_2_2">Components that permit per-use disabling
     * of the appending of the windowId in generated URLs must call this method
     * first before rendering those URLs.  The caller must call {@link #enableClientWindowUrlMode(javax.faces.context.FacesContext)}
     * from a <code>finally</code> block after rendering the URL.  If 
     * {@link #WINDOW_ID_MODE_PARAM_NAME} is "url" without the quotes, all generated
     * URLs that cause a GET request must append the windowId by default.</p>
     * 
     * @param context the {@link FacesContext} for this request.
     * 
     * @since 2.2
     */
    
    public static void disableClientWindowUrlMode(FacesContext context) {
        Map<Object, Object> attrMap = context.getAttributes();
        attrMap.put(PER_USE_CLIENT_WINDOW_URL_MODE_DISABLED_KEY, Boolean.TRUE);
    }
    
    /**
     * <p class="changed_added_2_2">Components that permit per-use disabling
     * of the appending of the windowId in generated URLs must call this method
     * first after rendering those URLs.  If 
     * {@link #WINDOW_ID_MODE_PARAM_NAME} is "url" without the quotes, all generated
     * URLs that cause a GET request must append the windowId by default.</p>
     * 
     * @param context the {@link FacesContext} for this request.
     * 
     * @since 2.2
     */
    
    public static void enableClientWindowUrlMode(FacesContext context) {
        Map<Object, Object> attrMap = context.getAttributes();
        attrMap.remove(PER_USE_CLIENT_WINDOW_URL_MODE_DISABLED_KEY);
        
    }
    
    /**
     * <p class="changed_added_2_2">Methods that append the windowId to generated
     * URLs must call this method to see if they are permitted to do so.  If 
     * {@link #WINDOW_ID_MODE_PARAM_NAME} is "url" without the quotes, all generated
     * URLs that cause a GET request must append the windowId by default.</p>
     * 
     * @param context the {@link FacesContext} for this request.
     * 
     * @since 2.2
     */
    
    public static boolean isClientWindowUrlModeEnabled(FacesContext context) {
        boolean result = false;
        Map<Object, Object> attrMap = context.getAttributes();
        result = !attrMap.containsKey(PER_USE_CLIENT_WINDOW_URL_MODE_DISABLED_KEY);
        return result;
    }
    
}
