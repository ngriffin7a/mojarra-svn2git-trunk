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

/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletRequest;

import org.apache.shale.remoting.Mechanism;

import com.sun.faces.sandbox.util.Util;
import com.sun.faces.sandbox.util.YuiConstants;

/**
 * I'm not a big fan of this class, but it will get me by until I can come up
 * with a better solution.  Ken Paulsen was telling me about some of the things
 * that JSFTemplating offers, and they sound pretty promising...
 * @author Jason Lee
 *
 */
public class YuiRendererHelper {
    protected static Map<String, String> cssClasses;
    protected static Map<String, String> imageVars1;
    protected static String YUI_HELPER_JS_RENDERED = "YUI_HELPER_JS";
    protected static String YUI_HELPER_MENU_JS_RENDERED = "YUI_HELPER_MENU_JS";
    protected static String YUI_HELPER_CSS_RENDERED = "YUI_HELPER_CSS";

    protected static Map<String, String> getCssClasses() {
        if (cssClasses == null) {
            cssClasses = new HashMap<String, String>();
            cssClasses.put(".ygtvtn", YuiConstants.YUI_ROOT + "assets/tn.gif");
            cssClasses.put(".ygtvtm", YuiConstants.YUI_ROOT + "assets/tm.gif");
            cssClasses.put(".ygtvtmh", YuiConstants.YUI_ROOT + "assets/tmh.gif");
            cssClasses.put(".ygtvtp", YuiConstants.YUI_ROOT + "assets/tp.gif");
            cssClasses.put(".ygtvtph", YuiConstants.YUI_ROOT + "assets/tph.gif");
            cssClasses.put(".ygtvln", YuiConstants.YUI_ROOT + "assets/ln.gif");
            cssClasses.put(".ygtvlm", YuiConstants.YUI_ROOT + "assets/lm.gif");
            cssClasses.put(".ygtvlmh", YuiConstants.YUI_ROOT + "assets/lmh.gif");
            cssClasses.put(".ygtvlp", YuiConstants.YUI_ROOT + "assets/lp.gif");
            cssClasses.put(".ygtvlph", YuiConstants.YUI_ROOT + "assets/lph.gif");
            cssClasses.put(".ygtvloading", YuiConstants.YUI_ROOT + "assets/loading.gif");
            cssClasses.put(".ygtvdepthcell", YuiConstants.YUI_ROOT + "assets/vline.gif");

            cssClasses.put("div.yuimenu div.topscrollbar, div.yuimenu div.bottomscrollbar", YuiConstants.YUI_ROOT + "assets/map.gif");
            cssClasses.put("div.yuimenu div.topscrollbar", YuiConstants.YUI_ROOT + "assets/map.gif");
            cssClasses.put("div.yuimenu div.topscrollbar_disabled", YuiConstants.YUI_ROOT + "assets/map.gif");
            cssClasses.put("div.yuimenu div.bottomscrollbar", YuiConstants.YUI_ROOT + "assets/map.gif");
            cssClasses.put("div.yuimenu div.bottomscrollbar_disabled", YuiConstants.YUI_ROOT + "assets/map.gif");
            cssClasses.put("div.yuimenu li.hassubmenu em.submenuindicator, div.yuimenubar li.hassubmenu em.submenuindicator", YuiConstants.YUI_ROOT + "assets/map.gif");
            cssClasses.put("div.yuimenu li.checked em.checkedindicator", YuiConstants.YUI_ROOT + "assets/map.gif");

            cssClasses.put(".yui-calendar .calnavleft", YuiConstants.YUI_ROOT + "assets/callt.gif");
            cssClasses.put(".yui-calendar .calnavright", YuiConstants.YUI_ROOT + "assets/calrt.gif");
        }

        return cssClasses;
    }

