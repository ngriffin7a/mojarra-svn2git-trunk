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
 * 
 * Copyright (c) 2004, Mikael Grev, MiG InfoCom AB. (base64 @ miginfocom . com)
 * All rights reserved.
 */

package com.sun.faces.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * <p>Provides a mechanism to accept and Base64 encode bytes into
 * chars which will be flushed to the provided writer as the
 * internal buffer fills.</p> 
 */
public class Base64OutputStreamWriter extends OutputStream {

    /** 
     * The buffer where data is stored. 
     */
    private byte[] buf;

    /**
     * <p>The Base64 encoded bytes as chars; essentially the output
     * buffer</p>
     */
    private char[] chars;

    /** 
     * The number of valid bytes in the buffer. 
     */
    private int count;

    /**
     * <p>The current position within <code>chars</code>
     */
    private int encCount;

    /**
     * <p>Tracks the total number of characters written.</p>
     */
    private int totalCharsWritten;

    /**
     * The writer we'll flush the bytes to instead of growing
     * the array.
     */
    private Writer writer;

    private static final char[] CA =
          "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
                .toCharArray();


    /**
     * Creates a new byte array output stream, with a buffer capacity of
     * the specified size, in bytes.
     *
     * @param size   the initial size.
     * @param writer the writer we'll flush to once
     *               we reach our capacity
     *
     * @throws IllegalArgumentException if size is negative.
     */
    public Base64OutputStreamWriter(int size, Writer writer) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: "
                                               + size);
        }
       
        buf = new byte[size];
        chars = new char[size];
        totalCharsWritten = 0;
        this.writer = writer;
    }

    /**
     * Writes the specified byte to this byte array output stream.
     *
     * @param b the byte to be written.
     */
    public void write(int b) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array
     * starting at offset <code>off</code> to this byte array output stream.
     *
     * @param b   the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     */
    @Override
    public void write(byte b[], int off, int len) throws IOException {
        if ((off < 0) || (off > b.length) || (len < 0) ||
            ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }

        if ((count + len) > buf.length) {
            encodePendingBytes(false);
        }
        System.arraycopy(b, off, buf, count, len);
        count += len;
    }

    /**
     * <p>Calls through to {@link #write(byte[], int, int)}/</p> 
     * @param b the bytes to write
     * @throws IOException if an error occurs
     */
    @Override 
    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    /** Closing <tt>Base64OutputStreamWriter</tt> does nothing. */
    @Override
    public void close() throws IOException {

    }

    /**
     * <p>Encodes the remaining bytes and flushes the <code>char[]</code>
     * to the wrapped <code>Writer</code>.</p>
     *
     * @throws IOException
     */
    public void finish() throws IOException {
        encodePendingBytes(true);        
    }


    /**
     * @return the total number of characters written
     */
    public int getTotalCharsWritten() {
        return totalCharsWritten;
    }


    /**
     * <p>Base64 encode any bytes found in <code>buf</code> and 
     * store the result as characters in <code>chars</code>.  This method
     * will automatically write the contents of <code>chars</code> when
     * necessary.
     * @param pad flag to signal we're finalizing the encoding processes.
     * @throws IOException if an error occurs 
     */
    private void encodePendingBytes(boolean pad) throws IOException {
        int eLen = (count / 3) * 3;         // Length of even 24-bits.
        for (int s = 0; s < eLen;) {
            // Copy next three bytes into lower 24 bits of int, paying attension to sign.
            int i = (buf[s++] & 0xff) << 16
                    | (buf[s++] & 0xff) << 8
                    | (buf[s++] & 0xff);

            if ((encCount + 4) > chars.length) {
                // we're full, so write the encoded chars
                // and reset the pointer
                drainCharBuffer();
            }
            // Encode the int into four chars               
            chars[encCount++] = CA[(i >>> 18) & 0x3f];
            chars[encCount++] = CA[(i >>> 12) & 0x3f];
            chars[encCount++] = CA[(i >>> 6) & 0x3f];
            chars[encCount++] = CA[i & 0x3f];

        }

        int left = (count - eLen);

        if (!pad) {
            // push the non-encoded bytes to the beginning of the byte array
            // and set count to the end of those bytes
            System.arraycopy(buf, eLen, buf, 0, left);
            count = left;
        } else {
            drainCharBuffer();
            // pad if necessary
            if (left > 0) {
                // Prepare the int
                int i = ((buf[eLen] & 0xff) << 10) | (left == 2
                                                      ? ((buf[count - 1]
                                                          & 0xff) << 2)
                                                      : 0);

                // write last four chars
                writer.write(CA[i >> 12]);
                writer.write(CA[(i >>> 6) & 0x3f]);
                writer.write(left == 2 ? CA[i & 0x3f] : '=');
                writer.write('=');
            }
        }
    }

    /**
     * <p>Write the contents of <code>chars</code> to the
     * wrapped <code>Writer</code> and reset <code>encCount</code>
     * to zero.</p>
     * @throws IOException if an error occurs
     */
    private void drainCharBuffer() throws IOException {
        writer.write(chars, 0, encCount);
        totalCharsWritten += encCount;
        encCount = 0;
    }

    // Test Case: com.sun.faces.io.TestIO

} // END Base64OutputStreamWriter