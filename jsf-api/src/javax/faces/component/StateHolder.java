/*
 * $Id: StateHolder.java,v 1.3 2003/08/02 05:11:32 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import java.io.IOException;

import javax.faces.context.FacesContext;

/**
 *
 * <p>This interface is implemented by classes that need to save their
 * state between requests.</p>
 *
 * <p>An implementor <strong>must</strong> implement both {@link
 * #getState} and {@link #restoreState} methods in this class, since
 * these two methods have a tightly coupled contract between themselves.
 * In other words, if there is an ineritance hierarchy, it is not
 * permissable to have the {@link #getState} and {@link #restoreState}
 * methods reside at different levels of the hierarchy.</p>
 *
 */

public interface StateHolder {

    /**
     * <p> Gets the state of the instance as a
     * <code>Serializable</code> Object.<p>
     *
     * <p>If the class that implements this interface has references to
     * instances that implement StateHolder (such as a
     * <code>UIComponent</code> with event handlers, validators, etc.)
     * this method must call the {@link #getState} method on all those
     * instances as well.</p>
     *
     * <p>The return from this method must be <code>Serializable</code></p>
     *
     */

    public Object getState(FacesContext context);

    /**
     *
     * <p> Perform any processing required to restore the state from the
     * entries in the state Object.</p>
     *
     * <p>If the class that implements this interface has references to
     * instances that also implement StateHolder (such as a
     * <code>UIComponent</code> with event handlers, validators, etc.)
     * this method must call the {@link #restoreState} method on all those
     * instances as well. </p>
     *
     */

    public void restoreState(FacesContext context, Object state) throws IOException;

    /**
     *
     * <p>If true, the Object implementing this interface must not
     * participate in state saving or restoring.</p>
     */

    public boolean isTransient();

    public void setTransient(boolean newTransientValue);

}
