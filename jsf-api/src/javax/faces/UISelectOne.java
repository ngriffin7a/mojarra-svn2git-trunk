/*
 * $Id: UISelectOne.java,v 1.3 2002/01/16 21:02:45 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Iterator;
import java.util.Hashtable;
import java.util.Collection;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 * Class for representing a user-interface component which allows
 * the user to select one value from many.
 */
public class UISelectOne extends UIComponent {
    private static String TYPE = "SelectOne";
    // PENDING(edburns): don't cast these to Strings all over the place.
    private Object modelRef = null;
    private Object selectedValueModelRef = null;
    private Collection items;
    private Object selectedItem;

    /** 
     * Returns a String representing the select-one type.  
     *
     * @return a String object containing &quot;SelectOne&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

    public Object getModel() {
        return modelRef;
    }

    public void setModel(Object newModelRef) {
	modelRef = newModelRef;
    }

    public Object getSelectedValueModel() {
        return selectedValueModelRef;
    }

    public void setSelectedValueModel(Object newSelectedValueModelRef) {
	selectedValueModelRef = newSelectedValueModelRef;
    }

    /**

    * @return true if modelRef and selectedValueModelRef are either both
    * null or both non-null.

    */

    private boolean checkModelConsistency() {
	boolean result = false;
	
	if (modelRef == null) {
	    result = (selectedValueModelRef == null);
	}
	if (selectedValueModelRef == null) {
	    result = (modelRef == null);
	}
	if (modelRef != null) {
	    result = (selectedValueModelRef != null);
	}
	if (selectedValueModelRef != null) {
	    result = (modelRef != null);
	}
	return result;
    }

    // This set of methods are the contract between the tag handler and
    // this UISelectOne instance.  The implementations of these methods
    // in turn access the model using the "model" set of methods.

    public void setItems(RenderContext rc, Collection newItems) {
	ParameterCheck.nonNull(rc);
	Assert.assert_it(checkModelConsistency());
	
	items = newItems;
	if (null != modelRef) {
	    try {
		rc.getObjectAccessor().setObject(rc.getRequest(), 
					       (String) modelRef, items);
		// Set this to null to insure the class invariant of
		// either having {modelRef, selectedValueModelRef} OR
		// having ivars {items, selectedItem}
		// The same holds for the other getters and setters;
		items = null;
	    } catch ( FacesException e ) {
	    }
	}
    }

    /**

    * PRECONDITION: we have a modelRef specified as a JSTL expression
    * string, such as "$ShipTypeBean.shipType".  This means there must
    * be an Object in the ObjectTable under the name "ShipTypeBean" and
    * this bean has a property called "shipType" whose value is a
    * Collection. <P>

    * POSTCONDITION: We return the Collection, if any.<P>

    * @return the collection of items.

    * @see addItem

    */
    
    public Collection getItems(RenderContext rc, String name) {
	ParameterCheck.nonNull(rc);
	ParameterCheck.nonNull(name);
	Assert.assert_it(checkModelConsistency());
	Collection modelItems = items;

	// We can't do anything without having a model
	if (null != modelRef) {
	    try {
		modelItems = (Collection) rc.getObjectAccessor().
		    getObject(rc.getRequest(), (String) modelRef);
		items = null;
	    } catch ( FacesException e ) {
	    }
	}
	return modelItems;
    }

    /**

    * PRECONDITION: same as getItems <P>

    * POSTCONDITION: same as getItems, but we return the selected value
    * from the bean. <P>

    * @see getItems

    */
    
    public Object getSelectedValue(RenderContext rc) { 
	ParameterCheck.nonNull(rc);
	Assert.assert_it(checkModelConsistency());
	Object result = selectedItem;

	// We can't do anything without having a model
	if (null != selectedValueModelRef) {
	    try {
		result = rc.getObjectAccessor().getObject(rc.getRequest(), 
							  (String) selectedValueModelRef);
		selectedItem = null;
	    } catch ( FacesException e ) {
	    }
	}
	return result;
    }

    /**

    * PRECONDITION: same as addItem <P>

    * POSTCONDITION: same as addItems, but we call the setter for the
    * model's selectedValueModel property.<P>.

    */
    
    public void setSelectedValue(RenderContext rc, Object value) {
	ParameterCheck.nonNull(rc);
	Assert.assert_it(checkModelConsistency());
	selectedItem = value;
	
	if (null != selectedValueModelRef) {
	    try {
		rc.getObjectAccessor().
		    setObject(rc.getRequest(),
			      (String) selectedValueModelRef, value);
		selectedItem = null;
	    } catch ( FacesException e ) {
	    }
	}
    }

    /**
     * Registers the specified listener id as a value-change listener
     * for this component.  The specified listener id must be registered
     * in the scoped namespace and it must be a listener which implements
     * the <code>ValueChangeListener</code> interface, else an exception will
     * be thrown.
     * @see ValueChangeListener
     * @param listenerId the id of the value-change listener
     * @throws FacesException if listenerId is not registered in the
     *         scoped namespace or if the object referred to by listeneIdr
     *         does not implement the <code>ValueChangeListener</code> interface.
     */
    public void addValueChangeListener(String listenerId) throws FacesException {
    }

    /**
     * Removes the specified listener id as a value-change listener
     * for this component.  
     * @param listenerId the id of the value-change listener
     * @throws FacesException if listenerId is not registered as a
     *         value-change listener for this component.
     */
    public void removeValueChangeListener(String listenerId) throws FacesException {
    }

    /**
     * @return Iterator containing the ValueChangeListener instances registered
     *         for this component
     */
    public Iterator getValueChangeListeners() {
	return null;
    }

}
