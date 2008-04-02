/*
 * $Id: Util.java,v 1.1 2004/06/24 16:53:26 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

// Util.java

package j2meDemo.util;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <B>Util</B> is a class which houses common functionality used by
 * other classes.
 *
 * @version $Id: Util.java,v 1.1 2004/06/24 16:53:26 rogerk Exp $
 */

public class Util extends Object {

//
// Protected Constants
//

//
// Class Variables
//

    /**
     * This array contains attributes that have a boolean value in JSP,
     * but have have no value in HTML.  For example "disabled" or
     * "readonly". <P>
     *
     * @see renderBooleanPassthruAttributes
     */

    private static String booleanPassthruAttributes[] = {
        "disabled",
        "readonly",
        "ismap"
    };

    /**
     * This array contains attributes whose value is just rendered
     * straight to the content.  This array should only contain
     * attributes that require no interpretation by the Renderer.  If an
     * attribute requires interpretation by a Renderer, it should be
     * removed from this array.<P>
     *
     * @see renderPassthruAttributes
     */
    private static String passthruAttributes[] = {
        "accesskey",
        "alt",
        "cols",
        "height",
        "lang",
        "longdesc",
        "maxlength",
        "onblur",
        "onchange",
        "onclick",
        "ondblclick",
        "onfocus",
        "onkeydown",
        "onkeypress",
        "onkeyup",
        "onload",
        "onmousedown",
        "onmousemove",
        "onmouseout",
        "onmouseover",
        "onmouseup",
        "onreset",
        "onselect",
        "onsubmit",
        "onunload",
        "rows",
        "size",
        "tabindex",
        //"class",   PENDING(rlubke)  revisit this for JSFA105
        "title",
        "style",
        "width",
        "dir",
        "rules",
        "frame",
        "border",
        "cellspacing",
        "cellpadding",
        "summary",
        "bgcolor",
        "usemap",
        "enctype",
        "accept-charset",
        "accept",
        "target",
        "onsubmit",
        "onreset"
    };

    private static long id = 0;


//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    private Util() {
        throw new IllegalStateException();
    }

