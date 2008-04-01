/*
 * $Id: Command_ButtonTag.java,v 1.7 2001/11/21 22:32:39 visvan Exp $
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

// Command_ButtonTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.WCommand;
import javax.faces.WForm;
import javax.faces.ObjectTable;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>Command_ButtonTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Command_ButtonTag.java,v 1.7 2001/11/21 22:32:39 visvan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Command_ButtonTag extends TagSupport {
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

    private String image = null;
    private String name = null;
    private String label = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Command_ButtonTag()
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name= name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Process the start of this tag.
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        Assert.assert_it( pageContext != null );
        ObjectTable ot = (ObjectTable) pageContext.getServletContext().
                getAttribute("objectTable");
        Assert.assert_it( ot != null );
        RenderContext renderContext = (RenderContext)ot.get(pageContext.getSession(),
                "renderContext");
        Assert.assert_it( renderContext != null );

        WCommand wCommand = (WCommand) ot.get(pageContext.getRequest(), name);
        if ( wCommand == null ) {
            wCommand = new WCommand();
            ot.put(pageContext.getRequest(), name, wCommand);
        }

        // 2. set tag attributes into the instance..
        //
        wCommand.setAttribute(renderContext, "name", getName());
        wCommand.setAttribute(renderContext, "image", getImage());
        wCommand.setAttribute(renderContext, "label", getLabel());
 
        // 3. find the parent (WForm), and add WCommand instance as
        // a child.
        // wForm.add(...
        //

        // 4. place back in namespace..
        //

        // 5. Obtain "Renderer" instance from the "RenderKit
        //
        Renderer renderer = null;

        RenderKit renderKit = renderContext.getRenderKit();
        if (renderKit == null) {
            throw new JspException("Can't determine RenderKit!");
        }

        try {
            renderer = renderKit.getRenderer(
                "com.sun.faces.renderkit.html_basic.ButtonRenderer");
        } catch (FacesException e) {
            throw new JspException(
                "FacesException!!! " + e.getMessage());
        } 
             
        if (renderer == null) {
            throw new JspException(
                "Could not determine 'renderer' for component");
        }
            
        // 6. Render the good stuff...
        //
        try {
            renderer.renderStart(renderContext, wCommand); 
        } catch (java.io.IOException e) {
            throw new JspException("Problem rendering component: "+
                e.getMessage());
        } catch (FacesException f) {
            throw new JspException("Problem rendering component: "+
                f.getMessage());
        }
         
        return (EVAL_BODY_INCLUDE);
    }
 
} // end of class Command_ButtonTag
