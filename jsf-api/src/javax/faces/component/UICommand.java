/*
 * $Id: UICommand.java,v 1.37 2003/07/20 01:05:17 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;


/**
 * <p><strong>UICommand</strong> is a {@link UIComponent} that represents
 * a user interface component which, when activated by the user, triggers
 * an application specific "command" or "action".  Such a component is
 * typically rendered as a push button, a menu item, or a hyperlink.</p>
 *
 * <p>When the <code>decode()</code> method of this {@link UICommand}, or
 * its corresponding {@link javax.faces.render.Renderer}, detects that
 * this control has been activated, it will queue an {@link ActionEvent}.
 * Later on, the <code>broadcast()</code> method will ensure that this
 * event is broadcast to all interested listeners.</p>
 *
 * <p>By default, the <code>rendererType</code> property is set to
 * "<code>Button</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UICommand extends UIOutput {


    // ------------------------------------------------------- Static Variables

    // ----------------------------------------------------------- Constructors


    /**
     * <p>Create a new {@link UICommand} instance with default property
     * values.</p>
     */
    public UICommand() {

        super();
        setRendererType("Button");

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>The literal outcome value.</p>
     */
    private String action = null;


    /**
     * <p>Return the literal action outcome value to be returned to the
     * {@link javax.faces.event.ActionListener} processing application level
     * events for this application.</p>
     */
    public String getAction() {

        return (this.action);

    }


    /**
     * <p>Set the literal action outcome value for this component.</p>
     *
     * @param action The new outcome value
     */
    public void setAction(String action) {

        this.action = action;

    }


    /**
     * <p>The action reference.</p>
     */
    private String actionRef = null;


    /**
     * <p>Return the <em>action reference expression</em> pointing at the
     * {@link javax.faces.application.Action} to be invoked, if this component
     * is activated by the user, during <em>Invoke Application</em> phase
     * of the request processing lifecycle.</p>
     */
    public String getActionRef() {

        return (this.actionRef);

    }


    /**
     * <p>Set the <em>action reference expression</em> pointing at the
     * {@link javax.faces.application.Action} to be invoked, if this component
     * is activated by the user, during <em>Invoke Application</em> phase
     * of the request processing lifecycle.</p>
     *
     * @param actionRef The new action reference
     */
    public void setActionRef(String actionRef) {

        this.actionRef = actionRef;

    }


    // ---------------------------------------------------- UIComponent Methods


    /**
     * <p>Override the default behavior and perform no model update.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception IllegalArgumentException if the <code>modelReference</code>
     *  property has invalid syntax for an expression
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void updateModel(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

    }


    // ----------------------------------------------- Event Processing Methods


    /**
     * <p>Array of {@link List}s of {@link ActionListener}s registered
     * for particular phases.  The array, as well as the individual
     * elements, are lazily instantiated as necessary.</p>
     */
    protected List listeners[] = null;


    /**
     * <p>Add a new {@link ActionListener} to the set of listeners interested
     * in being notified when {@link ActionEvent}s occur.</p>
     *
     * @param listener The {@link ActionListener} to be added
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void addActionListener(ActionListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            listeners = new List[PhaseId.VALUES.size()];
        }
        int ordinal = listener.getPhaseId().getOrdinal();
        if (listeners[ordinal] == null) {
            listeners[ordinal] = new ArrayList();
        }
        listeners[ordinal].add(listener);

    }


    /**
     * <p>Broadcast the specified {@link FacesEvent} to all registered
     * event listeners who have expressed an interest in events of this
     * type, for the specified {@link PhaseId}.  The order in which
     * registered listeners are notified is implementation dependent.</p>
     *
     * <p>After all interested listeners have been notified, return
     * <code>false</code> if this event does not have any listeners
     * interested in this event in future phases of the request processing
     * lifecycle.  Otherwise, return <code>true</code>.</p>
     *
     * @param event The {@link FacesEvent} to be broadcast
     * @param phaseId The {@link PhaseId} of the current phase of the
     *  request processing lifecycle
     *
     * @exception AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     * @exception IllegalArgumentException if the implementation class
     *  of this {@link FacesEvent} is not supported by this component
     * @exception IllegalStateException if PhaseId.ANY_PHASE is passed
     *  for the phase identifier
     * @exception NullPointerException if <code>event</code> or
     *  <code>phaseId</code> is <code>null</code>
     */
    public boolean broadcast(FacesEvent event, PhaseId phaseId)
        throws AbortProcessingException {

        if ((event == null) || (phaseId == null)) {
            throw new NullPointerException();
        }
        if (phaseId.equals(PhaseId.ANY_PHASE)) {
            throw new IllegalStateException();
        }
        if (event instanceof ActionEvent) {
            if (listeners == null) {
                return (false);
            }
            ActionEvent aevent = (ActionEvent) event;
            int ordinal = phaseId.getOrdinal();
            broadcast(aevent, listeners[PhaseId.ANY_PHASE.getOrdinal()]);
            broadcast(aevent, listeners[ordinal]);
            for (int i = ordinal + 1; i < listeners.length; i++) {
                if ((listeners[i] != null) && (listeners[i].size() > 0)) {
                    return (true);
                }
            }
            return (false);
        } else {
            throw new IllegalArgumentException();
        }

    }


    /**
     * <p>Broadcast the specified {@link ActionEvent} to the
     * {@link ActionListener}s on the specified list (if any)
     *
     * @param event The {@link ActionEvent} to be broadcast
     * @param list The list of {@link ActionListener}s, or
     *  <code>null</code> for no interested listeners
     */
    protected void broadcast(ActionEvent event, List list) {

        if (list == null) {
            return;
        }
        Iterator listeners = list.iterator();
        while (listeners.hasNext()) {
            ActionListener listener = (ActionListener) listeners.next();
            listener.processAction(event);
        }

    }


    /**
     * <p>Queue an {@link ActionEvent} for processing during the next
     * event processing cycle.</p>
     *
     * @param context The {@link FacesContext} for the current request
     */
    public void fireActionEvent(FacesContext context) {

        context.addFacesEvent(new ActionEvent(this));

    }


    /**
     * <p>Remove an existing {@link ActionListener} (if any) from the set of
     * listeners interested in being notified when {@link ActionEvent}s
     * occur.</p>
     *
     * @param listener The {@link ActionListener} to be removed
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public void removeActionListener(ActionListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            return;
        }
        int ordinal = listener.getPhaseId().getOrdinal();
        if (listeners[ordinal] == null) {
            return;
        }
        listeners[ordinal].remove(listener);

    }


}
