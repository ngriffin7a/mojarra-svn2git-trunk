<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
<html>
<head>
<title>jstl-import-01</title>
</head>
<body>
<h:output_text value="[A]"/>
<c:import url="jstl-import-01a.jsp"/>
<h:output_text value="[C]"/>
<c:import url="jstl-import-01b.jsp"/>
<h:output_text value="[E]"/>
</body>
</html>
</f:view>
