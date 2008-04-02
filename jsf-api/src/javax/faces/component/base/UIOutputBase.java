/*
 * $Id: UIOutputBase.java,v 1.13 2003/09/19 00:57:09 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.component.Repeater;
import javax.faces.component.RepeaterSupport;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.ValueHolderSupport;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;

import java.io.Serializable;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

/**
 * <p><strong>UIOutputBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UIOutput}.</p>
 */

public class UIOutputBase extends UIComponentBase implements UIOutput {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIOutputBase} instance with default property
     * values.</p>
     */
    public UIOutputBase() {

        super();
        setRendererType("Text");

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The {@link ValueHolderSupport} instance to which we delegate
     * our {@link ValueHolder} implementation processing.</p>
     */
    private ValueHolderSupport support = new ValueHolderSupport(this);



    // -------------------------------------------------------------- Properties


    public Converter getConverter() {

        return (support.getConverter());

    }


    public void setConverter(Converter converter) {

        support.setConverter(converter);

    }


    public Object getValue() {

        return (support.getValue());

    }


    public void setValue(Object value) {

        support.setValue(value);

    }


    public String getValueRef() {

        return (support.getValueRef());

    }


    public void setValueRef(String valueRef) {

        support.setValueRef(valueRef);

    }


    // ----------------------------------------------------- ValueHolder Methods


    public Object currentValue(FacesContext context) {

        return (support.currentValue(context));

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        List[] supportList = new List[1];
        List theSupport = new ArrayList(1);
        theSupport.add(support);
        supportList[0] = theSupport;
        values[1] =
            context.getApplication().getViewHandler().getStateManager().
            getAttachedObjectState(context, this, "support", supportList);
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        List[] supportList = (List[])
            context.getApplication().getViewHandler().getStateManager().
            restoreAttachedObjectState(context, values[1], null, this);
	if (supportList != null) {
            List theSupport = supportList[0];
            if ((theSupport != null) && (theSupport.size() > 0)) {
                support = (ValueHolderSupport) theSupport.get(0);
            }
	}

    }


}
