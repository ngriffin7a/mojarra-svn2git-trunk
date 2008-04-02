/*
 * $Id: ServletBean.java,v 1.1 2003/10/07 19:53:10 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;


/**
 * <p>This bean contains the <code>servlet-name</code> and <code>
 * servlet-class</code> information for exactly one occurance of the
 * <code>servlet</code> element in a web application deployment 
 * descriptor.</p>
 */
public class ServletBean {
    
    /**
     * <p>The <code>web-app/servlet/servlet-name</code> element.</p>
     */ 
    private String servletName;

    public String getServletName() {
        return servletName;
    }


    public void setServletName(String servletName) {       
        this.servletName = servletName;
    }

    
    /**
     * <p>The <code>web-app/servlet/servlet-class</code> element.</p>
     */ 
    private String servletClass;
    
    public String getServletClass() {
        return servletClass;
    }


    public void setServletClass(String servletClass) {        
        this.servletClass = servletClass;
    }


} // end of class ServletBean
