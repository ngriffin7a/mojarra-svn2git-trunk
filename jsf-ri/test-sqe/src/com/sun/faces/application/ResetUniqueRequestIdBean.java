/*
 * $Id: ResetUniqueRequestIdBean.java,v 1.1 2005/07/25 18:34:13 rajprem Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import javax.faces.context.FacesContext;
import com.sun.faces.RIConstants;
import org.apache.commons.collections.LRUMap;

public class ResetUniqueRequestIdBean extends Object {

    public ResetUniqueRequestIdBean() {}

    protected String reset = "Unique Id Counter Has Been Reset";
    public String getReset() {
	FacesContext context = FacesContext.getCurrentInstance();
	LRUMap lruMap = new LRUMap(15);
	context.getExternalContext().getSessionMap().put(RIConstants.LOGICAL_VIEW_MAP, lruMap);
	((StateManagerImpl)context.getApplication().getStateManager()).requestIdSerial = (char) -1;
	return reset;
    }

    public void setReset(String newReset) {
	reset = newReset;
    }

}
