/*
 * $Id: UIComponent.java,v 1.2 2002/01/16 21:02:44 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;


/**
 * The base class for representing all state associated with a
 * user-interface component.  UIComponent instances are created
 * and managed in the server in order to serve the following core 
 * functions to a JavaServer Faces application:
 * <ol>
 * <li>Identify a component's functional type
 * <li>Identify a component's name (identity)
 * <li>Store any component non-default state
 * <li>If component is data-bound, maintain reference to 
 *     application data object
 * <li>Provide descriptive API for getting/setting any component
 *     non-render-specific state
 * <li>Enable the creation and manipulation of component hierarchies
 * <li>Drive the rendering process of a component or hierarchy of
 *     components
 * <li>Provide event-handler registration on components
 * </ol>
 * <p>
 * A UIComponent instance may act as a "parent" container if one or
 * more UIComponent instances are added as children.
 * A UIComponent instance may act as a "child" if it is added to
 * one or more parent UIComponent instances.
 */
public abstract class UIComponent {
 
    private Hashtable ht;


    public UIComponent() {
        ht = new Hashtable();
    }

    /** 
     * Returns a String representing the type of this concrete
     * component class.  This read-only property is used by the RenderKit
     * instance as the name key for mapping a component type to its 
     * available renderers.  A concrete subclass must return a
     * String value which is descriptive of the functional purpose 
     * of the component.
     *
     * @return a String representing the component's functional type
     *         
     */

    // PENDING(edburns): Spec Compliance: instance into Scoped Namespace
    // per spec:
    // http://javaweb.sfbay.sun.com/~aim/faces/jsr-127-archive/msg00124.html
    // section "Configuring UI Components"

    public abstract String getType();

    /**
     * The name which identifies this component within the application.
     * This name should be equivelent to the name used to store the
     * component in the scoped namespace.
     * @see ObjectTable#put
     * @see #setId
     * @return String containing the id of this component
     */
    public String getId() {
	return null;
    }

    /**
     * Sets the id of this component.
     * @see #getId
     * @param id String containing the id of the component
     */
    public void setId(String id) {
    }

    /**
     * The renderer name to be used to render this component in the
     * specified render context.  
     * @param rc the render-context used to render this component
     * @return String containing the name of the renderer set for
     *         this component
     */
    public String getRendererName(RenderContext rc) {
	return (String)getAttribute(rc, Constants.REF_RENDERER_NAME);
    }

    /**
     * Sets the renderer name to be used to render this component
     * in the specified render context.  The specified rendererName
     * must exist in the render-context's render-kit and the renderer
     * corresponding to that name must support rendering this component's
     * type, else an exception will be thrown.
     *
     * @param rc the render context used to render this component
     * @param rendererName String containing the name of the renderer
     * @throws NullPointerException if rc or rendererName is null
     * @throws FacesException if the specified rendererName is not
     *         supported by the render kit or the renderer does not
     *         support rendering this component type
     */
    public void setRendererName(RenderContext rc, String rendererName) throws FacesException {
        setAttribute(rc, Constants.REF_RENDERER_NAME, rendererName);
    }
 
    /**
     * Returns an iteration containing all the attribute names
     * corresponding to attributes set specifically on this
     * component within the specified render context.
     *
     * @param rc the render context used to render this component
     * @return an Iteration of attribute names on this component
     */   
    public Iterator getAttributeNames(RenderContext rc) {
        Set set = ht.keySet();
        if (set.isEmpty()) {
            return null;
        } else {
            return set.iterator();
        }
    }

    /**
     * Returns the component attribute with the given name
     * within the specified render context or null if there is the
     * specified attribute is not set on this component.
     *
     * @param rc the render context used to render this component
     * @param attributeName a String specifying the name of the attribute
     * @return the Object bound to the attribute name, or null if the
     *          attribute does not exist.
     */
    public Object getAttribute(RenderContext rc, String attributeName) {
        return ht.get(attributeName);
    }

    /**
     * Binds an object to the specified attribute name for this component
     * within the specified render context.
     *
     * @param rc the render context used to render this component
     * @param attributeName a String specifying the name of the attribute
     * @param value an Object representing the value of the attribute
     */
    public void setAttribute(RenderContext rc, String attributeName, 
        Object value) {
        if (attributeName != null && value != null) {
            ht.put(attributeName,value);
        }
        else if (null != attributeName && null == value) {
            ht.remove(attributeName);
        }
    }

    /**
     * Adds the specified component as a child to this component.
     * The child will be added at the end of this component's sequence
     * of children.
     * @param child the UIComponent to be added as a child
     * @throws NullPointerException if child is null
     */
    public void add(UIComponent child) {}

    /**
     * Adds the specified component as a child to this component at
     * the specified index.
     * @param child the UIComponent to be added as a child
     * @param index the integer indicating where in the sequence of
     *        children the child should be added
     * @throws NullPointerException if child is null
     * @throws IndexOutOfBoundsException if index < 0 or index > childCount
     */
    public void add(UIComponent child, int index) {}

   /**
     * Adds the specified component as a child to this component with
     * the specified childID.  The childID is an abitrary String
     * which may be used internally by this parent component to logically
     * identify the child.  If a child already exists with this childID, 
     * it will be replaced with the new child specified in this method
     * call.
     *
     * @param child the UIComponent representing the child to be added
     * @param childID a String which logically identifies the child within
     *        this parent component
     * @throws NullPointerException if child is null
     */ 
    // Aim10-25-01: Don't implement yet
    public void add(UIComponent child, String childID) {}

