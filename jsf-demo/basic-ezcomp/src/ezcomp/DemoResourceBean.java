/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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

package ezcomp;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * Created by IntelliJ IDEA. User: rlubke Date: Oct 27, 2008 Time: 12:22:33 AM
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "demoBean", scope = "application", eager = true)
public class DemoResourceBean {

    private static final String DEMO_DESCRIPTOR = "/WEB-INF/demo.xml";

    private ArrayList<DemoBean> demoBeans;


    // ---------------------------------------------------------- Public Methods


    public Collection<DemoBean> getDemoBeans() {

        return demoBeans;

    }


    // --------------------------------------------------------- Private Methods


    private DocumentBuilder getBuilder() throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(false);
        return dbf.newDocumentBuilder();

    }


    private Document getDemoMetadata() throws Exception {

        FacesContext ctx = FacesContext.getCurrentInstance();
        DocumentBuilder builder = getBuilder();
        return builder.parse(ctx.getExternalContext().getResourceAsStream(DEMO_DESCRIPTOR));

    }


    @SuppressWarnings({"UnusedDeclaration"})
    @PostConstruct
    private void buildDemoMetaData()  {

        try {
            Document d = getDemoMetadata();
            demoBeans = new ArrayList<DemoBean>();
            NodeList demoElements = d.getElementsByTagName("demo");
            for (int i = 0, len = demoElements.getLength(); i < len; i++) {
                DemoBean b = processDemo(demoElements.item(i));
                if (b != null) {
                    demoBeans.add(b);
                }
            }
            demoBeans.trimToSize();
        } catch (Exception e) {
            throw new FacesException("Unable to initialize demo", e);
        }

    }


    private DemoBean processDemo(Node demoNode) {

        NodeList children = demoNode.getChildNodes();
        String name = null;
        List<DemoSourceInfo> sourceInfo = new ArrayList<DemoSourceInfo>();
        for (int i = 0, len = children.getLength(); i < len; i++) {
            Node n = children.item(i);
            if ("name".equals(n.getNodeName())) {
                name = getNodeText(n);
            } else if ("sources".equals(n.getNodeName())) {
                NodeList sources = n.getChildNodes();
                for (int j = 0, jlen = sources.getLength(); j < jlen; j++) {
                    Node s = sources.item(j);
                    DemoSourceInfo info = createDemoSourceInfo(s);
                    if (info != null) {
                        sourceInfo.add(createDemoSourceInfo(s));
                    }
                }
            }
        }

        if (name != null && !sourceInfo.isEmpty()) {
            return new DemoBean(name, sourceInfo);
        }
        return null;
    }


    private DemoSourceInfo createDemoSourceInfo(Node sourceInfo) {

        NodeList children = sourceInfo.getChildNodes();
        String label = null;
        String path = null;
        for (int i = 0, len = children.getLength(); i < len; i++) {
            Node n = children.item(i);
            if ("label".equals(n.getNodeName())) {
                label = getNodeText(n);
            } else if ("path".equals(n.getNodeName())) {
                path = getNodeText(n);
            }
        }

        if (label != null && path != null) {
            return new DemoSourceInfo(label, path);
        }
        return null;

    }


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


    // ---------------------------------------------------------- Nested Classes


    public static final class DemoBean {

        private List<DemoSourceInfo> sourceInfo;
        private String demoName;


        DemoBean(String demoName, List<DemoSourceInfo> sourceInfo) {

            this.demoName = demoName;
            this.sourceInfo = sourceInfo;

        }


        // ------------------------------------------------------ Public Methods


        public String getDemoName() {

            return demoName;

        }


        public List<DemoSourceInfo> getSourceInfo() {

            return sourceInfo;

        }


    } // END DemoBean


    public static final class DemoSourceInfo  {


        private String label;
        private String sourceFilePath;


        // -------------------------------------------------------- Constructors


        DemoSourceInfo(String label, String sourceFilePath) {

            this.label = label;
            this.sourceFilePath = sourceFilePath;

        }


        // ------------------------------------------------------ Public Methods


        public String getLabel() {

            return label;

        }


        public String getSourceFilePath() {

            return sourceFilePath;

        }


    } // END DemoSourceInfo

}
