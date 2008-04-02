/*
 * $Id: UIComponentTag.java,v 1.18 2003/10/03 17:38:12 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>{@link UIComponentTag} is the base class for all JSP custom
 * actions that correspond to user interface components in a page that is
 * rendered by JavaServer Faces.  Tags that need to process their tag bodies
 * should subclass {@link UIComponentBodyTag} instead.</p>
 */

public abstract class UIComponentTag implements Tag {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The request scope attribute under which an <code>Integer</code> value
     * describing the number of child identifiers that have been created
     * so far on this page.</p>
     */
    private static final String AUTO_ID_INDEX_PAGE_ATTR =
        "javax.faces.webapp.AUTO_INDEX";


    /**
     * <p>The request scope attribute under which a component tag stack
     * for the current request will be maintained.</p>
     */
    private static final String COMPONENT_TAG_STACK_ATTR =
        "javax.faes.webapp.COMPONENT_TAG_STACK";


    /**
     * <p>The {@link UIComponent} attribute under which we will store a
     * <code>List</code> of the component identifiers of child components
     * created on the previous generation of this page (if any).</p>
     */
    private static final String JSP_CREATED_COMPONENT_IDS =
        "javax.faces.webapp.COMPONENT_IDS";


    /**
     * <p>The {@link UIComponent} attribute under which we will store a
     * <code>List</code> of the facet names of facets created on the previous
     * generation of this page (if any).
     */
    private static final String JSP_CREATED_FACET_NAMES =
        "javax.faces.webapp.FACET_NAMES";
    
    /**
     * <p>The attribute name under which we will store all {@link UIComponent}
     * IDs of the current translation unit.</p>
     */ 
    private static final String GLOBAL_ID_VIEW = 
        "javax.faces.webapp.GLOBAL_ID_VIEW";

    /**
     * <p>The attribute name under which we will store the {@link FacesContext}
     * for this request.</p>
     */ 
    private static final String CURRENT_FACES_CONTEXT =
        "javax.faces.webapp.CURRENT_FACES_CONTEXT";
    
    
    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The {@link UIComponent} that is being encoded by this tag,
     * if any.</p>
     */
    private UIComponent component = null;


    /**
     * <p>The {@link FacesContext} for the request being processed, if any.
     * </p>
     */
    protected FacesContext context = null;


    /**
     * <p>Was a new component instance dynamically created when our
     * <code>findComponent()</code> method was called.</p>
     */
    private boolean created = false;


    /**
     * <p>The <code>Lst</code> of {@link UIComponent} ids created or located
     * by nested {@link UIComponentTag}s while processing the current
     * request.</p>
     */
    private List createdComponents = null;


    /**
     * <p>The <code>List</code> of facet names created or located by nested
     * {@link UIComponentTag}s while processing the current request.</p>
     */
    private List createdFacets = null;


    /**
     * <p>The JSP <code>PageContext</code> for the page we are embedded in.</p>
     */
    protected PageContext pageContext = null;


    /**
     * <p>The JSP <code>Tag</code> that is the parent of this tag.</p>
     */
    private Tag parent = null;
    
    
    // ------------------------------------------------------------- Attributes


    /**
     * <p>The component reference (if any) used to wire up this component
     * to a {@link UIComponent} property of a JavaBean class.</p>
     */
    private String componentRef = null;


    /**
     * <p>Set the component reference for our component.</p>
     *
     * @param componentRef The new component reference
     */
    public void setComponentRef(String componentRef) {

	this.componentRef = componentRef;

    }


    /**
     * <p>The component identifier for the associated component.</p>
     */
    protected String id = null;


    /**
     * <p>Set the component identifier for our component.</p>
     *
     * @param id The new component identifier
     */
    public void setId(String id) {

        this.id = id;

    }


    /**
     * <p>An override for the rendered attribute associated with our
     * {@link UIComponent}.</p>
     */
    protected boolean rendered = true;


    /**
     * <p>Flag indicating whether the <code>rendered</code> attribute was
     * set on this tag instance.</p>
     */
    protected boolean renderedSet = false;
    

