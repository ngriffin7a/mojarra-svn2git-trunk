/*
 * $Id: JspTLD12Generator.java,v 1.6 2007/04/27 22:02:50 ofung Exp $
 */

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

package com.sun.faces.generate;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.io.IOException;

import com.sun.faces.config.beans.AttributeBean;
import com.sun.faces.config.beans.ComponentBean;
import com.sun.faces.config.beans.DescriptionBean;
import com.sun.faces.config.beans.PropertyBean;
import com.sun.faces.config.beans.RendererBean;

/**
 * <p>A <code>JspTldGenerator</code> specific for JSP 1.2 style TLDs.</p>
 */
public class JspTLD12Generator extends JspTLDGenerator {

    // 1.1 spec still says 1.0 for tlib-version
    private static final String JSF_TLIB_VERSION = "1.0";
    private static final String JSP_VERSION = "1.2";


    // ------------------------------------------------------------ Constructors

    public JspTLD12Generator(PropertyManager propManager) {

        super(propManager);

    }

    // ------------------------------------------------------- Protected Methods

    /**
     * The XML header for the TLD file.
     */
    protected void writeProlog() throws IOException {
        super.writeProlog();
        writeDocType();
    }


    /**
     * The description element for this TLD.
     */
    protected void writeTldDescription() throws IOException {

        writer.startElement("taglib");
        writer.writeComment(
            "============== Tag Library Description Elements =============");

        writer.startElement("tlib-version");
        writer.writeText(JSF_TLIB_VERSION);
        writer.closeElement();

        writer.startElement("jsp-version");
        writer.writeText(JSP_VERSION);
        writer.closeElement();

        writer.startElement("short-name");
        writer.writeText(
            propManager.getProperty(PropertyManager.TAGLIB_SHORT_NAME));
        writer.closeElement();

        writer.startElement("uri");
        writer.writeText(propManager.getProperty(PropertyManager.TAGLIB_URI));
        writer.closeElement();

        String description =
            propManager.getProperty(PropertyManager.TAGLIB_DESCRIPTION);
        if (description != null && description.length() > 0) {
            writer.startElement("description");
            writer.writeText(description);
            writer.closeElement();
        }

    } // end tldDescription


