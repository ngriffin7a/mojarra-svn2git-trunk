<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>UISelectOne</title>
  </head>

  <body>

    <h1>UISelectOne</h1>

    <h3>$Id: UISelectOne.jsp,v 1.1 2002/09/10 20:18:41 edburns Exp $</h3>

    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="faces" %>

     <fmt:setBundle basename="basic.Resources" scope="session" 
                    var="basicBundle"/>

     <faces:usefaces>  

       <p>Form is rendered after this.</p>
     
       <faces:form id="standardRenderKitForm" 
                   formName="standardRenderKitForm">

         <faces:command_button id="standardRenderKitSubmit" 
                      commandName="standardRenderKitSubmit">
             <faces:output_text id="stdRenderKitLabel" key="standardRenderKitSubmitLabel"
                      bundle="${basicBundle}" />
         </faces:command_button>

         <table width="100%" border="1" cellpadding="3" cellspacing="3">

<!-- Each included page should have table rows for the appropriate widget. -->

           <%@ include file="table_header.jsp" %>

           <%@ include file="selectone_listbox_row.jsp" %>

           <%@ include file="selectone_menu_row.jsp" %>

           <%@ include file="selectone_radio_row.jsp" %>


         </table>

         <faces:command_button id="standardRenderKitSubmit" 
                      commandName="standardRenderKitSubmit">
             <faces:output_text id="stdRenderKitLabel" key="standardRenderKitSubmitLabel"
                      bundle="${basicBundle}" />
         </faces:command_button>

       </faces:form>

     </faces:usefaces>   


  </body>
</html>
