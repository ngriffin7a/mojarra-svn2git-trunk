/*
 * $Id: TaglibGenTask.java,v 1.3 2004/12/13 19:07:48 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.ant;

import org.apache.tools.ant.BuildException;

/**
 * <p>Task to create a JSP tags.</p>
 */
public class TaglibGenTask extends AbstractGeneratorTask {

    private static final String GENERATOR_CLASS =
        "com.sun.faces.generate.HtmlTaglibGenerator";


    // ---------------------------------------------------------- Public Methods


    public void execute() throws BuildException {

        setGeneratorClass(GENERATOR_CLASS);

        super.execute();

    } // END execute

}