    /**
     * <p>Set an override for the rendered attribute.</p>
     *
     * @param rendered The new value for rendered attribute
     */
    public void setRendered(boolean rendered) {

        this.rendered = rendered;
        this.renderedSet = true;

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type for the component that is or will be
     * bound to this tag.  This value can be passed to
     * {@link javax.faces.application.Application#createComponent} to create
     * the {@link UIComponent} instance for this tag.  Subclasses must
     * override this method to return the appropriate value.</p>
     */
    protected abstract String getComponentType();


    /**
     * <p>Return the {@link UIComponent} instance that is associated with
     * this tag instance.  This method is designed to be used by tags nested
     * within this tag, and only returns useful results between the
     * execution of <code>doStartTag()</code> and <code>doEndTag()</code>
     * on this tag instance.</p>
     */
    public UIComponent getComponent() {

        return (this.component);

    }


    /**
     * <p>Return <code>true</code> if we dynamically created a new component
     * instance during execution of this tag.  This method is designed to be
     * used by tags nested within this tag, and only returns useful results
     * between the execution of <code>doStartTag()</code> and
     * <code>doEndTag()</code> on this tag instance.</p>
     */
    public boolean getCreated() {

        return (this.created);

    }


    /**
     * <p>Locate and return the nearest enclosing {@link UIComponentTag} if any;
     * otherwise, return <code>null</code>.</p>
     *
     * @param context <code>PageContext</code> for the current page
     */
    public static UIComponentTag getParentUIComponentTag(PageContext context) {

        List list = (List) context.getAttribute(COMPONENT_TAG_STACK_ATTR,
                                                PageContext.REQUEST_SCOPE);
        if (list != null) {
            return ((UIComponentTag) list.get(list.size() - 1));
        } else {
            return (null);
        }

    }
    

    /**
     * <p>Return the <code>rendererType</code> property that selects the
     * <code>Renderer</code> to be used for encoding this component, or
     * <code>null</code> to ask the component to render itself directly.
     * Subclasses must override this method to return the appropriate value.
     * </p>
     */
    public abstract String getRendererType();


    // --------------------------------------------------------- Tag Properties


    /**
     * <p>Set the <code>PageContext</code> of the page containing this
     * tag instance.</p>
     *
     * @param pageContext The enclosing <code>PageContext</code>
     */
    public void setPageContext(PageContext pageContext) {

        this.pageContext = pageContext;

    }


    /**
     * <p>Return the <code>Tag</code> that is the parent of this instance.</p>
     */
    public Tag getParent() {

        return (this.parent);

    }


    /**
     * <p>Set the <code>Tag</code> that is the parent of this instance.</p>
     *
     * @param parent The new parent <code>Tag</code>
     */
    public void setParent(Tag parent) {

        this.parent = parent;

    }


    // ------------------------------------------------------------ Tag Methods


    /**
     * <p>Render the beginning of the {@link UIComponent} that is associated
     * with this tag (via the <code>id</code> attribute), by following these
     * steps.</p>
     * <ul>
     * <li>Ensure that an appropriate {@link ResponseWriter} is associated
     *     with the current {@link FacesContext}.  This ensures that encoded
     *     output from the components is routed through the
     *     <code>JspWriter</code> for the current page.</li>
     * <li>Locate the component (in the view) corresponding
     *     to this tag, creating a new one if necesary.</li>
     * <li>If this {@link UIComponentTag} is nested within the body of
     *     another {@link UIComponentTag}, call its <code>addChild</code>
     *     method to save the component identifier for the {@link UIComponent}
     *     that corresponds to this {@link UIComponentTag}.</li>
     * <li>Call <code>findComponent()</code> to locate the {@link UIComponent}
     *     instance associated wth this {@link UIComponentTag}, creating a new
     *     one if necessary.</li>
     * <li>Call the <code>encodeBegin()</code> method of the component,
     *     unless rendering is suppressed or our component renders its
     *     own children.</li>
     * </ul>
     *
     * <p>The flag value to be returned is acquired by calling the
     * <code>getDoStartValue()</code> method, which tag subclasses may
     * override if they do not want the default value.</p>
     *
     * @exception JspException if an error occurs
     */
    public int doStartTag() throws JspException {
        
        context = 
           (FacesContext) pageContext.getAttribute(CURRENT_FACES_CONTEXT);
        
        if (context == null) {
            context = FacesContext.getCurrentInstance();
            
            if (context == null) { // PENDING - i18n
                throw new JspException("Cannot find FacesContext");
            }
            
            // store the current FacesContext for use by other
            // UIComponentTags in the same page
            pageContext.setAttribute(CURRENT_FACES_CONTEXT, context);
        }        

        // Set up the ResponseWriter as needed
        setupResponseWriter();
        
        UIComponentTag parentTag = getParentUIComponentTag(pageContext);
        
        Map componentIds = null;
        if (parentTag == null) {
            // create the map if we're the top level UIComponentTag
            componentIds = new HashMap();
            pageContext.setAttribute(GLOBAL_ID_VIEW, componentIds);                        
        } else {
            componentIds = (Map) pageContext.getAttribute(GLOBAL_ID_VIEW);
        }
        
        // assert component ID uniqueness
        if (this.id != null) {
            if (componentIds.containsKey(this.id)) {
                // PENDING i18n                        
                throw new JspException(
                    new IllegalStateException("Duplicate component id: '" +
                        this.id + "', first used in tag: '" +
                        componentIds.get(this.id) + "'"));
            } else {
                componentIds.put(this.id, this.getClass().getName());
            }
        }

        // Locate the UIComponent associated with this UIComponentTag,
        // creating one if necessary
        component = findComponent(context);

        // Add to parent's list of created components or facets if needed
        
        if (parentTag != null) {
            if (getFacetName() == null) {
                parentTag.addChild(component);                
            } else {
                parentTag.addFacet(getFacetName());
            }
        }

        // Render the beginning of the component associated with this tag
        try {
            if (!isSuppressed() && !component.getRendersChildren()) {
                encodeBegin();
            }
        } catch (IOException e) {
            component = null;
            context = null;
            throw new JspException(e);
        }

        // Return the appropriate control value
        pushUIComponentTag();
        return (getDoStartValue());

    }


    /**
     * <p>Render the ending of the {@link UIComponent} that is associated
     * with this tag (via the <code>id</code> attribute), by following these
     * steps.</p>
     * <ul>
     * <li>Retrieve from the {@link UIComponent} the set of component ids
     *     of child components created by {@link UIComponentTag} instances
     *     the last time this page was processed (if any).  Compare it to
     *     the list of children created during this page processing pass,
     *     and remove all children present in the old list but not the new.
     *     Save the new list as a component attribute so that it gets saved
     *     as part of the component's state.</li>
     * <li>Retrieve from the {@link UIComponent} the set of facet names
     *     of facets created by {@link UIComponentTag} instances the last
     *     time this page was processed (if any).  Compare it to
     *     the list of facets created during this page processing pass,
     *     and remove all facets present in the old list but not the new.
     *     Save the new list as a component attribute so that it gets saved
     *     as part of the component's state.</li>
     * <li>If the <code>rendersChildren</code> property of this component is
     *     <code>true</code>, call the <code>encodeBegin()</code> method
     *     of this component.
     * <li>If the <code>rendersChildren</code> property of this component is
     *     <code>true</code>, call the <code>encodeChildren()</code> method
     *     of the component.</li>
     * <li>Call the <code>encodeEnd()</code> method of the component.</li>
     * <li>Release all references to the component, and pop it from
     *     the component stack for this response, removing the stack
     *     if this was the outermost component.</li>
     * </ul>
     *
     * <p>The flag value to be returned is acquired by calling the
     * <code>getDoEndValue()</code> method, which tag subclasses may
     * override if they do not want the default value.</p>
     *
     * @exception JspException if an error occurs
     */
    public int doEndTag() throws JspException {

        // Remove old children and facets as needed
        popUIComponentTag();
        removeOldChildren();
        removeOldFacets();

        // Render the children (if needed) and  end of the component
        // associated with this tag
        try {
            if (!isSuppressed()) {
                if (component.getRendersChildren()) {
                    encodeBegin();
                    encodeChildren();
                }
                encodeEnd();
            }
        } catch (IOException e) {
            throw new JspException(e);
        } finally {
            component = null;
            context = null;
        }

        // Return the appropriate control value
        created = false;
        return (getDoEndValue());

    }


    /**
     * <p>Release any resources allocated during the execution of this
     * tag handler.</p>
     */
    public void release() {

        this.parent = null;

	this.componentRef = null;
        this.id = null;
        this.created = false;
        this.rendered = true;
        this.renderedSet = false;
    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Delegate to the <code>encodeBegin()</code> method of our
     * corresponding {@link UIComponent}.  This method is called from
     * <code>doStartTag()</code>.  Normally, delegation occurs unconditionally;
     * however, this method is abstracted out so that advanced tags can
     * conditionally perform this call.
     *
     * @exception IOException if an input/output error occurs
     */
    protected void encodeBegin() throws IOException {

        component.encodeBegin(context);

    }


    /**
     * <p>Delegate to the <code>encodeChildren()</code> method of our
     * corresponding {@link UIComponent}.  This method is called from
     * <code>doStartTag()</code>.  Normally, delegation occurs unconditionally;
     * however, this method is abstracted out so that advanced tags can
     * conditionally perform this call.
     *
     * @exception IOException if an input/output error occurs
     */
    protected void encodeChildren() throws IOException {

        component.encodeChildren(context);

    }


    /**
     * <p>Delegate to the <code>encodeEnd()</code> method of our
     * corresponding {@link UIComponent}.  This method is called from
     * <code>doStartTag()</code>.  Normally, delegation occurs unconditionally;
     * however, this method is abstracted out so that advanced tags can
     * conditionally perform this call.
     *
     * @exception IOException if an input/output error occurs
     */
    protected void encodeEnd() throws IOException {

        component.encodeEnd(context);

    }


    /**
     * <p>Find and return the {@link UIComponent}, from the component
     * tree, that corresponds to this tag handler instance.  If there
     * is no such {@link UIComponent}, create one by calling
     * <code>createComponent()</code> or <code>createFacet()</code>,
     * and add it as a child or facet of the {@link UIComponent} associated
     * with our nearest enclosing {@link UIComponentTag}.  The process for
     * locating or creating the component is:
     * <ol>
     * <li>If we have previously located this component, return it.</li>
     * <li>Locate the parent component by looking for a parent
     *     {@link UIComponentTag} instance, and ask it for its component.
     *     If there is no parent {@link UIComponentTag} instance, this tag
     *     represents the root component, so get it from the current
     *     <code>Tree</code> and return it.</li>
     * <li>If this {@link UIComponentTag} instance has the
     *     <code>facetName</code> attribute set, ask the parent
     *     {@link UIComponent} for a facet with this name.  If not found,
     *     create one, call <code>overrideProperties() with the new
     *     component as a parameter, and register it under this name.  In either
     *     case, return the facet {@link UIComponent}.</li>
     * <li>Determine the component id to be assigned to the new
     *     component, as follows:  if this {@link UIComponentTag} has
     *     an <code>id</code> attribute set, use that value; otherwise,
     *     generate an identifier that is guaranteed to be the same for
     *     this {@link UIComponent} every time this page is processed
     *     (i.e. one based on the location of all {@link UIComponentTag}
     *     instances without an <code>id</code> attribute set).</li>
     * <li>Ask the parent {@link UIComponent} for a child this identifier.
     *     If not found, create one, call <code>overrideProperties()</code>
     *     with the new component as a parameter, and register it as a child
     *     with this identifier.  In either
     *     case, return the child {@link UIComponent}.</li>
     * </ol>
     */
    protected UIComponent findComponent(FacesContext context)
	throws JspException {

        // Step 1 -- Have we already found the relevant component?
        if (component != null) {
            return (component);
        }

        // Step 2 -- Identify the component that is, or will be, our parent
        UIComponentTag parentTag = getParentUIComponentTag(pageContext);
        UIComponent parentComponent = null;
        if (parentTag != null) {
            parentComponent = parentTag.getComponent();
        } else {
            parentComponent = context.getViewRoot();
            component = parentComponent;
            return (component);
        }

        // Step 3 -- Create or return a facet with the specified name (if any)
        String facetName = getFacetName();
        if (facetName != null) {
            component = (UIComponent)
                parentComponent.getFacets().get(facetName);
            if (component == null) {
                component = createFacet(context, parentComponent, facetName);
            }
            return (component);
        }

        // Step 4 -- Calculate the component identifier for this component
        String newId = this.id;
        if (newId == null) {
            newId = createId();
        }

        // Step 5 -- Create or return a child with the specified id
        component = getChild(parentComponent, newId);
        if (component == null) {
            component = createChild(context, parentComponent, newId);
        }
        return (component);

    }


    /**
     * <p>Return the flag value that should be returned from the
     * <code>doEnd()</code> method when it is called.  Subclasses
     * may override this method to return the appropriate value.</p>
     *
     * @exception JspException to cause <code>doEnd()</code> to
     *  throw an exception
     */
    protected int getDoEndValue() throws JspException {

        return (EVAL_PAGE);

    }


    /**
     * <p>Return the flag value that should be returned from the
     * <code>doStart()</code> method when it is called.  Subclasses
     * may override this method to return the appropriate value.</p>
     *
     * @exception JspException to cause <code>doStart()</code> to
     *  throw an exception
     */
    protected int getDoStartValue() throws JspException {

        return (EVAL_BODY_INCLUDE);

    }


    /**
     * <p>Return the facet name that we should be stored under, if any;
     * otherwise, return null (indicating that we will be a child component).
     * </p>
     */
    protected String getFacetName() {

        Tag parent = getParent();
        if (parent instanceof FacetTag) {
            return (((FacetTag) parent).getName());
        } else {
            return (null);
        }

    }


    /**
     * <p>Return <code>true</code> if rendering should be suppressed because
     * our component is a facet, or some parent component has been configured
     * with <code>getRendersChildren()</code> as true.</p>
     */
    protected boolean isSuppressed() {

        if (getFacetName() != null) {
            return (true);
        }
        UIComponent component = this.component.getParent();
        while (component != null) {
            if (component.getRendersChildren()) {
                return (true);
            }
            component = component.getParent();
        }
        return (false);

    }


    /**
     * <p>Override properties and attributes of the specified component,
     * if the corresponding properties of this tag handler instance were
     * explicitly set.  This method must be called <strong>ONLY</strong>
     * if the specified {@link UIComponent} was in fact created during
     * the execution of this tag handler instance, and this call will occur
     * <strong>BEFORE</strong> the {@link UIComponent} is added to
     * the view.</p>
     *
     * <p>Tag subclasses that want to support additional override properties
     * must ensure that the base class <code>overrideProperties()</code>
     * method is still called.  A typical implementation that supports
     * extra properties <code>foo</code> and <code>bar</code> would look
     * something like this:</p>
     * <pre>
     * protected void overrideProperties(UIComponent component) {
     *   super.overrideProperties(component);
     *   if (foo != null) {
     *     component.setAttribute("foo", foo);
     *   }
     *   if (bar != null) {
     *     component.setAttribute("bar", bar);
     *   }
     * }
     * </pre>
     *
     * <p>The default implementation overrides the following properties:</p>
     * <ul>
     * <li><code>componentId</code> - Set if a value for the
     *     <code>id</code> property is specified for
     *     this tag handler instance.</li>
     * <li><code>componentRef</code> - Set if a value for the
     *     <code>componentRef</code> property is specified for
     *     this tag handler instance.</li>
     * <li><code>rendered</code> - Set if a value for the
     *     <code>rendererd</code> property is specified for
     *     this tag handler instance.</li>
     * <li><code>rendererType</code> - Set if the <code>getRendererType()</code>
     *     method returns a non-null value.</li>
     * </ul>
     *
     * @param component {@link UIComponent} whose properties are to be
     *  overridden
     */
    protected void overrideProperties(UIComponent component) {

	if (componentRef != null) {
	    component.setComponentRef(componentRef);
	}
	if (id != null) {
	    component.setId(id);
	}
        if (renderedSet) {
            component.setRendered(rendered);
        }
	if (getRendererType() != null) {
	    component.setRendererType(getRendererType());
	}

    }


    /**
     * <p>Set up the {@link ResponseWriter} for the current response,
     * if this has not been done already.</p>
     */
    protected void setupResponseWriter() {

        ResponseWriter writer = context.getResponseWriter();
        if (writer == null) {
	    RenderKitFactory renderFactory = (RenderKitFactory)
		FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	    RenderKit renderKit = 
		renderFactory.getRenderKit
                (context.getViewRoot().getRenderKitId());
	    ServletResponse response = (ServletResponse)
		context.getExternalContext().getResponse();
            writer = 
		renderKit.createResponseWriter(new Writer() {
		    public void close() throws IOException {
			pageContext.getOut().close();
		    }
		    public void flush() throws IOException {
                        // PENDING(craigmcc) - causes problems with includes
			// pageContext.getOut().flush();
		    }
                    public void write(char cbuf) throws IOException {
			pageContext.getOut().write(cbuf);
		    }
		    public void write(char[] cbuf, int off, 
				      int len) throws IOException {
			pageContext.getOut().write(cbuf, off, len);
		    }
		    public void write(int c) throws IOException {
			pageContext.getOut().write(c);
		    }
		    public void write(String str) throws IOException {
			pageContext.getOut().write(str);
		    }
		    public void write(String str, int off, 
				      int len) throws IOException {
			pageContext.getOut().write(str, off, len);
		    }
		},
                                               null,
                                               response.getCharacterEncoding());
            context.setResponseWriter(writer);
        }

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Add the component identifier of the specified {@link UIComponent}
     * to the list of component identifiers created or located by nested
     * {@link UIComponentTag}s processing this request.</p>
     *
     * @param child New child whose identifier should be added
     */
    private void addChild(UIComponent child) {

        if (createdComponents == null) {
            createdComponents = new ArrayList();
        }
        createdComponents.add(child.getId());

    }


    /**
     * <p>Add the facet name of the specified facet to the list of
     * facet names created or located by nested {@link UIComponentTag}s
     * processing this request.</p>
     *
     * @param name Facet name to be added
     */
    private void addFacet(String name) {

        if (createdFacets == null) {
            createdFacets = new ArrayList();
        }
        createdFacets.add(name);

    }


    /**
     * <p>Create and return a new child component of the type returned by
     * calling <code>getComponentType()</code>.  If this {@link UIComponentTag}
     * has a non-null <code>componentType</code> attribute, this is done by
     * calling calling <code>getValue()</code> on a {@link ValueBinding} created
     * for the <code>componentRef</code> attribute's value.  If this returns
     * <code>null</code>, or there is no <code>componentRef</code> attribute
     * value, a new component is created and returned.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param parent Parent {@link UIComponent} for the new child
     * @param componentId Component identifier for the new child,
     *  or <code>null</code> for no explicit identifier
     */
    private UIComponent createChild(FacesContext context, UIComponent parent,
                                    String componentId) {

        UIComponent component = null;
        Application application = context.getApplication();
        if (componentRef != null) {
            ValueBinding binding = application.getValueBinding(componentRef);
            component = application.createComponent(binding, context,
                                                    getComponentType());
        } else {
            component = application.createComponent(getComponentType());
        }
        overrideProperties(component);
        component.setId(componentId);
        UIComponentTag parentTag = getParentUIComponentTag(pageContext);
        parent.getChildren().add(parentTag.getIndex(), component);
        created = true;
        return (component);

    }



    /**
     * <p>Create and return a new facet of the type returned by
     * calling <code>getComponentType()</code>.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param parent Parent {@link UIComponent} of the new facet
     * @param name Name of the new facet
     */
    private UIComponent createFacet(FacesContext context, UIComponent parent,
                                    String name) {

        UIComponent component =
            context.getApplication().createComponent(getComponentType());
        overrideProperties(component);
        parent.getFacets().put(name, component);
        created = true;
        return (component);

    }


    /**
     * <p>Generate and return a component identifier that includes a sequence
     * number reflecting the number of identifiers generated for this
     * particular page.</p>
     */
    private String createId() {

        Integer index =
            (Integer) pageContext.getAttribute(AUTO_ID_INDEX_PAGE_ATTR,
                                               PageContext.REQUEST_SCOPE);
        if (index == null) {
            index = new Integer(0);
        }
        index = new Integer(index.intValue() + 1);
        pageContext.setAttribute(AUTO_ID_INDEX_PAGE_ATTR,
                                 index,
                                 PageContext.REQUEST_SCOPE);
        return ("JSPid" + index);

    }


    /**
     * <p>Return a child with the specified component id from the specified
     * component, if any; otherwise, return <code>null</code>.</p>
     *
     * @param component {@link UIComponent} to be searched
     * @param componentId Component id to search for
     */
    private UIComponent getChild(UIComponent component, String componentId) {

        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (componentId.equals(kid.getId())) {
                return (kid);
            }
        }
        return (null);

    }


    /**
     * <p>Return the child list index to use for a new component to be created
     * by a nested {@link UIComponentTag} instance.</p>
     */
    private int getIndex() {

        if (createdComponents != null) {
            return (createdComponents.size());
        } else {
            return (0);
        }

    }


    /**
     * <p>Pop the top {@link UIComponentTag} instance off of our component tag
     * stack, deleting the stack if this was the last entry.</p>
     */
    private void popUIComponentTag() {

        List list = (List) pageContext.getAttribute(COMPONENT_TAG_STACK_ATTR,
                                                    PageContext.REQUEST_SCOPE);
        if (list != null) {
            list.remove(list.size() - 1);
            if (list.size() < 1) {
                pageContext.removeAttribute(COMPONENT_TAG_STACK_ATTR,
                                            PageContext.REQUEST_SCOPE);
            }
        }

    }


    /**
     * <p>Push the specified {@link UIComponentTag} instance onto our component
     * tag stack, creating a stack if necessary.</p>
     */
    private void pushUIComponentTag() {

        List list = (List) pageContext.getAttribute(COMPONENT_TAG_STACK_ATTR,
                                                    PageContext.REQUEST_SCOPE);
        if (list == null) {
            list = new ArrayList();
            pageContext.setAttribute(COMPONENT_TAG_STACK_ATTR, list,
                                     PageContext.REQUEST_SCOPE);
        }
        list.add(this);

    }


    /**
     * <p>Retrieve from the {@link UIComponent} corresponding to this tag
     * handler the list of all child component ids created by
     * {@link UIComponentTag} instances the previous time this tree was
     * rendered.  Compare it to the list of children created during this
     * page processing pass, and remove all children present on the old list
     * but not in the new list.  Save the list as a {@link UIComponent}
     * attribute so that it gets saved as part of the component's state.</p>
     */
    private void removeOldChildren() {

        // Remove old children that are no longer present
        List oldList =
            (List) component.getAttributes().get(JSP_CREATED_COMPONENT_IDS);
        if (oldList != null) {

            if (createdComponents != null) {

                // Components not in the new list need to be removed
                Iterator olds = oldList.iterator();
                while (olds.hasNext()) {
                    String old = (String) olds.next();
                    if (!createdComponents.contains(old)) {
                        UIComponent child = component.findComponent(old);
                        component.getChildren().remove(child);
                    }
                }

            } else {

                // All old components need to be removed
                Iterator olds = oldList.iterator();
                while (olds.hasNext()) {
                    String old = (String) olds.next();
                    UIComponent child = component.findComponent(old);
                    component.getChildren().remove(child);
                }

            }

        }

        // Save the current list as a component attribute
        if (createdComponents != null) {
            component.getAttributes().put(JSP_CREATED_COMPONENT_IDS,
                                          createdComponents);
        } else {
            component.getAttributes().remove(JSP_CREATED_COMPONENT_IDS);
        }
        createdComponents = null;

    }


    /**
     * <p>Retrieve from the {@link UIComponent} corresponding to this tag
     * handler the list of all facet names created by {@link UIComponentTag}
     * instances the previous time this tree was rendered.  Compare it to the
     * list of facets created during this page processing pass, and remove
     * all facets present on the old list but not in the new list.  Save the
     * list as a {@link UIComponent} attribute so that it gets saved as part
     * of the component's state.</p>
     */
    private void removeOldFacets() {

        // Remove old facets that are no longer present
        List oldList =
            (List) component.getAttributes().get(JSP_CREATED_FACET_NAMES);
        if (oldList != null) {

            if (createdFacets != null) {

                // Facets not in the new list need to be removed
                Iterator olds = oldList.iterator();
                while (olds.hasNext()) {
                    String old = (String) olds.next();
                    if (!createdFacets.contains(old)) {
                        component.getFacets().remove(old);
                    }
                }

            } else {

                // All old facets need to be removed
                Iterator olds = oldList.iterator();
                while (olds.hasNext()) {
                    String old = (String) olds.next();
                    component.getFacets().remove(old);
                }

            }

        }

        // Save the current list as a component attribute
        if (createdFacets != null) {
            component.getAttributes().put(JSP_CREATED_FACET_NAMES,
                                          createdFacets);
        } else {
            component.getAttributes().remove(JSP_CREATED_FACET_NAMES);
        }
        createdFacets = null;

    }


}