//
// Class methods
//
    public static Class loadClass(String name) throws ClassNotFoundException {
        ClassLoader loader =
            Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            return Class.forName(name);
        } else {
            return loader.loadClass(name);
        }
    }


    /**
     * Generate a new identifier currently used to uniquely identify
     * components.
     */
    public static synchronized String generateId() {
        if (id == Long.MAX_VALUE) {
            id = 0;
        } else {
            id++;
        }
        return Long.toHexString(id);
    }


    /**
     * Return a Locale instance using the following algorithm: <P>
     *
     * <UL>
     *
     * <LI>
     *
     * If this component instance has an attribute named "bundle",
     * interpret it as a model reference to a LocalizationContext
     * instance accessible via FacesContext.getModelValue().
     *
     * </LI>
     *
     * <LI>
     *
     * If FacesContext.getModelValue() returns a LocalizationContext
     * instance, return its Locale.
     *
     * </LI>
     *
     * <LI>
     *
     * If FacesContext.getModelValue() doesn't return a
     * LocalizationContext, return the FacesContext's Locale.
     *
     * </LI>
     *
     * </UL>
     */

    public static Locale
        getLocaleFromContextOrComponent(FacesContext context,
                                        UIComponent component) {
        Locale result = null;
        String bundleName = null, bundleAttr = "bundle";
	
//	ParameterCheck.nonNull(context);
//	ParameterCheck.nonNull(component);

        // verify our component has the proper attributes for bundle.
        if (null !=
            (bundleName = (String) component.getAttributes().get(bundleAttr))) {
            // verify there is a Locale for this modelReference
            javax.servlet.jsp.jstl.fmt.LocalizationContext locCtx = null;
            if (null != (locCtx =
                (javax.servlet.jsp.jstl.fmt.LocalizationContext)
                (Util.getValueBinding(bundleName)).getValue(context))) {
                result = locCtx.getLocale();
//		Assert.assert_it(null != result);
            }
        }
        if (null == result) {
            result = context.getViewRoot().getLocale();
        }

        return result;
    }


    /**
     * Render any boolean "passthru" attributes.
     * <P>
     *
     * @see passthruAttributes
     */

    public static String renderBooleanPassthruAttributes(FacesContext context,
                                                         UIComponent component) {
        int i = 0, len = booleanPassthruAttributes.length;
        String value;
        boolean thisIsTheFirstAppend = true;
        StringBuffer renderedText = new StringBuffer();

        for (i = 0; i < len; i++) {
            if (null != (value = (String)
                component.getAttributes().get(booleanPassthruAttributes[i]))) {
                if (thisIsTheFirstAppend) {
                    // prepend ' '
                    renderedText.append(' ');
                    thisIsTheFirstAppend = false;
                }
                if (Boolean.valueOf(value).booleanValue()) {
                    renderedText.append(booleanPassthruAttributes[i] + ' ');
                }
            }
        }

        return renderedText.toString();
    }


    /**
     * Render any "passthru" attributes, where we simply just output the
     * raw name and value of the attribute.  This method is aware of the
     * set of HTML4 attributes that fall into this bucket.  Examples are
     * all the javascript attributes, alt, rows, cols, etc.  <P>
     *
     * @return the rendererd attributes as specified in the component.
     *         Padded with leading and trailing ' '.  If there are no passthru
     *         attributes in the component, return the empty String.
     *
     * @see passthruAttributes
     */

    public static String renderPassthruAttributes(FacesContext context,
                                                  UIComponent component) {
        int i = 0, len = passthruAttributes.length;
        String value;
        boolean thisIsTheFirstAppend = true;
        StringBuffer renderedText = new StringBuffer();

        for (i = 0; i < len; i++) {
            if (null != (value = (String)
                component.getAttributes().get(passthruAttributes[i]))) {
                if (thisIsTheFirstAppend) {
                    // prepend ' '
                    renderedText.append(' ');
                    thisIsTheFirstAppend = false;
                }
                renderedText.append(passthruAttributes[i] + "=\"" + value +
                                    "\" ");
            }
        }

        return renderedText.toString();
    }


    public static ValueBinding getValueBinding(String valueRef) {
        ApplicationFactory af = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application a = af.getApplication();
        return (a.createValueBinding(valueRef));
    }


    public static MethodBinding createConstantMethodBinding(String outcome) {
        return new ConstantMethodBinding(outcome);
    }

    public static List getSelectItems(UIComponent component) {
        ArrayList list = new ArrayList();
        Iterator children = component.getChildren().iterator();
        while (children.hasNext()) {
            UIComponent child = (UIComponent) children.next();
                                                                                
            if (child instanceof UISelectItem) {
                Object value = ((UISelectItem) child).getValue();
                if (value == null) {
                    UISelectItem item = (UISelectItem) child;
                    list.add(new SelectItem(item.getItemValue(),
                     item.getItemLabel(),
                     item.getItemDescription(),
                     item.isItemDisabled()));
                } else if (value instanceof SelectItem) {
                    list.add(value);
                }
            } else if (child instanceof UISelectItems) {
                Object value = ((UISelectItems) child).getValue();
                if (value instanceof SelectItem) {
                    list.add(value);
                } else if (value instanceof SelectItem[]) {
                    list.addAll(Arrays.asList((SelectItem[]) value));
                } else if (value instanceof Collection) {
                    list.addAll((Collection) value);
                } else if (value instanceof Map) {
                    Iterator entries = ((Map) value).entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry entry = (Map.Entry) entries.next();
                                                                                
                        list.add(new SelectItem(entry.getKey(),
                            "" + entry.getValue()));
                    }
                }
            }
        }
        return list;
    }    

    public static boolean componentIsDisabledOrReadonly(UIComponent component) {
        Object disabledOrReadonly = null;
        boolean result = false;
        if (null !=
            (disabledOrReadonly = component.getAttributes().get("disabled"))) {
            if (disabledOrReadonly instanceof String) {
                result = ((String) disabledOrReadonly).equalsIgnoreCase("true");
            } else {
                result = disabledOrReadonly.equals(Boolean.TRUE);
            }
        }
        if ((result == false) &&
            null !=
            (disabledOrReadonly = component.getAttributes().get("readonly"))) {
            if (disabledOrReadonly instanceof String) {
                result = ((String) disabledOrReadonly).equalsIgnoreCase("true");
            } else {
                result = disabledOrReadonly.equals(Boolean.TRUE);
            }
        }
                                                                                                                        
        return result;
    }


//
// General Methods
//

} // end of class Util
