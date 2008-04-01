/*
 * $Id: TextEntry_TextAreaTag.java,v 1.3 2001/11/17 01:33:01 edburns Exp $
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

// TextEntry_TextAreaTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.WTextEntry;

import javax.servlet.http.*;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 *
 *  <B>TextEntry_TextAreaTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextEntry_TextAreaTag.java,v 1.3 2001/11/17 01:33:01 edburns Exp $
 * 
 *
 */

public class TextEntry_TextAreaTag extends BodyTagSupport
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
    private String name = null;
    private String value = null;
    private String rows = null;
    private String cols = null;
    private String wrap = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public TextEntry_TextAreaTag()
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

    /**
     * Renders TextEntry_TextAreaTag's start tag and its attributes.
     */
    public int doStartTag() throws JspException{
        // check if the tag is already created and exists in the 
        // JSP pool. If not, create form component.
        
        // PENDING(visvan) figure out the scope. For now use session scope
        // PENDING(visvan) use tagext class to validate attributes.
        if ( name != null ) {
            RenderContext rc = (RenderContext) pageContext.getSession().
                    getAttribute("renderContext");
            Assert.assert_it( rc != null );
            Renderer text_renderer = getRenderer(rc);
            HttpSession session = pageContext.getSession();
            WTextEntry c = (WTextEntry) session.getAttribute(name);
            if (c == null) {
                c = createComponent(rc);
                addToScope(c, session);
            }	
            try {
                text_renderer.renderStart(rc, c);
            } catch (java.io.IOException e) {
                throw new JspException("Problem rendering TextEntry component: "+
                        e.getMessage());
            }
        }
        return(EVAL_BODY_TAG);
    }

    /**
     * Gets the TextEntry_TextAreaTag's body if specified.
     */
    public int doAfterBody() throws JspException {
        String text = getBodyContent().getString();
        HttpSession session = pageContext.getSession();
        WTextEntry c = (WTextEntry) session.getAttribute(name);
        if ( c != null ) {
           c.setValue(text);
        }
        return SKIP_BODY;
    }

    /**
     * Renders the Form's end Tag
     */
    public int doEndTag() throws JspException{
        HttpSession session = pageContext.getSession();
        WTextEntry c = (WTextEntry) session.getAttribute(name);
        if ( c != null ) {
            RenderContext rc = (RenderContext)session.
                    getAttribute("renderContext");
            Assert.assert_it( rc != null );
            Renderer text_renderer = getRenderer(rc);
            try {
                text_renderer.renderEnd(rc, c);
            } catch (java.io.IOException e) {
                throw new JspException("Problem rendering TextArea component: "+
                        e.getMessage());
            }
        }
        return(EVAL_PAGE);
    }

    /**
     * Returns the appropriate renderer for the tag
     *
     * @param rc RenderContext to obtain renderkit
     */
    public Renderer getRenderer(RenderContext rc ) throws JspException{

        Renderer renderer = null;
        RenderKit renderKit = rc.getRenderKit();
        if (renderKit == null) {
            throw new JspException("Can't determine RenderKit!");
        }
        try {
            String class_name = "com.sun.faces.renderkit.html_basic.TextAreaRenderer";
            renderer = renderKit.getRenderer(class_name);
        } catch (FacesException e) {
            e.printStackTrace();
            throw new JspException("FacesException " + e.getMessage());
        }

        if (renderer == null) {
            throw new JspException(
                "Could not determine 'renderer' for TextEntry component");
        }
        return renderer;	
    }

    /**
     * Creates a TextEntry component and sets renderer specific
     * properties.
     */
    protected WTextEntry createComponent(RenderContext rc) {
        WTextEntry c = new WTextEntry();
        // set renderer specific properties 
        c.setAttribute(rc, "name", name);
        c.setAttribute(rc, "rows", rows);
        c.setAttribute(rc, "cols", cols);
        c.setAttribute(rc, "wrap", wrap);
        // set render independent attributes 
        return c;
    }

    /**
     * Figures out the name of the package to which the
     * class belongs.
     *
     * @param class_name name of the class
     * @return String package name of the class
     *
    protected String getRendererPackage(String class_name) {
        Class renderclass = null;
        System.out.println("class_name " + class_name);
        try {
            renderclass = Class.forName(class_name);
        } catch ( ClassNotFoundException e ) {
            System.out.println("Couldn't find Text Renderer class");
        }
        String packageName = (renderclass.getPackage()).getName();
        if ( packageName == null ) {
            System.out.println("Package name is null");
        }
        String full_name = packageName + "." + class_name;
        return full_name;
    } */

    /**
     * Adds the component to the specified scope.
     *
     * @param c component to add to scope.
     * @param scope scope to which the component is to be added
     * For now use session scope.
     *
     */
    void addToScope(WTextEntry c, HttpSession session) {
        session.setAttribute(name, c);
    }

    /**
     * Returns the value of the "name" attribute
     *
     * @return String value of "name" attribute
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the "name" attribute
     * @param name value of "name" attribute 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the value of the "value" attribute
     *
     * @return String value of "value" attribute
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets "value" attribute
     * @param value value of "value" attribute
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Sets the cols attribute
     * @param cols value of cols attribute
     */
    public void setCols(String cols) {
        this.cols = cols;
    }

    /**
     * Returns the value of cols attribute
     *
     * @return String value of cols attribute
     */
    public String getCols() {
        return this.cols;
    }

    /**
     * Sets  rows attribute
     * @param  rows value of row attribute
     */
    public void setRows(String rows) {
        this.rows = rows;
    }

   /**
     * Returns the value of rows attribute
     *
     * @return String value of rows attribute
     */
    public String getRows() {
        return this.rows;
    }

    /**
     * Sets  wrap attribute
     * @param  wrap value of wrap attribute
     */
    public void setWrap(String wrap) {
        this.wrap = wrap;
    }

   /**
     * Returns the value of wrap attribute
     *
     * @return String value of wrap attribute
     */
    public String getWrap() {
        return this.wrap;
    }


} // end of class TextEntry_TextAreaTag
