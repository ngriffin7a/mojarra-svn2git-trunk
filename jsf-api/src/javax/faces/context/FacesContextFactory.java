/*
 * $Id: FacesContextFactory.java,v 1.9 2002/07/26 19:02:36 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;


/**
 * <p><strong>FacesContextFactory</strong> is a Factory object that creates
 * and returns new {@link FacesContext} instances, initialized for the
 * processing of the specified request and response objects.
 * Implementations may take advantage of the calls to the
 * <code>release()</code> method of the allocated {@link FacesContext}
 * instances to pool and recycle them, rather than creating a new instance
 * every time.</p>
 *
 * <p>There shall be one <code>FacesContextFactory</code> instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 * <pre>
 *   FacesContextFactory factory = (FacesContextFactory)
 *    FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
 * </pre>
 */

public abstract class FacesContextFactory {


    /**
     * <p>Construct and return a {@link FacesContext} that is initialized
     * for the processing of the specified request and response objects,
     * utilizing the specified {@link Lifecycle} implementation for this
     * web application.</p>
     *
     * @param context The <code>ServletContext</code> that is associated
     *  with this web application
     * @param request The <code>ServletRequest</code> that is to be
     *  processed
     * @param response The <code>ServletResponse</code> that is to be
     *  created
     * @param lifecycle The {@link Lifecycle} instance being used
     *  to process this request
     *
     * @exception FacesException if a {@link FacesContext} cannot be
     *  constructed for the specified parameters
     * @exception NullPointerException if any of the parameters
     *  are <code>null</code>
     */
    public abstract FacesContext getFacesContext
        (ServletContext context, ServletRequest request,
         ServletResponse response, Lifecycle lifecycle)
        throws FacesException;


}
