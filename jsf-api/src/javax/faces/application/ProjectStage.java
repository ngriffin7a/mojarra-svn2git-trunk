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


package javax.faces.application;

/**

 * <p class="changed_added_2_0">This class enables a feature similar to
 * the <code><a target="_"
 * href="http://wiki.rubyonrails.org/rails/pages/Environments">RAILS_ENV</a></code>
 * feature of the Ruby on Rails web framework.  The constants in this
 * class represent the current state of the running application in a
 * typical product development lifecycle.  The value of this state may
 * be queried at any time after application startup by calling {@link
 * Application#getProjectStage}.</p>

 * @since 2.0
 */
public enum ProjectStage {

    /**
     * <p class="changed_added_2_0">
     * This value indicates the currently running application is right
     * now, at this moment, being developed.  This value will usually be
     * set during iterative development.</p>
     */
    Development,
    /**
     * <p class="changed_added_2_0">
     * This value indicates the currently running application is
     * undergoing unit testing.
     * </p>
     */
    UnitTest,
    /**
     * <p class="changed_added_2_0">
     * This value indicates the currently running application is
     * undergoing system testing.
     * </p>
     */
    SystemTest,
    /**
     * <p class="changed_added_2_0">
     * This value indicates the currently running application is
     * deployed in production.
     * </p>
     */
    Production,
    /**
     * <p class="changed_added_2_0">
     * This value enables a minimal level of extensibility.  See
     * {@link #toString} for details.
     * </p>
     */
    Extension;

    /**
     * <p class="changed_added_2_0">
     * The value of this constant is the value of the
     * <code>param-name</code> for setting the current value to be
     * returned by {@link Application#getProjectStage}.
     * </p>
     */
    public static final String PROJECT_STAGE_PARAM_NAME =
          "javax.faces.PROJECT_STAGE";

    private String extensionName = "Extension";

    /**
     * <p class="changed_added_2_0">Specialized to account for an
     * arbitrary product lifecycle phase value. If <code>this == *
     * Extension</code> return the value of the
     * <code>extensionName</code> * instance variable.  Otherwise,
     * return * <code>super.toString()</code>.</p>
     */
    @Override
    public String toString() {
        if (this == Extension) {
            return this.extensionName;
        }
        return super.toString();
    }

    /**
     * <p class="changed_added_2_0">Take the argument
     * <code>extensionName</code>, and set it as the value of the
     * <code>extensionName</code> instance variable of the
     * <code>Extension</code> enum constant.  This enables customizing
     * the value returned from {@link #toString}.  This method is only
     * called by the runtime, during application startup, in the case
     * where an arbitrary lifecycle phase is being assigned.</p>
     */
    public static ProjectStage getExtension(String extensionName) {
        Extension.extensionName = extensionName;
        return Extension;
    }
}
