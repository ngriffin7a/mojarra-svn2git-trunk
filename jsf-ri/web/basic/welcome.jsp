<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ page extends="com.sun.faces.Page" %>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>
    <H3> JSF Basic Components Test Page  </H3>
    <hr>
    <faces:UseFaces>

            <faces:Output_Text id='hello_label' value='Login Successful' />
             <P></P>
    </faces:UseFaces>
</HTML>
