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

/*
 * CatchExceptionServlet.java
 *
 * Created on March 19, 2007, 2:14 PM
 */

package com.sun.faces.systest.model;

import java.io.IOException;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author edburns
 * @version
 */
public class CatchExceptionServlet implements Servlet {
    
    private FacesServlet wrapped = null;
    
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        try {
            wrapped.service(servletRequest, servletResponse);
            
        }
        catch (ServletException e) {
            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            httpRequest.setAttribute("exceptionClass", e.getClass().getName());
            httpRequest.setAttribute("rootCause", e.getCause().getClass().getName());
            httpRequest.setAttribute("exceptionMessage", e.getMessage());
            throw e;
        }
    }

    public void init(ServletConfig servletConfig) throws ServletException {
        if (null == wrapped) {
            wrapped = new FacesServlet();
        }
        wrapped.init(servletConfig);
    }

    public void destroy() {
        wrapped.destroy();
    }

    public String getServletInfo() {
        return wrapped.getServletInfo();
    }

    public ServletConfig getServletConfig() {
        return wrapped.getServletConfig();
    }
    

}
