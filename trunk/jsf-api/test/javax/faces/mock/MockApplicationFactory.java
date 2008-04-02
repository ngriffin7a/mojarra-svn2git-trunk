/*
 * $Id: MockApplicationFactory.java,v 1.5 2004/02/04 23:39:10 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;


public class MockApplicationFactory extends ApplicationFactory {

    public MockApplicationFactory(ApplicationFactory oldImpl) {
	System.setProperty(FactoryFinder.APPLICATION_FACTORY, 
			   this.getClass().getName());
    }
    public MockApplicationFactory() {}

    private Application application = null;

    public Application getApplication() {
        if (application == null) {
            application = new MockApplication();
        }
        return (application);
    }

    public void setApplication(Application application) {
        this.application = application;
    }


}

