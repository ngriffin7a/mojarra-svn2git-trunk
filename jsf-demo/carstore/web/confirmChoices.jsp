<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<!--
 Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 
 Redistribution and use in source and binary forms, with or
 without modification, are permitted provided that the following
 conditions are met:
 
 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
 
 - Redistribution in binary form must reproduce the above
   copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials
   provided with the distribution.
    
 Neither the name of Sun Microsystems, Inc. or the names of
 contributors may be used to endorse or promote products derived
 from this software without specific prior written permission.
  
 This software is provided "AS IS," without a warranty of any
 kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
  
 You acknowledge that this software is not designed, licensed or
 intended for use in the design, construction, operation or
 maintenance of any nuclear facility.
-->

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<HTML>

<HEAD>
	<TITLE>Welcome to CarStore</TITLE>
        <link rel="stylesheet" type="text/css"
            href='<%= request.getContextPath() + "/stylesheet.css" %>'>
</HEAD>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<BODY BGCOLOR="white">

<f:loadBundle basename="carstore.bundles.Resources" var="bundle"/>
<f:view>  
<h:form>
 
<h:panel_grid id="mainPanel" columns="1" footerClass="subtitle"
   styleClass="medium" columnClasses="medium">

  <h:graphic_image  url="/images/cardemo.jpg" />
  <h:output_text binding="#{carstore.currentModel.components.title}" />
    
<h:panel_grid columns="2"  footerClass="subtitle"
   headerClass="subtitlebig" styleClass="medium" columnClasses="subtitle,medium">
    
    <f:facet name="header">
      <h:output_text  value="#{bundle.buyTitle}" />
    </f:facet>

    <h:output_text value="#{bundle.Engine}" />

    <h:output_text value="#{carstore.currentModel.attributes.engine}"  />

    <h:output_text value="#{bundle.Brakes}" />

    <h:output_text  value="#{carstore.currentModel.attributes.brake}" />

    <h:output_text  value="#{bundle.Suspension}" />

    <h:output_text  value="#{carstore.currentModel.attributes.suspension}" />

    <h:output_text  value="#{bundle.Speakers}" />

    <h:output_text  value="#{carstore.currentModel.attributes.speaker}" />

    <h:output_text  value="#{bundle.Audio}" />

    <h:output_text  value="#{carstore.currentModel.attributes.audio}" />

    <h:output_text  value="#{bundle.Transmission}" />

    <h:output_text  value="#{carstore.currentModel.attributes.transmission}" />

    <h:output_text  value="#{bundle.sunroofLabel}"  />

    <h:output_text  value="#{carstore.currentModel.attributes.sunroof}" />

    <h:output_text  value="#{bundle.cruiseLabel}"  />

    <h:output_text  value="#{carstore.currentModel.attributes.cruisecontrol}" />

    <h:output_text value="#{bundle.keylessLabel}"  />

    <h:output_text  value="#{carstore.currentModel.attributes.keylessentry}" />

    <h:output_text  value="#{bundle.securityLabel}"  />

    <h:output_text  value="#{carstore.currentModel.attributes.securitySystem}" />

    <h:output_text  value="#{bundle.skiRackLabel}"  />

    <h:output_text  value="#{carstore.currentModel.attributes.skiRack}" />

    <h:output_text  value="#{bundle.towPkgLabel}"  />

    <h:output_text  value="#{carstore.currentModel.attributes.towPackage}" />

    <h:output_text  value="#{bundle.gpsLabel}"  />

    <h:output_text  value="#{carstore.currentModel.attributes.gps}" />
    
  <f:facet name="footer">
     <h:panel_group>
        <h:output_text  value="#{bundle.yourPriceLabel}"  />
        &nbsp;
        <h:output_text  value="#{carstore.currentModel.currentPrice}" />
     </h:panel_group>
  </f:facet>

</h:panel_grid>

<h:panel_group>
<h:command_button value="#{bundle.buy}" action="customerInfo" title="#{bundle.buy}" />
<h:command_button value="#{bundle.back}" action="carDetail" title="#{bundle.back}"/>
</h:panel_group>

</h:panel_grid>
</h:form>
<jsp:include page="bottomMatter.jsp"/>
</f:view>

</BODY>
</HTML>

