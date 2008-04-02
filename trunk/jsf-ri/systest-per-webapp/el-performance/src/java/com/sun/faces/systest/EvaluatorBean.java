/*
 * $Id: EvaluatorBean.java,v 1.1 2004/05/08 17:00:48 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;

import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIOutput;
import javax.faces.el.ValueBinding;

public class EvaluatorBean extends Object {

    public EvaluatorBean() {}

    protected int reps = 3000;
    public int getReps() {
	return reps;
    }

    public void setReps(int newReps) {
	reps = newReps;
    }

    public void doGet(ActionEvent event) {
	int i = 0;
	FacesContext context = FacesContext.getCurrentInstance();
	String id = event.getComponent().getId();
	// strip off the first character
	id = "i" + id.substring(1);
	// get the expression to evaluate
	UIOutput output = (UIOutput) context.getViewRoot().findComponent("form" + NamingContainer.SEPARATOR_CHAR +  id);
	String expression = "#{" + output.getValue() + "}";
	ValueBinding vb = 
	    context.getApplication().createValueBinding(expression);
	// if the user wants to show results
	if (showResults) {
	    // clear the buffer for the new results
	    results = new StringBuffer();
	}
	// evaluate it as a get, reps number of times 
	for (i = 0; i < reps; i++) {
	    if (showResults) {
		results.append(vb.getValue(context) + "\n");
	    }
	    else {
		vb.getValue(context);
	    }
	}
    }

    protected boolean showResults = false;
    public boolean isShowResults() {
	return showResults;
    }

    public void setShowResults(boolean newShowResults) {
	showResults = newShowResults;
    }

    protected String [] expressions;
    public String [] getExpressions() {
	return expressions;
    }

    public void setExpressions(String [] newExpressions) {
	expressions = newExpressions;
    }


    protected StringBuffer results = new StringBuffer();
    public String getResults() {
	return results.toString();
    }

    public void setResults(String newResults) {
	results = new StringBuffer(newResults);
    }
    

}
