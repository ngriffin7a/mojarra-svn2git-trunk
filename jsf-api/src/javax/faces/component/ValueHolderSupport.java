/*
 * $Id: ValueHolderSupport.java,v 1.8 2003/10/06 18:34:21 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.Application;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;


/**
 * <p><strong>ValueHolderSupport</strong> is a utility class that may be
 * utilized by {@link UIComponent}s that implement {@link ValueHolder} to
 * delegate value holder management methods.</p>
 *
 * <p>Typical use in a {@link UIComponent} implementation class would be:</p>
 * <pre>
 *   public class MyComponent extends UIComponentBase
 *     implements ValueHolder {
 *
 *     private ValueHolderSupport support = new ValueHolderSupport();
 *
 *     public Converter getConverter() {
 *       return (support.getConverter());
 *     }
 *
 *     public void setConverter(Converter converter) {
 *       support.setConverter(converter);
 *     }
 *
 *     ... and so on ...
 *
 * </pre>
 *
 * <p>{@link ValueHolderSupport} implements StateHolder, and you will want
 * to make sure that the <code>support</code> instance variable gets saved
 * and restored as part of the state of your component class.</p>
 */

public class ValueHolderSupport
    implements StateHolder, ValueHolder {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link ValueHolderSupport} instance not associated
     * with any {@link UIComponent}.</p>
     */
    public ValueHolderSupport() {

        this(null);

    }


    /**
     * <p>Construct a new {@link ValueHolderSupport} instance associated with
     * the specified {@link UIComponent}.</p>
     *
     * @param component {@link UIComponent} with which we are associated
     */
    public ValueHolderSupport(UIComponent component) {

        this.component = component;

    }


    // ------------------------------------------------------ Instance Variables


    private UIComponent component = null;
    private Converter converter = null;
    private Object value = null;
    private String valueRef = null;


    // -------------------------------------------------------------- Properties



    public Converter getConverter() {

        return (this.converter);

    }


    public void setConverter(Converter converter) {

        this.converter = converter;

    }


    public Object getValue() {

        Repeater repeater = RepeaterSupport.findParentRepeater(component);
        if (repeater != null) {
            if (repeater.getRowIndex() > 0) {
                return (repeater.getChildValue(component));
            } else {
                return (this.value);
            }
        } else {
            return (this.value);
        }

    }


    public void setValue(Object value) {

        Repeater repeater = RepeaterSupport.findParentRepeater(component);
        if (repeater != null) {
            if (repeater.getRowIndex() > 0) {
                repeater.setChildValue(component, value);
            } else {
                this.value = value;
            }
        } else {
            this.value = value;
        }

    }


    public String getValueRef() {

        return (this.valueRef);

    }


    public void setValueRef(String valueRef) {

        this.valueRef = valueRef;

    }


    // ----------------------------------------------------- ValueHolder Methods

    /**
     * @exception EvaluationException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}  
     */
    public Object currentValue(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        Object value = getValue();
        if (value != null) {
            return (value);
        }
        String valueRef = getValueRef();
        if (valueRef != null) {
            Application application = context.getApplication();
            ValueBinding binding = application.getValueBinding(valueRef);
            return (binding.getValue(context));
        }
        return (null);

    }


    // ----------------------------------------------------- StateHolder Methods


    private boolean transientFlag = false;


    public boolean isTransient() {

        return (this.transientFlag);

    }


    public void setTransient(boolean transientFlag) {

        this.transientFlag = transientFlag;

    }


    public Object saveState(FacesContext context) {

        // NOTE:  The associated component is not stored as part of the state,
        // because it will be a different object instance on restoration

        Object values[] = new Object[3];
        values[0] = UIComponentBase.saveAttachedState(context, converter);
        int rowCount = 0;
        Repeater repeater = RepeaterSupport.findParentRepeater(component);
        if (repeater != null && repeater.getRowIndex() > 0) {
            rowCount = repeater.getRowCount();
            Object[] currentValues = new Object[rowCount];
            for (int i = 0; i < rowCount; ++i ) {
                repeater.setRowIndex(i+1);
                currentValues[i] = repeater.getChildValue(component);
            }
            values[1] = currentValues;

        } else {
            values[1] = value;
        }
        values[2] = valueRef;
        return (values);

    }

    /**
     * <p>This ivar is used to allow the actual restoring of state to
     * happen in {@link #setComponent}.  This is necessary because we
     * need to know the component to which we are attached to fully have
     * our state restored, and we don't know the component until {@link
     * #setComponent} is called.</p>
     *
     */
    private Object stateToRestore = null;

    public void restoreState(FacesContext context, Object state)
        throws IOException {
	stateToRestore = state;
    }

    public void setComponent(UIComponent yourComponent) {
        component = yourComponent;
	
        // Restore component reference from parameter
	if (null == stateToRestore || null == component) {
	    return;
	}

        // Restore other state information from saved state
        Object values[] = (Object[]) stateToRestore;

	try {
	    converter = (Converter) 
		UIComponentBase.restoreAttachedState(FacesContext.getCurrentInstance(),
						     values[0]);
	}
	catch (IllegalStateException ioe) {
	    String message = "restoreAttachedState failed";
	    FacesContext.getCurrentInstance().getExternalContext().log(message);
	    throw new FacesException(message, ioe);
	}

        Repeater repeater = RepeaterSupport.findParentRepeater(component);
        if (repeater != null && repeater.getRowIndex() > 0) {
            Object[] currentValues = (Object[])values[1];
            if ( currentValues != null ) {
                for (int i = 0; i < currentValues.length; ++i ) {
                    repeater.setRowIndex(i+1);
                    repeater.setChildValue(component, currentValues[i]);
                }
            }
        } else {
            value = values[1];
        }
        valueRef = (String) values[2];
	stateToRestore = null;
    }




}
