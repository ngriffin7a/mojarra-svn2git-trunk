/*
 * $Id: LifecycleImpl.java,v 1.35 2003/10/06 18:11:32 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// LifecycleImpl.java

package com.sun.faces.lifecycle;

import com.sun.faces.util.Util;
import com.sun.faces.RIConstants;
import com.sun.faces.application.ViewHandlerImpl;
import org.mozilla.util.Assert;

import javax.faces.lifecycle.Lifecycle;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseListener;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;



/**
 *
 *  <B>LifecycleImpl</B> is the stock implementation of the standard
 *  Lifecycle in the JSF RI. <P>
 *
 *
 * @version $Id: LifecycleImpl.java,v 1.35 2003/10/06 18:11:32 eburns Exp $
 * 
 * @see	javax.faces.lifecycle.Lifecycle
 *
 */

public class LifecycleImpl extends Lifecycle
{
//
// Protected Constants
//

    protected static final int BEFORE = 0;
    protected static final int AFTER = 1;
    
//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

/**

* INVARIANT: The contents of phaseWrappers must not change after the
* ctor returns.

*/

    protected ArrayList phaseWrappers;

    protected ArrayList phaseListeners;


    protected Object lock = null;    

    //
    // Constructors and Initializers    
    //

    public LifecycleImpl() {
        super();
        phaseWrappers  = new ArrayList();
        initPhases();
        lock = new Object();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    protected void initPhases() {
        phaseWrappers.add(new PhaseWrapper(new RestoreViewPhase()));
        phaseWrappers.add(new PhaseWrapper(new ApplyRequestValuesPhase()));
        phaseWrappers.add(new PhaseWrapper(new ProcessValidationsPhase()));
        phaseWrappers.add(new PhaseWrapper(new UpdateModelValuesPhase()));
        phaseWrappers.add(new PhaseWrapper(new InvokeApplicationPhase(this)));
        phaseWrappers.add(new PhaseWrapper(new RenderResponsePhase(Application.getCurrentInstance())));
    }

    protected Phase getRenderPhase(FacesContext context) throws FacesException {
        Assert.assert_it(null != phaseWrappers);
        Phase renderPhase = null;
        PhaseWrapper wrapper = null;
  
        Iterator it = phaseWrappers.iterator();
        while ( it.hasNext() ) {
            wrapper = (PhaseWrapper) it.next();
            Assert.assert_it(wrapper != null);
            renderPhase = wrapper.instance;
            Assert.assert_it(renderPhase != null);
            if ((renderPhase).getId() == PhaseId.RENDER_RESPONSE) {
                break;
            }
        }
    
        Assert.assert_it(null != renderPhase);
	return renderPhase;
    }   

    public void execute(FacesContext context) throws FacesException {
        if (context == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }

        PhaseWrapper wrapper = null;
        Phase curPhase = null;
        Iterator phaseIter = phaseWrappers.iterator();
        int curPhaseId = 0;
        Assert.assert_it(null != phaseIter);

        while (phaseIter.hasNext()) {
	    wrapper = (PhaseWrapper)phaseIter.next();
	    curPhase = wrapper.instance;

	    maybeCallListeners(curPhase, BEFORE);

            // Execute the current phase
            curPhase.execute(context);

            // Go to Render Phase?
            if ((curPhase.getId() == PhaseId.RESTORE_VIEW) && 
                (!hasPostDataOrQueryParams(context))) {
                curPhase = getRenderPhase(context);
                context.renderResponse();
            }

            if (context.getResponseComplete()) {
		maybeCallListeners(curPhase, AFTER);
                return;
            } else if (context.getRenderResponse()) {
		// If we're skipping to RENDER_RESPONSE, be sure to call
		// the after listener for the current phase.
		maybeCallListeners(curPhase, AFTER);

		// then call the before listener for RENDER_RESPONSE
                curPhase = getRenderPhase(context);
		maybeCallListeners(curPhase, BEFORE);

		// then execute the RENDER_RESPONSE phase
		curPhase.execute(context);

		// then call the after listener for RENDER_RESPONSE
		maybeCallListeners(curPhase, AFTER);

		return;
            }
	    
	    maybeCallListeners(curPhase, AFTER);

            curPhaseId++;
        }
    }

    
    /*
     * Check for request parameters or save state. If neither are present
     * then all phases can be skipped and can go directly to render response
     * phase.
     */
    private boolean hasPostDataOrQueryParams(FacesContext context) {

        Object request = context.getExternalContext().getRequest();
        if (request instanceof HttpServletRequest) {
            if ("GET".equals(((HttpServletRequest)request).getMethod())) {
                //check for request parameters
                Iterator paramNames = context.getExternalContext().
                    getRequestParameterNames();
                if (paramNames.hasNext()) {
                   return true;
                }
                return false;
            } else if ("POST".equals(((HttpServletRequest)request).getMethod()) ||
                "PUT".equals(((HttpServletRequest)request).getMethod())) {
                //we have to assume there is post data
                return true;
            }
        } else if (request instanceof ServletRequest) {
            Iterator paramNames = context.getExternalContext().
                getRequestParameterNames();
            if (paramNames.hasNext()) {
               return true;
            }
            return false;
        }

        //default to going through all processing phases
        return true;
    }

    public void addPhaseListener(PhaseListener listener) {
	if (null == listener) {
	    throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
	}
	
	synchronized (lock) {
	    if (null == phaseListeners) {
		phaseListeners = new ArrayList();
	    }
	    phaseListeners.add(listener);
	}
    }

    public void removePhaseListener(PhaseListener listener) {
	if (null == listener) {
	    throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
	}
	
	synchronized (lock) {
	    if (null != phaseListeners) {
		phaseListeners.remove(listener);
	    }
	}
    }

    //
    // Helper methods
    //

    /**
     * <p>If we have no listeners, just return.</p>
     * <p>For each listener in our listener list.</p>
     */

    protected void maybeCallListeners(Phase curPhase, int listenerMethod) {
	if (null == phaseListeners) {
	    return;
	}
	Assert.assert_it(null != curPhase);
	Assert.assert_it(listenerMethod == BEFORE || listenerMethod == AFTER);
	PhaseEvent event = new PhaseEvent(FacesContext.getCurrentInstance(),
					  curPhase.getId());
	
	synchronized(lock) {
	    Iterator listenerIter = phaseListeners.iterator();
	    PhaseListener curListener = null;
	    while (listenerIter.hasNext()) {
		curListener = (PhaseListener) listenerIter.next();
		if (curPhase.getId() == curListener.getPhaseId() ||
		    PhaseId.ANY_PHASE == curListener.getPhaseId()) {
		    switch (listenerMethod) {
		    case BEFORE:
			curListener.beforePhase(event);
			break;
		    case AFTER:
			curListener.afterPhase(event);
			break;
		    default:
			Assert.assert_it(false);
			break;
		    }
		}
	    }
	}
    }
    

    //
    // Helper classes
    //

    static class PhaseWrapper extends Object {

        protected Phase instance = null;

        PhaseWrapper(Phase newInstance) {
            instance = newInstance;
        }

        /**
         * PRECONDITION: phaseIter is non null <P>
         * POSTCONDITION: All the phases in phaseIter have had their execute
         * method called, unless one of them either threw an exception or
         * returned a result other than Phase.GOTO_NEXT.
         */

        private void executePhaseIterator(Iterator phaseIter, 
				          FacesContext context) 
                                          throws FacesException {
            Phase curPhase = null;
            while (phaseIter.hasNext()) {
	        curPhase = (Phase) phaseIter.next();
	        curPhase.execute(context);
                if (context.getResponseComplete() || 
                    context.getRenderResponse()) {
                    return;
                }
            }
        }

    } // end of class PhaseWrapper


// The testcase for this class is TestLifecycleImpl.java 


} // end of class LifecycleImpl
