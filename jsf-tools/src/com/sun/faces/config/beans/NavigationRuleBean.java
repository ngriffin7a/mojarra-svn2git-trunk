/*
 * $Id: NavigationRuleBean.java,v 1.4 2005/08/22 22:12:17 ofung Exp $
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

package com.sun.faces.config.beans;


import java.util.ArrayList;
import java.util.List;


/**
 * <p>Configuration bean for <code>&lt;navigation-case&gt; element.</p>
 */

public class NavigationRuleBean extends FeatureBean {


    // -------------------------------------------------------------- Properties


    private String fromViewId = "*";
    public String getFromViewId() { return fromViewId; }
    public void setFromViewId(String fromViewId)
    { this.fromViewId = fromViewId; }


    private List navigationCases = new ArrayList();
    public NavigationCaseBean[] getNavigationCases() {
        NavigationCaseBean results[] =
            new NavigationCaseBean[navigationCases.size()];
        return ((NavigationCaseBean[]) navigationCases.toArray(results));
    }


    // -------------------------------------------------------------- Extensions


    // ----------------------------------------------------------------- Methods


    public void addNavigationCase(NavigationCaseBean navigationCase) {
        navigationCases.add(navigationCase);
    }


}
