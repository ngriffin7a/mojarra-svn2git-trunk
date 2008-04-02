/*
 * $Id: LifecycleFactoryImpl.java,v 1.15 2003/08/22 16:49:28 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// LifecycleFactoryImpl.java

package com.sun.faces.lifecycle;

import com.sun.faces.util.Util;

import org.mozilla.util.Assert;

import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.FacesException;
import javax.faces.event.PhaseId;

import java.util.Iterator;
import java.util.HashMap;

/**
 *
 *  <B>LifecycleFactoryImpl</B> is the stock implementation of Lifecycle
 *  in the JSF RI. <P>
 *
 *
 * @version $Id: LifecycleFactoryImpl.java,v 1.15 2003/08/22 16:49:28 eburns Exp $
 * 
 * @see	javax.faces.lifecycle.LifecycleFactory
 *
 */

public class LifecycleFactoryImpl extends LifecycleFactory
{
//
// Protected Constants
//
static final int FIRST_PHASE = PhaseId.RESTORE_VIEW.getOrdinal();
static final int LAST_PHASE = PhaseId.RENDER_RESPONSE.getOrdinal();




//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

protected HashMap lifecycleMap = null;
protected Object lock = null;

//
// Constructors and Initializers    
//

public LifecycleFactoryImpl()
{
    super();
    lifecycleMap = new HashMap();

    // We must have an implementation under this key.
    lifecycleMap.put(LifecycleFactory.DEFAULT_LIFECYCLE, 
		     new LifecycleWrapper(new LifecycleImpl(),
					  false));
    lock = new Object();
}

//
// Class methods
//

//
// General Methods
//

/**

* @return true iff lifecycleId was already created

*/

boolean alreadyCreated(String lifecycleId)
{
    LifecycleWrapper wrapper = (LifecycleWrapper)lifecycleMap.get(lifecycleId);
    return (null != wrapper && wrapper.created);
}

/**

* POSTCONDITION: If no exceptions are thrown, it is safe to proceed with
* register*().;

*/

Lifecycle verifyRegisterArgs(String lifecycleId, 
			     int phaseId, Phase phase)
{
    LifecycleWrapper wrapper = null;
    Lifecycle result = null;
    Object [] params = { lifecycleId };
    if (null == lifecycleId || null == phase) {
	throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
    }

    if (null == (wrapper = (LifecycleWrapper) lifecycleMap.get(lifecycleId))) {
	throw new IllegalArgumentException(Util.getExceptionMessage(Util.LIFECYCLE_ID_NOT_FOUND_ERROR_MESSAGE_ID, params));
    }
    result = wrapper.instance;
    Assert.assert_it(null != result);
    
    if (alreadyCreated(lifecycleId)) {
	throw new IllegalStateException(Util.getExceptionMessage(Util.LIFECYCLE_ID_ALREADY_ADDED_ID, params));
    }
    
    if (!((FIRST_PHASE <= phaseId) &&
	  (phaseId <= LAST_PHASE))) {
	params = new Object [] { Integer.toString(phaseId) };
	throw new IllegalArgumentException(Util.getExceptionMessage(Util.PHASE_ID_OUT_OF_BOUNDS_ERROR_MESSAGE_ID, params));
    }
    return result;
}

//
// Methods from LifecycleFactory
//

public void addLifecycle(String lifecycleId, Lifecycle lifecycle)
{
    if (lifecycleId == null || lifecycle == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
    }
    if (null != lifecycleMap.get(lifecycleId)) {
	Object params[] = { lifecycleId };
	throw new IllegalArgumentException(Util.getExceptionMessage(Util.LIFECYCLE_ID_ALREADY_ADDED_ID, params));
    }

    lifecycleMap.put(lifecycleId, new LifecycleWrapper(lifecycle, false));
}

public Lifecycle getLifecycle(String lifecycleId) throws FacesException
{
    Lifecycle result = null;
    LifecycleWrapper wrapper = null;

    if (null == lifecycleId) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
    }
    
    try {
	wrapper = (LifecycleWrapper) lifecycleMap.get(lifecycleId);
	result = wrapper.instance;
	wrapper.created = true;
    }
    catch (Throwable e) {
	Object [] params = { lifecycleId };
	throw new FacesException(Util.getExceptionMessage(Util.CANT_CREATE_LIFECYCLE_ERROR_MESSAGE_ID, params),
				 e);
    }

    return result;
}

public Iterator getLifecycleIds()
{
    return lifecycleMap.keySet().iterator();
}

//
// Helper classes
//

static class LifecycleWrapper extends Object
{


Lifecycle instance = null;
boolean created = false;

LifecycleWrapper(Lifecycle newInstance, boolean newCreated)
{
    instance = newInstance;
    created = newCreated;
}

} // end of class LifecycleWrapper


// The testcase for this class is TestLifecycleFactoryImpl.java 


} // end of class LifecycleFactoryImpl
