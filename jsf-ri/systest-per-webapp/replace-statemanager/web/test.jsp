<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>test</title>
    <%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
  </head>

  <body>
    <h1>test</h1>

<f:view>

  <h:form>

    <h:outputText value="#{test2.stringProperty}" />

    <h:inputText /> <br />

    <h:commandButton value="submit" /> <br />

    <h:outputText value="#{replaceApplication.stateManagerClass}" /> <br />

    <h:outputText value="#{replaceApplication.viewHandlerClass}" /> <br />

    <h:outputText value="#{replaceApplication.applicationClass}" />

  </h:form>

</f:view>

    <hr>
  </body>
</html>
