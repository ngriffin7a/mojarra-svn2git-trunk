/* 
 * $Id: ViewHandlerResponseWrapper.java,v 1.3 2005/08/22 22:10:08 ofung Exp $ 
 */ 


/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */


// ViewHandlerResponseWrapper.java 

package com.sun.faces.application;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * <p>This class is used by {@link javax.faces.application.ViewHandler#createView} to obtain the
 * text that exists after the &lt;f:view&gt; tag.</p>
 */

public class ViewHandlerResponseWrapper extends HttpServletResponseWrapper
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

    ByteArrayServletOutputStream basos = null;

    PrintWriter pw = null;
    CharArrayWriter caw = null;

//
// Constructors and Initializers    
//

    public ViewHandlerResponseWrapper(HttpServletResponse wrapped) {
	super(wrapped);


    }

//
// Class methods
//

//
// General methods
// 

    public boolean isBytes() {
	return (null != basos);
    }

    public boolean isChars() {
	return (null != caw);
    }

    public byte [] getBytes() {
	byte [] result = null;
	if (null != basos) {
	    result = basos.toByteArray();
	}
	return result;
    }
    
    public char [] getChars() {
	char [] result = null;
	if (null != caw) {
	    result = caw.toCharArray();
	}
	return result;
    }

    public String toString() {
	String result = null;
	if (null != caw) {
	    result = caw.toString();
	}
	else if (null != basos) {
	    result = basos.toString();
	}
	return result;
    }

    public void flushContentToWrappedResponse() throws IOException {
	ServletResponse wrapped = this.getResponse();
	if (null != caw) {
	    wrapped.getWriter().print(caw.toCharArray());
	    pw.flush();
	    caw.reset();
	}
	else if (null != basos) {
	    wrapped.getOutputStream().write(basos.toByteArray());
	    basos.flush();
	    basos.resetByteArray();
	}
	    
    }

//
// Methods from ServletResponse
//

    public ServletOutputStream getOutputStream() throws IOException {
	if (null == basos) {
	    basos = new ByteArrayServletOutputStream();
	}
	return basos;
    }

    public PrintWriter getWriter() throws IOException {
	if (null == pw) {
	    caw = new CharArrayWriter();
	    pw = new PrintWriter(caw);
	}
	    
	return pw;
    }


    //
    // Inner clases
    // 

    static class ByteArrayServletOutputStream extends ServletOutputStream {
	private ByteArrayOutputStream baos = null;

	public ByteArrayServletOutputStream() {
	    baos = new ByteArrayOutputStream();
	}
	
	public void write(int n) {
	    baos.write(n);
	}

	/**
	 * <p>It's important to not expose this as reset.</p>
	 */

	public void resetByteArray() {
	    baos.reset();
	}
	
	public byte[] toByteArray() {
	    return baos.toByteArray();
	}

	public String toString() {
	    return baos.toString();
	}

	public String toString(String enc) {
	    String result = null;
	    try {
		result = baos.toString(enc);
	    }
	    catch (UnsupportedEncodingException usee) {
	    }
	    return result;
	}
    }


} // end of class ViewHandlerResponseWrapper
