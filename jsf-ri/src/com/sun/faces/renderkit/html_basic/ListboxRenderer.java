/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * $Id: ListboxRenderer.java,v 1.17 2003/12/22 21:26:38 jvisvanathan Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */


package com.sun.faces.renderkit.html_basic;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

/**
 * <B>ListRenderer</B> is a class that renders the current value of 
 * <code>UISelectOne<code> or <code>UISelectMany<code> component as a list of 
 * options.
 */

public class ListboxRenderer extends MenuRenderer {
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

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public ListboxRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //
    protected void writeDefaultSize(ResponseWriter writer, int itemCount) 
          throws IOException {
        // If size not specified, default to number of items
        writer.writeAttribute("size", new Integer(itemCount), "size");
    }
} // end of class ListboxRenderer
