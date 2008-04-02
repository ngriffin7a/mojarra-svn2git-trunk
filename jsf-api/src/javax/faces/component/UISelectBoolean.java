/*
 * $Id: UISelectBoolean.java,v 1.26 2003/03/13 01:11:59 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UISelectBoolean</strong> is a {@link UIComponent} that
 * represents a single boolean (<code>true</code> or <code>false</code>) value.
 * It is most commonly rendered as a checkbox.</p>
 *
 * <p>By default, the <code>rendererType</code> property is set to
 * "<code>Checkbox</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UISelectBoolean extends UIInput {


    // ------------------------------------------------------- Static Variables


    /**
     * <p>The component type of this {@link UIComponent} subclass.</p>
     */
    public static final String TYPE = "javax.faces.component.UISelectBoolean";


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Create a new {@link UISelectBoolean} instance with default property
     * values.</p>
     */
    public UISelectBoolean() {

        super();
        setRendererType("Checkbox");

    }


    // ------------------------------------------------------------- Attributes


    /**
     * <p>Return the local value of the selected state of this component.</p>
     */
    public boolean isSelected() {

        Boolean value = (Boolean) getValue();
        if (value != null) {
            return (value.booleanValue());
        } else {
            return (false);
        }

    }


    /**
     * <p>Set the local value of the selected state of this component.</p>
     *
     * @param selected The new selected state
     */
    public void setSelected(boolean selected) {

        if (selected) {
            setValue(Boolean.TRUE);
        } else {
            setValue(Boolean.FALSE);
        }

    }


    // ------------------------------------------------------------- Properties


    public String getComponentType() {

        return (TYPE);

    }


}
