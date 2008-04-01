/*
 * $Id: SimpleTreeImpl.java,v 1.3 2002/07/31 19:22:03 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SimpleTreeImpl.java

package com.sun.faces.tree;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.tree.Tree;
import javax.faces.FactoryFinder;

import javax.servlet.ServletContext;

import com.sun.faces.RIConstants;

/**
 *
 *  <B>SimpleTreeImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SimpleTreeImpl.java,v 1.3 2002/07/31 19:22:03 jvisvanathan Exp $
 * 
 * @see	javax.faces.tree.Tree
 *
 */

public class SimpleTreeImpl extends Tree 
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

protected String treeId = null;

// Relationship Instance Variables

// PENDING (visvan) once we migrate to the next version of the API,
// we don't have to make renderkit transient, since Tree no longer
// will store the renderkitId instead of instance.
protected transient RenderKit renderKit = null;
protected UIComponent root = null;

//
// Constructors and Initializers    
//
public SimpleTreeImpl() {
    super();
}

/**

* PRECONDITION: the ServletContext has been initialized with all the
* required factories.

*/ 

public SimpleTreeImpl(ServletContext servletContext, String newTreeId) {
    this(servletContext, null, newTreeId);
}    

public SimpleTreeImpl(ServletContext context, UIComponent newRoot, 
		   String newTreeId)
{
    super();
    ParameterCheck.nonNull(context);

    if ( newRoot == null ) {
        newRoot = new UIComponentBase() {
        public String getComponentType() { return "root"; }
        };
    }
    setRoot(newRoot);
    setTreeId(newTreeId);

    renderKit = (RenderKit) 
	context.getAttribute(RIConstants.DEFAULT_RENDER_KIT);
    if (null == renderKit) {
	// create and store the default RenderKit
	RenderKitFactory renderKitFactory = (RenderKitFactory)
	    FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	Assert.assert_it(null != renderKitFactory);
	
	renderKit = 
	    renderKitFactory.getRenderKit(RIConstants.DEFAULT_RENDER_KIT);
	Assert.assert_it(null != renderKit);
	context.setAttribute(RIConstants.DEFAULT_RENDER_KIT, renderKit);
    }
	
    Assert.assert_it(null != renderKit);
    
}

//
// Class methods
//

//
// General Methods
//

void setRoot(UIComponent newRoot)
{
    ParameterCheck.nonNull(newRoot);
    root = newRoot;
}

void setTreeId(String newTreeId)
{
   ParameterCheck.nonNull(newTreeId);
   treeId = newTreeId;
}

//
// Methods from Tree
//

public RenderKit getRenderKit()
{
    return renderKit;
}

public void setRenderKit(RenderKit newRenderKit)
{
    ParameterCheck.nonNull(newRenderKit);

    renderKit = newRenderKit;
}

public UIComponent getRoot()
{
    return root;
}
 
public String getTreeId()
{
    return treeId;
}

public void release()
{
    root = null;
    treeId = null;
    renderKit = null;
}

} // end of class SimpleTreeImpl
