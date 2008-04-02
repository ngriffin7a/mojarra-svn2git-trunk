/*
 * $Id: UIPanel.java,v 1.17 2003/09/30 14:35:01 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;


/**
 * <p><strong>UIPanel</strong> is a {@link UIComponent} that manages the
 * layout of its child components.</p>
 */

public class UIPanel extends UIComponentBase implements ValueHolder {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIPanel} instance with default property
     * values.</p>
     */
    public UIPanel() {

        super();
        setRendererType(null);

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The {@link ValueHolderSupport} instance to which we delegate
     * our {@link ValueHolder} implementation processing.</p>
     */
    private ValueHolderSupport support = new ValueHolderSupport(this);



    // -------------------------------------------------- UIComponent Properties


    /**
     * <p>Return <code>true</code> to indicate that this component takes
     * responsibility for rendering its children.</p>
     */
    public boolean getRendersChildren() {

        return (true);

    }


    // -------------------------------------------------- ValueHolder Properties


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
        List[] supportList = 
            context.getApplication().getViewHandler().getStateManager().
            restoreAttachedObjectState(context, values[1], null, this);
	if (supportList != null) {
            List theSupport = supportList[0];
            if ((theSupport != null) && (theSupport.size() > 0)) {
                support = (ValueHolderSupport) theSupport.get(0);
		support.setComponent(this);
            }
	}

    }


}
