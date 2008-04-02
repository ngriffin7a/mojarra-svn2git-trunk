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

<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <title>Customer Details</title>
   <link rel="stylesheet" type="text/css"
            href='<%= request.getContextPath() + "/stylesheet.css" %>'>
    
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
    <%@ taglib uri="/WEB-INF/cardemo.tld" prefix="cd" %>
</head>
<body>
<fmt:setBundle
	    basename="cardemo.Resources"
	    scope="session" var="cardemoBundle"/>

<jsp:useBean id="CustomerBean" class="cardemo.CustomerBean" scope="session" />
<f:use_faces>
<h:form  formName="CustomerForm" modelReference="CustomerBean" >
<TABLE BORDER="0" WIDTH="660" BGCOLOR="#4F4F72">
<tr>
<td VALIGN=TOP WIDTH="100%"><img SRC="/cardemo/cardemo.jpg" BORDER=0 height=60 width=660
align=BOTTOM></td>
</tr>
</table>

<p>

<h:output_text 	id="customerTitle" key="customerTitle" bundle="carDemoBundle" /></FONT>
<FONT FACE="Arial, Helvetica">     
<table cellpadding="2" cellspacing="2" border="0">
  <tbody>
    <tr>
      <td valign="top" align="right">
      <h:output_text  key="titleLabel" bundle="carDemoBundle" /><br>



      </td>
      <td valign="top">
      <h:selectone_menu id="title">
		<h:selectitem  itemValue="mr" modelReference="CustomerBean.mr" selected="true"/>
		<h:selectitem  itemValue="mrs" modelReference="CustomerBean.mrs" />
		<h:selectitem  itemValue="ms" modelReference="CustomerBean.ms" />
    </h:selectone_menu><br>
      </td>
    </tr>
    <tr>
      <td valign="top" align="right">
      <h:output_text 	key="firstLabel" bundle="carDemoBundle" /><br>



      </td>
      <td valign="top">
      <h:input_text  modelReference="CustomerBean.firstName" > 
      </h:input_text><br>
      </td>
    </tr>
    <tr>
      <td valign="top" align="right">
      <h:output_text 	key="middleLabel" bundle="carDemoBundle" /><br>



      </td>
      <td valign="top">
        <h:input_text id="middleInitial" size="1" maxlength="1" 
            modelReference="CustomerBean.middleInitial" > 
            <f:validate_stringrange minimum="A" maximum="z"/> 
        </h:input_text>
        <h:output_errors  clientId="middleInitial"/> 
        <br>
      </td>
    </tr>
    <tr>
      <td valign="top" align="right">
      <h:output_text 	key="lastLabel" bundle="carDemoBundle" /><br>



      </td>
      <td valign="top">
      <h:input_text  modelReference="CustomerBean.lastName" >
      </h:input_text><br>
      </td>
    </tr>
    <tr>
      <td valign="top" align="right">
      <h:output_text 	 key="mailingLabel" bundle="carDemoBundle"/><br>


      </td>
      <td valign="top">
      <h:input_text  modelReference="CustomerBean.mailingAddress" /><br>
      </td>
    </tr>
    <tr>
      <td valign="top" align="right">
      <h:output_text 	 key="cityLabel" bundle="carDemoBundle" /><br>


      </td>
      <td valign="top">
      <h:input_text  modelReference="CustomerBean.city" /><br>
      </td>
    </tr>
    <tr>
      <td valign="top" align="right">
      <h:output_text 	key="stateLabel" bundle="carDemoBundle" /><br>


      </td>
      <td valign="top">
      <h:selectone_menu  modelReference="CustomerBean.state" >

		<h:selectitem  itemValue="AL" itemLabel="AL" selected="true" />
		<h:selectitem  itemValue="AK" itemLabel="AK"/>
		<h:selectitem  itemValue="AZ" itemLabel="AZ"/>
		<h:selectitem  itemValue="AR" itemLabel="AR"/>
		<h:selectitem  itemValue="CA" itemLabel="CA"/>
		<h:selectitem  itemValue="CO" itemLabel="CO"/>
		<h:selectitem  itemValue="CT" itemLabel="CT"/>
		<h:selectitem  itemValue="DE" itemLabel="DE"/>
		<h:selectitem  itemValue="FL" itemLabel="FL"/>
		<h:selectitem  itemValue="GA" itemLabel="GA"/>

		<h:selectitem  itemValue="HI" itemLabel="HI"/>
		<h:selectitem  itemValue="ID" itemLabel="ID"/>
		<h:selectitem  itemValue="IL" itemLabel="IL"/>
		<h:selectitem  itemValue="IN" itemLabel="IN"/>
		<h:selectitem  itemValue="IA" itemLabel="IA"/>
		<h:selectitem  itemValue="KS" itemLabel="KS"/>
		<h:selectitem  itemValue="KY" itemLabel="KY"/>
		<h:selectitem  itemValue="LA" itemLabel="LA"/>
		<h:selectitem  itemValue="ME" itemLabel="ME"/>
		<h:selectitem  itemValue="MD" itemLabel="MD"/>

		<h:selectitem  itemValue="MA" itemLabel="MA"/>
		<h:selectitem  itemValue="MI" itemLabel="MI"/>
		<h:selectitem  itemValue="MN" itemLabel="MN"/>
		<h:selectitem  itemValue="MO" itemLabel="MO"/>
		<h:selectitem  itemValue="MT" itemLabel="MT"/>
		<h:selectitem  itemValue="NE" itemLabel="NE"/>
		<h:selectitem  itemValue="NV" itemLabel="NV"/>
		<h:selectitem  itemValue="NH" itemLabel="NH"/>
		<h:selectitem  itemValue="NJ" itemLabel="NJ"/>
		<h:selectitem  itemValue="NM" itemLabel="NM"/>

		<h:selectitem  itemValue="MY" itemLabel="MY"/>
		<h:selectitem  itemValue="NC" itemLabel="NC"/>
		<h:selectitem  itemValue="ND" itemLabel="ND"/>
		<h:selectitem  itemValue="OH" itemLabel="OH"/>
		<h:selectitem  itemValue="OK" itemLabel="OK"/>
		<h:selectitem  itemValue="OR" itemLabel="OR"/>
		<h:selectitem  itemValue="PA" itemLabel="PA"/>
		<h:selectitem  itemValue="RI" itemLabel="RI"/>
		<h:selectitem  itemValue="SC" itemLabel="SC"/>
		<h:selectitem  itemValue="SD" itemLabel="SD"/>

		<h:selectitem  itemValue="TN" itemLabel="TN"/>
		<h:selectitem  itemValue="TX" itemLabel="TX"/>
		<h:selectitem  itemValue="UT" itemLabel="UT"/>
		<h:selectitem  itemValue="VT" itemLabel="VT"/>
		<h:selectitem  itemValue="VA" itemLabel="VA"/>
		<h:selectitem  itemValue="WA" itemLabel="WA"/>
		<h:selectitem  itemValue="WV" itemLabel="WV"/>
		<h:selectitem  itemValue="WI" itemLabel="WI"/>
		<h:selectitem  itemValue="WY" itemLabel="WY"/>

	      </h:selectone_menu><br>
      </td>
    </tr>
    <tr>
      <td valign="top" align="right">
      <h:output_text  key="zipLabel" bundle="carDemoBundle" /><br>


      </td>
      <td valign="top">
      <h:input_number id="zip"  formatPattern="#####"
			modelReference="CustomerBean.zip"
                        size="5">
        <f:validate_longrange minimum="10000" maximum="99999" /> 
      </h:input_number>
      <h:output_errors  clientId="zip" />    
            <br>
      </td>
    </tr>
    <tr>
    <tr>
      <td valign="top" align="right">
      <h:output_text  key="ccNumberLabel" bundle="carDemoBundle" /><br>


      </td>
      <td valign="top">
      <h:input_text id="ccno" modelReference="CustomerBean.month" size="16"
           converter="creditcard" >
          <f:validate_required/>
          <f:validate_length minimum="16" maximum="16"/>
          <cd:creditcard_validator maximumChar="9" minimumChar="0" />
          
      </h:input_text>
      <h:output_errors  clientId="ccno"/> <br>
      </td>
    </tr>
    <tr>
      <td valign="top" align="right">
      <h:output_text  key="monthLabel" bundle="carDemoBundle" /><br>



      </td>
      <td valign="top"><h:selectone_menu  modelReference="CustomerBean.month">
        <h:selectitem itemValue="01" itemLabel="01"/>
        <h:selectitem itemValue="02" itemLabel="02"/>
        <h:selectitem itemValue="03" itemLabel="03"/>
        <h:selectitem itemValue="04" itemLabel="04"/>
        <h:selectitem itemValue="05" itemLabel="05"/>
        <h:selectitem itemValue="06" itemLabel="06"/>
        <h:selectitem itemValue="07" itemLabel="07"/>
        <h:selectitem itemValue="08" itemLabel="08"/>
        <h:selectitem itemValue="09" itemLabel="09"/>
        <h:selectitem itemValue="10" itemLabel="10"/>
        <h:selectitem itemValue="11" itemLabel="11"/>
        <h:selectitem itemValue="12" itemLabel="12"/>
    </h:selectone_menu>

<h:selectone_menu  modelReference="CustomerBean.year" >
        <h:selectitem itemValue="2002" itemLabel="2002"/>
        <h:selectitem itemValue="2003" itemLabel="2003"/>
        <h:selectitem itemValue="2004" itemLabel="2004"/>
        <h:selectitem itemValue="2005" itemLabel="2005"/>
        <h:selectitem itemValue="2006" itemLabel="2006"/>
        <h:selectitem itemValue="2007" itemLabel="2007"/>
        <h:selectitem itemValue="2008" itemLabel="2008"/>
    </h:selectone_menu><br>
      </td>
    </tr>
  </tbody>
</table>
</FONT>
    
<h:command_button  key="finishButton" bundle="carDemoBundle" commandName="finish"/>



<hr WIDTH="100%">
<p>
<h:graphic_image id="duke" url="/duke.gif" /><br>
<h:output_text  key="buyLabel" bundle="carDemoBundle" />
<br>
</h:form>
</f:use_faces>
</body>
</html>

