/*
 * $Id: ManagedBeanFactoryWrapper.java,v 1.4 2006/03/29 23:03:49 rlubke Exp $
 */

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

package com.sun.faces.spi;

import com.sun.faces.config.beans.ManagedBeanBean;
import java.util.Map;
import java.lang.reflect.Method;

import javax.faces.context.FacesContext;

/**
 *
 * <p>This is intended to be the main access point to the pluggable
 * {@link ManagedBeanFactory} mechanism.  Subclasses must provide a
 * public constructor that takes a single
 * <code>ManagedBeanFactory</code> argument and stores it as an ivar,
 * returning it from the {@link #getWrapped} method.</p>
 * 
 * @author edburns, rlubke
 */
public abstract class ManagedBeanFactoryWrapper extends ManagedBeanFactory {
    
    public abstract ManagedBeanFactory getWrapped();

    /**
     * @see ManagedBeanFactory#getScope
     */
    
    public Scope getScope() {
        return getWrapped().getScope();
    }
    
    /**
     * @see ManagedBeanFactory#newInstance
     */
    
    public Object newInstance(FacesContext context) {
        return getWrapped().newInstance(context);
    }
    
    
    /**
     * @see ManagedBeanFactory#setManagedBeanBean
     */
    
    public void setManagedBeanBean(ManagedBeanBean bean) {
        getWrapped().setManagedBeanBean(bean);
    }
    
    /**
     * @see ManagedBeanFactory#getManagedBeanBean
     */
    
    public ManagedBeanBean getManagedBeanBean() {
        return getWrapped().getManagedBeanBean();
    }
    
    /**
     * @see ManagedBeanFactory#setManagedBeanFactoryMap(java.util.Map<java.lang.String,com.sun.faces.spi.ManagedBeanFactory>) 
     */
    
    public void setManagedBeanFactoryMap(Map<String, ManagedBeanFactory> others) {
        getWrapped().setManagedBeanFactoryMap(others);
    }
    
    /**
     * @see ManagedBeanFactory#getManagedBeanFactoryMap() 
     */
    
    public Map<String, ManagedBeanFactory> getManagedBeanFactoryMap() {
        return getWrapped().getManagedBeanFactoryMap();
    }

    /**
     * @see com.sun.faces.spi.ManagedBeanFactory#getPostConstructMethods()      
     */
    public Method[] getPostConstructMethods() {
        return getWrapped().getPostConstructMethods();
    }

    /**
     * @see com.sun.faces.spi.ManagedBeanFactory#getPreDestroyMethods()       
     */
    public Method[] getPreDestroyMethods() {
        return getWrapped().getPreDestroyMethods();
    }


}
