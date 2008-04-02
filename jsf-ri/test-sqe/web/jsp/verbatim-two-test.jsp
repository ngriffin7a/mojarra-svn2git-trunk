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

<html>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<f:view>
<head>
<title><h:outputText id="title" value="title"/></title>
</head>

<body>
<h:form id="form">
   <h:panelGrid id="panel1" columns="2" styleClass="book"
      columnClasses="menuColumn, chapterColumn">

      <f:verbatim >
         verbatim one text here
      </f:verbatim>

      <h:panelGrid id="panel2" columns="1" >
         <h:outputText id="outputheader" value="this is the header" />
         <f:verbatim><hr/></f:verbatim>
      </h:panelGrid>

      <h:commandButton id="submit" value="submit"/>

      <f:verbatim >
         verbatim two text here
      </f:verbatim>

   </h:panelGrid>
</h:form>
</body>
</f:view>
</html>
