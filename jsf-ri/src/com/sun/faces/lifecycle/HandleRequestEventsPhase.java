/*
 * $Id: HandleRequestEventsPhase.java,v 1.4 2002/06/22 00:15:07 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HandleRequestEventsPhase.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;

import java.util.Iterator;


/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: HandleRequestEventsPhase.java,v 1.4 2002/06/22 00:15:07 jvisvanathan Exp $
 * 
 * @see	com.sun.faces.lifecycle.DefaultLifecycleImpl
 * @see	javax.faces.lifecycle.Lifecycle#HANDLE_REQUEST_EVENTS_PHASE
 *
 */

public class HandleRequestEventsPhase extends GenericPhaseImpl
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Genericializers    
//

public HandleRequestEventsPhase(Lifecycle newDriver, int newId)
{
    super(newDriver, newId, 
	  new LifecycleCallback() {
	      public int takeActionOnComponent(FacesContext context,
					       UIComponent comp) throws FacesException {
		  if (comp.processEvents(context)) {
		      return Phase.GOTO_RENDER;
		  }
		  // PENDING(): how to skip to rendering?
		  return Phase.GOTO_NEXT;
	      }
	  });
}

//
// Class methods
//

//
// General Methods
//


// The testcase for this class is TestHandleRequestEventsPhase.java


} // end of class HandleRequestEventsPhase