    /**
     * The tags for this TLD.
     */
    protected void writeTags() throws IOException {
        writer.writeComment(
            "===================== HTML 4.0 basic tags ======================");

        Map<String,ComponentBean> componentsByComponentFamily =
            GeneratorUtil.getComponentFamilyComponentMap(configBean);
        Map<String, ArrayList<RendererBean>> renderersByComponentFamily =
            GeneratorUtil.getComponentFamilyRendererMap(configBean,
                propManager.getProperty(PropertyManager.RENDERKIT_ID));
        String targetPackage =
            propManager.getProperty(PropertyManager.TARGET_PACKAGE);

	for (Map.Entry entry : renderersByComponentFamily.entrySet()) {

            String componentFamily = (String)entry.getKey();
            List<RendererBean> renderers = (List<RendererBean>)entry.getValue();
            for (Iterator<RendererBean> rendererIter = renderers.iterator();
                 rendererIter.hasNext();) {

                RendererBean renderer = rendererIter.next();
                String rendererType = renderer.getRendererType();
                writer.startElement("tag");

                String tagName = makeTldTagName(
                    GeneratorUtil.stripJavaxFacesPrefix(componentFamily),
                    GeneratorUtil.stripJavaxFacesPrefix(rendererType));

                if (tagName == null) {
                    throw new IllegalStateException(
                        "Could not determine tag name");
                }

                writer.startElement("name");
                writer.writeText(tagName);
                writer.closeElement();


                if (GeneratorUtil.makeTagClassName(
                    GeneratorUtil.stripJavaxFacesPrefix(componentFamily),
                    GeneratorUtil.stripJavaxFacesPrefix(rendererType)) ==
                    null) {
                    throw new IllegalStateException(
                        "Could not determine tag class name");
                }

                writer.startElement("tag-class");
                writer.writeText(targetPackage + '.' +
                    GeneratorUtil.makeTagClassName(GeneratorUtil.stripJavaxFacesPrefix(componentFamily),
                        GeneratorUtil.stripJavaxFacesPrefix(rendererType)));
                writer.closeElement();

                writer.startElement("tei-class");
                writer.writeText(getTeiClass(tagName));
                writer.closeElement();

                writer.startElement("body-content");
                writer.writeText(getBodyContent(tagName));
                writer.closeElement();

                DescriptionBean description = renderer.getDescription("");
                if (description != null) {
                    String descriptionText = description.getDescription().trim();

                    if (descriptionText != null) {
                        writer.startElement("description");
                        StringBuffer sb = new StringBuffer();
                        sb.append("<![CDATA[");
                        sb.append(descriptionText);
                        sb.append("]]>\n");
                        writer.writeText(sb.toString());
                        writer.closeElement();
                    }
                }


                // Generate tag attributes
                //

                // Component Properties first...
                //
                ComponentBean component = componentsByComponentFamily.get(componentFamily);

                PropertyBean[] properties = component.getProperties();
                PropertyBean property;

                for (int i = 0, len = properties.length; i < len; i++) {
                    if (null == (property = properties[i])) {
                        continue;
                    }
                    if (!property.isTagAttribute()) {
                        continue;
                    }

                    writer.startElement("attribute");


                    writer.startElement("name");
                    writer.writeText(property.getPropertyName());
                    writer.closeElement();

                    writer.startElement("required");
                    writer.writeText(property.isRequired() ?
                                     Boolean.TRUE.toString() :
                                     Boolean.FALSE.toString());
                    writer.closeElement();

                    writer.startElement("rtexprvalue");
                    writer.writeText(getRtexprvalue(tagName,
                        property.getPropertyName()));
                    writer.closeElement();

                    description = property.getDescription("");
                    if (description != null) {
                        String descriptionText =
                        description.getDescription().trim();

                        if (descriptionText != null) {
                            writer.startElement("description");
                            StringBuffer sb = new StringBuffer();
                            sb.append("<![CDATA[");
                            sb.append(descriptionText);
                            sb.append("]]>\n");
                            writer.writeText(sb.toString());
                            writer.closeElement();
                        }
                    }

                    writer.closeElement(); // closes attribute element above

                } // END property FOR loop


                // Renderer Attributes Next...
                //
                AttributeBean[] attributes = renderer.getAttributes();
                AttributeBean attribute;
                for (int i = 0, len = attributes.length; i < len; i++) {
                    if (null == (attribute = attributes[i])) {
                        continue;
                    }
                    if (!attribute.isTagAttribute()) {
                        continue;
                    }
                    if (attributeShouldBeExcluded(renderer,
                        attribute.getAttributeName())) {
                        continue;
                    }

                    writer.startElement("attribute");

                    writer.startElement("name");
                    writer.writeText(attribute.getAttributeName());
                    writer.closeElement();

                    writer.startElement("required");
                    writer.writeText(attribute.isRequired() ?
                                     Boolean.TRUE.toString() :
                                     Boolean.FALSE.toString());
                    writer.closeElement();

                    writer.startElement("rtexprvalue");
                    writer.writeText(getRtexprvalue(tagName,
                        attribute.getAttributeName()));
                    writer.closeElement();

                    description = attribute.getDescription("");
                    if (description != null) {
                        String descriptionText =
                        description.getDescription().trim();

                        if (descriptionText != null) {
                            writer.startElement("description");
                            StringBuffer sb = new StringBuffer();
                            sb.append("<![CDATA[");
                            sb.append(descriptionText);
                            sb.append("]]>\n");
                            writer.writeText(sb.toString());
                            writer.closeElement();
                        }
                    }

                    writer.closeElement(); // closes attribute element above

                } // END attribute FOR loop

                // SPECIAL: "Binding" needs to exist on every tag..
                writer.startElement("attribute");

                writer.startElement("name");
                writer.writeText("binding");
                writer.closeElement();

                writer.startElement("required");
                writer.writeText("false");
                writer.closeElement();

                writer.startElement("rtexprvalue");
                writer.writeText("false");
                writer.closeElement();

                writer.startElement("description");
                writer.writeText(
                    "The value binding expression linking this component to a property in a backing bean");

                // close the most recent description, attribute, and tag
                // elements
                writer.closeElement(3);

            }
        }

        //Include any other tags defined in the optional tag definition file.
        //These might be tags that were not picked up because they have no renderer
        //- for example "column".
        String tagDef = loadOptionalTags();
        if (tagDef != null) {
            writer.write(tagDef);
        }

    }


    // --------------------------------------------------------- Private Methods

    /**
     * TLD DOCTYPE
     */
    private void writeDocType() throws IOException {
        writer.write("\n<!DOCTYPE taglib\n");
        writer.write("PUBLIC \"-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN\"\n");
        writer.write("\"http://java.sun.com/dtd/web-jsptaglibrary_1_2.dtd\">\n\n");
    }
}
