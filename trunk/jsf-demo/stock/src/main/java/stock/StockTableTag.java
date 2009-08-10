/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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

package stock;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

public class StockTableTag extends UIComponentELTag {

    public String getRendererType() {
        return null;
    }

    public String getComponentType() {
        return "stock.StockTable";
    }

    @Override
    protected void setProperties(UIComponent component) {

        super.setProperties(component);
        if (border != null) {
            component.setValueExpression("border", border);
        }
        if (cellspacing != null) {
            component.setValueExpression("cellspacing", cellspacing);
        }
        if (columns != null) {
            component.setValueExpression("columns", columns);
        }
        if (style != null) {
            component.setValueExpression("style", style);
        }
        if (styleClass != null) {
            component.setValueExpression("styleClass", styleClass);
        }
    }

    private ValueExpression border;
    public void setBorder(javax.el.ValueExpression border) {
        this.border = border;
    }

    // PROPERTY: cellspacing
    private ValueExpression cellspacing;
    public void setCellspacing(javax.el.ValueExpression cellspacing) {
        this.cellspacing = cellspacing;
    }

    // PROPERTY: columns
    private ValueExpression columns;
    public void setColumns(javax.el.ValueExpression columns) {
        this.columns = columns;
    }

    // PROPERTY: style
    private ValueExpression style;
    public void setStyle(javax.el.ValueExpression style) {
        this.style = style;
    }

    // PROPERTY: styleClass
    private ValueExpression styleClass;
    public void setStyleClass(javax.el.ValueExpression styleClass) {
        this.styleClass = styleClass;
    }

}
