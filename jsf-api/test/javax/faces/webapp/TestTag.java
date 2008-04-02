/*
 * $Id: TestTag.java,v 1.7 2004/02/04 23:39:35 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;


// Test UIComponent Tag
public class TestTag extends UIComponentTag {

    public TestTag() {
        super();
    }


    public TestTag(String componentId) {
        this(componentId, componentId);
    }

    public TestTag(String componentId, String label) {
        super();
        setId(componentId);
        setLabel(label);
    }

    private String label = null;

    public void setLabel(String label) {
        this.label = label;
    }

    private boolean rendersChildren = false;
    private boolean rendersChildrenSet = false;

    public void setRendersChildren(boolean rendersChildren) {
        this.rendersChildren = rendersChildren;
        this.rendersChildrenSet = true;
    }

    public void release() {
        super.release();
        this.label = null;
        this.rendersChildrenSet = false;
    }

    public String getComponentType() {
        return ("TestComponent");
    }

    public String getRendererType() {
        return (null);
    }


    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        if (rendersChildrenSet) {
            ((TestComponent) component).setRendersChildren(rendersChildren);
        }
        if (label != null) {
            if (isValueReference(label)) {
                ValueBinding vb =
                    getFacesContext().getApplication().
		    createValueBinding(label);
                component.setValueBinding("label", vb);
            } else {
                ((TestComponent) component).setLabel(label);
            }
        }
    }


}
