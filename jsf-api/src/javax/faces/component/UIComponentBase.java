/*
 * $Id: UIComponentBase.java,v 1.19 2002/08/30 20:11:20 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.RequestEventHandler;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.Validator;


/**
 * <p><strong>UIComponentBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UIComponent} except <code>getComponentType()</code>.
 * Concrete subclasses of <code>UIComponentBase</code> must implement the
 * <code>getComponentType()</code> method.  Component types are used to
 * select the appropriate {@link Renderer} to be used for decoding and
 * encoding, if the <code>rendererType</code> property is non-null.</p>
 *
 * <p>By default, this class defines <code>getRendersChildren()</code> to
 * return <code>false</code>.  Subclasses that wish to manage the rendering
 * of their children should override this method to return <code>true</code>
 * instead.</p>
 */

public abstract class UIComponentBase implements UIComponent {


    // ------------------------------------------------------------- Attributes


    /**
     * <p>The set of attribute values, keyed by the corresponding attribute
     * name.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - The collection for attributes
     * must be lazily instantiated the first time that it is actually used,
     * in order to avoid wasted object creations.  This can be done by
     * calling <code>getAttributes()</code>.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - It is assumed that components
     * can only be modified in the context of a single thread.</p>
     */
    private HashMap attributes = null;


    /**
     * <p>Create (if necessary) and return the collection to be used for
     * attributes storage.</p>
     */
    private Map getAttributes() {

        if (attributes == null) {
            attributes = new HashMap();
        }
        return (attributes);

    }


    /**
     * <p>Have we allocated a collection for attributes yet?</p>
     */
    private boolean isAttributesAllocated() {

        return (attributes != null);

    }


    /**
     * <p>Return the value of the attribute with the specified name
     * (if any); otherwise, return <code>null</code>.</p>
     *
     * @param name Name of the requested attribute
     *
     * @exception NullPointerException if <code>name</code> is
     *  <code>null</code>
     */
    public Object getAttribute(String name) {

        // Validate method parameters
        if (name == null) {
            throw new NullPointerException("getAttribute");
        }

        // Special cases for read-only and special case attributes
        if ("componentType".equals(name)) {
            return (getComponentType());
        } else if ("compoundId".equals(name)) {
            return (getCompoundId());
        } else if ("rendersChildren".equals(name)) {
            if (getRendersChildren()) {
                return (Boolean.TRUE);
            } else {
                return (Boolean.FALSE);
            }
        } else if ("rendersSelf".equals(name)) {
            if (getRendersSelf()) {
                return (Boolean.TRUE);
            } else {
                return (Boolean.FALSE);
            }
        }

        // Return the selected attribute value
        if (!isAttributesAllocated()) {
            return (null);
        }
        return (getAttributes().get(name));

    }


