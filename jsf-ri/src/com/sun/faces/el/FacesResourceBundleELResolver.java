/*
 * $Id: FacesResourceBundleELResolver.java,v 1.2 2005/08/22 22:10:12 ofung Exp $
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

package com.sun.faces.el;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.config.beans.DescriptionBean;
import com.sun.faces.config.beans.DisplayNameBean;
import com.sun.faces.config.beans.ResourceBundleBean;
import com.sun.faces.util.Util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

/**
 * @author edburns
 */
public class FacesResourceBundleELResolver extends ELResolver {
    
    /** Creates a new instance of FacesResourceBundleELResolver */
    public FacesResourceBundleELResolver() {
    }
    
    public Object getValue(ELContext context, Object base, Object property) {
        if (null != base) {
            return null;
        }
        if (null == base && null == property) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            throw new PropertyNotFoundException(message);
        }
        ResourceBundle result = null;
        FacesContext facesContext = (FacesContext) 
           context.getContext(FacesContext.class);
        Application app = facesContext.getApplication();
        
        result = app.getResourceBundle(facesContext, property.toString());
        if (null != result) {
            context.setPropertyResolved(true);
        }
        
        return result;
    }
    
    

    public Class getType(ELContext context, Object base, Object property) 
        throws ELException {
        
        if (null != base) {
            return null;
        }

        if (null == base && null == property) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            throw new PropertyNotFoundException(message);
        }
        
        ResourceBundle result = null;
        FacesContext facesContext = (FacesContext) 
           context.getContext(FacesContext.class);
        Application app = facesContext.getApplication();
        
        result = app.getResourceBundle(facesContext, property.toString());
        if (null != result) {
            context.setPropertyResolved(true);
            return ResourceBundle.class;
        }        
        
        return null;

    }

    public void  setValue(ELContext context, Object base, Object property,
        Object val) throws ELException {
        String message = null;

        if (base == null && property == null) {
            message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
        
        ResourceBundle result = null;
        FacesContext facesContext = (FacesContext) 
           context.getContext(FacesContext.class);
        Application app = facesContext.getApplication();
        
        result = app.getResourceBundle(facesContext, property.toString());
        if (null != result) {
            context.setPropertyResolved(true);
            message = Util.getExceptionMessageString
                (Util.OBJECT_IS_READONLY);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotWritableException(message);
        }        
        

    }

    public boolean isReadOnly(ELContext context, Object base, Object property) 
        throws ELException {
        if (base != null) {
            return false;
        }
        if (property == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
        ResourceBundle result = null;
        FacesContext facesContext = (FacesContext) 
           context.getContext(FacesContext.class);
        Application app = facesContext.getApplication();

        result = app.getResourceBundle(facesContext, property.toString());
        if (null != result) {
            context.setPropertyResolved(true);
            return true;
        }        

        return false;
    }

    public Iterator getFeatureDescriptors(ELContext context, Object base) {
        
        if (base != null) {
            return null;
        }

        ArrayList list = new ArrayList();
       
        FacesContext facesContext = 
            (FacesContext) context.getContext(FacesContext.class);
        ApplicationAssociate associate = 
            ApplicationAssociate.getInstance(facesContext.getExternalContext());
        Map rbMap = associate.getResourceBundleBeanMap();
        if (rbMap == null) {
            return list.iterator();
        }
        // iterate over the list of managed beans
        for (Iterator i = rbMap.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            String var = (String) entry.getKey();
            ResourceBundleBean resourceBundleBean = (ResourceBundleBean)
                entry.getValue();
            if ( resourceBundleBean != null) {
                Locale curLocale = Util.getLocaleFromContextOrSystem(facesContext);
                String locale = curLocale.toString();
                DescriptionBean descBean = 
                    resourceBundleBean.getDescription(locale);
                DisplayNameBean displayNameBean =
                        resourceBundleBean.getDisplayName(locale);
                String desc = "",
                        displayName = "";
                descBean = (null != descBean) ? descBean :
                    resourceBundleBean.getDescription("");
                if (null != descBean) {
                    // handle the case where the lang or xml:lang attributes
                    // are not specified on the description
                    desc = descBean.getDescription();
                }
                displayNameBean = (null != displayNameBean) ? displayNameBean :
                    resourceBundleBean.getDisplayName("");
                if (null != displayNameBean) {
                    displayName = displayNameBean.getDisplayName();
                }
                list.add(Util.getFeatureDescriptor(var, 
                    displayName, desc, false, false, true,
                    ResourceBundle.class, Boolean.TRUE));
            }
        }
        return list.iterator();
    }
    
    public Class getCommonPropertyType(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        return String.class;
    }

}
