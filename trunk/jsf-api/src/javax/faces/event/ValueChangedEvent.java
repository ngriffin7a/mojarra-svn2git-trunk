/*
 * $Id: ValueChangedEvent.java,v 1.2 2003/01/16 20:24:21 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import javax.faces.component.UIComponent;


/**
 * <p>A {@link ValueChangedEvent} is a notification that the local value of
 * the source component has been changed as a result of user interface
 * activity.  It is not fired unless validation of the new value was
 * completed successfully.</p>
 */

public class ValueChangedEvent extends FacesEvent {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new event object from the specified source component,
     * old value, and new value.</p>
     *
     * @param component Source {@link UIComponent} for this event
     * @param oldValue The previous local value of this {@link UIComponent}
     * @param newValue The new local value of thie {@link UIComponent}
     *
     * @exception IllegalArgumentException if <code>component</code> is
     *  <code>null</code>
     */
    public ValueChangedEvent(UIComponent source,
                             Object oldValue, Object newValue) {

        super(source);
        this.oldValue = oldValue;
        this.newValue = newValue;

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>The previous local value of the source {@link UIComponent}.</p>
     */
    private Object oldValue = null;


    /**
     * <p>Return the previous local value of the source {@link UIComponent}.
     * </p>
     */
    public Object getOldValue() {

        return (this.oldValue);

    }


    /**
     * <p>The current local value of the source {@link UIComponent}.</p>
     */
    private Object newValue = null;


    /**
     * <p>Return the current local value of the source {@link UIComponent}.
     * </p>
     */
    public Object getNewValue() {

        return (this.newValue);

    }


}