    /*
    protected static Map<String, String> getImageVars() {
        if (imageVars == null) {
            imageVars = new HashMap<String, String>();
//          imageVars.put("YAHOO.widget.MenuItem.prototype.IMG_ROOT", "");
            imageVars.put("YAHOO.widget.MenuItem.prototype.SUBMENU_INDICATOR_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuarorght8_nrm_1.gif");
            imageVars.put("YAHOO.widget.MenuItem.prototype.SELECTED_SUBMENU_INDICATOR_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuarorght8_hov_1.gif");
            imageVars.put("YAHOO.widget.MenuItem.prototype.DISABLED_SUBMENU_INDICATOR_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuarorght8_dim_1.gif");
            imageVars.put("YAHOO.widget.MenuItem.prototype.CHECKED_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuchk8_nrm_1.gif");
            imageVars.put("YAHOO.widget.MenuItem.prototype.SELECTED_CHECKED_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuchk8_hov_1.gif");
            imageVars.put("YAHOO.widget.MenuItem.prototype.DISABLED_CHECKED_IMAGE_PATH",
                    YuiConstants.YUI_ROOT + "assets/menuchk8_dim_1.gif");
            imageVars.put("YAHOO.widget.MenuBarItem.prototype.SUBMENU_INDICATOR_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuarodwn8_nrm_1.gif");
            imageVars.put("YAHOO.widget.MenuBarItem.prototype.SELECTED_SUBMENU_INDICATOR_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuarodwn8_hov_1.gif");
            imageVars.put("YAHOO.widget.MenuBarItem.prototype.DISABLED_SUBMENU_INDICATOR_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuarodwn8_dim_1.gif");
            
            imageVars.put("YAHOO.widget.Calendar.prototype.IMG_ROOT", 
                    YuiConstants.YUI_ROOT + "");
            imageVars.put("YAHOO.widget.Calendar.prototype.NAV_ARROW_LEFT", 
                    YuiConstants.YUI_ROOT + "assets/callt.gif");
            imageVars.put("YAHOO.widget.Calendar.prototype.NAV_ARROW_RIGHT", 
                    YuiConstants.YUI_ROOT + "assets/calrt.gif");



        }
        return imageVars;
    }
    */

    // TODO:  This needs to be improved
    public static void renderSandboxStylesheet(FacesContext context, ResponseWriter writer, UIComponent comp) throws IOException{
        if (!hasResourceBeenRendered(context, YUI_HELPER_CSS_RENDERED)) {
            writer.startElement("style", comp);
            writer.writeAttribute("type", "text/css", "type");
            for (Map.Entry<String, String> cssClass : getCssClasses().entrySet()) {
                writer.write(cssClass.getKey() + " {background-image:url(" + 
                        Util.getXhtmlHelper().mapResourceId(context, Mechanism.CLASS_RESOURCE, cssClass.getValue()) +
                ");}");
            }
            writer.write("div.yuimenu { background-color: #efefef; border:solid 1px #527B97; }");
            writer.write("div.yuimenubar { background-color: #efefef; }");
            writer.write("div.yuimenu ul { border-color: #527B97; }");
            writer.write("div.yuimenu li.yuimenuitem { padding:2px 24px; }");
            writer.write("div.yuimenubar h6 { border-color:#527B97; }");
            writer.write("div.yuimenubar li.selected { background-color:#527B97; }");
            writer.write("div.yuimenubar li.yuimenubaritem { border-color:#527B97; }");
            writer.write("div.yuimenu li.selected, div.yuimenubar li.selected { background-color: #527B97; }");
            writer.endElement("style");
            setResourceAsRendered(context, YUI_HELPER_CSS_RENDERED);
        }
    }
    
