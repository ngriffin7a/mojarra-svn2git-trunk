/*
 * $Id: Output_TextTag.java,v 1.29 2002/06/07 00:01:06 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Output_TextTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;

import com.sun.faces.taglib.FacesTag;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: Output_TextTag.java,v 1.29 2002/06/07 00:01:06 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Output_TextTag extends FacesTag
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

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public Output_TextTag()
{
    super();
}

//
// Class methods
//

// 
// Accessors
//

//
// General Methods
//

    public String getLocalRendererType() { return null; }

//
// Methods from TagSupport
// 


} // end of class Output_TextTag
