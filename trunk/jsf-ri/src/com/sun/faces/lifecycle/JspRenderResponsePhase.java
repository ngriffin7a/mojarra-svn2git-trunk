/*
 * $Id: JspRenderResponsePhase.java,v 1.4 2002/06/18 04:58:35 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// JspRenderResponsePhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.tree.XmlTreeImpl;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.tree.Tree;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;

import java.util.Iterator;
import java.io.IOException;


/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: JspRenderResponsePhase.java,v 1.4 2002/06/18 04:58:35 rkitain Exp $
 * 
 * @see	com.sun.faces.lifecycle.DefaultLifecycleImpl
 * @see	javax.faces.lifecycle.Lifecycle#UPDATE_MODEL_VALUES_PHASE
 *
 */

public class JspRenderResponsePhase extends GenericPhaseImpl
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

public JspRenderResponsePhase(Lifecycle newDriver, int newId)
{
    super(newDriver, newId);
}

//
// Class methods
//

//
// General Methods
//

//
// Methods from Phase
//

public int execute(FacesContext facesContext) throws FacesException
{
    int rc = Phase.GOTO_NEXT;
    String requestURI = null;
    HttpServletRequest request = (HttpServletRequest) 
	facesContext.getServletRequest();
    RequestDispatcher requestDispatcher = null;
    Tree tree = facesContext.getResponseTree();
    try {

//PENDING(rogerk) do we want to add "getPageUrl" method to 
//javax.faces.tree.Tree class (in which case, we won;t have to cast??
//
        requestURI = ((XmlTreeImpl)tree).getPageUrl();
	requestDispatcher = request.getRequestDispatcher(requestURI);
	requestDispatcher.forward(request, facesContext.getServletResponse());
    }
    catch (IOException e) {
	throw new FacesException("Can't forward", e);
    }
    catch (ServletException e) {
	throw new FacesException("Can't forward", e);
    }
    return rc;
}



// The testcase for this class is TestRenderResponsePhase.java


} // end of class JspRenderResponsePhase
