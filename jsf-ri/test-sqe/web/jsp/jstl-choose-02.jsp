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

<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
<html>
<head>
<title>jstl-choose-02</title>
</head>
<body>
<h:outputText value="[1]"/>
<c:choose>
  <c:when test="${param.choose == 'a'}">
    <f:subview id="naming2a">
      <h:outputText value="[2a]"/>
      <h:outputText value="[2z]"/>
    </f:subview>
  </c:when>
  <c:when test="${param.choose == 'b'}">
    <f:subview id="naming2b">
      <h:outputText value="[2b]"/>
      <h:outputText value="[2y]"/>
    </f:subview>
  </c:when>
  <c:otherwise>
    <f:subview id="naming2c">
      <h:outputText value="[2c]"/>
      <h:outputText value="[2x]"/>
    </f:subview>
  </c:otherwise>
</c:choose>
<h:outputText value="[3]"/>
</body>
</html>
</f:view>
