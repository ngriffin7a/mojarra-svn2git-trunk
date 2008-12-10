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

package javax.faces.component.visit;

import javax.faces.component.UIComponent;

/**
 *
 * <p class="changed_added_2_0">A simple callback interface that enables 
 * taking action on a specific UIComponent (either facet or child) during 
 * a component tree visit.</p>
 * 
 * RELEASE_PENDING
 *
 * @see UIComponent#visitTree UIComponent.visitTree()
 *
 * @since 2.0
 */
public interface VisitCallback {
    
    /**
     * <p>This method is called during component tree visits by 
     * {@link VisitContext#invokeVisitCallback VisitContext.invokeVisitCallback()} 
     * to visit the specified component.  At the point in time when this 
     * method is called, the argument {@code target} is guaranteed
     * to be in the proper state with respect to its ancestors in the
     * View.</p>
     *
     * @param context the {@link VisitContext} for this tree visit.
     *
     * @param target the {@link UIComponent} to visit
     *
     * @return a {@link VisitResult} that indicates whether to continue
     *   visiting the component's subtree, skip visiting the component's
     *   subtree or end the visit.
     */
    public VisitResult visit(VisitContext context, UIComponent target);
}
