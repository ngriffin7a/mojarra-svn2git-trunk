/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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


package javax.faces.webapp.pdl;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0">The abstract base interface for a
 * handler representing an <em>attached object</em> in a PDL page.
 * Subinterfaces are provided for the common attached objects that
 * expose {@link javax.faces.convert.Converter}s, {@link
 * javax.faces.validator.Validator}s, {@link
 * javax.faces.event.ValueChangeListener}s, and {@link
 * javax.faces.event.ActionListener}s for use by <em>page
 * authors</em>.</p>
 *
 * @since 2.0
 */
public interface AttachedObjectHandler {

    /**
     * <p class="changed_added_2_0">Take the argument
     * <code>parent</code> and apply this attached object to it.  The
     * action taken varies with class that implements one of the
     * subinterfaces of this interface.</p>
     * @param context The <code>FacesContext</code> for this request
     * @param parent The <code>UIComponent</code> to which this
     * particular attached object must be applied.
     */
    public void applyAttachedObject(FacesContext context, UIComponent parent);


    /**
     * <p class="changed_added_2_0">Return the value of the "for"
     * attribute specified by the <em>page author</em> on the tag for
     * this <code>AttachedObjectHandler</code>.</p>
     */
    public String getFor();

}
