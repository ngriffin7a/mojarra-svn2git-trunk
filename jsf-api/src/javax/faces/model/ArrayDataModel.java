/*
 * $Id: ArrayDataModel.java,v 1.12 2004/01/23 04:24:18 craigmcc Exp $
 */

/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package javax.faces.model;


import javax.faces.FacesException;


/**
 * <p><strong>ArrayDataModel</strong> is a convenience implementation of
 * {@link DataModel} that wraps an array of Java objects.</p>
 */

public class ArrayDataModel extends DataModel {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link ArrayDataModel} with no specified
     * wrapped data.</p>
     */
    public ArrayDataModel() {

        this(null);

    }


    /**
     * <p>Construct a new {@link ArrayDataModel} wrapping the specified
     * array.</p>
     *
     * @param array Array to be wrapped (if any)
     */
    public ArrayDataModel(Object array[]) {

        super();
        setWrappedData(array);

    }


    // ------------------------------------------------------ Instance Variables


    // The array we are wrapping
    private Object array[];


    // The current row index (zero relative)
    private int index = -1;


    // -------------------------------------------------------------- Properties


    /**
     * @exception FacesException {@inheritDoc}
     */ 
    public boolean isRowAvailable() {

        if (array == null) {
	    return (false);
        } else if ((index >= 0) && (index < array.length)) {
            return (true);
        } else {
            return (false);
        }

    }


    /**
     * @exception FacesException {@inheritDoc}     
     */ 
    public int getRowCount() {

        if (array == null) {
	    return (-1);
        }
        return (array.length);

    }


    /**
     * @exception FacesException {@inheritDoc}     
     * @exception IllegalArgumentException {@inheritDoc}     
     */ 
    public Object getRowData() {

        if (array == null) {
	    return (null);
        } else if (!isRowAvailable()) {
            throw new IllegalArgumentException();
        } else {
            return (array[index]);
        }

    }


    /**
     * @exception FacesException {@inheritDoc}     
     */ 
    public int getRowIndex() {

        return (index);

    }


    /**
     * @exception FacesException {@inheritDoc}
     * @exception IllegalArgumentException {@inheritDoc}
     */ 
    public void setRowIndex(int rowIndex) {

        if (rowIndex < -1) {
            throw new IllegalArgumentException();
        }
        int old = index;
        index = rowIndex;
	if (array == null) {
	    return;
	}
	DataModelListener [] listeners = getDataModelListeners();
        if ((old != index) && (listeners != null)) {
            Object rowData = null;
            if (isRowAvailable()) {
                rowData = getRowData();
            }
            DataModelEvent event =
                new DataModelEvent(this, index, rowData);
            int n = listeners.length;
            for (int i = 0; i < n; i++) {
		if (null != listeners[i]) {
		    listeners[i].rowSelected(event);
		}
            }
        }

    }


    public Object getWrappedData() {

        return (this.array);

    }


    /**
     * @exception ClassCastException {@inheritDoc}
     */
    public void setWrappedData(Object data) {

        if (data == null) {
            array = null;
            setRowIndex(-1);
        } else {
            array = (Object[]) data;
            index = -1;
            setRowIndex(0);
        }

    }


}
