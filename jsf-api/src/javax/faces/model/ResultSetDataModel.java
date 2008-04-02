/*
 * $Id: ResultSetDataModel.java,v 1.13 2003/10/20 20:26:01 craigmcc Exp $
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


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.faces.FacesException;
import javax.faces.el.PropertyNotFoundException;


/**
 * <p><strong>ResultSetDataModel</strong> is a convenience implementation of
 * {@link DataModel} that wraps a <code>ResultSet</code> of Java objects.
 * Note that the specified <code>ResultSet</code> <strong>MUST</strong>
 * be scrollable.  In addition, if input components (that will be updating
 * model values) reference this object in value reference expressions, the
 * specified <code>ResultSet</code> <strong>MUST</strong> be updatable.</p>
 */

public class ResultSetDataModel extends DataModel {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link ResultSetDataModel} with no specified
     * wrapped data.</p>
     */
    public ResultSetDataModel() {

        this(null);

    }


    /**
     * <p>Construct a new {@link ResultSetDataModel} wrapping the specified
     * <code>ResultSet</code>.</p>
     *
     * @param resultSet <code>ResultSet</code> to be wrapped (if any)
     */
    public ResultSetDataModel(ResultSet resultSet) {

        super();
        setWrappedData(resultSet);

    }


    // ------------------------------------------------------ Instance Variables


    // The row index for the row whose column values may be read or written
    private int current = -1;


    // The current row index (zero relative)
    private int index = -1;


    // The metadata for the ResultSet we are wrapping (lazily instantiated)
    private ResultSetMetaData metadata = null;


    // The ResultSet we are wrapping
    private ResultSet resultSet = null;


    // Has the row at the current index been updated?
    private boolean updated = false;


    // -------------------------------------------------------------- Properties


    /**
     * @exception FacesException {@inheritDoc}
     * @exception IllegalStateException (@inheritDoc}
     */ 
    public boolean isRowAvailable() {

        if (resultSet == null) {
            throw new IllegalStateException();
        } else if (index < 0) {
            return (false);
        }
        try {
            if (resultSet.absolute(index + 1)) {
                return (true);
            } else {
                return (false);
            }
        } catch (SQLException e) {
            throw new FacesException(e);
        }

    }


    /**
     * @exception FacesException {@inheritDoc}     
     * @exception IllegalStateException (@inheritDoc}
     */ 
    public int getRowCount() {

        if (resultSet == null) {
            throw new IllegalStateException();
        } else {
            return (-1);
        }

    }


    /**
     * @exception FacesException {@inheritDoc}     
     * @exception IllegalArgumentException (@inheritDoc}
     * @exception IllegalStateException (@inheritDoc}
     */ 
    public Object getRowData() {

        if (resultSet == null) {
            throw new IllegalStateException();
        } else if (!isRowAvailable()) {
            throw new IllegalArgumentException();
        }
        // PENDING(craigmcc) - Spec required behavior of the Map we create
        try {
            // NOTE:  isRowAvailable() positioned us already
            TreeMap map = new TreeMap(String.CASE_INSENSITIVE_ORDER);
            ResultSetMetaData metadata = getMetaData();
            int n = metadata.getColumnCount();
            for (int i = 1; i <= n; i++) {
                String name = metadata.getColumnName(i);
                map.put(name, resultSet.getObject(name));
            }
            return (map);
        } catch (SQLException e) {
            throw new FacesException(e);
        }

    }


    /**
     * @exception FacesException {@inheritDoc}     
     * @exception IllegalStateException (@inheritDoc}
     */ 
    public int getRowIndex() {

        if (resultSet == null) {
            throw new IllegalStateException();
        }
        return (index);

    }


    /**
     * @exception FacesException {@inheritDoc}
     * @exception IllegalArgumentException {@inheritDoc}
     * @exception IllegalStateException (@inheritDoc}
     */ 
    public void setRowIndex(int rowIndex) {

        if (resultSet == null) {
            throw new IllegalStateException();
        } else if (rowIndex < -1) {
            throw new IllegalArgumentException();
        }

        // Tell the ResultSet that the previous row was updated if necessary
        if (updated) {
            try {
                resultSet.updateRow();
                updated = false;
            } catch (SQLException e) {
                throw new FacesException(e);
            }
        }

        int old = index;
        index = rowIndex;
        if ((old != index) && (listeners != null)) {
            Object rowData = null;
            if (isRowAvailable()) {
                rowData = getRowData();
            }
            DataModelEvent event =
                new DataModelEvent(this, index, rowData);
            int n = listeners.size();
            for (int i = 0; i < n; i++) {
                ((DataModelListener) listeners.get(i)).rowSelected(event);
            }
        }


    }


    public Object getWrappedData() {

        return (this.resultSet);

    }


    /**
     * @exception ClassCastException {@inheritDoc}
     */
    public void setWrappedData(Object data) {

        if (data == null) {
            this.resultSet = null;
            this.metadata = null;
            return;
        }

        resultSet = (ResultSet) data;
        index = 0;
        updated = false;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the <code>ResultSetMetaData</code> for the
     * <code>ResultSet</code> we are wrapping, caching it the first time
     * it is returned.</p>
     *
     * @exception FacesException if the <code>ResultSetMetaData</code>
     *  cannot be acquired
     */
    private ResultSetMetaData getMetaData() {

        if (metadata == null) {
            try {
                metadata = resultSet.getMetaData();
            } catch (SQLException e) {
                throw new FacesException(e);
            }
        }
        return (metadata);

    }


    /**
     * <p>Mark the current row as having been updated, so that we will call
     * <code>updateRow()</code> before moving elsewhere.</p>
     */
    private void updated() {

        this.updated = true;

    }


    // --------------------------------------------------------- Private Classes


    // Private implementation of Map that delegates column get and put
    // operations to the underlying ResultSet, after setting the required
    // row index
    private class ResultSetMap extends TreeMap {

        public ResultSetMap(Comparator comparator) {
            super(comparator);
            index = ResultSetDataModel.this.index;
            try {
                resultSet.absolute(index + 1);
                int n = metadata.getColumnCount();
                for (int i = 1; i <= n; i++) {
                    super.put(metadata.getColumnName(i), null);
                }
            } catch (SQLException e) {
                throw new FacesException(e);
            }
        }

        // The zero-relative row index of our row
        private int index;

        // Removing entries is not allowed
        public void clear() {
            throw new UnsupportedOperationException();
        }

        public boolean containsValue(Object value) {
            Iterator keys = keySet().iterator();
            while (keys.hasNext()) {
                Object key = keys.next();
                Object contained = get(key);
                if (value == null) {
                    if (contained == null) {
                        return (true);
                    }
                } else {
                    if (value.equals(contained)) {
                        return (true);
                    }
                }
            }
            return (false);
        }

        public Set entrySet() {
            return (new ResultSetEntries(this));
        }

        public Object get(Object key) {
            if (!containsKey(key)) {
                return (null);
            }
            if (!(key instanceof String)) {
                throw new IllegalArgumentException();
            }
            try {
                resultSet.absolute(index + 1);
                return (resultSet.getObject((String) key));
            } catch (SQLException e) {
                throw new FacesException(e);
            }
        }

        public Object put(Object key, Object value) {
            if (!containsKey(key)) {
                throw new IllegalArgumentException();
            }
            if (!(key instanceof String)) {
                throw new IllegalArgumentException();
            }
            try {
                resultSet.absolute(index + 1);
                Object previous = resultSet.getObject((String) key);
                if ((previous == null) && (value == null)) {
                    return (previous);
                } else if ((previous != null) && (value != null) &&
                           !previous.equals(value)) {
                    return (previous);
                }
                resultSet.updateObject((String) key, value);
                ResultSetDataModel.this.updated();
                return (previous);
            } catch (SQLException e) {
                throw new FacesException(e);
            }
        }

        public void putAll(Map map) {
            Iterator keys = map.keySet().iterator();
            while (keys.hasNext()) {
                Object key = keys.next();
                put(key, map.get(key));
            }
        }

        // Removing entries is not allowed
        public Object remove(Object key) {
            throw new UnsupportedOperationException();
        }

        public Collection values() {
            return (new ResultSetValues(this));
        }

    }


    // Private implementation of Set that implements the entrySet() behavior
    // for ResultSetMap
    private class ResultSetEntries extends AbstractSet {

        public ResultSetEntries(ResultSetMap map) {
            this.map = map;
        }

        private ResultSetMap map;

        // Adding entries is not allowed
        public boolean add(Object o) {
            throw new UnsupportedOperationException();
        }

        // Adding entries is not allowed
        public boolean addAll(Collection c) {
            throw new UnsupportedOperationException();
        }

        // Removing entries is not allowed
        public void clear() {
            throw new UnsupportedOperationException();
        }

        public boolean contains(Object o) {
            if (o == null) {
                throw new NullPointerException();
            }
            Map.Entry e = (Map.Entry) o;
            Object k = e.getKey();
            Object v = e.getValue();
            if (!map.containsKey(k)) {
                return (false);
            }
            if (v == null) {
                return (map.get(k) == null);
            } else {
                return (v.equals(map.get(k)));
            }
        }

        public boolean isEmpty() {
            return (map.isEmpty());
        }

        public Iterator iterator() {
            return (new ResultSetEntriesIterator(map));
        }

        // Removing entries is not allowed
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        // Removing entries is not allowed
        public boolean removeAll(Collection c) {
            throw new UnsupportedOperationException();
        }

        // Removing entries is not allowed
        public boolean retainAll(Collection c) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return (map.size());
        }

    }


    // Private implementation of Iterator that implements the iterator()
    // behavior for the Set returned by entrySet() from ResultSetMap
    private class ResultSetEntriesIterator implements Iterator {

        public ResultSetEntriesIterator(ResultSetMap map) {
            this.map = map;
            this.keys = map.keySet().iterator();
        }

        private ResultSetMap map = null;
        private Iterator keys = null;

        public boolean hasNext() {
            return (keys.hasNext());
        }

        public Object next() {
            Object key = keys.next();
            return (new ResultSetEntry(map, key));
        }

        // Removing entries is not allowed
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }


    // Private implementation of Map.Entry that implements the behavior for
    // a single entry from the Set returned by entrySet() from ResultSetMap
    private class ResultSetEntry implements Map.Entry {

        public ResultSetEntry(ResultSetMap map, Object key) {
            this.map = map;
            this.key = key;
        }

        private ResultSetMap map;
        private Object key;

        public boolean equals(Object o) {
            if (o == null) {
                return (false);
            }
            if (!(o instanceof Map.Entry)) {
                return (false);
            }
            Map.Entry e = (Map.Entry) o;
            if (key == null) {
                if (e.getKey() != null) {
                    return (false);
                }
            } else {
                if (!key.equals(e.getKey())) {
                    return (false);
                }
            }
            Object v = map.get(key);
            if (v == null) {
                if (e.getValue() != null) {
                    return (false);
                }
            } else {
                if (!v.equals(e.getValue())) {
                    return (false);
                }
            }
            return (true);
        }

        public Object getKey() {
            return (key);
        }

        public Object getValue() {
            return (map.get(key));
        }

        public int hashCode() {
            Object value = map.get(key);
            return (((key == null) ? 0 : key.hashCode()) ^
                    ((value == null) ? 0 : value.hashCode()));
        }

        public Object setValue(Object value) {
            Object previous = map.get(key);
            map.put(key, value);
            return (previous);
        }

    }


    // Private implementation of Collection that implements the behavior
    // for the Collection returned by values() from ResultSetMap
    private class ResultSetValues extends AbstractCollection {

        public ResultSetValues(ResultSetMap map) {
            this.map = map;
        }

        private ResultSetMap map;

        public boolean contains(Object value) {
            return (map.containsValue(value));
        }

        public Iterator iterator() {
            return (new ResultSetValuesIterator(map));
        }


        public int size() {
            return (map.size());
        }

    }


    // Private implementation of Iterator that implements the behavior
    // for the Iterator returned by values().iterator() from ResultSetMap
    private class ResultSetValuesIterator implements Iterator {

        public ResultSetValuesIterator(ResultSetMap map) {
            this.map = map;
            this.keys = map.keySet().iterator();
        }

        private ResultSetMap map;
        private Iterator keys;

        public boolean hasNext() {
            return (keys.hasNext());
        }

        public Object next() {
            return (map.get(keys.next()));
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }


}
