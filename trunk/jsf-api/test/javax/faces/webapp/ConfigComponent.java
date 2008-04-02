/*
 * $Id: ConfigComponent.java,v 1.3 2004/02/26 20:32:13 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


/**
 * <p>Config Bean for an Component.</p>
 */
public class ConfigComponent extends ConfigFeature {

    private String componentClass;
    public String getComponentClass() {
        return (this.componentClass);
    }
    public void setComponentClass(String componentClass) {
        this.componentClass = componentClass;
    }

    private String componentType;
    public String getComponentType() {
        return (this.componentType);
    }
    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

}
