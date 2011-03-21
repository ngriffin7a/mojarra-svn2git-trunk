
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package com.sun.faces.application.resource;

import com.sun.faces.util.FacesLogger;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;



class ZipDirectoryEntryScanner {

    private static final Logger LOGGER = FacesLogger.RESOURCE.getLogger();
    private static final String prefix = "META-INF/resources";
    private static final int prefixLen = prefix.length();
    Map<String, Boolean> resourceLibraries;


    ZipDirectoryEntryScanner() {
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        Set<String> webInfLibJars = extContext.getResourcePaths("/WEB-INF/lib");
        resourceLibraries = new ConcurrentHashMap<String, Boolean>();
        ZipInputStream zis = null;
        ZipEntry ze = null;
        String entryName = null;
        for (String cur : webInfLibJars) {
            zis = new ZipInputStream(extContext.getResourceAsStream(cur));
            try {
                while (null != (ze = zis.getNextEntry())) {
                    entryName = ze.getName();
                    if (entryName.startsWith(prefix)) {
                        if (prefixLen < entryName.length()) {
                            entryName = entryName.substring(prefixLen + 1);
                            if (!entryName.endsWith("/")) {
                                // Assume this code is only reached if the zip entry
                                // is NOT a 'directory' entry.
                                int i = entryName.lastIndexOf("/");
                                if (-1 != i) {
                                    entryName = entryName.substring(0, i);
                                    if (!resourceLibraries.containsKey(entryName)) {
                                        resourceLibraries.put(entryName, Boolean.TRUE);
                                    }
                                }
                            }
                            
                        }
                    }
                }

            } catch (IOException ioe) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, "Unable to inspect resource library " + cur, ioe);
                }
            }
        }

        // remove the optional local prefix entries
        Iterator<String> iter = resourceLibraries.keySet().iterator();
        String cur;
        while (iter.hasNext()) {
            cur = iter.next();
            if (cur.contains("/")) {
                iter.remove();
            }
        }
    }

    boolean libraryExists(String libraryName) {
        return resourceLibraries.containsKey(libraryName);
    }



}
