/*
 * $Id: ELContextImpl.java,v 1.6 2006/03/29 22:38:32 rlubke Exp $
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

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

/**
 * Concrete implementation of {@link javax.el.ELContext}.
 * ELContext's constructor is protected to control creation of ELContext
 * objects through their appropriate factory methods.  This version of
 * ELContext forces construction through FacesContextImpl.
 */
public class ELContextImpl extends ELContext {


    private ELResolver resolver;

    private FunctionMapper functionMapper;
    private VariableMapper variableMapper;

    // ------------------------------------------------------------ Constructors


    /** Constructs a new ELContext associated with the given ELResolver. */
    public ELContextImpl(ELResolver resolver) {

        this.resolver = resolver;

    }

    // ---------------------------------------------------------- Public Methods


    public FunctionMapper getFunctionMapper() {

        return functionMapper;

    }


    public void setFunctionMapper(FunctionMapper fnMapper) {

        functionMapper = fnMapper;

    }


    public VariableMapper getVariableMapper() {

        return variableMapper;

    }


    public void setVariableMapper(VariableMapper varMapper) {

        variableMapper = varMapper;

    }


    public ELResolver getELResolver() {

        return resolver;

    }

}
