/*
 * $Id: PrintTreePhaseListener.java,v 1.2 2005/08/22 22:10:55 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.systest.model;

import javax.faces.event.PhaseListener;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseEvent;

import com.sun.faces.util.DebugUtil;
import javax.faces.context.FacesContext;

public class PrintTreePhaseListener extends Object implements PhaseListener {
    
    public void afterPhase(PhaseEvent event) {
	DebugUtil.printTree(FacesContext.getCurrentInstance().getViewRoot(),
			    System.out);
    }


    public void beforePhase(PhaseEvent event) {
	
    }

    public PhaseId getPhaseId() {
	return PhaseId.ANY_PHASE;
    }

}
