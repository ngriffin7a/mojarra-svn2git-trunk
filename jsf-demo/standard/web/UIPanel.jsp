<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%-- $Id: UIPanel.jsp,v 1.2 2003/08/25 16:37:16 jvisvanathan Exp $ --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>UIPanel</title>
    <link rel="stylesheet" type="text/css"
       href='<%= request.getContextPath() + "/stylesheet.css" %>'>
  </head>

  <body>

    <h1>UIPanel</h1>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

     <fmt:setBundle basename="standard.Resources" scope="session"
                    var="standardBundle"/>

     <jsp:useBean id="LoginBean" class="standard.LoginBean" scope="session" />
     <jsp:useBean id="ListBean" class="standard.ListBean" scope="session" />

     <f:view>  

       <p>Form is rendered after this.</p>
     
       <h:form id="standardRenderKitForm" 
                   formName="standardRenderKitForm">

         <h:command_button id="standardRenderKitSubmit" 
             action="success"
             key="standardRenderKitSubmitLabel"
             bundle="standardBundle">
         </h:command_button>

         <table width="100%" border="1" cellpadding="3" cellspacing="3">

<!-- Each included page should have table rows for the appropriate widget. -->

           <%@ include file="table_header.jsp" %>

           <%@ include file="panel_list_row.jsp" %>

           <%@ include file="panel_grid_row.jsp" %>

         </table>

         <h:command_button id="standardRenderKitSubmit1" 
             action="success"
             key="standardRenderKitSubmitLabel"
             bundle="standardBundle">
         </h:command_button>

       </h:form>

     </f:view>   


  </body>
</html>
