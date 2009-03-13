/*
 * $Id: UIViewRoot.java,v 1.50.8.18 2008/04/21 20:31:24 edburns Exp $
 */

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

package javax.faces.component;


import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ProjectStage;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.webapp.FacesServlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.event.PostRestoreStateEvent;
import javax.faces.event.PostConstructViewMapEvent;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.webapp.pdl.PageDeclarationLanguage;
import javax.faces.webapp.pdl.ViewMetadata;


/**
 * <p><strong class="changed_modified_2_0">UIViewRoot</strong> is the
 * UIComponent that represents the root of the UIComponent tree.  This
 * component renders markup as the response to Ajax requests.  It also 
 * serves as the root of the component tree, and as a place to hang 
 * per-view {@link PhaseListener}s.</p>
 *
 * <p>For each of the following lifecycle phase methods:</p>

 * <ul>

 * <li><p>{@link #processDecodes} </p></li>

 * <li><p>{@link #processValidators} </p></li>

 * <li><p>{@link #processUpdates} </p></li>

 * <li><p>{@link #processApplication} </p></li>

 * <li><p>RenderResponse, via {@link #encodeBegin} and {@link
 * #encodeEnd} </p></li>

 * </ul>

 * <p>Take the following action regarding
 * <code>PhaseListener</code>s.</p>

 * <ul>

 * <p>Initialize a state flag to <code>false</code>.</p>

 * <p>If {@link #getBeforePhaseListener} returns non-<code>null</code>,
 * invoke the listener, passing in the correct corresponding {@link
 * PhaseId} for this phase.</p>

 * <p>Upon return from the listener, call {@link
 * FacesContext#getResponseComplete} and {@link
 * FacesContext#getRenderResponse}.  If either return <code>true</code>
 * set the internal state flag to <code>true</code>. </p>

 * <p>If or one or more listeners have been added by a call to {@link
 * #addPhaseListener}, invoke the <code>beforePhase</code> method on
 * each one whose {@link PhaseListener#getPhaseId} matches the current
 * phaseId, passing in the same <code>PhaseId</code> as in the previous
 * step.</p>

 * <p>Upon return from each listener, call {@link
 * FacesContext#getResponseComplete} and {@link
 * FacesContext#getRenderResponse}.  If either return <code>true</code>
 * set the internal state flag to <code>true</code>. </p>


 * <p>Execute any processing for this phase if the internal state flag
 * was not set.</p>

 * <p>If {@link #getAfterPhaseListener} returns non-<code>null</code>,
 * invoke the listener, passing in the correct corresponding {@link
 * PhaseId} for this phase.</p>
 * <p/>
 * <p>If or one or more listeners have been added by a call to {@link
 * #addPhaseListener}, invoke the <code>afterPhase</code> method on each
 * one whose {@link PhaseListener#getPhaseId} matches the current
 * phaseId, passing in the same <code>PhaseId</code> as in the previous
 * step.</p>
 * <p/>
 * <p/>
 * </ul>
 */

public class UIViewRoot extends UIComponentBase {

    // ------------------------------------------------------ Manifest Constants

    public static final String METADATA_FACET_NAME = "javax_faces_metadata";
    
    /**
     * <p class="changed_added_2_0">The key in the value set of the
     * <em>view metadata BeanDescriptor</em>, the value of which is a 
     * <code>List&lt;{@link UIViewParameter.Reference}&gt;</code>.</p>
     *
     * @since 2.0
     */
    public static final String VIEW_PARAMETERS_KEY = "javax.faces.component.VIEW_PARAMETERS_KEY";
    
    /** <p>The standard component type for this component.</p> */
    public static final String COMPONENT_TYPE = "javax.faces.ViewRoot";


    /** <p>The standard component family for this component.</p> */
    public static final String COMPONENT_FAMILY = "javax.faces.ViewRoot";


    /**
     * <p>The prefix that will be used for identifiers generated
     * by the <code>createUniqueId()</code> method.
     */
    static public final String UNIQUE_ID_PREFIX = "j_id";

    private static Lifecycle lifecycle;

    private static final Logger LOGGER =
          Logger.getLogger("javax.faces", "javax.faces.LogStrings");

