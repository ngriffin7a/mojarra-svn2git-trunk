/*
 * $Id: ConfigManagedBeanProperty.java,v 1.2 2003/04/29 20:51:33 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigManagedBeanProperty implements Cloneable {

    private String propertyName = null;
    private ConfigManagedBeanPropertyValue value = null;
    private ArrayList values;
    private ConfigManagedPropertyMap mapEntry = null;
    private ArrayList mapEntries;
    private String mapKeyClass = null;
    private String mapValueClass = null;

    public String getPropertyName() {
        return propertyName;
    }
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public ConfigManagedBeanPropertyValue getValue() {
        return value;
    }
    public void setValue(ConfigManagedBeanPropertyValue value) {
        this.value = value;
    }

    // For Array & Collection Property Assignments

    public void addValue(ConfigManagedBeanPropertyValue value) {
        if (values == null) {
            values = new ArrayList();
        }
        values.add(value);
    }
    public List getValues() {
        if (values == null) {
            return (Collections.EMPTY_LIST);
        } else {
            return values;
        }
    }

    // For Map Property Assignments

    public void setMapKeyClass(String mapKeyClass) {
        this.mapKeyClass = mapKeyClass;
    }
    public String getMapKeyClass() {
        return mapKeyClass;
    }
    public void setMapValueClass(String mapValueClass) {
        this.mapValueClass = mapValueClass;
    }
    public String getMapValueClass() {
        return mapValueClass;
    }

    public void addMapEntry(ConfigManagedPropertyMap mapEntry) {
        if (mapEntries == null) {
            mapEntries = new ArrayList();
        }
        mapEntries.add(mapEntry);
    }
    public List getMapEntries() {
        if (mapEntries == null) {
            return (Collections.EMPTY_LIST);
        } else {
            return mapEntries;
        }
    }

    // Utility Methods

    public boolean hasValuesArray() {
        if (getValues() != Collections.EMPTY_LIST) {
            return true;
        } else {
            return false;
        }
    }

    public String getArrayType() {
        String valueType = null;
        List list = getValues();
        for (int i=0; i<list.size(); i++) {
            ConfigManagedBeanPropertyValue cmpv = 
                (ConfigManagedBeanPropertyValue)list.get(i);
            if (cmpv.getValueCategory() == ConfigManagedBeanPropertyValue.VALUE_CLASS) {
                valueType = cmpv.getValue();
                break;
            }
        }
        return valueType;
    }

    public boolean hasMapEntries() {
        if (getMapEntries() != Collections.EMPTY_LIST) {
            return true;
        } else {
            return false;
        }
    }

    public Object clone() {
        ConfigManagedBeanProperty cmbp = null;

        try {
            cmbp = (ConfigManagedBeanProperty)super.clone();
            if (cmbp != null) {
                cmbp.value = (ConfigManagedBeanPropertyValue)cmbp.value.clone();
            }
            if (values != null) {
                cmbp.values = (ArrayList)values.clone();
            }
            if (mapEntries != null) {
                cmbp.mapEntries = (ArrayList)mapEntries.clone();
            }
        } catch (CloneNotSupportedException e) {
        }
        return cmbp;
    }
}