    /**
     * <p>Return an <code>Iterator</code> over the names of all
     * currently defined attributes of this <code>UIComponent</code>.
     */
    public Iterator getAttributeNames() {

        if (isAttributesAllocated()) {
            return (getAttributes().keySet().iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }

    }


    /**
     * <p>Set the new value of the attribute with the specified name,
     * replacing any existing value for that name.</p>
     *
     * @param name Name of the requested attribute
     * @param value New value (or <code>null</code> to remove
     *  any attribute value for the specified name
     *
     * @exception IllegalArgumentException if <code>name</code> represents
     *  a read-only property of this component
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public void setAttribute(String name, Object value) {

        // Validate method parameters
        if (name == null) {
            throw new NullPointerException("setAttribute");
        }
        // FIXME - validate length and contents for componentId

        // Special cases for read-only pseudo-attributes
        if ("componentType".equals(name) ||
            "compoundId".equals(name) ||
            "rendersChildren".equals(name) ||
            "rendersSelf".equals(name)) {
            throw new IllegalArgumentException(name);
        }

        // FIXME - special cases for setComponentId and setModel values

        // Set or remove the specified value
        if (value != null) {
            getAttributes().put(name, value);
        } else {
            getAttributes().remove(name);
        }

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the identifier of this <code>UIComponent</code>.</p>
     */
    public String getComponentId() {

        return ((String) getAttribute("componentId"));

    }


    /**
     * <p>Set the identifier of this <code>UIComponent</code>.
     *
     * @param componentId The new identifier
     *
     * @exception IllegalArgumentException if <code>componentId</code>
     *  is zero length or contains invalid characters
     * @exception NullPointerException if <code>componentId</code>
     *  is <code>null</code>
     */
    public void setComponentId(String componentId) {

        if (componentId == null) {
            throw new NullPointerException("setComponentId");
        }
        // FIXME - validate length>0 and valid characters
        setAttribute("componentId", componentId);

    }


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public abstract String getComponentType();


    /**
     * <p>Return the <em>compound identifier</em> of this component.</p>
     */
    public String getCompoundId() {

        // Special handling for root node
        UIComponent parent = getParent();
        if (parent == null) {
            return ("/");
        }

        // Accumulate the component identifiers of our ancestors
        ArrayList list = new ArrayList();
        list.add(getComponentId());
        while (parent != null) {
            list.add(0, parent.getComponentId());
            parent = parent.getParent();
        }

        // Render the compound identifier from the top down
        StringBuffer sb = new StringBuffer();
        int n = list.size();
        for (int i = 0; i < n; i++) {
            if (i != 1) {
                sb.append(EXPR_SEPARATOR);
            }
            if (i > 0) {
                sb.append((String) list.get(i));
            }
        }
        return (sb.toString());

    }


    /**
     * <p>Return the model reference expression of this
     * <code>UIComponent</code>, if any.</p>
     */
    public String getModelReference() {

        return ((String) getAttribute("modelReference"));

    }


    /**
     * <p>Set the model reference expression of this
     * <code>UIComponent</code>.</p>
     *
     * @param modelReference The new model reference expression, or
     *  <code>null</code> to disconnect this component from any model data
     */
    public void setModelReference(String modelReference) {

        setAttribute("modelReference", modelReference);

    }


    /**
     * <p>Return the parent <code>UIComponent</code> of this
     * <code>UIComponent</code>, if any.</p>
     */
    public UIComponent getParent() {

        return ((UIComponent) getAttribute("parent"));

    }


    /**
     * <p>Set the parent <code>UIComponent</code> of this
     * <code>UIComponent</code>.</p>
     *
     * @param parent The new parent, or <code>null</code> for the root node
     *  of a component tree
     */
    void setParent(UIComponent parent) {

        setAttribute("parent", parent);

    }


    /**
     * <p>Return the {@link Renderer} type for this <code>UIComponent</code>
     * (if any).</p>
     */
    public String getRendererType() {

        return ((String) getAttribute("rendererType"));

    }


    /**
     * <p>Set the {@link Renderer} type for this <code>UIComponent</code>,
     * or <code>null</code> for components that render themselves.</p>
     *
     * @param rendererType Logical identifier of the type of
     *  {@link Renderer} to use, or <code>null</code> for components
     *  that render themselves
     */
    public void setRendererType(String rendererType) {

        setAttribute("rendererType", rendererType);

    }


    /**
     * <p>Return a flag indicating whether this component is responsible
     * for rendering its child components.  The default implementation returns
     * <code>false</code>; components that want to return <code>true</code>
     * must override this method to do so. </p>
     */
    public boolean getRendersChildren() {

        return (false);

    }


    /**
     * <p>Return a flag indicating whether this component has concrete
     * implementations of the <code>decode()</code> and
     * <code>encodeXxx()</code> methods, and is therefore suitable for
     * use in the <em>direct implementation</em> programming model
     * for rendering.  The default implementation returns <code>false</code>;
     * components that want to return <code>true</code> must override
     * this method to do so.</p>
     */
    public boolean getRendersSelf() {

        return (false);

    }


    /**
     * <p>Return <code>true</code> if the <code>decode()</code> method,
     * called during the <em>Apply Request Values</em> phase of the
     * request processing lifecycle, was successful in converting the
     * incoming request parameters into an updated local <code>value</code>.
     * If conversion was unsuccessful, return <code>false</code>.
     */
    public boolean isValid() {

        Boolean valid = (Boolean) getAttribute("valid");
        if (valid != null) {
            return (valid.booleanValue());
        } else {
            return (false);

        }

    }


    /**
     * <p>Define the value to be returned by the <code>isValid()</code>
     * method.  This method should only be called from the
     * <code>decode()</code> method of a {@link Renderer} instance to which
     * decoding has been delegated for this component.</p>
     *
     * @param valid The new <code>valid</code> value
     */
    protected void setValid(boolean valid) {

        if (valid) {
            setAttribute("valid", Boolean.TRUE);
        } else {
            setAttribute("valid", Boolean.FALSE);
        }

    }


    /**
     * <p>Return the local value of this <code>UIComponent</code>, if any.
     */
    public Object getValue() {

        return (getAttribute("value"));

    }


    /**
     * <p>Set the local value of this <code>UIComponent</code>.</p>
     *
     * @param value The new local value
     */
    public void setValue(Object value) {

        setAttribute("value", value);

    }


    /**
     * <p>Evaluate and return the current value of this component, according
     * to the following algorithm.</p>
     * <ul>
     * <li>If the <code>value</code> property has been set (corresponding
     *     to the local value for this component), return that; else</li>
     * <li>If the <code>model</code> property has been set, retrieve and
     *     return the corresponding model value, if possible; else</li>
     * <li>Return <code>null</code>.</li>
     * </ul>
     *
     * @param context FacesContext within which to evaluate the model
     *  reference expression, if necessary
     *
     * @exception NullPointerException if <code>context</code> is null
     */
    public Object currentValue(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        Object value = getValue();
        if (value != null) {
            return (value);
        }
        String modelReference = getModelReference();
        if (modelReference != null) {
            if (context != null) {
                return (context.getModelValue(modelReference));
            }
        }
        return (null);

    }


    // ------------------------------------------------ Tree Management Methods


    /**
     * <p>The set of child <code>UIComponent</code>s associated with this
     * component, in the order specified by how the components were added.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - The collection for children
     * must be lazily instantiated the first time that it is actually used,
     * in order to avoid wasted object creations.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - It is assumed that components
     * can only be modified in the context of a single thread.</p>
     */
    private ArrayList children = null;


    /**
     * <p>If the specified <code>componentId</code> is already present
     * in one of our children, throw an exception.</p>
     *
     * @param componentId The component identifier to check
     *
     * @exception IllegalArgumentException if this component identifier
     *  is <code>null</code>
     * @exception IllegalArgumentException if this component identifier is
     *  already in use by one of our children
     */
    private void checkComponentId(String componentId) {

        if (componentId == null) {
            throw new IllegalArgumentException("Component Id is null");
        }
        if (isChildrenAllocated()) {
            Iterator kids = children.iterator();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                if (componentId.equals(kid.getComponentId())) {
                    throw new IllegalArgumentException(componentId);
                }
            }
        }

    }
            


