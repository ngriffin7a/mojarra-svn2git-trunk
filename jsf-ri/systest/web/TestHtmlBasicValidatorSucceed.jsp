<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<html>
<title>Validator Test Page</title>
<head>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
</head>
<body>

<h1>TLV commandButton, no lable or key</h1>
This page should Succeed.
<br>
<br>

<f:view>

  <h:commandButton value="hello"/>

  <h:commandButton value="hello"/>

</f:view>

</body>
</head>
</html>
