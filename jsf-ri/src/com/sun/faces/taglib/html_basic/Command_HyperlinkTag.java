/*
 * $Id: Command_HyperlinkTag.java,v 1.15 2002/01/24 00:35:24 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Command_HyperlinkTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.util.Util;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.UICommand;
import javax.faces.UIForm;
import javax.faces.ObjectManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>Command_HyperlinkTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Command_HyperlinkTag.java,v 1.15 2002/01/24 00:35:24 rogerk Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Command_HyperlinkTag extends TagSupport
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

    private String target = null;
    private String image = null;
    private String text = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Command_HyperlinkTag() {
        super();
        // ParameterCheck.nonNull();
        this.init();
    }

    protected void init() {
        // super.init();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Process the start of this tag.
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        Assert.assert_it( pageContext != null );
        ObjectManager objectManager = (ObjectManager) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( objectManager != null );
        RenderContext renderContext = 
            (RenderContext)objectManager.get(pageContext.getSession(),
            Constants.REF_RENDERCONTEXT);
        Assert.assert_it( renderContext != null );

        UICommand uiCommand = null;

        // 1. if we don't have an "id" generate one
        //
        if (id == null) {
            String gId = Util.generateId(); 
            setId(gId);
        }

        // 2. Get or create the component instance.
        //
        uiCommand = (UICommand) objectManager.get(pageContext.getRequest(), id);
        if ( uiCommand == null ) {
            uiCommand = new UICommand();
            objectManager.put(pageContext.getRequest(), getId(), uiCommand);
        }

        uiCommand.setId(getId());
        uiCommand.setAttribute("target", getTarget());
        uiCommand.setAttribute("image", getImage());
        uiCommand.setAttribute("text", getText());

        // 3. Render the component.
        //
        try {
            uiCommand.setRendererType("HyperlinkRenderer");
            uiCommand.render(renderContext);
        } catch (java.io.IOException e) {
            throw new JspException("Problem rendering component: "+
                e.getMessage());
        } catch (FacesException f) {
            throw new JspException("Problem rendering component: "+
                f.getMessage());
        }
        return (EVAL_BODY_INCLUDE);
    }

    /**
     * End Tag Processing
     */
    public int doEndTag() throws JspException{

        Assert.assert_it( pageContext != null );
        // get ObjectManager from ServletContext.
        ObjectManager objectManager = (ObjectManager)pageContext.getServletContext().
                 getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( objectManager != null );
        RenderContext renderContext = 
            (RenderContext)objectManager.get(pageContext.getSession(),
            Constants.REF_RENDERCONTEXT);
        Assert.assert_it( renderContext != null );

//PENDING(rogerk)can we eliminate this extra get if component is instance
//variable? If so, threading issue?
//
        UICommand uiCommand = (UICommand) objectManager.get(pageContext.getRequest(), getId());
        Assert.assert_it( uiCommand != null );

        // Complete the rendering process
        //
        try {
            uiCommand.renderComplete(renderContext);
        } catch (java.io.IOException e) {
            throw new JspException("Problem completing rendering: "+
                e.getMessage());
        } catch (FacesException f) {
            throw new JspException("Problem completing rendering: "+
                f.getMessage());
        }

        return EVAL_PAGE;
    }

    /**
     * Tag cleanup method.
     */
    public void release() {

        super.release();

        target = null;
        image = null;
        text = null;
    }


} // end of class Command_HyperlinkTag
