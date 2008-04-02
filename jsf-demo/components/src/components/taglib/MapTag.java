/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package components.taglib;


import components.components.MapComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;



/**
 * <p>{@link UIComponentTag} for an image map.</p>
 */
 
public class MapTag extends UIComponentTag {


    private String current = null;
    public void setCurrent(String current) {
        this.current = current;
    }

    private String actionListenerRef = null;
    public void setActionListenerRef(String actionListenerRef) {
        this.actionListenerRef = actionListenerRef;
    }

    private String action = null;
    public void setAction(String action) {
        this.action = action;
    }

    private String immediate = null;
    public void setImmediate(String immediate) {
        this.immediate = immediate;
    }

    private String actionRef = null;
    public void setActionRef(String actionRef) {
        this.actionRef = actionRef;
    }

    private String styleClass = null;
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }


    public String getComponentType() {
        return ("DemoMap");
    }


    public String getRendererType() {
        return ("DemoMap");
    }


    public void release() {
        super.release();
        current = null;
        styleClass = null;
        actionListenerRef = null;
        action = null;
    }


    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        MapComponent map = (MapComponent) component;
	//        if (current != null) {
	//            map.setCurrent(current);
	//        }
        if (styleClass != null) {
            map.getAttributes().put("styleClass", styleClass);
        }

        map.setActionListenerRef(actionListenerRef);
        map.setActionRef(actionRef);
	if (null != action) {
	    if (isValueReference(action)) {
                ValueBinding vb = FacesContext.getCurrentInstance().getApplication().
		    getValueBinding(action);
                map.setValueBinding("action", vb);
		
	    }
	    else {
		map.setAction(action);
	    }
	}

        if (immediate != null) {
            if (isValueReference(immediate)) {
                ValueBinding vb = FacesContext.getCurrentInstance().getApplication().
		    getValueBinding(immediate);
                map.setValueBinding("immediate", vb);
            } else {
                boolean _immediate = new Boolean(immediate).booleanValue();
                map.setImmediate(_immediate);
            }
        }
	
    }

    
}
