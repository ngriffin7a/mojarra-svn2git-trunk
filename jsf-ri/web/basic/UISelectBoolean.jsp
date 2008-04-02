<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>UISelectBoolean</title>
  </head>

  <body>

    <h1>UISelectBoolean</h1>

    <h3>$Id: UISelectBoolean.jsp,v 1.10 2003/04/15 19:26:52 rkitain Exp $</h3>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

     <fmt:setBundle basename="basic.Resources" scope="session" 
                    var="basicBundle"/>

     <jsp:useBean id="LoginBean" class="basic.LoginBean" scope="session" />
     <f:use_faces>  

       <p>Form is rendered after this.</p>
     
       <h:form id="standardRenderKitForm" 
                   formName="standardRenderKitForm">

         <h:command_button id="standardRenderKitSubmit" 
             commandName="standardRenderKitSubmit" action="success"
             key="standardRenderKitSubmitLabel"
             bundle="basicBundle">
         </h:command_button>

         <table width="100%" border="1" cellpadding="3" cellspacing="3">

<!-- Each included page should have table rows for the appropriate widget. -->

           <%@ include file="table_header.jsp" %>

           <%@ include file="selectboolean_checkbox_row.jsp" %>

         </table>

         <h:command_button id="standardRenderKitSubmit1" 
             commandName="standardRenderKitSubmit" action="success"
             key="standardRenderKitSubmitLabel"
             bundle="basicBundle">
         </h:command_button>

       </h:form>

     </f:use_faces>   


  </body>
</html>