    /**
     * <p>Create (if necessary) and return an iterator over the child
     * components of this component.</p>
     */
    private List getChildList() {

        if (children == null) {
            children = new ArrayList();
        }
        return (children);

    }


    /**
     * <p>Have we allocated a collection for children yet?</p>
     */
    private boolean isChildrenAllocated() {

        return (children != null);

    }


    /**
     * <p>Append the specified <code>UIComponent</code> to the end of the
     * child list for this component.</p>
     *
     * @param component {@link UIComponent} to be added
     *
     * @exception IllegalArgumentException if the component identifier
     *  of the new component has not been set
     * @exception IllegalArgumentException if the component identifier
     *  of the new component is not unique within the children of
     *  this component
     * @exception NullPointerException if <code>component</code> is null
     */
    public void addChild(UIComponent component) {

        checkComponentId(component.getComponentId());
        getChildList().add(component);
        if (component instanceof UIComponentBase) { // FIXME - Hmmmm
            ((UIComponentBase) component).setParent(this);
        }

    }


    /**
     * <p>Insert the specified <code>UIComponent</code> at the specified
     * position in the child list for this component.</p>
     *
     * @param index Zero-relative index at which to add this
     *  <code>UIComponent</code>
     * @param component Component to be added
     *
     * @exception IllegalArgumentException if the component identifier
     *  of the new component has not been set
     * @exception IllegalArgumentException if the component identifier
     *  of the new component is not unique within the children of
     *  this component
     * @exception IndexOutOfBoundsException if the index is out of range
     *  ((index < 0) || (index &gt;= size()))
     * @exception NullPointerException if <code>component</code> is null
     */
    public void addChild(int index, UIComponent component) {

        checkComponentId(component.getComponentId());
        getChildList().add(index, component);
        if (component instanceof UIComponentBase) { // FIXME - Hmmmm
            ((UIComponentBase) component).setParent(this);
        }

    }


