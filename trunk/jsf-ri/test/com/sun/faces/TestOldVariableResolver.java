/*
 * $Id: TestOldVariableResolver.java,v 1.6 2005/08/09 17:38:28 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestOldVariableResolver.java

package com.sun.faces;

import javax.faces.el.VariableResolver;
import javax.faces.el.EvaluationException;
import javax.faces.context.FacesContext;

public class TestOldVariableResolver extends VariableResolver {
   
    VariableResolver resolver = null;
    public TestOldVariableResolver(VariableResolver variableResolver) {
       this.resolver = variableResolver;
    }
    
    //
    // Relationship Instance Variables
    // 

    // Specified by javax.faces.el.VariableResolver.resolveVariable()
    public Object resolveVariable(FacesContext context, String name)
            throws EvaluationException {
        if (name.equals("customVRTest2")) {
            return "TestOldVariableResolver";
        }
        return resolver.resolveVariable(context, name);
    }

}
