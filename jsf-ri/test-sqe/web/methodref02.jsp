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
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.faces.el.MethodBinding"
%><%@ page import="com.sun.faces.systest.model.TestBean"
%><%@ page import="com.sun.faces.systest.model.TestBeanSubclass"
%><%

  // Instantiate our test bean in request scope
  TestBeanSubclass bean = new TestBeanSubclass();
  FacesContext context = FacesContext.getCurrentInstance();
  context.getExternalContext().getRequestMap().put
   ("testMB", bean);
  MethodBinding mb;
  Class signature[] = new Class[] { String.class };
  Object params[] = new Object[1];
  Object result;

  // Access public methods defined on the bean class itself
  try {
      mb = context.getApplication().createMethodBinding
        ("#{testMB.setExtraProperty}", signature);
      params[0] = "New Extra Property Value";
      mb.invoke(context, params);
  } catch (Exception e) {
    out.println("/methodref02.jsp FAILED - setExtraProperty() exception: " + e);
    e.printStackTrace(System.out);
    return;
  }
  try {
      mb = context.getApplication().createMethodBinding
        ("#{testMB.getExtraProperty}", null);
      result = mb.invoke(context, null);
      if (!params[0].equals(result)) {
          out.println("/methodref02.jsp FAILED - getExtraProperty() returned: " + result);
          return;
      }
  } catch (Exception e) {
    out.println("/methodref02.jsp FAILED - getExtraProperty() exception: " + e);
    e.printStackTrace(System.out);
    return;
  }

  // Access public methods defined on the superclass
  try {
      mb = context.getApplication().createMethodBinding
        ("#{testMB.setStringProperty}", signature);
      params[0] = "New String Property Value";
      mb.invoke(context, params);
  } catch (Exception e) {
    out.println("/methodref02.jsp FAILED - setStringProperty() exception: " + e);
    e.printStackTrace(System.out);
    return;
  }
  try {
      mb = context.getApplication().createMethodBinding
        ("#{testMB.getStringProperty}", null);
      result = mb.invoke(context, null);
      if (!params[0].equals(result)) {
          out.println("/methodref02.jsp FAILED - getStringProperty() returned: " + result);
          return;
      }
  } catch (Exception e) {
    out.println("/methodref02.jsp FAILED - getStringProperty() exception: " + e);
    e.printStackTrace(System.out);
    return;
  }

  // Report success
  out.println("/methodref02.jsp PASSED");

%>