    /**
     * <p>Remove all child <code>UIComponent</code>s from the child list.
     */
    public void clearChildren() {

        if (!isChildrenAllocated()) {
            return;
        }
        children.clear();

    }


    /**
     * <p>Return <code>true</code> if the specified <code>UIComponent</code>
     * is a direct child of this <code>UIComponent</code>; otherwise,
     * return <code>false</code>.</p>
     *
     * @param component Component to be checked
     *
     * @exception NullPointerException if <code>component</code> is null
     */
    public boolean containsChild(UIComponent component) {

        return (component.getParent() == this);

    }


    /**
     * Segment separator in navigation expressions.
     */
    private static final String EXPR_SEPARATOR = "/";


    /**
     * "This element" selector in navigation expressions.
     */
    private static final String EXPR_CURRENT = ".";


    /**
     * "Parent element" selector in navigation expressions.
     */
    private static final String EXPR_PARENT = "..";


    /**
     * <p>Find a related component in the current component tree by evaluating
     * the specified navigation expression (which may be absolute or relative)
     * to locate the requested component, which is then returned.
     * Valid expression values are:</p>
     * <ul>
     * <li><em>Absolute Path</em> (<code>/a/b/c</code>) - Expressions that
     *     start with a slash begin at the root component of the current tree,
     *     and match exactly against the <code>compoundId</code> of the
     *     selected component.</li>
     * <li><em>Root Component</em> - (<code>/</code>) - An expression with
     *     only a slash selects the root component of the current tree.</li>
     * <li><em>Relative Path</em> - (<code>a/b</code>) - Start at the current
     *     component (rather than the root), and navigate downward.</li>
     * <li><em>Special Path Elements</em> - A path element with a single
     *     period (".") selects the current component, while a path with two
     *     periods ("..") selects the parent of the current node.</li>
     * </ul>
     *
     * @param expr Navigation expression to interpret
     *
     * @exception IllegalArgumentException if the syntax of <code>expr</code>
     *  is invalid
     * @exception IllegalArgumentException if <code>expr</code> attempts to
     *  cause navigation to a component that does not exist
     * @exception NullPointerException if <code>expr</code>
     *  is <code>null</code>
     */
    public UIComponent findComponent(String expr) {

        if (expr == null) {
            throw new NullPointerException("findChildren");
        }

        // If this is an absolute expression, start at the root node
        // Otherwise, start at the current node
        UIComponent node = this;
        if (expr.startsWith(EXPR_SEPARATOR)) {
            while (node.getParent() != null) {
                node = node.getParent();
            }
            expr = expr.substring(1);
        }

        // Parse and process each segment of the path
        while (expr.length() > 0) {

            // Identify the next segment
            String segment = null;
            int separator = expr.indexOf(EXPR_SEPARATOR);
            if (separator < 0) {
                segment = expr;
                expr = "";
            } else {
                segment = expr.substring(0, separator);
                expr = expr.substring(separator + 1);
            }

            // Process the identified segment
            if (segment.equals(EXPR_CURRENT)) {
                ; // node already points here
            } else if (segment.equals(EXPR_PARENT)) {
                node = node.getParent();
                if (node == null) {
                    throw new IllegalArgumentException(segment);
                }
            } else {
                boolean found = false;
                Iterator kids = node.getChildren();
                while (kids.hasNext()) {
                    node = (UIComponent) kids.next();
                    if (segment.equals(node.getComponentId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new IllegalArgumentException(segment);
                }
            }

        }

        // Return the selected node
        return (node);

    }



    /**
     * <p>Return the <code>UIComponent</code> at the specified position
     * in the child list for this component.</p>
     *
     * @param index Position of the desired component
     *
     * @exception IndexOutOfBoundsException if index is out of range
     *  ((index &lt; 0) || (index &gt;= size()))
     */
    public UIComponent getChild(int index) {

        return ((UIComponent) getChildList().get(index));

    }


    /**
     * <p>Return the number of <code>UIComponent</code>s on the child list
     * for this component.</p>
     */
    public int getChildCount() {

        if (isChildrenAllocated()) {
            return (getChildList().size());
        } else {
            return (0);
        }

    }


    /**
     * <p>Return an <code>Iterator</code> over the child
     * <code>UIComponent</code>s of this <code>UIComonent</code>,
     * in the order of their position in the child list.  If this
     * component has no children, an empty <code>Iterator</code>
     * is returned.</p>
     */
    public Iterator getChildren() {

        if (isChildrenAllocated()) {
            return (getChildList().iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }

    }


    /**
     * <p>Remove the child <code>UIComponent</code> at the specified position
     * in the child list for this component.</p>
     *
     * @param index Position of the component to be removed
     *
     * @exception IndexOutOfBoundsException if the index is out of range
     *  ((index < 0) || (index &gt;= size()))
     */
    public void removeChild(int index) {

        UIComponent kid = getChild(index);
        getChildList().remove(index);
        if (kid instanceof UIComponentBase) { // FIXME -- Hmmmmm
            ((UIComponentBase) kid).setParent(null);
        }

    }


    /**
     * <p>Remove the child <code>UIComponent</code> from the child list
     * for this component.</p>
     *
     * @param component Child component to be removed
     *
     * @exception IllegalArgumentException if <code>component</code> is
     *  not a child of this component
     * @exception NullPointerException if <code>component</code> is null
     */
    public void removeChild(UIComponent component) {

        if (component == null) {
            throw new NullPointerException("removeChild");
        }
        if (containsChild(component)) {
            getChildList().remove(component);
            if (component instanceof UIComponentBase) { // FIXME -- Hmmmmm
                ((UIComponentBase) component).setParent(null);
            }
        } else {
            throw new IllegalArgumentException("removeChild");
        }

    }


    // ------------------------------------------ Request Event Handler Methods


    /**
     * <p>The set of {@link RequestEventHandler}s associated with this
     * <code>UIComponent</code>.</p>
     */
    private ArrayList handlers = null;


    /**
     * <p>Add a {@link RequestEventHandler} instance to the set associated with
     * this <code>UIComponent</code>.</p>
     *
     * @param handler The {@link RequestEventHandler} to add
     *
     * @exception NullPointerException if <code>handler</code>
     *  is null
     */
    public void addRequestEventHandler(RequestEventHandler handler) {

        if (handler == null) {
            throw new NullPointerException();
        }
        if (handlers == null) {
            handlers = new ArrayList();
        }
        handlers.add(handler);

    }


    /**
     * <p>Clear any {@link RequestEventHandler}s that have been registered for
     * processing by this component.</p>
     */
    public void clearRequestEventHandlers() {

        handlers = null;

    }


    /**
     * <p>Return an <code>Iterator</code> over the {@link RequestEventHandler}s
     * associated with this <code>UIComponent</code>.</p>
     */
    public Iterator getRequestEventHandlers() {

        if (handlers != null) {
            return (handlers.iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }

    }


    /**
     * <p>Remove a {@link RequestEventHandler} instance from the set associated with
     * this <code>UIComponent</code>, if it was previously associated.
     * Otherwise, do nothing.</p>
     *
     * @param handler The {@link RequestEventHandler} to remove
     */
    public void removeRequestEventHandler(RequestEventHandler handler) {

        if (handlers != null) {
            handlers.remove(handler);
        }

    }


    // ----------------------------------------------------- Validators Methods


    /**
     * <p>The set of {@link Validator}s associated with this
     * <code>UIComponent</code>.</p>
     */
    private ArrayList validators = null;


    /**
     * <p>Add a {@link Validator} instance to the set associated with
     * this <code>UIComponent</code>.</p>
     *
     * @param validator The {@link Validator} to add
     *
     * @exception NullPointerException if <code>validator</code>
     *  is null
     */
    public void addValidator(Validator validator) {

        if (validator == null) {
            throw new NullPointerException();
        }
        if (validators == null) {
            validators = new ArrayList();
        }
        validators.add(validator);

    }


    /**
     * <p>Clear any {@link Validator}s that have been registered for
     * processing by this component.</p>
     */
    public void clearValidators() {

        validators = null;

    }


    /**
     * <p>Return an <code>Iterator</code> over the {@link Validator}s
     * associated with this <code>UIComponent</code>.</p>
     */
    public Iterator getValidators() {

        if (validators != null) {
            return (validators.iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }

    }


    /**
     * <p>Remove a {@link Validator} instance from the set associated with
     * this <code>UIComponent</code>, if it was previously associated.
     * Otherwise, do nothing.</p>
     *
     * @param validator The {@link Validator} to remove
     */
    public void removeValidator(Validator validator) {

        if (validators != null) {
            validators.remove(validator);
        }

    }


    // ------------------------------------------- Lifecycle Processing Methods


    /**
     * <p>Decode the current state of this <code>UIComponent</code> from the
     * request contained in the specified {@link FacesContext}, and attempt
     * to convert this state information into an object of the required type
     * for this component.  If conversion is successful, save the resulting
     * object via a call to <code>setValue()</code>, and set the
     * <code>valid</code> property of this component to <code>true</code>.
     * If conversion is not successful:</p>
     * <ul>
     * <li>Save the state information in such a way that encoding
     *     can reproduce the previous input (even though it was syntactically
     *     or semantically incorrect)</li>
     * <li>Add an appropriate conversion failure error message by calling
     *     <code>context.addMessage()</code>.</li>
     * <li>Set the <code>valid</code> property of this comonent
     *     to <code>false</code>.</li>
     * </ul>
     *
     * <p>During decoding, events may be enqueued for later processing
     * (by this component or some other component),  by calling
     * <code>addRequestEvent()</code> on the associated {@link FacesContext}.
     * </p>
     *
     * <p>The default behavior of this method is to delegate to the
     * associated {@link Renderer} if there is one; otherwise this method
     * simply returns <code>true</code>.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @return <code>true</code> if conversion was successful, or
     *  <code>false</code> if conversion failed
     *
     * @exception IOException if an input/output error occurs during decoding
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public boolean decode(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            RenderKitFactory rkFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = rkFactory.getRenderKit
                (context.getRequestTree().getRenderKitId());
            Renderer renderer = renderKit.getRenderer(rendererType);
            boolean result = renderer.decode(context, this);
            setValid(result);
            return(result);
        } else {
            setValid(true);
            return (true);
        }

    }


    /**
     * <p>Render the beginning of the current state of this
     * <code>UIComponent</code> to the response contained in the specified
     * {@link FacesContext}.  If the conversion attempted in a previous call
     * to <code>decode()</code> for this component failed, the state
     * information saved during execution of <code>decode()</code> should be
     * utilized to reproduce the incorrect input.  If the conversion was
     * successful, or if there was no previous call to <code>decode()</code>,
     * the value to be displayed should be acquired by calling
     * <code>currentValue()</code>, and rendering the value as appropriate.
     * </p>
     *
     * <p>The default behavior of this method is to delegate to the
     * associated {@link Renderer} if there is one; otherwise this method
     * does nothing.</p>
     *
     * @param context FacesContext for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeBegin(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            RenderKitFactory rkFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = rkFactory.getRenderKit
                (context.getResponseTree().getRenderKitId());
            Renderer renderer = renderKit.getRenderer(rendererType);
            renderer.encodeBegin(context, this);
        }

    }


    /**
     * <p>Render the child components of this component, following the
     * rules described for <code>encodeBegin()</code> to acquire the
     * appropriate value to be rendered.  This method will only be called
     * if the <code>rendersChildren</code> property is <code>true</code>.</p>
     *
     * <p>The default behavior of this method is to delegate to the
     * associated {@link Renderer} if there is one; otherwise this method
     * does nothing.</p>
     *
     * @param context FacesContext for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeChildren(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            RenderKitFactory rkFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = rkFactory.getRenderKit
                (context.getResponseTree().getRenderKitId());
            Renderer renderer = renderKit.getRenderer(rendererType);
            renderer.encodeChildren(context, this);
        }

    }


    /**
     * <p>Render the ending of the current state of this
     * <code>UIComponent</code>, following the rules described for
     * <code>encodeBegin()</code> to acquire the appropriate value
     * to be rendered.</p>
     *
     * <p>The default behavior of this method is to delegate to the
     * associated {@link Renderer} if there is one; otherwise this method
     * does nothing.</p>
     *
     * @param context FacesContext for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            RenderKitFactory rkFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = rkFactory.getRenderKit
                (context.getResponseTree().getRenderKitId());
            Renderer renderer = renderKit.getRenderer(rendererType);
            renderer.encodeEnd(context, this);
        }

    }


    /**
     * <p>Process an individual event queued to this <code>UIComponent</code>.
     * The default implementation does nothing, but can be overridden by
     * subclasses of <code>UIComponent</code>.  Return <code>true</code> if
     * lifecycle processing should proceed directly to the <em>Render
     * Response</em> phase once all events have been processed for all
     * components, or <code>false</code> for the normal lifecycle flow.</p>
     *
     * @param context FacesContext for the request we are processing
     * @param event Event to be processed against this component
     *
     * @exception NullPointerException if <code>context</code> or
     *  <code>event</code> is <code>null</code>
     */
    public boolean processEvent(FacesContext context, FacesEvent event) {

        if ((context == null) || (event == null)) {
            throw new NullPointerException();
        }
        return (false); // Default implementation does nothing

    }


    /**
     * <p>Process all events queued to this <code>UIComponent</code>, by
     * calling the <code>processEvent()</code> method of the component itself,
     * followed by the <code>processEvent()</code> method of each registered
     * {@link RequestEventHandler}, for each of them.
     * Normally, component writers will not override this method -- it is
     * primarily available for use by tools.  Component writers should
     * override the <code>processEvent()</code> method instead.</p>
     *
     * <p>Return <code>true</code> if the <code>processEvent()</code> method
     * call for any queued event returned <code>true</code>; otherwise,
     * return <code>false</code>.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public boolean processEvents(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        boolean result = false;
        Iterator events = context.getRequestEvents(this);
        while (events.hasNext()) {
            FacesEvent event = (FacesEvent) events.next();
            if (processEvent(context, event)) {
                result = true;
            }
            if (handlers != null) {
                Iterator handlers = getRequestEventHandlers();
                while (handlers.hasNext()) {
                    RequestEventHandler handler =
                        (RequestEventHandler) handlers.next();
                    if (handler.processEvent(context, this, event)) {
                        result = true;
                    }
                }
            }
        }
        return (result);

    }


    /**
     * <p>Perform all validations for this component, by performing the
     * following algorithm.</p>
     * <ul>
     * <li>Call the <code>validate()</code> method on this component,
     *     to perform any self-validation that has been defined.</li>
     * <li>Call the <code>validate()</code> method on each registered
     *     {@link Validator} for this component.</li>
     * <li>If any of the calls to a <code>validate()</code> method performed
     *     in the preceding steps returns <code>false</code>, set the
     *     <code>valid</code> property of this component to <code>false</code>.
     *     Otherwise, set the <code>valid</code> property of this component
     *     to <code>true</code>.</li>
     * <ul>
     *
     * <p>Normally, component writers will not overwrite this method -- it is
     * primarily available for use by tools.  Component writers should
     * override the <code>validate()</code> method instead.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processValidators(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        boolean result = true;
        if (!validate(context)) {
            result = false;
        }
        if (this.validators != null) {
            Iterator validators = getValidators();
            while (validators.hasNext()) {
                Validator validator = (Validator) validators.next();
                if (!validator.validate(context, this)) {
                    result = false;
                }
            }
        }
        setValid(result);

    }


    /**
     * <p>If this <code>UIComponent</code> has a non-null
     * <code>modelReference</code> property, use the
     * <code>setModelValue()</code> method of the specified
     * {@link FacesContext} to update the corresponding model data
     * from the current value of this component.  This method can be
     * overridden by custom component classes when more complex update
     * logic is required.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception FacesException if an error occurs during execution
     *  of the <code>setModelValue()</code> method
     * @exception IllegalArgumentException if the <code>modelReference</code>
     *  property has invalid syntax for an expression
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void updateModel(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!isValid()) {
            return;
        }
        String modelReference = getModelReference();
        if (modelReference == null) {
            return;
        }
        context.setModelValue(modelReference, getValue());

    }


    /**
     * <p>Perform any correctness checks that this component wishes to perform
     * on itself.  This method will be called, along with calls to all
     * {@link Validator}s registered on this component, during the
     * <em>Process Validations</em> phase of the request processing lifecycle.
     * If errors are encountered, appropriate <code>Message</code> instances
     * should be added to the {@link FacesContext} for the current request,
     * and <code>setValid(false)</code> should be called.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @return <code>true</code> if all validations performed by this
     *  method passed successfully, or <code>false</code> if one or more
     *  validations performed by this method failed
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public boolean validate(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        return (true); // Default implementation simply returns true

    }


}
