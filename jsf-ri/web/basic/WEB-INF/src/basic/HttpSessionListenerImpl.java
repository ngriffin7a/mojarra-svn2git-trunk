/*
 * $Id: HttpSessionListenerImpl.java,v 1.5 2002/07/23 19:37:30 eburns Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package basic;

// HttpSessionListenerImpl.java

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;

/**
 *
 *  <B>HttpSessionListenerImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: HttpSessionListenerImpl.java,v 1.5 2002/07/23 19:37:30 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class HttpSessionListenerImpl extends Object implements HttpSessionListener
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
// Constructors and Initializers    
//

public HttpSessionListenerImpl()
{
    super();
    // ParameterCheck.nonNull();
    this.init();
}

protected void init()
{
    // super.init();
}

//
// Class methods
//

//
// General Methods
//

//
// Methods from HttpSessionListener
//

/**

* This is where the model objects are configured. <P>

* PRECONDITION: The FacesServlet's init() method has been called and it
* executed correctly <P>

* POSTCONDITION: The ObjectTable has been correctly configured with all
* the model objects for use in this web-app. <P>

*/

public void sessionCreated(HttpSessionEvent sce) {
    System.out.println("sessionCreated");
    if (null == sce.getSession().getAttribute("LoginBean")) {
	sce.getSession().setAttribute("LoginBean", new LoginBean());
    }
}

public void sessionDestroyed(HttpSessionEvent sce) {
    System.out.println("sessionDestroyed");
}

} // end of class HttpSessionListenerImpl
