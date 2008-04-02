<!--
Copyright 2004 Sun Microsystems, Inc. All rights reserved.
SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->
<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
    <html>
    <head>
        <title>interweaving06</title>
    </head>

    <body>
    <p><h:outputText
          value="Begin test jsp:include without subview and iterator tag in included page"/></p>
    <br/>

    <p>
        <jsp:include page="subviewIterator02.jsp"/>
    </p>

    <p><h:outputText value="Text from interweaving06.jsp"/></p>
    </p><h:outputText
          value="End test jsp:include without subview and iterator tag in included page"/>
    </p>
    </body>
    </html>
</f:view>
