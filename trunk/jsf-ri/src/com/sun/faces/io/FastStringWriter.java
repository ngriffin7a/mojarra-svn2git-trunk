/*
 * $Id: FastStringWriter.java,v 1.3 2006/03/29 23:03:45 rlubke Exp $
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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.io;

import java.io.IOException;
import java.io.Writer;

/**
 * <p>This is based on {@link java.io.StringWriter} but backed by a
 * {@link StringBuilder} instead.</p>
 * <p/>
 * <p>This class is not thread safe.</p>
 */
public class FastStringWriter extends Writer {

    private StringBuilder builder;

    // ------------------------------------------------------------ Constructors

    /**
     * <p>Constructs a new <code>FastStringWriter</code> instance
     * using the default capacity of <code>16</code>.</p>
     */
    public FastStringWriter() {
        builder = new StringBuilder();
    }

    /**
     * <p>Constructs a new <code>FastStringWriter</code> instance
     * using the specified <code>initialCapacity</code>.</p>
     *
     * @param initialCapacity specifies the initial capacity of the buffer
     *
     * @throws IllegalArgumentException if initialCapacity is less than zero
     */
    public FastStringWriter(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException();
        }
        builder = new StringBuilder(initialCapacity);
    }

    // ----------------------------------------------------- Methods from Writer

    /**
     * <p>Write a portion of an array of characters.</p>
     *
     * @param cbuf Array of characters
     * @param off  Offset from which to start writing characters
     * @param len  Number of characters to write
     *
     * @throws IOException
     */
    public void write(char cbuf[], int off, int len) throws IOException {
        if ((off < 0) || (off > cbuf.length) || (len < 0) ||
            ((off + len) > cbuf.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        builder.append(cbuf, off, len);
    }

    /**
     * <p>This is a no-op.</p>
     *
     * @throws IOException
     */
    public void flush() throws IOException {
    }

    /**
     * <p>This is a no-op.</p>
     *
     * @throws IOException
     */
    public void close() throws IOException {
    }

    // ---------------------------------------------------------- Public Methods

    /**
     * Write a string.
     *
     * @param str String to be written
     */
    public void write(String str) {
        write(str, 0, str.length());
    }

    /**
     * Write a portion of a string.
     *
     * @param str A String
     * @param off Offset from which to start writing characters
     * @param len Number of characters to write
     */
    public void write(String str, int off, int len) {
        builder.append(str.substring(off, off + len));
    }

    /**
     * Return the <code>StringBuilder</code> itself.
     *
     * @return StringBuilder holding the current buffer value.
     */
    public StringBuilder getBuffer() {
        return builder;
    }

    /** @return the buffer's current value as a string. */
    public String toString() {
        return builder.toString();
    }

}
