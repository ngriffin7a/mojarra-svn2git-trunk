
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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Validators</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>Validators</h1>

<f:view>

  <h:form>

    <h:panelGrid columns="2">

<%-- Case 1: Custom Validator with "validatorId" attribute --%>

      <h:inputText id="text1"> 
        <f:validator validatorId="TestValidator01" />
      </h:inputText>

      <h:message for="text1" />

<%-- Case 2: Custom Validator with "binding" attribute --%>

      <h:inputText id="text2"> 
        <f:validator binding="#{validatorBean.validator}" />
      </h:inputText>

      <h:message for="text2" />

<%-- Case 3: "validatorId" and "binding" specified                        --%>
<%--         "binding" will set the instance (created from "validatorId") --%>
<%--         to a property on the backing bean                     --%>

      <h:inputText id="text3"> 
        <f:validator validatorId="TestValidator01"
           binding="#{validatorBean.validator}" />
      </h:inputText>

      <h:message for="text3" />

<%-- Bind the validator we created (Case 3) to the component --%>

      <h:inputText id="text4">
        <f:validator binding="#{validatorBean.validator}" />
      </h:inputText>

      <h:message for="text4" />

<%-- Double Range Validator with "binding" attribute --%>
                                                                                     
      <h:inputText id="text5">
        <f:validateDoubleRange binding="#{validatorBean.doubleValidator}" 
           maximum="2" />
      </h:inputText>
                                                                                     
      <h:message for="text5" />
                                                                                     
<%-- Length Validator with "binding" attribute --%>
                                                                                     
      <h:inputText id="text6">
        <f:validateLength binding="#{validatorBean.lengthValidator}" 
           maximum="5" />
      </h:inputText>
                                                                                     
      <h:message for="text6" />
                                                                                     
<%-- Long Range Validator with "binding" attribute --%>
                                                                                     
      <h:inputText id="text7">
        <f:validateLongRange binding="#{validatorBean.longRangeValidator}"
           maximum="4" />
      </h:inputText>
                                                                                     
      <h:message for="text7" />
                                                                                     

      <h:commandButton value="submit" /> <h:messages />

    </h:panelGrid>

  </h:form>

</f:view>

    <hr>
  </body>
</html>
