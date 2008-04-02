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
   <title>CarDemo</title>
</head>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsf/demo/components" prefix="d" %>

     <fmt:setBundle
	    basename="cardemo.Resources"
	    scope="session" var="carDemoBundle"/>

<f:use_faces>
<h:form  formName="imageMapForm" >
    <p><font color="blue"><h:output_text id="chooseLocale" key="chooseLocale" bundle="carDemoBundle" /></font> </p>
    <table>
          <tr> <td> 
          <d:image id="mapImage" url="/world.jpg" usemap="#worldMap" />
                <d:map id="worldMap" currentArea="NAmericas" >
                    <f:action_listener type="cardemo.ImageMapEventHandler"/> 
                        <d:area id="NAmericas" modelReference="NA" onmouseover="/cardemo/world_namer.jpg" onmouseout="/cardemo/world.jpg" />
                        <d:area id="SAmericas" modelReference="SA" onmouseover="/cardemo/world_samer.jpg" onmouseout="/cardemo/world.jpg" />
                        <d:area id="Germany" modelReference="gerA" onmouseover="/cardemo/world_germany.jpg" onmouseout="/cardemo/world.jpg" />
                        <d:area id="France" modelReference="fraA" onmouseover="/cardemo/world_france.jpg" onmouseout="/cardemo/world.jpg" />
                </d:map>
     </td> </tr>
    </table>

</h:form>
</f:use_faces>
</html>
