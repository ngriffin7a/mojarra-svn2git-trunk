/*
 * $Id: TestActionListener01.java,v 1.4 2006/03/29 23:03:59 rlubke Exp $
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

package com.sun.faces.systest;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
import javax.faces.component.StateHolder;
import javax.faces.event.AbortProcessingException;

public class TestActionListener01 implements ActionListener {
    
    public TestActionListener01() {}

    public void processAction(ActionEvent ae)
        throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(ae.getComponent().getClientId(context),
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            	ae.getComponent().getId() + " was pressed", null));
    } 
    
    
}
