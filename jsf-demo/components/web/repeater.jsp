<!--
 Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 
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

<%-- $Id: repeater.jsp,v 1.11 2005/01/08 02:05:40 edburns Exp $ --%>

<%@ taglib uri="http://java.sun.com/jsf/core"  prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html"  prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/demo/components" prefix="d" %>

<f:view>
<html>
<head>
  <title>Demonstration Components - Repeater Renderer</title>
</head>
<body>

<h:messages        globalOnly="true"/>

<h:form id="myform">

  <d:data_repeater         id="table"
                      binding="#{RepeaterBean.data}"
	                 rows="5"
                        value="#{RepeaterBean.customers}"
                          var="customer">

    <f:facet             name="header">
      <h:outputText    value="Customer List"/>
    </f:facet>

    <h:column>
      <%-- Visible checkbox for selection --%>
      <h:selectBooleanCheckbox
                           id="checked"
                      binding="#{RepeaterBean.checked}"/>
      <%-- Invisible checkbox for "created" flag --%>
      <h:selectBooleanCheckbox
                           id="created"
                      binding="#{RepeaterBean.created}"
                     rendered="false"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:outputText  value="Account Id"/>
      </f:facet>
      <h:inputText        id="accountId"
                      binding="#{RepeaterBean.accountId}"
                     required="true"
                         size="6"
                        value="#{customer.accountId}">
      </h:inputText>
      <h:message          for="accountId"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:outputText  value="Customer Name"/>
      </f:facet>
      <h:inputText        id="name"
                     required="true"
                         size="50"
                        value="#{customer.name}">
      </h:inputText>
      <h:message          for="name"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:outputText  value="Symbol"/>
      </f:facet>
      <h:inputText        id="symbol"
                     required="true"
                         size="6"
                        value="#{customer.symbol}">
        <f:validateLength
                      maximum="6"
                      minimum="2"/>
      </h:inputText>
      <h:message          for="symbol"/>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:outputText  value="Total Sales"/>
      </f:facet>
      <h:outputText       id="totalSales"
                        value="#{customer.totalSales}">
        <f:convertNumber
                         type="currency"/>
      </h:outputText>
    </h:column>

    <h:column>
      <f:facet           name="header">
        <h:outputText  value="Commands"/>
      </f:facet>
      <h:commandButton    id="press"
                    action="#{RepeaterBean.press}"
                    immediate="true"
                        value="#{RepeaterBean.pressLabel}"
                         type="SUBMIT"/>
      <h:commandLink id="click"
                    actionListener="#{RepeaterBean.clickAction}"
                    immediate="true">
        <f:attribute name="key" value="#{customer.accountId}" />
        <h:outputText
                        value="Click"/>
      </h:commandLink>
    </h:column>

  </d:data_repeater>

  <h:commandButton        id="create"
                    action="#{RepeaterBean.create}"
                    immediate="false"
                        value="Create New Row"
                         type="SUBMIT"/>

  <h:commandButton        id="delete"
                    action="#{RepeaterBean.delete}"
                    immediate="false"
                        value="Delete Checked"
                         type="SUBMIT"/>

  <h:commandButton        id="first"
                    action="#{RepeaterBean.first}"
                    immediate="true"
                        value="First Page"
                         type="SUBMIT"/>

  <h:commandButton        id="last"
                    action="#{RepeaterBean.last}"
                    immediate="true"
                        value="Last Page"
                         type="SUBMIT"/>

  <h:commandButton        id="next"
                    action="#{RepeaterBean.next}"
                    immediate="true"
                        value="Next Page"
                         type="SUBMIT"/>

  <h:commandButton        id="previous"
                    action="#{RepeaterBean.previous}"
                    immediate="true"
                        value="Prev Page"
                         type="SUBMIT"/>

  <h:commandButton        id="reset"
                    action="#{RepeaterBean.reset}"
                    immediate="true"
                        value="Reset Changes"
                         type="SUBMIT"/>

  <h:commandButton        id="update"
                    action="#{RepeaterBean.update}"
                    immediate="false"
                        value="Save Changes"
                         type="SUBMIT"/>

</h:form>

<hr>
<p><a href='<%= request.getContextPath() + "/" %>'>Back</a> to home page.</p>

<h1>How to Use this Component</h1>

<p>This tag uses the standard <code>UIData</code> component, coupled with
a custom <em>javax.faces.render.Renderer</em> implementation
(<a href="ShowSource.jsp?filename=/src/components/renderkit/RepeaterRenderer.java">RepeaterRenderer.java</a>)
that takes advantage of the fact that <code>UIData</code> automatically manages
the iteration of the rows in the underlying data model.  At the same time, this
class can serve as the basis for your own <em>Renderer</em> implementation if
you wish to customize the output.  The following information describes the
behavior of the default implementation.</p>

<h2>JSP Attributes</h2>

<table border="1">

<tr>
  <th>JSP Attribute Name</th>
  <th>What it Does</th>
</tr>

<tr>
  <td><code>component</code></td>
  <td>
    <p>Value binding expression to link this <code>UIData</code> component
    to a property in your backing file bean.</p>
  </td>
</tr>

<tr>
  <td><code>first</code></td>
  <td>
    <p>Zero-relative row number of the first row to be displayed.  If not
    specified, the default value is zero (i.e. the first row of the
    underlying data model).</p>
  </td>
</tr>

<tr>
  <td><code>id</code></td>
  <td>
    <p>Component identifier of this component.</p>
  </td>
</tr>

<tr>
  <td><code>rendered</code></td>
  <td>
    <p>Boolean flag indicating whether or not this component should be
    rendered.  If not specified, the default value is <code>true</code>.</p>
  </td>
</tr>

<tr>
  <td><code>rows</code></td>
  <td>
    <p>The maximum number of rows to be displayed, or zero to display
    the entire table.  If not specified, the default value is zero.</p>
  </td>
</tr>

<tr>
  <td><code>styleClass</code></td>
  <td>
    <p>CSS style class to use for the entire table.</p>
  </td>
</tr>

<tr>
  <td><code>value</code></td>
  <td>
    <p>Value reference expression pointing at one of the following:</p>
    <ul>
    <li>An instance of <em>javax.faces.model.DataModel</em>.</li>
    <li>An array of JavaBeans.</li>
    <li>A <em>List</em> of JavaBeans.</li>
    <li>An instance of <em>java.sql.ResultSet</em> (or RowSet).</li>
    <li>An instance of <em>javax.servlet.jsp.jstl.sql.Result</em>.</li>
    <li>A single JavaBean (which will be treated as a one-row table.</li>
    </ul>
  </td>
</tr>

<tr>
  <td><code>var</code></td>
  <td>
    <p>Name of the request scope attribute under which to expose an object
    that represents the data for the row identified by <code>rowIndex</code>.
    </p>
  </td>
</tr>

</table>

<hr>
<p><a href='<%= request.getContextPath() + "/" %>'>Back</a> to home page.</p>

</body>
</html>
</f:view>
