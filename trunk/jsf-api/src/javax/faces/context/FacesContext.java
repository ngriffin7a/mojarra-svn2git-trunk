/*
 * $Id: FacesContext.java,v 1.6 2002/05/14 15:02:28 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;


import javax.faces.FacesException;     // FIXME - subpackage?
import javax.faces.lifecycle.Lifecycle;
import javax.faces.render.RenderKit;
import javax.faces.tree.Tree;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;


/**
 * <p><strong>FacesContext</strong> contains all of the per-request state
 * information related to the processing of a single JavaServer Faces request,
 * and the rendering of the corresponding response.  It is passed to, and
 * potentially modified by, each phase of the request processing lifecycle.</p>
 *
 * <h3>Lifecycle</h3>
 *
 * <p>A <code>FacesContext</code> instance is associated with a particular
 * request at the beginning of request processing, by a call to the
 * <code>createFacesContext()</code> method of the {@link FacesContextFactory}
 * instance associated with the current web application.  The instance
 * remains active until its <code>release()</code> method is called, after
 * which no further references to this instance are allowed.  While a
 * <code>FacesContext</code> instance is active, it MUST NOT be referenced
 * from any thread other than the one upon which the servlet container
 * executing this web application utilizes for the processing of this request.
 * </p>
 *
 * <p><strong>FIXME</strong> - ObjectManager et. al.</p>
 *
 * <p><strong>FIXME</strong> - Do we need direct access to the
 * output stream or writer associated with our response so it can be cached?
 * </p>
 */

public abstract class FacesContext {


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the <code>HttpSession</code> instance for the session
     * associated with the current request (if any); otherwise, return
     * <code>null</code>.</p>
     */
    public abstract HttpSession getHttpSession();


    /**
     * <p>Return the {@link Lifecycle} instance that is managing the
     * processing of the request represented by this <code>FacesContext</code>
     * instance.</p>
     */
    public abstract Lifecycle getLifecycle();


    /**
     * <p>Return the {@link RenderKit} instance that is used during the
     * <em>Render Response</em> phase of the request processing lifecycle.</p>
     */
    public abstract RenderKit getRenderKit();


    /**
     * <p>Set the {@link RenderKit} instance that is used during the
     * <em>Render Response</em> phase of the request processing lifecycle.</p>
     *
     * @param renderKit The new RenderKit instance
     *
     * @exception NullPointerException if <code>renderKit</code>
     *  is <code>null</code>
     */
    public abstract void setRenderKit(RenderKit renderKit);


    /**
     * <p>Return the {@link Tree} that is associated with the inbound request.
     * </p>
     */
    public abstract Tree getRequestTree();


    /**
     * <p>Set the {@link Tree} that is associated with the inbound request.
     * </p>
     *
     * <p><strong>FIXME</strong> - Does this method need to be public?</p>
     *
     * @param tree The new inbound request tree
     */
    public abstract void setRequestTree(Tree tree);


    /**
     * <p>Return the {@link Tree} that is associated with the outbound
     * response.  Unless otherwise specified (by a call to
     * <code>setResponseTree()</code>, this will return the same
     * {@link Tree} returned by <code>getRequestTree()</code>.</p>
     */
    public abstract Tree getResponseTree();


    /**
     * <p>Set the {@link Tree} that is the associated with the
     * outbound response.</p>
     *
     * @param tree The new outbound response tree
     */
    public abstract void setResponseTree(Tree tree);


    /**
     * <p>Return the <code>ServletContext</code> object for the web application
     * associated with this request.</p>
     */
    public abstract ServletContext getServletContext();


    /**
     * <p>Return the <code>ServletRequest</code> object representing the
     * current request that is being processed.</p>
     */
    public abstract ServletRequest getServletRequest();


    /**
     * <p>Return the <code>ServletResponse</code> object representing the
     * current response that is being rendered.</p>
     */
    public abstract ServletResponse getServletResponse();


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Evaluate the specified model reference expression, and return the
     * expected type of the corresponding value, if it can be determined;
     * otherwise, return <code>null</code>.</p>
     *
     * @param model Model reference expression to be evaluated
     *
     * @exception FacesException if an error occurs during expression
     *  evaluation
     * @exception IllegalArgumentException if the model reference
     *  expression is invalid
     * @exception NullPointerException if <code>model</code>
     *  is <code>null</code>
     */
    public abstract Class getModelType(String model) throws FacesException;


    /**
     * <p>Evaluate the specified model reference expression, and return the
     * corresponding data value (which may be null).  No data type conversion
     * is performed.</p>
     *
     * @param model Model reference to be evaluated
     *
     * @exception FacesException if an error occurs during expression
     *  evaluation
     * @exception IllegalArgumentException if the model reference
     *  expression is invalid
     * @exception NullPointerException if <code>model</code>
     *  is <code>null</code>
     */
    public abstract Object getModelValue(String model) throws FacesException;


    /**
     * <p>Evaluate the specified model reference expression, and set the
     * corresponding data value (which may be null).  No data type conversion
     * is performed.</p>
     *
     * @exception FacesException if an error occurs during expression
     *  evaluation
     * @exception IllegalArgumentException if the model reference
     *  expression is invalid
     * @exception NullPointerException if <code>model</code>
     *  is <code>null</code>
     */
    public abstract Object setModelValue(String model, Object value)
        throws FacesException;


    /**
     * <p>Release any resources associated with this <code>FacesContext</code>
     * instance.  Faces implementations may choose to pool instances in the
     * associated {@link FacesContextFactory} to avoid repeated object creation
     * and garbage collection.</p>
     */
    public abstract void release();


}
