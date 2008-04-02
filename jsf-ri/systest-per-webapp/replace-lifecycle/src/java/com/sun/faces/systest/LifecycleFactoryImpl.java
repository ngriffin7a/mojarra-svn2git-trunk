/*
 * $Id: LifecycleFactoryImpl.java,v 1.1 2005/01/13 19:57:49 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javax.faces.FactoryFinder;
import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import java.util.Iterator;

public class LifecycleFactoryImpl extends LifecycleFactory {

    private LifecycleFactory previous = null;

    private Lifecycle newLifecycle = null;

    public LifecycleFactoryImpl(LifecycleFactory previous) {
	this.previous = previous;
	try {
	    newLifecycle = new NewLifecycle();
	    this.previous.addLifecycle("com.sun.faces.systest.NewLifecycle",
				       newLifecycle);
	}
	catch (Throwable e) {
	    throw new FacesException(e);
	}
    }

    public void addLifecycle(String lifecycleId,
			     Lifecycle lifecycle) {
	previous.addLifecycle(lifecycleId, lifecycle);
    }

    public Lifecycle getLifecycle(String lifecycleId) {
	return previous.getLifecycle(lifecycleId);
    }


    public Iterator getLifecycleIds() {
	return previous.getLifecycleIds();
    }
}
