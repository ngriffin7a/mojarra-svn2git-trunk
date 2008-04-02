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

<HTML>
    <HEAD> <title>Hello</title> </HEAD>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <body bgcolor="white">
    <f:view>
    <h:form id="helloForm" >
      <h2>Hi. My name is Duke.  I'm thinking of a number from
      <h:outputText value="#{UserNumberBean.minimum}"/> to
      <h:outputText value="#{UserNumberBean.maximum}"/>.  Can you guess
      it?</h2>

        <h:graphicImage id="waveImg" url="/wave.med.gif" />
  	<h:inputText id="userNo" value="#{UserNumberBean.userNumber}"
                      validator="#{UserNumberBean.validate}"/>          
	 <h:commandButton id="submit" action="success" value="Submit" />
         <p>
	 <h:message style="color: red; font-family: 'New Century Schoolbook', serif; font-style: oblique; text-decoration: overline" id="errors1" for="userNo"/>

    </h:form>
    </f:view>
</HTML>  
