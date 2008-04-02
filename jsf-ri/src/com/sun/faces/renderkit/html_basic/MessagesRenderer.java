/*
 * $Id: MessagesRenderer.java,v 1.17 2004/10/12 14:39:52 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// MessagesRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.Iterator;

/**
 * <p><B>MessagesRenderer</B> handles rendering for the Messages<p>.
 *
 * @version $Id
 */

public class MessagesRenderer extends HtmlBasicRenderer {

    //
    // Prviate/Protected Constants
    //
    private static final Log log = LogFactory.getLog(MessagesRenderer.class);
   
    //
    // Methods From Renderer
    //

    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }


    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        Iterator messageIter = null;
        FacesMessage curMessage = null;
        ResponseWriter writer = null;

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (log.isTraceEnabled()) {
            log.trace("End encoding component " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component "
                          + component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        writer = context.getResponseWriter();
        assert (writer != null);

        // String clientId = ((UIMessages) component).getFor();
        String clientId = null; // PENDING - "for" is actually gone now
        // if no clientId was included
        if (clientId == null) {
            // and the author explicitly only wants global messages
            if (((UIMessages) component).isGlobalOnly()) {
                // make it so only global messages get displayed.
                clientId = "";
            }
        }

        //"for" attribute optional for Messages
        messageIter = getMessageIter(context, clientId, component);
        assert (messageIter != null);

        String layout = (String) component.getAttributes().get("layout");
        boolean wroteTable = false;

        //Add style and class attributes to table. If layout attribute is not
        //present or layout is list just do the spans in a linear fashion.
        if ((layout != null) && (layout.equals("table"))) {
            writer.startElement("table", component);
            writeIdAttributeIfNecessary(context, writer, component);
            wroteTable = true;
        }

        boolean showSummary = ((UIMessages)component).isShowSummary();
        boolean showDetail = ((UIMessages)component).isShowDetail();
        String style = (String) component.getAttributes().get("style");
        String styleClass = (String) component.getAttributes().get(
                    "styleClass");

        while (messageIter.hasNext()) {
            curMessage = (FacesMessage) messageIter.next();

            String
                summary = null,
                detail = null,
                severityStyle = null,
                severityStyleClass = null;

            // make sure we have a non-null value for summary and
            // detail.
            summary = (null != (summary = curMessage.getSummary())) ?
                summary : "";
            detail = (null != (detail = curMessage.getDetail())) ?
                detail : "";


            if (curMessage.getSeverity() == FacesMessage.SEVERITY_INFO) {
                severityStyle =
                    (String) component.getAttributes().get("infoStyle");
                severityStyleClass = (String)
                    component.getAttributes().get("infoClass");
            } else if (curMessage.getSeverity() == FacesMessage.SEVERITY_WARN) {
                severityStyle =
                    (String) component.getAttributes().get("warnStyle");
                severityStyleClass = (String)
                    component.getAttributes().get("warnClass");
            } else if (curMessage.getSeverity() == FacesMessage.SEVERITY_ERROR) {
                severityStyle =
                    (String) component.getAttributes().get("errorStyle");
                severityStyleClass = (String)
                    component.getAttributes().get("errorClass");
            } else if (curMessage.getSeverity() == FacesMessage.SEVERITY_FATAL) {
                severityStyle =
                    (String) component.getAttributes().get("fatalStyle");
                severityStyleClass = (String)
                    component.getAttributes().get("fatalClass");
            }

            // if we have style and severityStyle
            if ((style != null) && (severityStyle != null)) {
                // severityStyle wins
                style = severityStyle;
            }
            // if we have no style, but do have severityStyle
            else if ((style == null) && (severityStyle != null)) {
                // severityStyle wins
                style = severityStyle;
            }

            // if we have styleClass and severityStyleClass
            if ((styleClass != null) && (severityStyleClass != null)) {
                // severityStyleClass wins
                styleClass = severityStyleClass;
            }
            // if we have no styleClass, but do have severityStyleClass
            else if ((styleClass == null) && (severityStyleClass != null)) {
                // severityStyleClass wins
                styleClass = severityStyleClass;
            }

            //Done intializing local variables. Move on to rendering.

            if (wroteTable) {
                writer.startElement("tr", component);
                writer.startElement("td", component);
            }

            boolean wroteSpan = false;

            if (styleClass != null || style != null) {
                writer.startElement("span", component);
                if (!wroteTable) {
                    writeIdAttributeIfNecessary(context, writer, component);
                }
                wroteSpan = true;
                if (null != styleClass) {
                    writer.writeAttribute("class", styleClass, "styleClass");
                }
                if (style != null) {
                    writer.writeAttribute("style", style, "style");
                }
            }

            Object tooltip = component.getAttributes().get("tooltip");
            boolean isTooltip = false;
            if (tooltip instanceof Boolean) {
                //if it's not a boolean can ignore it
                isTooltip = ((Boolean) tooltip).booleanValue();
            }

            boolean wroteTooltip = false;
            if (showSummary && showDetail && isTooltip) {

                if (!wroteSpan) {
                    writer.startElement("span", component);
                }
                writer.writeAttribute("title", summary, "title");
                writer.flush();
                writer.writeText("\t", null);
                wroteTooltip = true;
            } else if (wroteSpan) {
                writer.flush();
            }

            if (!wroteTooltip && showSummary) {
                writer.writeText("\t", null);
                writer.writeText(summary, null);
                writer.writeText(" ", null);
            }
            if (showDetail) {
                writer.writeText(detail, null);
            }

            if (wroteSpan || wroteTooltip) {
                writer.endElement("span");
            }

            //close table row if present
            if (wroteTable) {
                writer.endElement("td");
                writer.endElement("tr");
            }

        } //messageIter

        //close table if present
        if (wroteTable) {
            writer.endElement("table");
        }
    }

} // end of class MessagesRenderer


