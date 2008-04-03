<%--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 
 Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 
 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.
 
 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"
 
 Contributor(s):
 
 If you wish your version of this file to be governed by only the CDDL or
 only the GPL Version 2, indicate your decision by adding "[Contributor]
 elects to include this software in this distribution under the [CDDL or GPL
 Version 2] license."  If you don't indicate a single choice of license, a
 recipient has the option to distribute your version of this file under
 either the CDDL, the GPL Version 2 or to extend the choice of license to
 its licensees as provided above.  However, if you add GPL Version 2 code
 and therefore, elected the GPL Version 2 license, then the option applies
 only if the new code is made subject to such option by the copyright
 holder.
--%>

<%@ page contentType="text/html" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%
  if (session.getAttribute("status") == null) {
    String[] a = {"1", "2"};
    session.setAttribute("status", a);
  }
%>
<f:view>
  <h:form id="form1">
    <h:selectManyCheckbox value="#{status}">
      <f:selectItem itemValue="1" itemLabel="Open" />
      <f:selectItem itemValue="2" itemLabel="Submitted" />
      <f:selectItem itemValue="3" itemLabel="Accepted" />
      <f:selectItem itemValue="4" itemLabel="Rejected" />
    </h:selectManyCheckbox>
    <h:commandButton id="modify" value="Update" />
    <p>Current model value:
    ${status[0]}, ${status[1]}, ${status[2]}, ${status[3]}
  </h:form>
  <h:form id="form2">
    Resets the rendered values but not the model:
    <h:commandButton id="doNotModify" value="Click" />
  </h:form>
</f:view>
