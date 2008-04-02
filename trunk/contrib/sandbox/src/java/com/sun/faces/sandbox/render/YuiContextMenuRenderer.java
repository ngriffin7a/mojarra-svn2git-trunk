/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.sandbox.component.YuiContextMenu;
import com.sun.faces.sandbox.component.YuiMenuBase;
import com.sun.faces.sandbox.model.Menu;

/**
 * This <code>Renderer</code> will render a context menu. For an 
 * example of this component, see the Yahoo! UI example <a target="_blank"
 * href="http://developer.yahoo.com/yui/examples/menu/contextmenu.html">page</a>.
 * @author Jason Lee
 *
 */
public class YuiContextMenuRenderer extends YuiMenuRenderer {
    // TODO:  this will likely have XHTML issues
    /**
     * This will render the JavaScript needed to instantiate the YUI context menu object
     */
    protected void renderJavaScript(ResponseWriter writer, YuiMenuBase component, Menu menu) throws IOException {
        YuiContextMenu contextMenu = (YuiContextMenu) component;
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", "type");
        String javaScript = 
                "var oMenu_%%%ID%%% = new YAHOO.widget.ContextMenu(\"%%%ID%%%\", {" + buildConstructorArgs(contextMenu) + "});" +
                "oMenu_%%%ID%%%.render(document.getElementById(\"%%%TRIGGER%%%\"));";
        javaScript = javaScript.replaceAll("%%%ID%%%", component.getId() + "_1")
                .replaceAll("%%%TRIGGER%%%", component.getParent().getClientId(FacesContext.getCurrentInstance())); 
                        //getFullyQualifiedId(contextMenu.getTrigger()));
        
        writer.writeText(javaScript, null);
        writer.endElement("script");
    }
    
    private String buildConstructorArgs(YuiContextMenu component) {
        return ("trigger: \"%%%TRIGGER%%%\", width: \"" + component.getWidth() + "\"");
    }
}