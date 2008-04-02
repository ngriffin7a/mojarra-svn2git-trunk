/*
 * $Id: VariableResolver.java,v 1.6 2005/05/05 20:51:10 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package javax.faces.el;


import javax.faces.context.FacesContext;


/**
 * <p><strong>VariableResolver</strong> represents a pluggable mechanism
 * for resolving a top-level variable reference at evaluation time.</p>
 *
 * @deprecated This has been replaced by {@link javax.el.ELResolver}
 * when operating with a <code>null</code> <code>base</code> argument.
 */

public abstract class VariableResolver {


    /**
     * <p>Resolve the specified variable name, and return the corresponding
     * object, if any; otherwise, return <code>null</code>.</p>
     *
     * @param context {@link FacesContext} against which to resolve
     *  this variable name
     * @param name Name of the variable to be resolved
     *
     * @exception EvaluationException if an exception is thrown while resolving
     *  the variable name (the thrown exception must be included as the
     *  <code>cause</code> property of this exception)
     * @exception NullPointerException if <code>context</code>
     *  or <code>name</code> is <code>null</code>
     */
    public abstract Object resolveVariable(FacesContext context, String name)
        throws EvaluationException;


}
