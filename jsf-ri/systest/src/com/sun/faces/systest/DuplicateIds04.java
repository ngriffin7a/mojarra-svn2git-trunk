/*
 * $Id: DuplicateIds04.java,v 1.3 2005/08/22 22:10:37 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */



package com.sun.faces.systest;

import java.util.List;
import javax.faces.component.UISelectItem;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.ValueChangeEvent;

/**
 * Backing bean for search criteria screen.
 *
 * @author  Mark Roth
 */
public class DuplicateIds04 {
    
    /** Either "and" or "or" */
    private String operator = "and";
    
    /** Panel grid component to store components */
    private HtmlPanelGrid panelGrid;

    private int serialNumber = 0;

    public DuplicateIds04() { }
    
    /**
     * Called when the "More" button is pressed
     */
    public String more() {
        return "again";
    }
    
    /**
     * Called when the "Fewer" button is pressed
     */
    public String fewer() {
        return "again";
    }
    
    /**
     * Called when the "Search" button is pressed
     */
    public String search() {
        return "search";
    }
    
    /**
     * Getter for property operator.
     * @return Value of property operator.
     */
    public String getOperator() {
        return operator;
    }
    
    /**
     * Setter for property operator.
     * @param operator New value of property operator.
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }
    
    /**
     * Getter for property panelGrid.  If the panel grid does not yet exist,
     * create it, and add the initial children.
     *
     * @return Value of property panelGrid.
     */
    public HtmlPanelGrid getPanelGrid() {
        if(panelGrid == null) {
            panelGrid = new HtmlPanelGrid();
	    //	    panelGrid.setId("searchCriteria" + serialNumber++);

            panelGrid.setColumns(3);
            panelGrid.setBorder(1);
            panelGrid.setCellpadding("5");
            panelGrid.setCellspacing("0");
            List children = panelGrid.getChildren();
            HtmlSelectOneMenu field = createFieldMenu();
            HtmlSelectOneMenu operator = createOperatorMenu();
            HtmlInputText text = new HtmlInputText();
	    //	    text.setId("searchCriteria" + serialNumber++);

            text.setSize(25);
            children.add(field);
            children.add(operator);
            children.add(text);
        }
        return panelGrid;
    }
    
    /**
     * Creates the menu that allows the user to select a field.
     */
    private HtmlSelectOneMenu createFieldMenu() {
        HtmlSelectOneMenu field = new HtmlSelectOneMenu();
	//	field.setId("searchCriteria" + serialNumber++);

        List children = field.getChildren();
        children.add(createSelectItem("Subject"));
        children.add(createSelectItem("Sender"));
        children.add(createSelectItem("Date"));
        children.add(createSelectItem("Priority"));
        children.add(createSelectItem("Status"));
        children.add(createSelectItem("To"));
        children.add(createSelectItem("Cc"));
        children.add(createSelectItem("To or Cc"));
        return field;
    }
    
    /**
     * Creates the menu that allows the user to select an operator
     */
    private HtmlSelectOneMenu createOperatorMenu() {
        HtmlSelectOneMenu field = new HtmlSelectOneMenu();
	//	field.setId("searchCriteria" + serialNumber++);

        List children = field.getChildren();
        children.add(createSelectItem("contains"));
        children.add(createSelectItem("doesn't contain"));
        children.add(createSelectItem("is"));
        children.add(createSelectItem("isn't"));
        children.add(createSelectItem("starts with"));
        children.add(createSelectItem("ends with"));
        return field;
    }
    
    /**
     * Creates a select tiem with the given value and label.
     */
    private UISelectItem createSelectItem(String label) {
        UISelectItem result = new UISelectItem();
	//	result.setId("searchCriteria" + serialNumber++);
        result.setItemValue(label);
        result.setItemLabel(label);
        return result;
    }
    
    /**
     * Setter for property panelGrid.
     * @param panelGrid New value of property panelGrid.
     */
    public void setPanelGrid(HtmlPanelGrid panelGrid) {
        this.panelGrid = panelGrid;
    }
    
}
