/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 * <p>
 * Wrapper around the <code>/WEB-INF/faces-config.xml</code>, if present,
 * to expose information relevant to the intialization of the runtime.
 * </p>
*/
public class WebInfFacesConfigInfo {


    private static final String ABSOLUTE_ORDERING = "absolute-ordering";
    private static final String NAME = "name";
    private static final String OTHERS = "others";

    private double version;
    private boolean exists;
    private boolean metadataComplete;
    private List<String> absoluteOrdering;


    // -------------------------------------------------------- Constructors


    /**
     * Creates a new <code>WebInfFacesConfig</code> document based
     * on the provided <code>Document</code>.  If the <code>Document</code>
     * does not represent the <code>WEB-INF/faces-config.xml</code> the
     * {@link #exists()} method will return <code>false</code>
     *
     * @param document document representing the <code>/WEB-INF/faces-config.xml</code>
     */
    public WebInfFacesConfigInfo(Document document) {

        exists = isWebinfFacesConfig(document);
        if (exists) {
            version = getVersion(document);
            if (isVersionGreaterOrEqual(2.0)) {
                extractOrdering(document);
            }
        }
        metadataComplete = isMetadataComplete(document);

    }


    // ------------------------------------------------------ Public Methods


    /**
     * @param version version to check
     * @return <code>true</code> if <code>version</code> is greater or
     *  equal to the version of the <code>/WEB-INF/faces-config.xml</code>
     */
    public boolean isVersionGreaterOrEqual(double version) {

        return (this.version >= version);

    }


    /**
     * @return <code>true</code> if the <code>Document</code> provided at
     *  construction time represents the <code>/WEB-INF/faces-config.xml</code>.
     */
    public boolean exists() {

        return exists;

    }


    /**
     * @return <code>true</code> if the <code>Document</code> provided at
     *  construction time represents the <code>/WEB-INF/faces-config.xml and is
     *  metadata complete.
     */
    public boolean isMetadataComplete() {

        return metadataComplete;

    }


    /**
     * @return a <code>List</code> of document names that in the order that
     *  they should be processed.  The presense of the keyword "others" indicates
     *  all documents not explicitly referenced by name in the list should be
     *  places in the final parsing order at same location.  If there are multiple
     *  documents that aren't named and the others element is present, the
     *  order that these documents are inserted into the final list is unspecified
     *  at this time.
     */
    public List<String> getAbsoluteOrdering()  {

        return ((absoluteOrdering != null)
                ? Collections.unmodifiableList(absoluteOrdering)
                : null);

    }


    // ----------------------------------------------------- Private Methods


    /**
     * @param document document representing <code>WEB-INF/faces-config.xml</code>
     * @return return the value of the version attribute of the provided document.
     *  If no version attribute is specified, assume 1.1.
     */
    private double getVersion(Document document) {

        String version = document.getDocumentElement()
              .getAttributeNS(document.getNamespaceURI(), "version");
        if (version != null && version.length() > 0) {
            return Double.parseDouble(version);
        } else {
            return 1.1d;
        }

    }


    /**
     * @param document the <code>Document</code> to inspect
     * @return <code>true</code> if the document represents the
     *         <code>/WEB-INF/faces-config.xml</code>
     */
    private boolean isWebinfFacesConfig(Document document) {

        String marker = document.getDocumentElement().getAttribute(ConfigManager.WEB_INF_MARKER);
        return (marker != null && marker.length() > 0);

    }


    private boolean isMetadataComplete(Document document) {

        if (exists) {
            if (isVersionGreaterOrEqual(2.0)) {
                String metadataComplete = document.getDocumentElement().getAttributeNS(document.getNamespaceURI(), "metadata-complete");
                return ((metadataComplete != null) ? Boolean.valueOf(metadataComplete) : false);
            } else {
                // not a 2.0 application, so annotation processing will not occur
                return true;
            }
        }
        // no faces-config.xml so assume it's not metadata-complete; process annotations
        return false;

    }


    private void extractOrdering(Document document) {

        Element documentElement = document.getDocumentElement();
        String namespace = documentElement.getNamespaceURI();

        NodeList orderingElements =
              documentElement.getElementsByTagNameNS(namespace, ABSOLUTE_ORDERING);

        if (orderingElements.getLength() > 0) {
            // according to the schema there, should be only one
            if (orderingElements.getLength() > 1) {
                throw new IllegalStateException("Multiple 'absolute-ordering' elements found within WEB-INF/faces-config.xml");
            }
            Node absoluteOrderingNode = orderingElements.item(0);
            NodeList children = absoluteOrderingNode.getChildNodes();
            absoluteOrdering = new ArrayList<String>(children.getLength());
            for (int i = 0, len = children.getLength(); i < len; i++) {
                Node n = children.item(i);
                if (NAME.equals(n.getLocalName())) {
                    absoluteOrdering.add(getNodeText(n));
                } else if (OTHERS.equals(n.getLocalName())) {
                    if (absoluteOrdering.contains("others")) {
                        throw new IllegalStateException("'absolute-ordering' element defined with multiple 'others' child elements found within WEB-INF/faces-config.xml");
                    }
                    absoluteOrdering.add("others");
                }
            }
        }
    }


    /**
     * Return the textual content, if any, of the provided <code>Node</code>.
     */
    private String getNodeText(Node node) {

        String res = null;
        if (node != null) {
            res = node.getTextContent();
            if (res != null) {
                res = res.trim();
            }
        }

        return ((res != null && res.length() != 0) ? res : null);

    }

} // END WebInfFacesConfigInfo
