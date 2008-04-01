/*
 * $Id: FormTag.java,v 1.10 2001/11/29 01:54:36 rogerk Exp $
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

// FormTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.WForm;

import javax.servlet.http.*;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.faces.ObjectTable;

/**
 *
 *  <B>FormTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FormTag.java,v 1.10 2001/11/29 01:54:36 rogerk Exp $
 * 
 *
 */

public class FormTag extends TagSupport
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

    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public FormTag()
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
     * Renders Form's start tag
     */
    public int doStartTag() throws JspException{

        // PENDING(visvan) use tagext class to validate attributes.
        // get ObjectTable from ServletContext.
        Assert.assert_it( pageContext != null );
        ObjectTable ot = (ObjectTable) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTTABLE);
        Assert.assert_it( ot != null );
        RenderContext rc = (RenderContext)ot.get(pageContext.getSession(), 
                Constants.REF_RENDERCONTEXT);
        Assert.assert_it( rc != null );

        if ( name != null ) {
            Renderer renderer = getRenderer(rc);
            WForm c = (WForm) ot.get(pageContext.getRequest(), name);
            if (c == null) {
                c = createComponent(rc);
                // PENDING (visvan ) scope should be an attribute of the tag
                // for now using the default scope, request
                ot.put(pageContext.getRequest(), name, c);
            }
            try {
               renderer.renderStart(rc, c);
            } catch (java.io.IOException e) {
                throw new JspException("Problem rendering Form component: "+
                        e.getMessage());
            } catch (FacesException f) {
                throw new JspException("Problem rendering component: "+
                f.getMessage());
            }
        
        }
        return(EVAL_BODY_INCLUDE);
    }

    public Renderer getRenderer(RenderContext rc ) throws JspException{
        Renderer renderer = null;
   
        RenderKit renderKit = rc.getRenderKit();
        if (renderKit == null) {
            throw new JspException("Can't determine RenderKit!");
        }
        try {
            String class_name = "com.sun.faces.renderkit.html_basic.FormRenderer";
            renderer = renderKit.getRenderer(class_name);
        } catch (FacesException e) {
            e.printStackTrace();
            throw new JspException("FacesException " + e.getMessage());
        }

        if (renderer == null) {
            throw new JspException(
                "Could not determine 'renderer' for Form component");
        }
        return renderer;	
    }

    /**
     * Creates a Form component and sets renderer specific
     * properties.
     */
    protected WForm createComponent(RenderContext rc) {
        WForm c = new WForm();
        // set renderer specific properties 
        c.setAttribute(rc, "name", name);
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
            System.out.println("Couldn't find Form Renderer class");
        }
        String packageName = (renderclass.getPackage()).getName();
        if ( packageName == null ) {
            System.out.println("Package name is null");
        }
        String full_name = packageName + "." + class_name;
        return full_name;
    } */

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
     * Renders the Form's end Tag
     */
    public int doEndTag() throws JspException{

        Assert.assert_it( pageContext != null );
        // get ObjectTable from ServletContext.
        ObjectTable ot = (ObjectTable)pageContext.getServletContext().
                 getAttribute(Constants.REF_OBJECTTABLE);
        Assert.assert_it( ot != null );
        RenderContext rc = (RenderContext)ot.get(pageContext.getSession(), 
                Constants.REF_RENDERCONTEXT);
        Assert.assert_it( rc != null );

        WForm c = (WForm) ot.get(pageContext.getRequest(), name);
        if ( c != null ) {
            Renderer form_renderer = getRenderer(rc);
            try {
                form_renderer.renderComplete(rc, c);
            } catch (java.io.IOException e) {
                throw new JspException("Problem rendering Form component: "+
                        e.getMessage());
            }catch (FacesException e) {
                e.printStackTrace();
                throw new JspException("FacesException " + e.getMessage());
            }
        }
        return(EVAL_PAGE);
    }

} // end of class FormTag
