/*
 * $Id: DynamicComponent.java,v 1.1 2003/09/24 23:58:54 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;


import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.component.UIComponent;
import javax.faces.component.base.UIComponentBase;
import javax.faces.component.base.UIOutputBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;


/**
 * <p>Test <code>UIComponent</code> for sys tests that dynamically creates
 * or removes children UIOutput components with specified ids and values.
 * It pays attention to the following request parameters in the
 * <code>encodeBegin()</code> method:</p>
 * <ul>
 * <li><code>?mode=create&id=foo&value=bar</code> - Create a new
 *     <code>UIOutput</code> child with a component identifier of
 *     <code>foo</code> and a value of <code>bar</code> (optional).  Set the
 *     <code>rendererType</code> property to <code>Text</code>.  The
 *     new child will be appended to the child list.</li>
 * <li><code>?mode=delete&id=foo</code> - Remove any child with a
 *     component identifier of <code>foo</code>.</li>
 * </ul>
 *
 * <p>In accordance with our current restrictions, this component sets
 * <code>rendersChildren</code> to <code>true</code>, and recursively
 * renders its children in <code>encodeChildren</code>.  This component
 * itself renders "{" at the beginning and "}" at the end, just like
 * <code>ChildrenComponent</code>.</p>
 */

public class DynamicComponent extends UIComponentBase {


    // ------------------------------------------------------------ Constructors


    public DynamicComponent() {
        this("dynamic");
    }


    public DynamicComponent(String componentId) {
        super();
        setId(componentId);
        System.out.println("Created dynamic id=" + componentId + " " + this);
    }


    // ----------------------------------------------------- UIComponent Methods


    public boolean getRendersChildren() { return (true); }


    public void encodeBegin(FacesContext context) throws IOException {
        process(context);
        ResponseWriter writer = context.getResponseWriter();
        writer.write("{ ");
    }


    public void encodeChildren(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Iterator kids = getChildren().iterator();
        while (kids.hasNext()) {
            encodeRecursive(context, (UIComponent) kids.next());
            writer.write(" ");
        }
    }


    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.write(" }\n");
    }


    // --------------------------------------------------------- Private Methods


    private void encodeRecursive(FacesContext context, UIComponent component)
        throws IOException {

        System.out.println("Rendered output id=" + component.getId() + " " + component);
        component.encodeBegin(context);
        if (component.getRendersChildren()) {
            component.encodeChildren(context);
        } else {
            Iterator kids = component.getChildren().iterator();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                encodeRecursive(context, kid);
            }
        }
        component.encodeEnd(context);

    }


    private void process(FacesContext context) {

        Map map = context.getExternalContext().getRequestParameterMap();
        String mode = (String) map.get("mode");
        String id = (String) map.get("id");
        String value = (String) map.get("value");
        if (mode == null) {
            return;
        } else if ("create".equals(mode)) {
            UIOutputBase output = new UIOutputBase();
            output.setId(id);
            output.setRendererType("Text");
            output.setValue(value);
            getChildren().add(output);
            System.out.println("Created output id=" + id + " " + output);
        } else if ("delete".equals(mode)) {
            Iterator kids = getChildren().iterator();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                if (id.equals(kid.getId())) {
                    getChildren().remove(kid);
                    System.out.println("Removed output id=" + id + " " + kid);
                    break;
                }
            }
        }

    }


}
