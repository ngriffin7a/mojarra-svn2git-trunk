/*
 * $Id: ViewHandler.java,v 1.8 2003/08/25 15:06:27 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;


/**
 * <p><strong>ViewHandler</strong> is the pluggablity mechanism for
 * allowing implementations of or applications using the JavaServer
 * Faces specification to provide their own handling of the activities
 * in the <em>Render Response</em> and <em>Restore View</em>
 * phases of the request processing lifecycle.  This allows for
 * implementations to support different response generation
 * technologies, and state saving/restoring approaches.  JSF
 * implementations, or JSF-based applications, can register an instance
 * of this interface by calling the <code>setViewHandler()</code> method
 * of the {@link Application} prior to the application receiving its
 * first request from a client.  </p>
 *
 * <p>The <code>ViewHandler</code> implementation must provide a
 * reference to an implementation of a {@link StateManager}, which is
 * used for saving and restoring the state of a Faces UI between
 * requests.</p>
 *
 * <p>A default implementation of <code>ViewHandler</code> must be
 * provided by the JSF implementation, which will be utilized unless
 * <code>setViewHandler()</code> is called to establish a different one.
 * During <em>Render Response</em>, this default instance will treat the
 * <code>viewId</code> property of the response view as a
 * context-relative path (after prefixing it with a slash), and will
 * perform a {@link javax.faces.context.ExternalContext#dispatchMessage}
 * call to that path.</p>
 *
 * <p>Please see {@link StateManager} for information on how the
 * <code>ViewHandler</code> uses the {@link StateManager}. </p>
 */

public interface ViewHandler {


    /**
     * <p>Perform whatever actions are required to render the response
     * view to the <code>ServletResponse</code> associated
     * with the specified {@link FacesContext}.  Also perform required
     * actions to save the state of the response between requests, using
     * the {@link StateManager}.  This method is responsible for
     * ensuring that the {@link FacesContext} has been provided with
     * valid {@link javax.faces.context.ResponseWriter} and {@link
     * javax.faces.context.ResponseStream} instances for the current
     * request.</p>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     * @exception FacesException if a servlet error occurs
     */
    public void renderView(FacesContext context)
        throws IOException, FacesException;

    /**
     * <p>Perform whatever actions are required to restore the view
     * associated with the specified {@link FacesContext} and viewId.
     * This method may call through to {@link StateManager#getView}.</p>
     *
     * <p>This method must be called from the <em>Restore View</em>
     * phase of the request processing lifecycle.</p>
     *
     * <p>This method must cause the new <code>UIViewRoot</code> to be
     * stored in the <code>FacesContext</code> as the new root.</p>
     *
     * <p>If this is an initial request - usually marked by a lack of
     * available state for this view - <code>restoreView()</code> must
     * call <code>FacesContext.renderResponse()</code> to cause the
     * intervening phases between <em>Restore View</em> and <em>Render
     * Response</em> to be skipped.<p>
     *
     * <p>The implementation must ensure that the argument {@link
     * FacesContext} has been populated with an appropriate {@link
     * UIViewRoot} for the argument <code>viewId</code>.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param viewId the view identifier for the current request
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     * @exception FacesException if a servlet error occurs
     */
    public UIViewRoot restoreView(FacesContext context, String viewId);

    /**
     * <p>Create an and return new {@link UIViewRoot} instance
     * initialized with information from the argument
     * <code>FacesContext</code> and <code>viewId</code>.</p>
     *
     * <p>This method must be called from {@link
     * NavigationHandler#handleNavigation}.</p>
     *
     * <p>PENDING(edburns): do we formalize that the renderkitId should
     * be set into the ViewRoot here?</p>
     *
     */

    public UIViewRoot createView(FacesContext context, String viewId);

    /**
     * @return the {@link StateManager} instance for this
     * <code>ViewHandler</code>.
     */
    public StateManager getStateManager();



}
