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
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
package javax.faces.render;

import java.io.IOException;
import javax.faces.FacesWrapper;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;


/**
 * <p class="changed_added_2_2">Provides a simple implementation of 
 * {@link Renderer} that can be subclassed by developers wishing to
 * provide specialized behavior to an existing {@link Renderer}
 * instance.  The default implementation of all methods is to call
 * through to the wrapped {@link Renderer} instance.</p>
 *
 * <p class="changed_added_2_2">Usage: extend this class and override 
 * {@link #getWrapped} to
 * return the instance being wrapping.</p>
 *
 * @since 2.2
 */

public abstract class RendererWrapper extends Renderer implements FacesWrapper<Renderer> {

    /**
     * @return the instance we are wrapping
     */
    @Override
    public abstract Renderer getWrapped();
    

    @Override
    public String convertClientId(FacesContext context, String clientId) {
        return getWrapped().convertClientId(context, clientId);
    }
    
    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        return getWrapped().getConvertedValue(context, component, submittedValue);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        getWrapped().decode(context, component);
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        getWrapped().encodeBegin(context, component);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        getWrapped().encodeChildren(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        getWrapped().encodeEnd(context, component);
    }

    @Override
    public boolean getRendersChildren() {
        return getWrapped().getRendersChildren();
    }
    
}
