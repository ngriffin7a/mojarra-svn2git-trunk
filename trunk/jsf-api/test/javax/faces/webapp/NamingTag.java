/*
 * $Id: NamingTag.java,v 1.4 2004/02/04 23:39:35 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.faces.component.UIComponent;


// Test UINamingContainer Tag
public class NamingTag extends UIComponentTag {

    public String getComponentType() {
        return ("TestNamingContainer");
    }

    public String getRendererType() {
        return (null);
    }

}