    /** 
     * Removes the child component located at the specified index from
     * this component.
     * @param index the integer indicating the index of the child to be 
     *        removed 
     * @throws IndexOutOfBoundsException if index < 0 or index > childCount-1
     */ 
    public void remove(int index) {}

    /** 
     * Removes the child component associated with the specified logical
     * childID from this component.
     * @param childID a String which logically identifies the child within
     *        this parent component
     * @throws NullPointerException if childID is null
     */ 
    // Aim10-25-01: Don't implement yet
    public void remove(String childID) {}

    /**
     * Removes all child components from this component.
     */
    public void removeAll() {}

    /**
     * Sets the child components on this component to be the specified 
     * array of components.  If this component already contains children at
     * the time this method is invoked, they will first be removed
     * before the new children are added.
     * @param kids the array containing the components to add as children
     * @throws NullPointerException if kids is null
     */
    public void setChildren(UIComponent[] kids) {}

    /**
     * Returns an iterator containing all the children of this component
     * for the specified render context.
     * @param rc the render context used to render this component
     */
    public Iterator getChildren(RenderContext rc) {
        return null;
    }

    /**
     * Returns an iterator containing Strings representing all the
     * childIDs of all children associated with childID for this component
     * for the specified render context.
     * @param rc the render context used to render this component
     */
    public Iterator getChildIds(RenderContext rc) {
        return null;
    }

    /**
     * Returns the child at the specified index in this component
     * for the specified render context.
     * @param rc the render context used to render this component
     * @param index the integer indicating the index of the child
     * @throws IndexOutOfBoundsException if index < 0 or index > childCount-1
     */
    public UIComponent getChild(RenderContext rc, int index) {
        return null;
    }

    /**
     * Returns the child associated with the specified logical childID
     * for the specified render context.  If no child exists with that
     * childID, returns null.
     * @param rc the render context used to render this component
     * @param childID a String which logically identifies the child within
     *        this parent component
     * @return the UIComponent representing the child associated with the
     *          childID, or null if it does not exist
     * @throws NullPointerException if childID is null
     */
    // Aim10-25-01: don't implement yet
    public UIComponent getChild(RenderContext rc, String childID) {
        return null;
    }

    /**
     * Returns the number of children in this component for the
     * specified render context.
     * @param rc the render context used to render this component
     * @return an integer indicating the number of children in this
     *          component.
     */
    public int getChildCount(RenderContext rc) {
        return 0;
    }

    /**
     * The rendersChildren property.  Returns <code>false</code>
     * by default.
     * @param rc the render context used to render this component
     * @return boolean value indicating whether or not this component
     *         takes responsibility for laying out and rendering its
     *         children.
     */
    public boolean getRendersChildren(RenderContext rc) {
	return false;
    }

    /**
     * Invokes full render processing on this component and its
     * children. This method should only be called on components
     * which return <code>true</code> from <code>getRendersChildren()</code>.
     * It calls the following sequence of methods, passing 
     * in the specified render context:
     * <ol>
     * <li>render(rc)
     * <li>renderChildren(rc)
     * <li>postComplete(rc)
     * </ol>
     * This method will recursively drive the rendering process for
     * this component and all of its descendents, treating the
     * rendering of that hierarchy as an atomic process.  This 
     * method should only be called if the the portion of the page
     * defined by this hierarchy is completely defined by this
     * hierarchy (i.e. no interleaved presentation markup).
     *
     * @see #getRendersChildren
     * @param rc the render context used to render this component
     * @throws IOException if input or output exception occurred
     */
    public void renderAll(RenderContext rc) throws IOException {}

    /**
     * Renders this component.  By default it will invoke renderStart()
     * on the renderer corresponding to the rendererName property of
     * this component.  
     * This method renders this component only and does not perform
     * any recursive rendering on children.
     *
     * @param rc the render context used to render this component
     * @throws IOException if input or output exception occurred
     */
    public void render(RenderContext rc) throws IOException,
        FacesException {

        RenderKit rk = rc.getRenderKit();
        String rendererName = getRendererName(rc);
        if (rendererName == null) {
            throw new FacesException("Renderer Name Not Set.");
        }
        Renderer r = rk.getRenderer(rendererName);
        rc.pushChild(this);
        r.renderStart(rc, this);
    }

    /**
     * Invoked from renderAll() to Render the children of this component.  
     * By default it will invoke renderChildren() on the renderer 
     * corresponding to the rendererName property of this component.  
     * If this component type does not support rendering the layout 
     * of its children, it should override this method to do nothing.
     *
     * @param rc the render context used to render this component
     * @throws IOException if input or output exception occurred
     */
    public void renderChildren(RenderContext rc) throws IOException {}

    /**
     * Invoked after this component and all of it's children are
     * rendered. By default it will invoke renderComplete() on the renderer 
     * corresponding to the rendererName property of this component. 
     * 
     * @param rc the render context used to render this component
     * @throws IOException if input or output exception occurred
     */
    public void renderComplete(RenderContext rc) throws IOException,
        FacesException {
        RenderKit rk = rc.getRenderKit();
        String rendererName = getRendererName(rc);
        if (rendererName == null) {
            throw new FacesException("Renderer Name Not Set.");
        }
        Renderer r = rk.getRenderer(rendererName);
        r.renderComplete(rc, this);
        rc.popChild();
    }

}

