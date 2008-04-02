/*
 * $Id: VariableResolverImpl.java,v 1.16 2004/02/06 18:54:29 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;


import com.sun.faces.application.ApplicationImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;


/**
 * <p>Concrete implementation of <code>VariableResolver</code>.</p>
 */

public class VariableResolverImpl extends VariableResolver {

    private static final Log log = LogFactory.getLog(
        VariableResolverImpl.class);

    //
    // Relationship Instance Variables
    // 

    // Specified by javax.faces.el.VariableResolver.resolveVariable()
    public Object resolveVariable(FacesContext context, String name)
        throws EvaluationException {

        ExternalContext ec = context.getExternalContext();

        if ("applicationScope".equals(name)) {
            return (ec.getApplicationMap());
        } else if ("cookie".equals(name)) {
            return (ec.getRequestCookieMap());
        } else if ("facesContext".equals(name)) {
            return (context);
        } else if ("header".equals(name)) {
            return (ec.getRequestHeaderMap());
        } else if ("headerValues".equals(name)) {
            return (ec.getRequestHeaderValuesMap());
        } else if ("initParam".equals(name)) {
            return (ec.getInitParameterMap());
        } else if ("param".equals(name)) {
            return (ec.getRequestParameterMap());
        } else if ("paramValues".equals(name)) {
            return (ec.getRequestParameterValuesMap());
        } else if ("requestScope".equals(name)) {
            return (ec.getRequestMap());
        } else if ("sessionScope".equals(name)) {
            return (ec.getSessionMap());
        } else if ("view".equals(name)) {
            return (context.getViewRoot());
        } else {
            // do the scoped lookup thing
            Object value = null;

            if (null == (value = ec.getRequestMap().get(name))) {
                if (null == (value = ec.getSessionMap().get(name))) {
                    if (null == (value = ec.getApplicationMap().get(name))) {
// if it's a managed bean try and create it
                        ApplicationFactory aFactory = (ApplicationFactory) FactoryFinder.getFactory(
                            FactoryFinder.APPLICATION_FACTORY);
                        Application application = aFactory.getApplication();
                        if (application instanceof ApplicationImpl) {
                            value =
                                ((ApplicationImpl) application).createAndMaybeStoreManagedBeans(
                                    context, name);
                        }
                    }
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("resolveVariable: Resolved variable:" + value);
            }
            return (value);
        }

    }
}
