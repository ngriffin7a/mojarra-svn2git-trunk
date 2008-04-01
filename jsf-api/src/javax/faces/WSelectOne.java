package javax.faces;

import java.util.Iterator;

/**
 * Class for representing a user-interface component which allows
 * the user to select one value from many.
 */
public class WSelectOne extends WComponent {
    private static String TYPE = "SelectOne";

    /** 
     * Returns a String representing the select-one type.  
     *
     * @return a String object containing &quot;SelectOne&quot;
     *         
     */
    public String getType() {
	return TYPE;
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

}
