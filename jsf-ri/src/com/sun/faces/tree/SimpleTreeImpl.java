/*
 * $Id: SimpleTreeImpl.java,v 1.5 2002/10/07 20:39:52 jvisvanathan Exp $
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
import javax.faces.tree.Tree;
import javax.faces.FactoryFinder;
import javax.faces.render.RenderKitFactory;
import javax.faces.context.FacesContext;

import com.sun.faces.RIConstants;

/**
 *
 *  <B>SimpleTreeImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SimpleTreeImpl.java,v 1.5 2002/10/07 20:39:52 jvisvanathan Exp $
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

protected String renderKitId = null;
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

public SimpleTreeImpl(FacesContext facesContext, String newTreeId) {
    this(facesContext, null, newTreeId);
}    

public SimpleTreeImpl(FacesContext context, UIComponent newRoot, 
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
    setRenderKitId(RenderKitFactory.DEFAULT_RENDER_KIT);
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

public String getRenderKitId()
{
    return renderKitId;
}

public void setRenderKitId(String newRenderKitId)
{
    ParameterCheck.nonNull(newRenderKitId);
    renderKitId = newRenderKitId;
}

public UIComponent getRoot()
{
    return root;
}
 
public String getTreeId()
{
    return treeId;
}

} // end of class SimpleTreeImpl
