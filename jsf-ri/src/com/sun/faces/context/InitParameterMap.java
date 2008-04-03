/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.context;

import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;

/**
 * @see javax.faces.context.ExternalContext#getInitParameterMap()  
 */
public class InitParameterMap extends BaseContextMap<String> {

    private final ServletContext servletContext;


    // ------------------------------------------------------------ Constructors


    public InitParameterMap(ServletContext newServletContext) {
        servletContext = newServletContext;
    }


    // -------------------------------------------------------- Methods from Map


    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        String keyString = key.toString();
        return servletContext.getInitParameter(keyString);
    }


    @Override
    public Set<Map.Entry<String,String>> entrySet() {
        return Collections.unmodifiableSet(super.entrySet());
    }


    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }


    @Override
    public Collection<String> values() {
        return Collections.unmodifiableCollection(super.values());
    }


    @Override
    public boolean containsKey(Object key) {
        return (servletContext.getInitParameter(key.toString()) != null);
    }


    @Override
    public boolean equals(Object obj) {
        return !(obj == null ||
                 !(obj.getClass()
                   == ExternalContextImpl
                       .theUnmodifiableMapClass)) && super.equals(obj);
    }


    @Override
    public int hashCode() {
        int hashCode = 7 * servletContext.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }


    // --------------------------------------------- Methods from BaseContextMap


    protected Iterator<Map.Entry<String,String>> getEntryIterator() {
        return new EntryIterator(servletContext.getInitParameterNames());
    }


    protected Iterator<String> getKeyIterator() {
        return new KeyIterator(servletContext.getInitParameterNames());
    }


    protected Iterator<String> getValueIterator() {
        return new ValueIterator(servletContext.getInitParameterNames());
    }

} // END InitParameterMap
