/*
 * $Id: MockEnumeration.java,v 1.1 2005/10/18 17:47:54 edburns Exp $
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

package com.sun.faces.mock;


import java.util.Enumeration;
import java.util.Iterator;


/**
 * <p>General purpose <code>Enumeration</code> wrapper around an
 * <code>Iterator</code> specified to our controller.</p>
 */

public class MockEnumeration implements Enumeration {


    public MockEnumeration(Iterator iterator) {
        this.iterator = iterator;
    }


    protected Iterator iterator;


    public boolean hasMoreElements() {
        return (iterator.hasNext());
    }


    public Object nextElement() {
        return (iterator.next());
    }


}
