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
<%@ taglib uri="http://java.sun.com/jsf/demo/components" prefix="d" %>

<f:view>

<html>
<head>
<title>Welcome to JavaServer Faces</title>
</head>
<body>

  <f:loadBundle basename="demo.model.Resources" var="mapBundle"/>
  <h:form>
  <table>

    <tr><td>
      <h:output_text  id="welcomeLabel" 
                                    value="#{mapBundle.welcomeLabel}" />
    </td></tr>

    <tr><td>

      <h:graphic_image id="mapImage"
                      url="/images/world.gif"
                   usemap="#worldMap"/>

      <d:map           id="worldMap"
                  current="NAmericas">

        <d:area_selected
                     type="demo.model.AreaSelectedHandler"/>

        <d:area        id="NAmericas"
                 valueRef="#{NA}"
              onmouseover="/images/world_namer.gif"
               onmouseout="/images/world.gif"
               targetImage="mapImage"/>

        <d:area        id="SAmericas"
                 valueRef="#{SA}"
              onmouseover="/images/world_samer.gif"
               onmouseout="/images/world.gif"
               targetImage="mapImage"/>

        <d:area        id="Finland"
                 valueRef="#{finA}"
              onmouseover="/images/world_finland.gif"
               onmouseout="/images/world.gif"
               targetImage="mapImage"/>

        <d:area        id="Germany"
                 valueRef="#{gerA}"
              onmouseover="/images/world_germany.gif"
               onmouseout="/images/world.gif"
               targetImage="mapImage"/>

        <d:area        id="France"
                 valueRef="#{fraA}"
              onmouseover="/images/world_france.gif"
               onmouseout="/images/world.gif"
               targetImage="mapImage"/>

      </d:map>

    </td></tr>

  </table>

<hr>


<a href='<%= request.getContextPath() + "/index.jsp" %>'>Back</a> to home page.

<h1>How to Use this Component</h1>

<p>This component renders a clickable image map of the world and regions
can be selected to change the locale.</p>

<p>You can mouse over and click on some parts of the world that speak
U.S. English, French, German, Finnish, and Latin American Spanish.  This
will cause the appropriate Locale to be set into the application,
causing the proper ResourceBundle lookup.</p>

<h1>Custom Tags / Components</h1>

<p>The <code>MapComponent</code> component is driven by the <code>map</code>
tag, and it keeps track of the the selected area on the map.  It determines
the selected area from the incoming request, and fires an
<code>AreaSelectedEvent</code> whenver the selected area is changed.  You
can use the <code>&lt;d:area_selected&gt;</code> tag to specify an event
listener to respond to these events.</p>

<p>The <code>AreaComponent</code> component is driven by the <code>area</code>
tag. It uses Javascript events to visually show the selected area, and it sends
the identifier of the selected area as part of the request.  This tag must be
nested within an <code>&lt;d:map&gt;</code> tag.</p>

<hr>

</body>
</html>
</h:form>
</f:view>
