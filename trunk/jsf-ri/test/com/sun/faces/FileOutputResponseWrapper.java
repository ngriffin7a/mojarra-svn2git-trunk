/*
 * $Id: FileOutputResponseWrapper.java,v 1.8 2004/02/26 20:33:59 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FileOutputResponseWrapper.java

package com.sun.faces;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The sole purpose of <B>FileOutputResponseWrapper</B> is to wrap an
 * ServletResponse and change its writer object  so that
 * output can be directed to a file.  <P>
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FileOutputResponseWrapper.java,v 1.8 2004/02/26 20:33:59 eburns Exp $
 */

public class FileOutputResponseWrapper extends HttpServletResponseWrapper {

//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    protected PrintWriter out = null;
    public static String FACES_RESPONSE_FILENAME = "FacesResponse.txt";

// Attribute Instance Variables


// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public FileOutputResponseWrapper(HttpServletResponse toWrap) {
        super(toWrap);
        try {
            FileOutputResponseWriter.initializeFacesResponseRoot();
            File file = new File(FACES_RESPONSE_FILENAME);
            FileOutputStream fs = new FileOutputStream(file);
            out = new PrintWriter(fs);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void flushBuffer() throws IOException {
        out.flush();
        out.close();
    }


//
// Class methods
//

//
// Methods from ServletResponse 
//

    public PrintWriter getWriter() {
        return out;
    }


} // end of class FileOutputResponseWrapper


