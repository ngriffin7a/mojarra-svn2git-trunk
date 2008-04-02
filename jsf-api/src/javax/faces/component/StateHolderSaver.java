/*
 * $Id: StateHolderSaver.java,v 1.3 2003/11/06 15:39:42 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.StateHolder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.io.Serializable;

/**
 * <p>Helper class for saving and restoring attached objects.</p>
 *
 * <p>PENDING(edburns): move this to the component.base package, along
 * with the {get,restore}AttachedState() machinery.  Make it
 * package private again.</p>
 *
 */
public class StateHolderSaver extends Object implements Serializable {
    protected String className = null;
    protected Object savedState = null;

    public StateHolderSaver(FacesContext context, Object toSave) {
        className = toSave.getClass().getName();

        if (toSave instanceof StateHolder) {
            // do not save an attached object that is marked transient.
            if (!((StateHolder)toSave).isTransient()) {
                savedState = ((StateHolder)toSave).saveState(context);
            } else {
                className = null;
            }
        }
    }

    /**
     *
     * @return the restored {@link StateHolder} instance.
     */

    public Object restore(FacesContext context) throws IllegalStateException {
        Object result = null;
        Class toRestoreClass = null;
        if ( className == null) {
            return null;
        }
        
        try {
            toRestoreClass = loadClass(className, this);
        }
        catch (ClassNotFoundException e) {
	    throw new IllegalStateException(e.getMessage());
        }

        if (null != toRestoreClass) {
            try {
                result = toRestoreClass.newInstance();
            }
            catch (InstantiationException e) {
                throw new IllegalStateException(e.getMessage());
            }
            catch (IllegalAccessException a) {
                throw new IllegalStateException(a.getMessage());
            }
        }

        if (null != result && null != savedState &&
	    result instanceof StateHolder) {
	    // don't need to check transient, since that was done on
	    // the saving side.
	    ((StateHolder)result).restoreState(context, savedState);
        }
        return result;
    }


    private static Class loadClass(String name, 
            Object fallbackClass) throws ClassNotFoundException {
        ClassLoader loader =
            Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = fallbackClass.getClass().getClassLoader();
        }
        return loader.loadClass(name);
    }
}
