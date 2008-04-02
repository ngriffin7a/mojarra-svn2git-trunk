<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
-->

<%@ page contentType="text/html"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.component.UIComponent"
%><%@ page import="com.sun.faces.systest.TestComponent"
%><%

  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
   FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();

  // replace mappings provided by the JSF implementation in order to 
  // customize the behavior of standard JSF features.
  UIComponent oldForm = appl.createComponent("javax.faces.Form");
  appl.addComponent("javax.faces.Form", "com.sun.faces.systest.TestComponent");

  // try to retrieve our component from Application
  UIComponent result = appl.createComponent("javax.faces.Form");
  // report the result
  if (result == null || 
      !(result instanceof com.sun.faces.systest.TestComponent)) {
    out.println("/component01.jsp FAILED");
    return;
  } else {
      out.println("/component01.jsp PASSED");
  }

  // restore the old mapping
  appl.addComponent("javax.faces.Form", oldForm.getClass().getName());
  
%>
