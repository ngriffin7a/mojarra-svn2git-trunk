/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


/**
 * $Id: SelectManyCheckboxListRenderer.java,v 1.40 2005/05/05 20:51:25 edburns Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// SelectManyCheckboxListRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import java.io.IOException;
import java.util.Iterator;

/**
 * <B>SelectManyCheckboxListRenderer</B> is a class that renders the
 * current value of <code>UISelectMany<code> component as a list of checkboxes.
 */

public class SelectManyCheckboxListRenderer extends MenuRenderer {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public SelectManyCheckboxListRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //

    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        String alignStr = null;
        Object borderObj = null;
        boolean alignVertical = false;
        int border = 0;

        if (null !=
            (alignStr = (String) component.getAttributes().get("layout"))) {
            alignVertical = alignStr.equalsIgnoreCase("pageDirection") ?
                true : false;
        }
        if (null != (borderObj = component.getAttributes().get("border"))) {
            if (borderObj instanceof Integer) {
                border = ((Integer) borderObj).intValue();
            } else {
                try {
                    border = Integer.valueOf(borderObj.toString()).intValue();
                } catch (Throwable e) {
                    border = 0;
                }
            }
        }

        renderBeginText(component, border, alignVertical, context, true);

        Iterator items = Util.getSelectItems(context, component);
        SelectItem curItem = null;
        while (items.hasNext()) {
            curItem = (SelectItem) items.next();
            // If we come across a group of options, render them as a nested
            // table.
            if (curItem instanceof SelectItemGroup) {
                // write out the label for the group.
                if (curItem.getLabel() != null) {
                    if (alignVertical) {
                        writer.startElement("tr", component);
                    }
                    writer.startElement("td", component);
                    writer.writeText(curItem.getLabel(), "label");
                    writer.endElement("td");
                    if (alignVertical) {
                        writer.endElement("tr");
                    }

                }
                if (alignVertical) {
                    writer.startElement("tr", component);
                }
                writer.startElement("td", component);
                writer.writeText("\n", null);
                renderBeginText(component, 0, alignVertical,
                                context, false);
                // render options of this group.
                SelectItem[] itemsArray =
                    ((SelectItemGroup) curItem).getSelectItems();
                for (int i = 0; i < itemsArray.length; ++i) {
                    renderOption(context, component, itemsArray[i],
                                 alignVertical);
                }
                renderEndText(component, alignVertical, context, false);
                writer.endElement("td");
                if (alignVertical) {
                    writer.endElement("tr");
                    writer.writeText("\n", null);
                }
            } else {
                renderOption(context, component, curItem, alignVertical);
            }
        }

        renderEndText(component, alignVertical, context, true);
    }


    protected void renderOption(FacesContext context, UIComponent component,
                                SelectItem curItem, boolean alignVertical)
        throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
                
        // disable the check box if the attribute is set.
        String labelClass = null;
	boolean componentDisabled = false;
	if (component.getAttributes().get("disabled") != null) {
            if ((component.getAttributes().get("disabled")).equals(Boolean.TRUE)) {
	        componentDisabled = true;
	    }
	}
        if (componentDisabled || curItem.isDisabled()) {
            labelClass = (String) component.
                getAttributes().get("disabledClass");
        } else {
            labelClass = (String) component.
                getAttributes().get("enabledClass");
        }
        if (alignVertical) {
            writer.writeText("\t", null);
            writer.startElement("tr", component);
            writer.writeText("\n", null);
        }
        writer.startElement("td", component);
        writer.writeText("\n", null);

        writer.startElement("label", component);
        // if enabledClass or disabledClass attributes are specified, apply
        // it on the label.
        if (labelClass != null) {
            writer.writeAttribute("class", labelClass, "labelClass");
        }

        writer.startElement("input", component);
        writer.writeAttribute("name", component.getClientId(context),
                              "clientId");
        String valueString = getFormattedValue(context, component,
                                               curItem.getValue());
        writer.writeAttribute("value", valueString, "value");
        writer.writeAttribute("type", "checkbox", null);

        Object submittedValues[] = getSubmittedSelectedValues(context,
                                                              component);
        boolean isSelected;
        
        Class type = String.class;
        Object valuesArray = null;
        Object itemValue = null;
        if (submittedValues != null) {
            valuesArray = submittedValues;
            itemValue = valueString;
        } else {
            valuesArray = getCurrentSelectedValues(context, component);
            itemValue = curItem.getValue();
        }
        if (valuesArray != null) {
            type = valuesArray.getClass().getComponentType();
        } 

        Object newValue = context.getApplication().getExpressionFactory().
            coerceToType(itemValue, type);

        isSelected = isSelected(newValue, valuesArray);

        if (isSelected) {
            writer.writeAttribute(getSelectedTextString(), Boolean.TRUE, null);
        }
        if (curItem.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", "disabled");
        }

        // Apply HTML 4.x attributes specified on UISelectMany component to all 
        // items in the list except styleClass and style which are rendered as
        // attributes of outer most table.
        Util.renderPassThruAttributes(context, writer, component,
                                      new String[]{"style", "border"});
        Util.renderBooleanPassThruAttributes(writer, component);
       
        String itemLabel = curItem.getLabel();
        if (itemLabel != null) {
            writer.writeText(" ", null);
            writer.writeText(itemLabel, "label");
        }
        writer.endElement("input");
        writer.endElement("label");
        writer.endElement("td");
        writer.writeText("\n", null);
        if (alignVertical) {
            writer.writeText("\t", null);
            writer.endElement("tr");
            writer.writeText("\n", null);
        }
    }


    String getSelectedTextString() {
        return " checked";
    }


    protected void renderBeginText(UIComponent component, int border,
                                   boolean alignVertical, FacesContext context, boolean outerTable)
        throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
        
        writer.startElement("table", component);
        if (border != Integer.MIN_VALUE) {
            writer.writeAttribute("border", new Integer(border), "border");
        }

        // render style and styleclass attribute on the outer table instead of 
        // rendering it as pass through attribute on every option in the list.
        if (outerTable) {
            // render "id" only for outerTable.
            if (shouldWriteIdAttribute(component)) {
                writeIdAttributeIfNecessary(context, writer, component);
            }
            String styleClass = (String) component.getAttributes().get(
                "styleClass");
            String style= (String) component.getAttributes().get("style");
            if (styleClass != null) {
                writer.writeAttribute("class", styleClass, "class");
            }
            if (style!= null) {
                writer.writeAttribute("style", style, "style");
            }
        }
        writer.writeText("\n", null);

        if (!alignVertical) {
            writer.writeText("\t", null);
            writer.startElement("tr", component);
            writer.writeText("\n", null);
        }
    }


    protected void renderEndText(UIComponent component, boolean alignVertical,
                                 FacesContext context, boolean outerTable)
        throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        if (!alignVertical) {
            writer.writeText("\t", null);
            writer.endElement("tr");
            writer.writeText("\n", null);
        }
        writer.endElement("table");
    }

} // end of class SelectManyCheckboxListRenderer
