/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
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

package javax.faces.event;

import javax.faces.component.UIViewRoot;

/**
 *
 * <p class="changed_added_2_0">When an instance of this event is passed
 * to {@link SystemEventListener#processEvent} or {@link
 * ComponentSystemEventListener#processEvent}, the listener
 * implementation may assume that the <code>source</code> of this event
 * instance is the {@link UIViewRoot} instance that is about to be
 * rendered.</p>

 * <div class="changed_added_2_0">
 *
 * <p>It is valid for a listener for this event to change the {@link
 * UIViewRoot} in the current {@link javax.faces.context.FacesContext},
 * but the listener must ensure that the new <code>UIViewRoot</code> was
 * created with a call to {@link
 * javax.faces.application.ViewHandler#createView}, and that the view is
 * fully populated with the children to be traversed during render.  The
 * listener implementation may call {@link
 * javax.faces.view.ViewDeclarationLanguage#buildView} to populate
 * the <code>UIViewRoot</code>.</p>
 *
 * </div>
 *
 * @since 2.0
 */
public class PreRenderViewEvent extends ComponentSystemEvent {


    // ------------------------------------------------------------ Constructors


    private static final long serialVersionUID = -781238104491250220L;

    /**

     * <p class="changed_added_2_0">Instantiate a new
     * <code>PreRenderViewEvent</code> that indicates the argument
     * <code>root</code> is about to be rendered.</p>

     * @param root the <code>UIViewRoot</code> that is about to be
     * rendered.

     * @throws IllegalArgumentException if the argument is <code>null</code>.
     */
    public PreRenderViewEvent(UIViewRoot root) {
        super(root);
    }

}
