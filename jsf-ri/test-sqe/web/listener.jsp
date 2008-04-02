
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
    <title>ActionListeners and ValueChangeListeners</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>ActionListeners and ValueChangeListeners</h1>

<f:view>

  <h:form>

    <p>Enter some text:</p>

    <h:panelGrid columns="2">

<%-- Case 1: "type" specified --%>

      <h:inputText id="text1"> 
          <f:valueChangeListener type="com.sun.faces.systest.TestValueChangeListener01"/>
      </h:inputText>

      <h:message for="text1" />

<%-- Case 2: "binding" specified --%>

      <h:inputText id="text2">
          <f:valueChangeListener binding="#{TestValueChangeListener01}"/>
      </h:inputText>

      <h:message for="text2" />

<%-- Case 3: "type" and "binding" specified                        --%>
<%--         "binding" will set the instance (created from "type") --%>
<%--         to a property on the backing bean                     --%>

      <h:inputText id="text3">
          <f:valueChangeListener type="com.sun.faces.systest.TestValueChangeListener01" binding="#{listenerBean.valueChangeListener}"/>
      </h:inputText>      

      <h:message for="text3" />

<%-- Bind the listener we created (Case 3) to the component --%>

      <h:inputText id="text4">
          <f:valueChangeListener binding="#{listenerBean.valueChangeListener}"/>
      </h:inputText>

      <h:message for="text4" />

<%-- Case 1: "type" specified --%>

      <h:commandButton id="button1" value="submit" > 
          <f:actionListener type="com.sun.faces.systest.TestActionListener01"/>
      </h:commandButton>

      <h:message for="button1" />

<%-- Case 2: "binding" specified --%>

      <h:commandButton id="button2" value="submit" > 
          <f:actionListener binding="#{TestActionListener01}"/>
      </h:commandButton>

      <h:message for="button2" />

<%-- Case 3: "type" and "binding" specified                        --%>
<%--         "binding" will set the instance (created from "type") --%>
<%--         to a property on the backing bean                     --%>

      <h:commandButton id="button3"  value="submit" > 
          <f:actionListener type="com.sun.faces.systest.TestActionListener01"
              binding="#{listenerBean.actionListener}"/>
      </h:commandButton>

      <h:message for="button3" />

<%-- Bind the listener we created (Case 3) to the component --%>

      <h:commandButton id="button4" value="submit" >
          <f:actionListener binding="#{listenerBean.actionListener}"/>      
      </h:commandButton>

      <h:message for="button4" />

      <h:messages />

    </h:panelGrid>

  </h:form>

</f:view>

    <hr>
  </body>
</html>
