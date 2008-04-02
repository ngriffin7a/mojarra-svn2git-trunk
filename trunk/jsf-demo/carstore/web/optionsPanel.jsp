<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->
<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:subview id="optionsPanel">

  <h:panelGrid>

    <h:outputText value="#{bundle.OptionsPackages}" />

    <!-- options package chooser -->

    <h:panelGrid columns="4">    

      <h:commandButton id="Custom" value="#{bundle.Custom}"
          styleClass="#{carstore.customizers.Custom.buttonStyle}"
          actionListener="#{carstore.choosePackage}" />

      <h:commandButton id="Standard" value="#{bundle.Standard}"
          styleClass="#{carstore.customizers.Standard.buttonStyle}"
          actionListener="#{carstore.choosePackage}" />

      <h:commandButton id="Performance" value="#{bundle.Performance}"
          styleClass="#{carstore.customizers.Performance.buttonStyle}"
          actionListener="#{carstore.choosePackage}" />

      <h:commandButton id="Deluxe" value="#{bundle.Deluxe}"
          styleClass="#{carstore.customizers.Deluxe.buttonStyle}"
          actionListener="#{carstore.choosePackage}" />

    </h:panelGrid>

  </h:panelGrid>

  <h:panelGrid columns="2">

        <h:outputText value="#{bundle.Engine}" 
                       styleClass="optionLabel"/>

         <h:selectoneMenu styleClass="optionValue"
               binding="#{carstore.currentModel.components.engine}"/>

          <h:outputText value="#{bundle.Brakes}" 
               styleClass="optionLabel" />

          <h:selectoneRadio styleClass="optionValue"
               binding="#{carstore.currentModel.components.brake}"/>

         <h:outputText value="#{bundle.Suspension}" 
                       styleClass="optionLabel"/>

        <h:selectoneMenu  styleClass="optionValue"
                   binding="#{carstore.currentModel.components.suspension}"/>

        <h:outputText value="#{bundle.Speakers}" 
                       styleClass="optionLabel"/>

        <h:selectoneRadio  styleClass="optionValue"
           binding="#{carstore.currentModel.components.speaker}"/>

        <h:outputText value="#{bundle.Audio}" 
                       styleClass="optionLabel"/>

        <h:selectoneRadio styleClass="optionValue"
               binding="#{carstore.currentModel.components.audio}"/>

        <h:outputText value="#{bundle.Transmission}" 
                       styleClass="optionLabel"/>

        <h:selectoneMenu  styleClass="optionValue"
               binding="#{carstore.currentModel.components.transmission}"/>

      </h:panelGrid>

   <h:outputText value="#{bundle.OtherOptions}" 
                   styleClass="optionLabel"/>

   <h:panelGrid columns="6">

     <h:selectbooleanCheckbox title="#{bundle.sunroofLabel}" 
         binding="#{carstore.currentModel.components.sunroof}">
     </h:selectbooleanCheckbox>    

     <h:outputText value="#{bundle.sunroofLabel}" /> 

     <h:selectbooleanCheckbox  title="#{bundle.cruiseLabel}"  
         binding="#{carstore.currentModel.components.cruisecontrol}" >
     </h:selectbooleanCheckbox>

     <h:outputText value="#{bundle.cruiseLabel}" /> 

     <h:selectbooleanCheckbox title="#{bundle.keylessLabel}"  
          binding="#{carstore.currentModel.components.keylessentry}" >
     </h:selectbooleanCheckbox>

     <h:outputText value="#{bundle.keylessLabel}" /> 

     <h:selectbooleanCheckbox 
        title="#{bundle.securityLabel}" 
        binding="#{carstore.currentModel.components.securitySystem}" >
     </h:selectbooleanCheckbox>

     <h:outputText value="#{bundle.securityLabel}" />  

     <h:selectbooleanCheckbox  title="#{bundle.skiRackLabel}"  
         binding="#{carstore.currentModel.components.skiRack}" >
     </h:selectbooleanCheckbox>

     <h:outputText value="#{bundle.skiRackLabel}" /> 

     <h:selectbooleanCheckbox  title="#{bundle.towPkgLabel}"  
           binding="#{carstore.currentModel.components.towPackage}" >
     </h:selectbooleanCheckbox>

     <h:outputText value="#{bundle.towPkgLabel}" /> 

     <h:selectbooleanCheckbox  title="#{bundle.gpsLabel}" 
         binding="#{carstore.currentModel.components.gps}" >
     </h:selectbooleanCheckbox>

     <h:outputText value="#{bundle.gpsLabel}" /> 

   </h:panelGrid>

</f:subview>
