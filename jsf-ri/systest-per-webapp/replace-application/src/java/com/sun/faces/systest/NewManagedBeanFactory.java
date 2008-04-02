/*
 * $Id: NewManagedBeanFactory.java,v 1.1 2005/08/24 16:13:35 edburns Exp $
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

package com.sun.faces.systest;

import com.sun.faces.spi.ManagedBeanFactory;
import com.sun.faces.spi.ManagedBeanFactoryWrapper;

/**
 *
 * @author edburns
 */
public class NewManagedBeanFactory extends ManagedBeanFactoryWrapper {
    
    private ManagedBeanFactory parent = null;
    
    public static ManagedBeanFactory mostRecentMBF = null;
    
    /** Creates a new instance of NewManagedBeanFactory */
    public NewManagedBeanFactory(ManagedBeanFactory old) {
        this.parent = old;
        mostRecentMBF = this;
    }
    
    public ManagedBeanFactory getWrapped() {
        return parent;
    }
    
}
