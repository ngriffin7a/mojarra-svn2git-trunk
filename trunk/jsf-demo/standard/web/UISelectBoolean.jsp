<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%-- $Id: UISelectBoolean.jsp,v 1.5 2004/01/27 21:31:43 eburns Exp $ --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>UISelectBoolean</title>
  </head>

  <body>

    <h1>UISelectBoolean</h1>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

     <f:loadBundle basename="standard.Resources" var="standardBundle"/>

     <jsp:useBean id="LoginBean" class="standard.LoginBean" scope="session" />
     <f:view>  

       <p>Form is rendered after this.</p>
     
       <h:form id="standardRenderKitForm" 
                  >

         <h:commandButton id="standardRenderKitSubmit" 
            action="success"
             value="#{standardBundle.standardRenderKitSubmitLabel}">
         </h:commandButton>

         <table width="100%" border="1" cellpadding="3" cellspacing="3">

<!-- Each included page should have table rows for the appropriate widget. -->

           <%@ include file="table_header.jsp" %>

           <%@ include file="selectboolean_checkbox_row.jsp" %>

         </table>

         <h:commandButton id="standardRenderKitSubmit1" 
             action="success"
             value="#{standardBundle.standardRenderKitSubmitLabel}">
         </h:commandButton>

       </h:form>

     </f:view>   


  </body>
</html>
