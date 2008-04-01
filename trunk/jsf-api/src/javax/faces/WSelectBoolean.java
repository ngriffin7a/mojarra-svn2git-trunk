package javax.faces;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * Class for representing a user-interface component which allows
 * the user to select a boolean value.
 */
public class WSelectBoolean extends WComponent {
    private static String TYPE = "SelectBoolean";
    private boolean selected;
    private Object model = null;

//RWK:11-4-2001-kludge using temporarily for storing attributes...
//setAttribute/getAttribute should work differently...
    private Hashtable ht;

    public WSelectBoolean() {
        ht = new Hashtable();
    }

    /** 
     * Returns a String representing the select-boolean type.  
     *
     * @return a String object containing &quot;SelectBoolean&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

    // Aim10-30-01: the model is of type Object to give us
    // flexibility
    /**
     * Returns an Object which models the value of this component.
     * This object will be an instance of one of the following:
     * <ul>
     * <li>Boolean
     * </ul>
     * The component will maintain a reference to this object
     * (the value will not be copied).
     *
     * @return Object which models the value of this component
     */
    public Object getModel() {
        return model;
    }

    /**
     * Sets the model of this component to the specified object.
     * This object must be an instance of one of the following:
     * <ul>
     * <li>Boolean
     * </ul>
     *
     * @param value the Boolean object containing the true/false value
     * @throws NullPointerException if value is null
     */
    public void setModel(Object model) {
        this.model = model;
    }

    
    /**
     * Returns the component attribute with the given name
     * within the specified render context or null if there is the
     * specified attribute is not set on this component.
     *
     * @param rc the render context used to render this component
     * @param attributeName a String specifying the name of the attribute
     * @return the Object bound to the attribute name, or null if the
     *          attribute does not exist.
     */
    public Object getAttribute(RenderContext rc, String attributeName) {
        return ht.get(attributeName);
    }

    /**
     * Binds an object to the specified attribute name for this component
     * within the specified render context.
     *
     * @param rc the render context used to render this component
     * @param attributeName a String specifying the name of the attribute
     * @param value an Object representing the value of the attribute
     */
    public void setAttribute(RenderContext rc, String attributeName,
        Object value) {
        if (attributeName != null && value != null) {
            ht.put(attributeName,value);
        }
    }

    /**
     * Registers the specified listener name as a value-change listener
     * for this component.  The specified listener name must be registered
     * in the scoped namespace and it must be a listener which implements
     * the <code>ValueChangeListener</code> interface, else an exception will
     * be thrown.
     * @see ValueChangeListener
     * @param listenerName the name of the value-change listener
     * @throws FacesException if listenerName is not registered in the
     *         scoped namespace or if the object referred to by listenerName
     *         does not implement the <code>ValueChangeListener</code> interface.
     */
    public void addValueChangeListener(String listenerName) throws FacesException {
    }

    /**
     * Removes the specified listener name as a value-change listener
     * for this component.  
     * @param listenerName the name of the value-change listener
     * @throws FacesException if listenerName is not registered as a
     *         value-change listener for this component.
     */
    public void removeValueChangeListener(String listenerName) throws FacesException {
    }

    /**
     * @return Iterator containing the ValueChangeListener instances registered
     *         for this component
     */
    public Iterator getValueChangeListeners() {
	return null;
    }

    /**
     * Returns the current state for this component.
     * If this component's model property is non-null, it will
     * return the current value contained in the object
     * referenced by the model property. If the model property
     * is null, it will return a locally stored value.
     *
     * @see #getModel
     * @param rc the render context used to render this component
     * @return boolean containing the current state
     */
    public boolean isSelected(RenderContext rc) {

        boolean state = false;
        if ( model == null )  {
            return selected;
        }
        else {
            try {
                String state_str = (String) ModelAccessor.
                        getModelObject(rc, (String) model);
                state = (Boolean.valueOf(state_str)).booleanValue();
            } catch ( FacesException e ) {
                // PENDING (visvan) skip this exception ??
                return selected;
            }
            return state;
        }
    }

    /**
     * Sets the current state for this component.
     * If this component's model property is non-null, it will
     * store the new value in the object referenced by the
     * model property.  If the model property is null, it
     * will store the value locally.
     * @param rc the render context used to render this component
     * @param state boolean containing the new state for this component
     */
    public void setSelected(RenderContext rc, boolean state) {
        if ( model == null ) {
            selected = state;
        } else {
            try {
                String state_str = String.valueOf( state );
                ModelAccessor.setModelObject(rc,(String)model,state_str);
            } catch ( FacesException e ) {
                // PENDING ( visvan ) skip this exception ??
                selected = state;
            }
        }
    }
}

