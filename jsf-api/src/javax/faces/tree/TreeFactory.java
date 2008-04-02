/*
 * $Id: TreeFactory.java,v 1.12 2002/09/22 01:35:34 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.tree;

import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;


/**
 * <p><strong>TreeFactory</strong> is a factory object that creates and
 * returns new {@link Tree} instances.</p>
 *
 * <p>There must be one <code>TreeFactory</code> instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 * <pre>
 *   TreeFactory factory = (TreeFactory)
 *    FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
 * </pre>
 */

public abstract class TreeFactory {


    /**
     * <p>Create (if needed) and return a {@link Tree} that is initialized
     * with a root node, and whose <code>renderKitId</code> property is
     * initialized to the identifier of the default <code>RenderKit</code>.</p>
     *
     * @param context FacesContext for the current request
     * @param treeId Tree identifier of the tree to be constructed and
     *  returned
     *
     * @exception FacesException if a {@link Tree} cannot be
     *  constructed for the specified parameters
     * @exception NullPointerException if <code>context</code> or
     *  <code>treeId</code> is null
     */
    public abstract Tree getTree(FacesContext context,
                                 String treeId) throws FacesException;


}

