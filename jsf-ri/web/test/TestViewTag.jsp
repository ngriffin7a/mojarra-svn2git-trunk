<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>TestViewTag</title>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

  </head>

  <body>
    <h1>TestViewTag</h1>

<f:loadBundle basename="com.sun.faces.TestMessages" var="testMessages" />
<f:view locale="#{testMessages.localeFromBundle}">
<h:form styleClass="formClass" accept="html">

<table>

<tr>
<td>Name:</td>
<td><h:inputText value="Gilligan"/></td>
<td><h:commandButton value="submit"/></td>
</tr>
</table>

</h:form>
</f:view>


    <hr>
    <address><a href="mailto:Ed Burns <ed.burns@sun.com>"></a></address>
<!-- Created: Wed Oct 15 17:31:05 Eastern Daylight Time 2003 -->
<!-- hhmts start -->
Last modified: Sun Oct 19 10:33:56 EDT 2003
<!-- hhmts end -->
  </body>
</html>
