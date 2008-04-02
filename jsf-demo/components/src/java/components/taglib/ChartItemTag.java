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
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package components.taglib;

import components.components.ChartItemComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * <p><strong>ChartItemTag</strong> is the tag handler that processes the 
 * <code>chartItem</code> custom tag.</p>
 */

public class ChartItemTag extends UIComponentTag {

    public ChartItemTag() {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

    /**
     * <p>The label for this item</p>
     */
    private String itemLabel = null;
    /**
     *<p>Set the label for this item.
     */
    public void setItemLabel(String label) {
        this.itemLabel = label;
    }

    /**
     * <p>The color for this item.</p>
     */
    private String itemColor = null;
    /**
     *<p>Set the color for this item.
     */
    public void setItemColor(String color) {
        this.itemColor = color;
    }
    
    /**
     * <p>The value for this item.</p>
     */
    private String itemValue = null;
    /**
     *<p>Set the ualue for this item.
     */
    public void setItemValue(String itemVal) {
        this.itemValue = itemVal;
    }

    private String value = null;
    public void setValue(String value) {
        this.value = value;
    }

    //
    // General Methods
    //

    /**
     * <p>Return the type of the component.
     */
    public String getComponentType() {
        return "ChartItem";
    }

    /**
     * <p>Return the renderer type (if any)
     */
    public String getRendererType() {
        return null;
    }

    /**
     * <p>Release any resources used by this tag handler
     */
    public void release() {
        super.release();
        itemLabel = null;
        itemValue = null;
        itemColor = null;
    }

    //
    // Methods from BaseComponentTag
    //

    /**
     * <p>Set the component properties
     */
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        ChartItemComponent chartItem = (ChartItemComponent) component;
        if (null != value) {
            if (isValueReference(value)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                    .getApplication().createValueBinding(value);
                chartItem.setValueBinding("value", vb);
            } else {
                chartItem.setValue(value);
            }
        }

        if (null != itemLabel) {
            if (isValueReference(itemLabel)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                    .getApplication().createValueBinding(itemLabel);
                chartItem.setValueBinding("itemLabel", vb);
            } else {
                chartItem.setItemLabel(itemLabel);
            }
        }
        
        if (null != itemColor) {
            if (isValueReference(itemColor)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                    .getApplication().createValueBinding(itemColor);
                chartItem.setValueBinding("itemColor", vb);
            } else {
                chartItem.setItemColor(itemColor);
            }
        }
        
        if (null != itemValue) {
            if (isValueReference(itemValue)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                    .getApplication().createValueBinding(itemValue);
                chartItem.setValueBinding("itemValue", vb);
            } else {
                chartItem.setItemValue(itemValue);
            }
        }
    }

}