    public static void renderSandboxMenuJavaScript(FacesContext context, ResponseWriter writer, UIComponent comp) throws IOException{
        Map<String, String> imageVars = new HashMap<String, String>();
//      imageVars.put("YAHOO.widget.MenuItem.prototype.IMG_ROOT", "");
        imageVars.put("YAHOO.widget.MenuItem.prototype.SUBMENU_INDICATOR_IMAGE_PATH", 
                YuiConstants.YUI_ROOT + "assets/menuarorght8_nrm_1.gif");
        imageVars.put("YAHOO.widget.MenuItem.prototype.SELECTED_SUBMENU_INDICATOR_IMAGE_PATH", 
                YuiConstants.YUI_ROOT + "assets/menuarorght8_hov_1.gif");
        imageVars.put("YAHOO.widget.MenuItem.prototype.DISABLED_SUBMENU_INDICATOR_IMAGE_PATH", 
                YuiConstants.YUI_ROOT + "assets/menuarorght8_dim_1.gif");
        imageVars.put("YAHOO.widget.MenuItem.prototype.CHECKED_IMAGE_PATH", 
                YuiConstants.YUI_ROOT + "assets/menuchk8_nrm_1.gif");
        imageVars.put("YAHOO.widget.MenuItem.prototype.SELECTED_CHECKED_IMAGE_PATH", 
                YuiConstants.YUI_ROOT + "assets/menuchk8_hov_1.gif");
        imageVars.put("YAHOO.widget.MenuItem.prototype.DISABLED_CHECKED_IMAGE_PATH",
                YuiConstants.YUI_ROOT + "assets/menuchk8_dim_1.gif");
        imageVars.put("YAHOO.widget.MenuBarItem.prototype.SUBMENU_INDICATOR_IMAGE_PATH", 
                YuiConstants.YUI_ROOT + "assets/menuarodwn8_nrm_1.gif");
        imageVars.put("YAHOO.widget.MenuBarItem.prototype.SELECTED_SUBMENU_INDICATOR_IMAGE_PATH", 
                YuiConstants.YUI_ROOT + "assets/menuarodwn8_hov_1.gif");
        imageVars.put("YAHOO.widget.MenuBarItem.prototype.DISABLED_SUBMENU_INDICATOR_IMAGE_PATH", 
                YuiConstants.YUI_ROOT + "assets/menuarodwn8_dim_1.gif");
        renderSandboxJavaScript(context, writer, comp, YUI_HELPER_MENU_JS_RENDERED, imageVars);

    }

    private static void renderSandboxJavaScript(FacesContext context, ResponseWriter writer, 
            UIComponent comp, String key, Map<String, String> imageVars) throws IOException{
        if (!hasResourceBeenRendered(context, key)) {
            writer.startElement("script", comp);
            writer.writeAttribute("type", "text/javascript", "type");
//            writer.write("YAHOO.widget.MenuItem.prototype.IMG_ROOT = \"\";");
            for (Map.Entry<String, String> var : imageVars.entrySet()) {
                writer.write(var.getKey() + " = \"" + 
                        Util.getXhtmlHelper().mapResourceId(context, Mechanism.CLASS_RESOURCE, var.getValue()) +
                    "\";\n");
            }
            writer.endElement("script");
            setResourceAsRendered(context, key);
        }
    }

    /**
     * Return a JavaScript-friendly variable name based on the clientId
     */
    public static String getJavascriptVar(UIComponent comp) {
        return comp.getClientId(FacesContext.getCurrentInstance()).replaceAll(":", "_");
    }
    
    public static String getRenderedOutput(FacesContext context, ResponseWriter writer, UIComponent component) throws IOException {
        String output = "";
        if (component != null) {
            StringWriter stringWriter = new StringWriter();
            ResponseWriter newWriter = writer.cloneWithWriter(stringWriter);
            context.setResponseWriter(newWriter);
            component.encodeBegin(context);
            component.encodeChildren(context);
            component.encodeEnd(context);
            output = stringWriter.toString();
            if (output != null) {
                output = output.trim();
                output = sanitizeStringForJavaScript(output);
            }
            context.setResponseWriter(writer);
        }

        return output;
    }

    /**
     * @param context the <code>FacesContext</code> for the current request
     *
     * @return <code>true</code> If the YUI JS and CSS overrides have been rendered
     */
    private static boolean hasResourceBeenRendered(FacesContext context, String key) {
        return (context.getExternalContext().getRequestMap().get(key) != null);
    }


    /**
     * <p>Set a flag to indicate that the YUI JS and CSS overrides have been rendered
     *
     * @param context the <code>FacesContext</code> of the current request
     */
    @SuppressWarnings("unchecked")
    private static void setResourceAsRendered(FacesContext context, String key) {
        context.getExternalContext().getRequestMap().put(key, Boolean.TRUE);
    }

    private static String sanitizeStringForJavaScript( String s )
    {
        /*
        StringBuffer buf = new StringBuffer();
        for ( int i = 0; i < s.length(); i++ )
        {
            char c = s.charAt( i );
            if ( c>='a' && c<='z' || c>='A' && c<='Z' || c>='0' && c<='9' )
            {
                buf.append( c );
            }
            else
            {
                buf.append( "&#" + (int)c + ";" );
            }
        }
        return buf.toString();
        */
        return s.replaceAll("\\n", "")
            .replaceAll("'", "\\\\'");
    }
}