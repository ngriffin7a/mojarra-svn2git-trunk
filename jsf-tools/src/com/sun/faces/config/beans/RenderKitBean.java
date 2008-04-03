/*
 * $Id: RenderKitBean.java,v 1.7 2007/04/27 22:02:43 ofung Exp $
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

package com.sun.faces.config.beans;


import com.sun.faces.util.ToolsUtil;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * <p>Configuration bean for <code>&lt;render-kit&gt; element.</p>
 */

public class RenderKitBean extends FeatureBean {


    private static final Logger logger = ToolsUtil.getLogger(ToolsUtil.FACES_LOGGER +
            ToolsUtil.BEANS_LOGGER);


    // -------------------------------------------------------------- Properties


    private String renderKitClass;
    public String getRenderKitClass() { return renderKitClass; }
    public void setRenderKitClass(String renderKitClass)
    { this.renderKitClass = renderKitClass; }


    private String renderKitId = "HTML_BASIC";
    public String getRenderKitId() { return renderKitId; }
    public void setRenderKitId(String renderKitId)
    { this.renderKitId = renderKitId; }


    // -------------------------------------------------- RendererHolder Methods


    // Key is family + rendererType
    private Map<String,RendererBean> renderers = new TreeMap<String, RendererBean>();


    public void addRenderer(RendererBean descriptor) {
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "addRenderer(" +
                      descriptor.getComponentFamily() + "," +
                      descriptor.getRendererType() + ")");
        }
        renderers.put(descriptor.getComponentFamily() + "|" +
                      descriptor.getRendererType(), descriptor);
    }


    public RendererBean getRenderer(String componentFamily,
                                    String rendererType) {
        return (renderers.get
                (componentFamily + "|" + rendererType));
    }


    public RendererBean[] getRenderers() {
        RendererBean results[] = new RendererBean[renderers.size()];
        return (renderers.values().toArray(results));
    }


    public void removeRenderer(RendererBean descriptor) {
        renderers.remove(descriptor.getComponentFamily() + "|" +
                         descriptor.getRendererType());
    }


    // -------------------------------------------------------------- Extensions


    // ----------------------------------------------------------------- Methods


}
