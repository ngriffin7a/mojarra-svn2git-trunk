/*
 * $Id: Panel_GridTag.java,v 1.13 2003/09/24 23:17:21 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import com.sun.faces.taglib.FacesTag;

/**
 * This class is the tag handler that evaluates the 
 * <code>panel_grid</code> custom tag.
 */

public class Panel_GridTag extends FacesTag {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private String columnClasses = null;
    private int columns = 2;
    private String footerClass = null;
    private String headerClass = null;
    private String panelClass = null;
    private String rowClasses = null;
     
    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //
    
    public Panel_GridTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //
    
    public void setColumnClasses(String newColumnClasses) {
        this.columnClasses = newColumnClasses;
    }
    
    public void setColumns(int newColumns) {
        this.columns = newColumns;
    }
    
    public void setFooterClass(String newFooterClass) {
        this.footerClass = newFooterClass;
    }
    
    public void setHeaderClass(String newHeaderClass) {
        this.headerClass = newHeaderClass;
    }
    
    public void setPanelClass(String newPanelClass) {
        this.panelClass = newPanelClass;
    }
    
    public void setRowClasses(String newRowClasses) {
        this.rowClasses = newRowClasses;
    }
    
    public void release() {
        super.release();
        this.columnClasses = null;
        this.columns = 2;
        this.footerClass = null;
        this.headerClass = null;
        this.panelClass = null;
        this.rowClasses = null;
    }


    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        if (columnClasses != null) {
            component.getAttributes().put("columnClasses", columnClasses);
        }
	component.getAttributes().put("columns", new Integer(columns));
        if (footerClass != null) {
            component.getAttributes().put("footerClass", footerClass);
        }
        if (headerClass != null) {
            component.getAttributes().put("headerClass", headerClass);
        }
        if (panelClass != null) {
            component.getAttributes().put("panelClass", panelClass);
        }
        if (rowClasses != null) {
            component.getAttributes().put("rowClasses", rowClasses);
        }
        
        // set HTML 4.0 attributes if any
        if (summary != null) {
            component.getAttributes().put("summary", summary);
        }
        if (width != null) {
            component.getAttributes().put("width", width);
        }
        if (bgcolor != null) {
            component.getAttributes().put("bgcolor", bgcolor);
        }
        if (frame != null) {
            component.getAttributes().put("frame", frame);
        }
        if (rules != null) {
            component.getAttributes().put("rules", rules);
        }
        if (border != null) {
            component.getAttributes().put("border", border);
        }
        if (cellspacing != null) {
            component.getAttributes().put("cellspacing", cellspacing);
        }
        if (cellpadding != null) {
            component.getAttributes().put("cellpadding", cellpadding);
        }
    }

    /**
     * This is implemented by faces subclasses to allow globally turing off
     * the render kit.
     */
    public String getRendererType() { 
        return ("Grid");
    }
    public String getComponentType() { 
        return ("Panel"); 
    }    
    

}