    private static final String LOCATION_IDENTIFIER_PREFIX = "javax_faces_location_";
    private static final Map<String,String> LOCATION_IDENTIFIER_MAP =
          new HashMap<String,String>(6, 1.0f);
    static {
        LOCATION_IDENTIFIER_MAP.put("head", LOCATION_IDENTIFIER_PREFIX + "HEAD");
        LOCATION_IDENTIFIER_MAP.put("form", LOCATION_IDENTIFIER_PREFIX + "FORM");
        LOCATION_IDENTIFIER_MAP.put("body", LOCATION_IDENTIFIER_PREFIX + "BODY");        
    }


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIViewRoot} instance with default property
     * values.</p>
     */
    public UIViewRoot() {

        super();
        setRendererType(null);

    }

    // ------------------------------------------------------ Instance Variables

    private int lastId = 0;

    /**
     * <p>Set and cleared during the lifetime of a lifecycle phase.  Has
     * no meaning between phases.  If <code>true</code>, the lifecycle
     * processing for the current phase must not take place.</p>
     */
    private boolean skipPhase;

    /**
     * <p>Set and cleared during the lifetime of a lifecycle phase.  Has no
     * meaning between phases.  If <code>true</code>, the
     * <code>MethodExpression</code> associated with <code>afterPhase</code>
     * will not be invoked nor will any PhaseListeners associated with this
     * UIViewRoot.
     */
    private boolean beforeMethodException;

    /**
     * <p>Set and cleared during the lifetime of a lifecycle phase.  Has no
     * meaning between phases.
     */
    private ListIterator<PhaseListener> phaseListenerIterator;


    // -------------------------------------------------------------- Properties


    /**
     * <p class="changed_added_2_0">Return <code>trues</code>.</p>
     *
     * @since 2.0
     */
    @Override
    public boolean isInView() {

        return true;

    }

    /**
     * <p class="changed_added_2_0">Overridden to take no action.</p>
     *
     * @since 2.0
     * @param isInView
     */
    @Override
    public void setInView(boolean isInView) {
        // no-op
    }

    /**
     * @see UIComponent#getFamily()
     */
    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    /**
     * <p>The render kit identifier of the {@link javax.faces.render.RenderKit} associated
     * wth this view.</p>
     */
    private String renderKitId = null;


    /**
     * <p>Return the render kit identifier of the {@link
     * javax.faces.render.RenderKit} associated with this view.  Unless
     * explicitly set, as in {@link
     * javax.faces.application.ViewHandler#createView}, the returned
     * value will be <code>null.</code></p>
     */
    public String getRenderKitId() {

        String result;
        if (null != renderKitId) {
            result = this.renderKitId;
        } else {
            ValueExpression vb = getValueExpression("renderKitId");
            FacesContext context = getFacesContext();
            if (vb != null) {
                try {
                    result = (String) vb.getValue(context.getELContext());
                }
                catch (ELException e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "severe.component.unable_to_process_expression",
                                   new Object[]{vb.getExpressionString(),
                                                "renderKitId"});
                    }
                    result = null;
                }
            } else {
                result = null;
            }
        }
        return result;
    }


    /**
     * <p>Set the render kit identifier of the {@link javax.faces.render.RenderKit}
     * associated with this view.  This method may be called at any time
     * between the end of <em>Apply Request Values</em> phase of the
     * request processing lifecycle (i.e. when events are being broadcast)
     * and the beginning of the <em>Render Response</em> phase.</p>
     *
     * @param renderKitId The new {@link javax.faces.render.RenderKit} identifier,
     *                    or <code>null</code> to disassociate this view with any
     *                    specific {@link javax.faces.render.RenderKit} instance
     */
    public void setRenderKitId(String renderKitId) {

        this.renderKitId = renderKitId;

    }


    /** <p>The view identifier of this view.</p> */
    private String viewId = null;


    /** <p>Return the view identifier for this view.</p> */
    public String getViewId() {

        return (this.viewId);

    }


    /**
     * <p>Set the view identifier for this view.</p>
     *
     * @param viewId The new view identifier
     */
    public void setViewId(String viewId) {

        this.viewId = viewId;

    }

    // ------------------------------------------------ Event Management Methods

    private MethodExpression beforePhase = null;
    private MethodExpression afterPhase = null;

    /**
     * <p>Return the {@link MethodExpression} that will be invoked
     * before this view is rendered.</p>
     *
     * @return the {@link MethodExpression} that will be invoked before
     *         this view is rendered.
     * @since 1.2
     */
    public MethodExpression getBeforePhaseListener() {
        return beforePhase;
    }

    /**
     * <p><span class="changed_modified_2_0">Allow</span> an arbitrary
     * method to be called for the "beforePhase" event as the UIViewRoot
     * runs through its lifecycle.  This method will be called for all
     * phases <span class="changed_modified_2_0">including {@link
     * PhaseId#RESTORE_VIEW}</span>.  Unlike a true {@link
     * PhaseListener}, this approach doesn't allow for only receiving
     * {@link PhaseEvent}s for a given phase.</p> <p/> <p>The method
     * must conform to the signature of {@link
     * PhaseListener#beforePhase}.</p>
     *
     * @param newBeforePhase the {@link MethodExpression} that will be
     *                       invoked before this view is rendered.
     * @since 1.2
     */
    public void setBeforePhaseListener(MethodExpression newBeforePhase) {
        beforePhase = newBeforePhase;
    }

    /**
     * <p>Return the {@link MethodExpression} that will be invoked after
     * this view is rendered.</p>
     *
     * @return the {@link MethodExpression} that will be invoked after
     *         this view is rendered.
     *
     * @since 1.2
     */
    public MethodExpression getAfterPhaseListener() {
        return afterPhase;
    }

    /**
     * <p><span class="changed_modified_2_0">Allow</span> an arbitrary
     * method to be called for the "afterPhase" event as the UIViewRoot
     * runs through its lifecycle.  This method will be called for all
     * phases <span class="changed_modified_2_0">including {@link
     * PhaseId#RESTORE_VIEW}</span>.  Unlike a true {@link
     * PhaseListener}, this approach doesn't allow for only receiving
     * {@link PhaseEvent}s for a given phase.</p> <p/> <p>The method
     * must conform to the signature of {@link
     * PhaseListener#afterPhase}.</p>
     *
     * @param newAfterPhase the {@link MethodExpression} that will be
     *                      invoked after this view is rendered.
     *
     * @since 1.2
     */
    public void setAfterPhaseListener(MethodExpression newAfterPhase) {
        afterPhase = newAfterPhase;
    }

    private List<PhaseListener> phaseListeners = null;

    /**
     * <p>If the argument <code>toRemove</code> is in the list of {@link
     * PhaseListener}s for this instance, it must be removed.</p>
     * @param toRemove the {@link PhaseListener} to remove.
     *
     * @since 1.2
     */
    public void removePhaseListener(PhaseListener toRemove) {
        if (null != phaseListeners) {
            phaseListeners.remove(toRemove);
        }
    }

    /**
     * <p>Add the argument <code>newPhaseListener</code> to the list of
     * {@link PhaseListener}s on this <code>UIViewRoot</code>.</p>
     *
     * @param newPhaseListener the {@link PhaseListener} to add
     *
     * @since 1.2
     */
    public void addPhaseListener(PhaseListener newPhaseListener) {
        if (null == phaseListeners) {
            //noinspection CollectionWithoutInitialCapacity
            phaseListeners = new ArrayList<PhaseListener>();
        }
        phaseListeners.add(newPhaseListener);
    }

    
    /**
     * 
     * <p class="changed_added_2_0">Return an unmodifiable list of the 
     * <code>PhaseListener</code> instances attached to this 
     * <code>UIViewRoot</code> instance.</p>
     *
     * @since 2.0
     */
    public List<PhaseListener> getPhaseListeners() {

        List<PhaseListener> result;

        if (null == phaseListeners) {
            result = Collections.unmodifiableList(Collections.<PhaseListener>emptyList());
        } else {
            result = Collections.unmodifiableList(phaseListeners);
        }
        
        return result;

    }

    /**
     * <p class="changed_added_2_0">Add argument <code>component</code>,
     * which is assumed to represent a resource instance, as a resource
     * to this view.  A resource instance is rendered by a resource
     * <code>Renderer</code>, as described in the Standard HTML
     * RenderKit. The default implementation must call through to
     * {@link #addComponentResource(javax.faces.context.FacesContext, 
     * javax.faces.component.UIComponent, java.lang.String)}.</p>
     *
     * <div class="changed_added_2_0">
     * <p>
     * 
     * @param context {@link FacesContext} for the current request
     * @param componentResource The {@link UIComponent} representing a 
     * {@link javax.faces.application.Resource} instance
     *
     * @since 2.0
     */
    public void addComponentResource(FacesContext context, UIComponent componentResource) {
        addComponentResource(context, componentResource, null);
    }

    /**
     * <p class="changed_added_2_0">Add argument <code>component</code>,
     * which is assumed to represent a resource instance, as a resource
     * to this view.  A resource instance is rendered by a resource
     * <code>Renderer</code>, as described in the Standard HTML
     * RenderKit. </p>
     *
     * <div class="changed_added_2_0">

     * <p> The <code>component</code> must be added using the following
     * algorithm:</p>

     * <ul>

     * <li><p>If the <code>target</code> argument is <code>null</code>,
     * look for a <code>target</code> attribute on the
     * <code>component</code>.  If there is no <code>target</code>
     * attribute, set <code>target</code> to be the default value
     * <code>head</code></p></li>

     * <li><p>Call {@link #getComponentResources} to obtain the child
     * list for the given target.</p></li>

     * <li><p>Add the <code>component</code> resource to the
     * list.</p></li>

     * </ul>

     * </div>
     *  
     * @param context {@link FacesContext} for the current request
     * @param componentResource The {@link UIComponent} representing a 
     * {@link javax.faces.application.Resource} instance 
     * @param target The name of the facet for which the {@link UIComponent} will be added
     *
     * @since 2.0
     */
    public void addComponentResource(FacesContext context, UIComponent componentResource, String target) {
        final Map<String,Object> attributes = componentResource.getAttributes();
        // look for a target in the component attribute set if arg is not set.
        if (target == null) {
            target = (String) attributes.get("target");
        }
        if (target == null) {
            target = "head";
        }
        List<UIComponent> facetChildren = getComponentResources(context,
                                                                target,
                                                                true);
        // add the resource to the facet
        facetChildren.add(componentResource);
    }

    /**
     * <p class="changed_added_2_0">Return an unmodifiable
     * <code>List</code> of {@link UIComponent}s for the provided
     * <code>target</code> agrument.  Each <code>component</code> in the
     * <code>List</code> is assumed to represent a resource
     * instance.</p>
     *
     * <div class="changed_added_2_0">
     * <p>The default implementation must use an algorithm equivalent to the
     * the following.</p>
     * <ul>
     * <li>Locate the facet for the <code>component</code> by calling <code>getFacet()</code> using
     * <code>target</code> as the argument.</li>
     * <li>If the facet is not found, create the facet by calling <code>context.getApplication().createComponent()
     * </code> using <code>javax.faces.Panel</code> as the argument</li> 
     * <ul>
     * <li>Set the <code>id</code> of the facet to be <code>target</code></li>
     * <li>Add the facet to the facets <code>Map</code> using <code>target</code> as the key</li>
     * </ul>
     * <li>return the children of the facet</li>
     * </ul>

     * </div>
     *
     * @param target The name of the facet for which the components will be returned. 
     *
     * @return A <code>List</code> of {@link UIComponent} children of
     * the facet with the name <code>target</code>.  If no children are
     * found for the facet, return <code>Collections.emptyList()</code>.
     *
     * @throws NullPointerException if <code>target</code> or
     * <code>context</code> is <code>null</code>
     *
     * @since 2.0
     */
    public List<UIComponent> getComponentResources(FacesContext context, 
                                                   String target) {
        if (target == null) {
            throw new NullPointerException();
        }

        List<UIComponent> resources = getComponentResources(context,
                                                            target,
                                                            false);

        return ((resources != null)
                ? resources
                : Collections.<UIComponent>emptyList());

    }
    
    /**
     * <p class="changed_added_2_0">Remove argument <code>component</code>,
     * which is assumed to represent a resource instance, as a resource
     * to this view.</p>
     *
     * <div class="changed_added_2_0">
     * <p>
     * 
     * @param context {@link FacesContext} for the current request
     * @param componentResource The {@link UIComponent} representing a 
     * {@link javax.faces.application.Resource} instance
     *
     * @since 2.0
     */
    public void removeComponentResource(FacesContext context, UIComponent componentResource) {
        removeComponentResource(context, componentResource, null);
    }
    
    /**
     * <p class="changed_added_2_0">Remove argument <code>component</code>,
     * which is assumed to represent a resource instance, as a resource
     * to this view.  A resource instance is rendered by a resource
     * <code>Renderer</code>, as described in the Standard HTML
     * RenderKit. </p>
     *
     * <div class="changed_added_2_0">
     * <p>
     * The <code>component</code> must be removed using the following algorithm:
     * <ul>
     * <li>If the <code>target</code> argument is <code>null</code>, look for a <code>target</code>
     * attribute on the <code>component</code>.<br>
     * If there is no <code>target</code> attribute, set <code>target</code> to be the default value <code>head</code></li>
     * <li>Call {@link #getComponentResources} to obtain the child list for the
     * given target.</li>
     * <li>Remove the <code>component</code> resource from the child list.</li>
     * </ul>
     * </p>
     * </div>
     *  
     * @param context {@link FacesContext} for the current request
     * @param componentResource The {@link UIComponent} representing a 
     * {@link javax.faces.application.Resource} instance 
     * @param target The name of the facet for which the {@link UIComponent} will be added
     *
     * @since 2.0
     */
    public void removeComponentResource(FacesContext context, UIComponent componentResource, String target) {

        final Map<String,Object> attributes = componentResource.getAttributes();
        // look for a target in the component attribute set if arg is not set.
        if (target == null) {
            target = (String) attributes.get("target");
        }
        if (target == null) {
            target = "head";
        }
        List<UIComponent> facetChildren = getComponentResources(context,
                                                                target,
                                                                false);
        if (facetChildren != null) {
            facetChildren.remove(componentResource);
        }

    }

    /**
     * <p>An array of Lists of events that have been queued for later
     * broadcast, with one List for each lifecycle phase.  The list
     * indices match the ordinals of the PhaseId instances.  This
     * instance is lazily instantiated.  This list is
     * <strong>NOT</strong> part of the state that is saved and restored
     * for this component.</p>
     */
    private List<List<FacesEvent>> events = null;


    /**
     * <p>Override the default {@link UIComponentBase#queueEvent} behavior to
     * accumulate the queued events for later broadcasting.</p>
     *
     * @param event {@link FacesEvent} to be queued
     *
     * @throws IllegalStateException if this component is not a
     *                               descendant of a {@link UIViewRoot}
     * @throws NullPointerException  if <code>event</code>
     *                               is <code>null</code>
     */
    public void queueEvent(FacesEvent event) {

        if (event == null) {
            throw new NullPointerException();
        }
        // We are a UIViewRoot, so no need to check for the ISE
        if (events == null) {
            int len = PhaseId.VALUES.size();
            List<List<FacesEvent>> events = new ArrayList<List<FacesEvent>>(len);
            for (int i = 0; i < len; i++) {
                events.add(new ArrayList<FacesEvent>(5));
            }
            this.events = events;
        }
        events.get(event.getPhaseId().getOrdinal()).add(event);
    }


    /**
     * <p class="changed_added_2_0">Broadcast any events that have been
     * queued.  First broadcast events that have been queued for {@link
     * PhaseId#ANY_PHASE}.  Then broadcast ane events that have been
     * queued for the current phase.  In both cases, {@link
     * UIComponent#pushComponentToEL} must be called before the event is
     * broadcast, and {@link UIComponent#popComponentFromEL} must be
     * called after the return from the broadcast, even in the case of
     * an exception.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param phaseId {@link PhaseId} of the current phase

     * @since 2.0
     */
    public void broadcastEvents(FacesContext context, PhaseId phaseId) {

        if (null == events) {
            // no events have been queued
            return;
        }
        boolean hasMoreAnyPhaseEvents;
        boolean hasMoreCurrentPhaseEvents;

        List<FacesEvent> eventsForPhaseId =
             events.get(PhaseId.ANY_PHASE.getOrdinal());

        // keep iterating till we have no more events to broadcast.
        // This is necessary for events that cause other events to be
        // queued.  PENDING(edburns): here's where we'd put in a check
        // to prevent infinite event queueing.
        do {
            // broadcast the ANY_PHASE events first
            if (null != eventsForPhaseId) {
                // We cannot use an Iterator because we will get
                // ConcurrentModificationException errors, so fake it
                while (!eventsForPhaseId.isEmpty()) {
                    FacesEvent event =
                          eventsForPhaseId.get(0);
                    UIComponent source = event.getComponent();
                    UIComponent compositeParent = null;
                    try {
                        if (!UIComponent.isCompositeComponent(source)) {
                            compositeParent = UIComponent.getCompositeComponentParent(source);
                        }
                        if (compositeParent != null) {
                            pushComponentToEL(context, compositeParent);
                        }
                        pushComponentToEL(context, source);
                        source.broadcast(event);
                    } catch (AbortProcessingException e) {
                        context.getApplication().publishEvent(ExceptionQueuedEvent.class,
                                                              new ExceptionQueuedEventContext(context,
                                                                                        e,
                                                                                        source,
                                                                                        phaseId));
                    }
                    finally {
                        popComponentFromEL(context);
                        if (compositeParent != null) {
                            popComponentFromEL(context);
                        }
                    }
                    eventsForPhaseId.remove(0); // Stay at current position
                }
            }

            // then broadcast the events for this phase.
            if (null != (eventsForPhaseId = events.get(phaseId.getOrdinal()))) {
                // We cannot use an Iterator because we will get
                // ConcurrentModificationException errors, so fake it
                while (!eventsForPhaseId.isEmpty()) {
                    FacesEvent event = eventsForPhaseId.get(0);
                    UIComponent source = event.getComponent();
                    UIComponent compositeParent = null;
                    try {
                        if (!UIComponent.isCompositeComponent(source)) {
                            compositeParent = getCompositeComponentParent(source);
                        }
                        if (compositeParent != null) {
                            pushComponentToEL(context, compositeParent);
                        }
                        pushComponentToEL(context, source);
                        source.broadcast(event);
                    } catch (AbortProcessingException ape) {
                        // A "return" here would abort remaining events too
                        context.getApplication().publishEvent(ExceptionQueuedEvent.class,
                                                              new ExceptionQueuedEventContext(context,
                                                                                        ape,
                                                                                        source,
                                                                                        phaseId));
                    }
                    finally {
                        popComponentFromEL(context);
                        if (compositeParent != null) {
                            popComponentFromEL(context);
                        }
                    }
                    eventsForPhaseId.remove(0); // Stay at current position
                }
            }

            // true if we have any more ANY_PHASE events
            hasMoreAnyPhaseEvents =
                  (null != (eventsForPhaseId =
                        events.get(PhaseId.ANY_PHASE.getOrdinal()))) &&
                        !eventsForPhaseId.isEmpty();
            // true if we have any more events for the argument phaseId
            hasMoreCurrentPhaseEvents =
                  (null != events.get(phaseId.getOrdinal())) &&
                  !events.get(phaseId.getOrdinal()).isEmpty();

        } while (hasMoreAnyPhaseEvents || hasMoreCurrentPhaseEvents);
    
    }

    // ------------------------------------------------ Lifecycle Phase Handlers


    private void initState() {
        skipPhase = false;
        beforeMethodException = false;
        phaseListenerIterator =
              ((phaseListeners != null) ? phaseListeners.listIterator() : null);
    }

    // avoid creating the PhaseEvent if possible by doing redundant
    // null checks.
    private void notifyBefore(FacesContext context, PhaseId phaseId) {
        if (null != beforePhase || null != phaseListenerIterator) {
            notifyPhaseListeners(context, phaseId, true);
        }
    }
    
    // avoid creating the PhaseEvent if possible by doing redundant
    // null checks.
    private void notifyAfter(FacesContext context, PhaseId phaseId) {
        if (null != afterPhase || null != phaseListenerIterator) {
            notifyPhaseListeners(context, phaseId, false);
        }
    }


    /**
     * <p class="changed_added_2_0">The default implementation must call
     * {@link UIComponentBase#processRestoreState} from within a
     * <code>try</code> block.  The <code>try</code> block must have a
     * <code>finally</code> block that ensures that no {@link
     * FacesEvent}s remain in the event queue, that any
     * <code>PhaseListener</code>s in {@link #getPhaseListeners} are
     * invoked as appropriate, and that the <code>this.{@link
     * UIComponent#visitTree} is called, passing a {@link
     * ContextCallback} that takes the following action: call the {@link
     * UIComponent#processEvent} method of the current component. The
     * argument <code>event</code> must be an instance of {@link
     * javax.faces.event.PostRestoreStateEvent} whose
     * <code>component</code> property is the current component in the
     * traversal.</code></p>
     * @param context the <code>FacesContext</code> for this requets
     * @param state the opaque state object obtained from the {@link
     * javax.faces.application.StateManager}
     */
    @Override
    public void processRestoreState(FacesContext context, Object state) {

        initState();
        try {
            super.processRestoreState(context, state);
        } finally {
            clearFacesEvents(context);
            notifyAfter(context, PhaseId.RESTORE_VIEW);
            final PostRestoreStateEvent event = new PostRestoreStateEvent(this);
            try {
                this.visitTree(VisitContext.createVisitContext(context), 
                        new VisitCallback() {

                    public VisitResult visit(VisitContext context, UIComponent target) {
                        event.setComponent(target);
                        target.processEvent(event);
                        return VisitResult.ACCEPT;
                    }
                });
            } catch (AbortProcessingException e) {
                context.getApplication().publishEvent(ExceptionQueuedEvent.class,
                                                      new ExceptionQueuedEventContext(context,
                                                                                e,
                                                                                null,
                                                                                PhaseId.RESTORE_VIEW));
            }
        }

    }
    

    /**
     * <div class="changed_added_2_0">
     * <p>Perform partial processing by calling
     * {@link javax.faces.context.PartialViewContext#processPartial} with
     * {@link PhaseId#APPLY_REQUEST_VALUES} if:
     * <ul>
     * <li>{@link javax.faces.context.PartialViewContext#isPartialRequest}
     * returns <code>true</code> and we don't have a request to process all 
     * components in the view
     * ({@link javax.faces.context.PartialViewContext#isExecuteAll} returns
     * <code>false</code>)</li>
     * </ul>
     * Perform full processing by calling 
     * {@link UIComponentBase#processDecodes} if one of the following 
     * conditions are met:
     * <ul>
     * <li> {@link javax.faces.context.PartialViewContext#isPartialRequest}
     * returns <code>true</code> and we have a request to process all 
     * components in the view
     * ({@link javax.faces.context.PartialViewContext#isExecuteAll} returns
     * <code>true</code>)</li>
     * <li>{@link javax.faces.context.PartialViewContext#isPartialRequest}
     * returns <code>false</code></li>
     * </ul>
     * </p>
     * </div>
     * <p class="changed_modified_2_0">Override the default 
     * {@link UIComponentBase#processDecodes} behavior to broadcast any queued 
     * events after the default processing or partial processing has been 
     * completed and to clear out any events for later phases if the event 
     * processing for this phase caused {@link FacesContext#renderResponse} 
     * or {@link FacesContext#responseComplete} to be called.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @throws NullPointerException if <code>context</code>
     *                              is <code>null</code>
     */
    @Override
    public void processDecodes(FacesContext context) {
        initState();
        notifyBefore(context, PhaseId.APPLY_REQUEST_VALUES);

        try {
            if (!skipPhase) {
                if (context.getPartialViewContext().isPartialRequest() &&
                    !context.getPartialViewContext().isExecuteAll()) {
                    context.getPartialViewContext().processPartial(PhaseId.APPLY_REQUEST_VALUES);
                } else {
                    super.processDecodes(context);
                }
                broadcastEvents(context, PhaseId.APPLY_REQUEST_VALUES);
            }
        } finally {
            clearFacesEvents(context);
            notifyAfter(context, PhaseId.APPLY_REQUEST_VALUES);
        }
    }

    /**
     * <p><span class="changed_added_2_0">Override</span> the default
     * {@link UIComponentBase#encodeBegin} behavior.  If
     * {@link #getBeforePhaseListener} returns non-<code>null</code>,
     * invoke it, passing a {@link PhaseEvent} for the {@link
     * PhaseId#RENDER_RESPONSE} phase.  If the internal list populated
     * by calls to {@link #addPhaseListener} is non-empty, any listeners
     * in that list must have their {@link PhaseListener#beforePhase}
     * method called, passing the <code>PhaseEvent</code>.  Any errors
     * that occur during invocation of any of the the beforePhase
     * listeners must be logged and swallowed.  After listeners are invoked
     * call superclass processing.</p>
     */
    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        initState();
        notifyBefore(context, PhaseId.RENDER_RESPONSE);

        if (!skipPhase) {
            super.encodeBegin(context);
        }
    }

    /** 
     * <p class="changed_added_2_0">If {@link
     * javax.faces.context.PartialViewContext#isAjaxRequest} returns 
     * <code>true</code>, perform partial rendering by calling
     * {@link javax.faces.context.PartialViewContext#processPartial} with 
     * {@link PhaseId#RENDER_RESPONSE}.  If {@link
     * javax.faces.context.PartialViewContext#isAjaxRequest} returns
     * <code>false</code>, delegate to the parent {@link
     * javax.faces.component.UIComponentBase#encodeChildren} method.</p>
     *
     * @since 2.0
     */
    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        if (context.getPartialViewContext().isAjaxRequest()) {
            context.getPartialViewContext().processPartial(PhaseId.RENDER_RESPONSE);
        } else {
            super.encodeChildren(context);
        }
    }

    /**
     * <p class="changed_added_2_0"> If {@link #getAfterPhaseListener}
     * returns non-<code>null</code>, invoke it, passing a {@link
     * PhaseEvent} for the {@link PhaseId#RENDER_RESPONSE} phase.  Any
     * errors that occur during invocation of the afterPhase listener
     * must be logged and swallowed.  If the current view has view
     * parameters, as indicated by a non-empty and
     * non-<code>UnsupportedOperationException</code> throwing return
     * from {@link javax.faces.webapp.pdl.PageDeclarationLanguage#getViewMetadata(javax.faces.context.FacesContext, String)},
     * call {@link UIViewParameter#encodeAll} on each parameter.  If
     * calling <code>getViewParameters()</code> causes
     * <code>UnsupportedOperationException</code> to be thrown, the
     * exception must be silently swallowed.</p>
     */
    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        super.encodeEnd(context);
        encodeViewParameters(context);
        notifyAfter(context, PhaseId.RENDER_RESPONSE);               
    }

    /**
     * <p class="changed_added_2_0">Call {@link UIComponentBase#getRendersChildren}
     * If {@link javax.faces.context.PartialViewContext#isAjaxRequest}
     * returns <code>true</code> this method must return <code>true</code>.</p>
     *
     * @since 2.0
     */
    @Override
    public boolean getRendersChildren() {
        boolean value = super.getRendersChildren();
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getPartialViewContext().isAjaxRequest()) {
            value = true;
        }
        return value;
    }

    /**
     * <p>Utility method that notifies phaseListeners for the given
     * phaseId.  Assumes that either or both the MethodExpression or
     * phaseListeners data structure are non-null.</p>
     *
     * @param context   the context for this request
     * @param phaseId   the {@link PhaseId} of the current phase
     * @param isBefore, if true, notify beforePhase listeners.  Notify
     *                  afterPhase listeners otherwise.
     */
    private void notifyPhaseListeners(FacesContext context,
                                      PhaseId phaseId,
                                      boolean isBefore) {
        PhaseEvent event = createPhaseEvent(context, phaseId);

        boolean hasPhaseMethodExpression =
              (isBefore && (null != beforePhase)) ||
              (!isBefore && (null != afterPhase) && !beforeMethodException);
        MethodExpression expression = isBefore ? beforePhase : afterPhase;

        if (hasPhaseMethodExpression) {
            try {
                expression.invoke(context.getELContext(), new Object[]{event});
                skipPhase = context.getResponseComplete() ||
                            context.getRenderResponse();
            }
            catch (Exception e) {
                if (isBefore) {
                    beforeMethodException = true;
                }
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               "severe.component.unable_to_process_expression",
                               new Object[] { expression.getExpressionString(),
                                              (isBefore ? "beforePhase" : "afterPhase")});
                }
                return;
            }
        }
        if (phaseListenerIterator != null && !beforeMethodException) {
            while ((isBefore)
                   ? phaseListenerIterator.hasNext()
                   : phaseListenerIterator.hasPrevious()) {
                PhaseListener curListener = ((isBefore)
                                             ? phaseListenerIterator.next()
                                             : phaseListenerIterator
                                                   .previous());
                if (phaseId == curListener.getPhaseId() ||
                    PhaseId.ANY_PHASE == curListener.getPhaseId()) {
                    try {
                        if (isBefore) {
                            curListener.beforePhase(event);
                        } else {
                            curListener.afterPhase(event);
                        }
                        skipPhase = context.getResponseComplete() ||
                                    context.getRenderResponse();
                    }
                    catch (Exception e) {
                        if (isBefore && phaseListenerIterator.hasPrevious()) {
                            phaseListenerIterator.previous();
                        }
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE,
                                       "severe.component.uiviewroot_error_invoking_phaselistener",
                                       curListener.getClass().getName());
                        }
                        return;
                    }
                }
            }
        }
    }

    private static PhaseEvent createPhaseEvent(FacesContext context,
                                               PhaseId phaseId)
    throws FacesException {

        if (lifecycle == null) {
            LifecycleFactory lifecycleFactory = (LifecycleFactory)
                  FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            String lifecycleId =
                  context.getExternalContext()
                        .getInitParameter(FacesServlet.LIFECYCLE_ID_ATTR);
            if (lifecycleId == null) {
                lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
            }
            lifecycle = lifecycleFactory.getLifecycle(lifecycleId);
        }

        return (new PhaseEvent(context, phaseId, lifecycle));

    }


    /**
     * <div class="changed_added_2_0">
     * <p>Perform partial processing by calling
     * {@link javax.faces.context.PartialViewContext#processPartial} with
     * {@link PhaseId#PROCESS_VALIDATIONS} if:
     * <ul>
     * <li>{@link javax.faces.context.PartialViewContext#isPartialRequest}
     * returns <code>true</code> and we don't have a request to process all
     * components in the view
     * ({@link javax.faces.context.PartialViewContext#isExecuteAll} returns
     * <code>false</code>)</li>
     * </ul>
     * Perform full processing by calling
     * {@link UIComponentBase#processValidators} if one of the following
     * conditions are met:
     * <ul>
     * <li> {@link javax.faces.context.PartialViewContext#isPartialRequest}
     * returns <code>true</code> and we have a request to process all
     * components in the view
     * ({@link javax.faces.context.PartialViewContext#isExecuteAll} returns
     * <code>true</code>)</li>
     * <li>{@link javax.faces.context.PartialViewContext#isPartialRequest}
     * returns <code>false</code></li>
     * </ul>
     * </p>
     * </div>
     * <p class="changed_modified_2_0">Override the default 
     * {@link UIComponentBase#processValidators} behavior to broadcast any 
     * queued events after the default processing or partial processing has been 
     * completed and to clear out any events for later phases if the event 
     * processing for this phase caused {@link FacesContext#renderResponse} or 
     * {@link FacesContext#responseComplete} to be called.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @throws NullPointerException if <code>context</code>
     *                              is <code>null</code>
     */
    @Override
    public void processValidators(FacesContext context) {
        initState();
        notifyBefore(context, PhaseId.PROCESS_VALIDATIONS);

        try {
            if (!skipPhase) {
                if (context.getPartialViewContext().isPartialRequest() &&
                    !context.getPartialViewContext().isExecuteAll()) {
                    context.getPartialViewContext().processPartial(PhaseId.PROCESS_VALIDATIONS);
                } else {
                    super.processValidators(context);
                }
                broadcastEvents(context, PhaseId.PROCESS_VALIDATIONS);
            }
        } finally {
            clearFacesEvents(context);
            notifyAfter(context, PhaseId.PROCESS_VALIDATIONS);
        }
    }

    /**
     * <div class="changed_added_2_0">
     * <p>Perform partial processing by calling
     * {@link javax.faces.context.PartialViewContext#processPartial} with
     * {@link PhaseId#UPDATE_MODEL_VALUES} if:
     * <ul>
     * <li>{@link javax.faces.context.PartialViewContext#isPartialRequest}
     * returns <code>true</code> and we don't have a request to process all
     * components in the view
     * ({@link javax.faces.context.PartialViewContext#isExecuteAll} returns
     * <code>false</code>)</li>
     * </ul>
     * Perform full processing by calling
     * {@link UIComponentBase#processUpdates} if one of the following
     * conditions are met:
     * <ul>
     * <li> {@link javax.faces.context.PartialViewContext#isPartialRequest}
     * returns <code>true</code> and we have a request to process all
     * components in the view
     * ({@link javax.faces.context.PartialViewContext#isExecuteAll} returns
     * <code>true</code>)</li>
     * <li>{@link javax.faces.context.PartialViewContext#isPartialRequest}
     * returns <code>false</code></li>
     * </ul>
     * </p>
     *</div>
     * <p class="changed_modified_2_0">Override the default {@link UIComponentBase}
     * behavior to broadcast any queued events after the default processing or 
     * partial processing has been completed and to clear out any events for 
     * later phases if the event processing for this phase caused 
     * {@link FacesContext#renderResponse} or
     * {@link FacesContext#responseComplete} to be called.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @throws NullPointerException if <code>context</code>
     *                              is <code>null</code>
     */
    @Override
    public void processUpdates(FacesContext context) {
        initState();
        notifyBefore(context, PhaseId.UPDATE_MODEL_VALUES);

        try {
            if (!skipPhase) {
                if (context.getPartialViewContext().isPartialRequest() &&
                    !context.getPartialViewContext().isExecuteAll()) {
                    context.getPartialViewContext().processPartial(PhaseId.UPDATE_MODEL_VALUES);
                } else {
                    super.processUpdates(context);
                }
                broadcastEvents(context, PhaseId.UPDATE_MODEL_VALUES);
            }
        } finally {
            clearFacesEvents(context);
            notifyAfter(context, PhaseId.UPDATE_MODEL_VALUES);
        }
    }

    /**
     * <p>Broadcast any events that have been queued for the <em>Invoke
     * Application</em> phase of the request processing lifecycle
     * and to clear out any events for later phases if the event processing
     * for this phase caused {@link FacesContext#renderResponse} or
     * {@link FacesContext#responseComplete} to be called.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @throws NullPointerException if <code>context</code>
     *                              is <code>null</code>
     */
    public void processApplication(FacesContext context) {
        initState();
        notifyBefore(context, PhaseId.INVOKE_APPLICATION);
        try {
            if (!skipPhase) {
                // NOTE - no tree walk is performed; this is a UIViewRoot-only operation
                broadcastEvents(context, PhaseId.INVOKE_APPLICATION);
            }
        } finally {
            clearFacesEvents(context);
            notifyAfter(context, PhaseId.INVOKE_APPLICATION);
        }
    }


    // clear out the events if we're skipping to render-response
    // or if there is a response complete signal.
    private void clearFacesEvents(FacesContext context) {
        if (context.getRenderResponse() || context.getResponseComplete()) {
            if (events != null) {
                for (List<FacesEvent> eventList : events) {
                    if (eventList != null) {
                        eventList.clear();
                    }
                }
                events = null;
            }
        }
    }

    /**
     * <p>Generate an identifier for a component.  The identifier will
     * be prefixed with UNIQUE_ID_PREFIX, and will be unique within
     * this UIViewRoot.</p>
     */
    public String createUniqueId() {
        return UNIQUE_ID_PREFIX + lastId++;
    }

    /*
    * <p>The locale for this view.</p>
    */
    private Locale locale = null;

    /**
     * <p>Return the <code>Locale</code> to be used in localizing the
     * response being created for this view.</p>
     * <p/>
     * <p>Algorithm:</p>
     * <p/>
     * <p>If we have a <code>locale</code> ivar, return it.  If we have
     * a value expression for "locale", get its value.  If the value is
     * <code>null</code>, return the result of calling {@link
     * javax.faces.application.ViewHandler#calculateLocale}.  If the
     * value is an instance of <code>java.util.Locale</code> return it.
     * If the value is a String, convert it to a
     * <code>java.util.Locale</code> and return it.  If there is no
     * value expression for "locale", return the result of calling {@link
     * javax.faces.application.ViewHandler#calculateLocale}.</p>
     *
     * @return The current <code>Locale</code> obtained by executing the
     *         above algorithm.
     */
    public Locale getLocale() {
        Locale result = null;
        if (null != locale) {
            result = this.locale;
        } else {
            ValueExpression vb = getValueExpression("locale");
            FacesContext context = getFacesContext();
            if (vb != null) {
                Object resultLocale = null;

                try {
                    resultLocale = vb.getValue(context.getELContext());
                }
                catch (ELException e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "severe.component.unable_to_process_expression",
                                   new Object[]{vb.getExpressionString(), "locale"});
                    }
                }

                if (null == resultLocale) {
                    result =
                          context.getApplication().getViewHandler()
                                .calculateLocale(context);
                } else if (resultLocale instanceof Locale) {
                    result = (Locale) resultLocale;
                } else if (resultLocale instanceof String) {
                    result = getLocaleFromString((String) resultLocale);
                }
            } else {
                result =
                      context.getApplication().getViewHandler()
                            .calculateLocale(context);
            }
        }
        return result;
    }


    // W3C XML specification refers to IETF RFC 1766 for language code
    // structure, therefore the value for the xml:lang attribute should
    // be in the form of language or language-country or
    // language-country-variant.

    private static Locale getLocaleFromString(String localeStr)
        throws IllegalArgumentException {
        // length must be at least 2.
        if (null == localeStr || localeStr.length() < 2) {
            throw new IllegalArgumentException("Illegal locale String: " +
                                               localeStr);
        }

        Locale result = null;
        String lang = null;
        String country = null;
        String variant = null;
        char[] seps = {
            '-',
            '_'
        };
        int inputLength = localeStr.length();
        int i = 0;
        int j = 0;

        // to have a language, the length must be >= 2
        if ((inputLength >= 2) &&
            ((i = indexOfSet(localeStr, seps, 0)) == -1)) {
            // we have only Language, no country or variant
            if (2 != localeStr.length()) {
                throw new
                    IllegalArgumentException("Illegal locale String: " +
                                             localeStr);
            }
            lang = localeStr.toLowerCase();
        }

        // we have a separator, it must be either '-' or '_'
        if (i != -1) {
            lang = localeStr.substring(0, i);
            // look for the country sep.
            // to have a country, the length must be >= 5
            if ((inputLength >= 5) &&
                (-1 == (j = indexOfSet(localeStr, seps, i + 1)))) {
                // no further separators, length must be 5
                if (inputLength != 5) {
                    throw new
                        IllegalArgumentException("Illegal locale String: " +
                                                 localeStr);
                }
                country = localeStr.substring(i + 1);
            }
            if (j != -1) {
                country = localeStr.substring(i + 1, j);
                // if we have enough separators for language, locale,
                // and variant, the length must be >= 8.
                if (inputLength >= 8) {
                    variant = localeStr.substring(j + 1);
                } else {
                    throw new
                        IllegalArgumentException("Illegal locale String: " +
                                                 localeStr);
                }
            }
        }
        if (variant != null && country != null && lang != null) {
            result = new Locale(lang, country, variant);
        } else if (lang != null && country != null) {
            result = new Locale(lang, country);
        } else if (lang != null) {
            result = new Locale(lang, "");
        }
        return result;
    }


    /**
     * @param str local string
     * @param set the substring
     * @param fromIndex starting index
     * @return starting at <code>fromIndex</code>, the index of the
     *         first occurrence of any substring from <code>set</code> in
     *         <code>toSearch</code>, or -1 if no such match is found
     */
    private static int indexOfSet(String str, char[] set, int fromIndex) {
        int result = -1;
        for (int i = fromIndex, len = str.length(); i < len; i++) {
            for (int j = 0, innerLen = set.length; j < innerLen; j++) {
                if (str.charAt(i) == set[j]) {
                    result = i;
                    break;
                }
            }
            if (-1 != result) {
                break;
            }
        }
        return result;
    }

    /**
     * <p>Set the <code>Locale</code> to be used in localizing the
     * response being created for this view. </p>
     *
     * @param locale The new localization Locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
        // Make sure to appraise the EL of this switch in Locale.
        FacesContext.getCurrentInstance().getELContext().setLocale(locale);
    }
    
    private Map<String, Object> viewScope = null;

    /**
     * <p class="changed_added_2_0">This implementation simply calls through to {@link
     * #getViewMap(boolean)}, passing <code>true</code> as the argument, and
     * returns the result.</p>
     * <div class="changed_added_2_0">
     *
     * @since 2.0
     */
    public Map<String, Object> getViewMap() {

        return getViewMap(true);
        
    }

    /**
     * <p class="changed_added_2_0">Returns a <code>Map</code> that acts as the
     * interface to the data store that is the "view scope", or, if this
     * instance does not have such a <code>Map</code> and the
     * <code>create</code> argument is <code>true</code>, creates one and
     * returns it.  This map must be instantiated lazily and cached for return
     * from subsequent calls to this method on this <code>UIViewRoot</code>
     * instance. {@link javax.faces.application.Application#publishEvent} must
     * be called, passing {@link PostConstructViewMapEvent}<code>.class</code> as the
     * first argument and this <code>UIViewRoot</code> instance as the second
     * argument.</p>
     *
     * <p>The returned <code>Map</code> must be implemented such that calling
     * <code>clear()</code> on the <code>Map</code> causes {@link javax.faces.application.Application#publishEvent} to be
     * called, passing {@link PreDestroyViewMapEvent}<code>.class</code>
     * as the first argument and this <code>UIViewRoot</code> instance
     * as the second argument.</p>
     * 
     * <p>See {@link FacesContext#setViewRoot} for the specification of when the
     * <code>clear()</code> method must be called.</p>
     * <p/>
     * </div>
     *
     * @param create <code>true</code> to create a new <code>Map</code> for this
     *               instance if necessary; <code>false</code> to return
     *               <code>null</code> if there's no current <code>Map</code>.
     *
     * @since 2.0
     */
    public Map<String, Object> getViewMap(boolean create) {

        if (create && viewScope == null) {
            viewScope = new ViewMap(getFacesContext().getApplication().getProjectStage());
            getFacesContext().getApplication()
                  .publishEvent(PostConstructViewMapEvent.class, this);
        }
        return viewScope;
        
    }
    
    private void encodeViewParameters(FacesContext context) {
        PageDeclarationLanguage pdl = null;
        
        try {
            pdl = context.getApplication().getViewHandler().
                    getPageDeclarationLanguage(context, getViewId());
        } catch (UnsupportedOperationException uoe) {
            
        }
        
        if (null == pdl) {
            return;
        }
        ViewMetadata metadata = pdl.getViewMetadata(context, getViewId());
        if (metadata != null) { // perhaps it's not supported
            Collection<UIViewParameter> params =
                  metadata.getViewParameters(this);
            if (params.isEmpty()) {
                return;
            }

            try {
                for (UIViewParameter param : params) {
                    param.encodeAll(context);
                }
            } catch (IOException e) {
                // IOException is forced by contract and is not expected to be thrown in this case
                throw new RuntimeException("Unexpected IOException", e);
            }
        }
    }

    // END TENATIVE

    // ----------------------------------------------------- StateHolder Methods


    private Object[] values;

    @Override
    public Object saveState(FacesContext context) {

        if (values == null) {
            values = new Object[9];
        }

        values[0] = super.saveState(context);
        values[1] = renderKitId;
        values[2] = viewId;
        values[3] = locale;
        values[4] = lastId;
        values[5] = saveAttachedState(context, beforePhase);
        values[6] = saveAttachedState(context, afterPhase);
        values[7] = saveAttachedState(context, phaseListeners);
        values[8] = saveAttachedState(context, viewScope);
        return (values);

    }

    @Override
    public void restoreState(FacesContext context, Object state) {

        values = (Object[]) state;
        super.restoreState(context, values[0]);
        renderKitId = (String) values[1];
        viewId = (String) values[2];
        locale = (Locale) values[3];
        lastId = ((Integer) values[4]).intValue();
        beforePhase =
              (MethodExpression) restoreAttachedState(context, values[5]);
        afterPhase =
              (MethodExpression) restoreAttachedState(context, values[6]);
        phaseListeners = TypedCollections.dynamicallyCastList((List)
              restoreAttachedState(context, values[7]), PhaseListener.class);
        //noinspection unchecked
        viewScope =
              (Map<String, Object>) restoreAttachedState(context, values[8]);
    }


    // --------------------------------------------------------- Private Methods


    private static String getIdentifier(String target) {

        // check map
        String id = LOCATION_IDENTIFIER_MAP.get(target);
        if (id == null) {
            id = LOCATION_IDENTIFIER_PREFIX + target;
            LOCATION_IDENTIFIER_MAP.put(target, id);
        }
        return id;

    }


    private List<UIComponent> getComponentResources(FacesContext context,
                                                   String target,
                                                   boolean create) {

        String location = getIdentifier(target);
        UIComponent facet = getFacet(location);
        if (facet == null && create) {
            facet = context.getApplication().createComponent("javax.faces.Panel");
            facet.setId(location);
            getFacets().put(location, facet);
        }

        return ((facet != null) ? facet.getChildren() : null);

    }


    // ----------------------------------------------------------- Inner Classes

    private static final class ViewMap extends HashMap<String,Object> {

        private static final long serialVersionUID = -1l;

        private ProjectStage stage;

        
        // -------------------------------------------------------- Constructors


        ViewMap(ProjectStage stage) {

            this.stage = stage;

        }


        // ---------------------------------------------------- Methods from Map


        @Override
        public void clear() {

            FacesContext context = FacesContext.getCurrentInstance();
            context.getApplication()
                  .publishEvent(PreDestroyViewMapEvent.class, context.getViewRoot());
            super.clear();

        }


        @Override
        public Object put(String key, Object value) {

            if (value != null && ProjectStage.Development.equals(stage) && !(value instanceof Serializable)) {
                LOGGER.log(Level.WARNING,
                           "warning.component.uiviewroot_non_serializable_attribute_viewmap",
                           new Object[] { key, value.getClass().getName() });
            }
            return super.put(key, value);

        }


        @Override
        public void putAll(Map<? extends String,?> m) {

            for (Map.Entry<? extends String,?> entry : m.entrySet()) {
                String k = entry.getKey();
                Object v = entry.getValue();
                this.put(k, v);
            }

        }


    } // END ViewMap


}
