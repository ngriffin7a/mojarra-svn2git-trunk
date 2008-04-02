<!--
 Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%@ page import="com.sun.faces.CustomerBean" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<%

  // Construct a preconfigured customer list in session scope
  List list = (List)
    pageContext.getAttribute("ListBean", PageContext.SESSION_SCOPE);
  if (list == null) {
    list = new ArrayList();
    list.add(new CustomerBean("123456", "Alpha Beta Company", "ABC", 1234.56));
    list.add(new CustomerBean("445566", "General Services, Ltd.", "GS", 33.33));
    list.add(new CustomerBean("654321", "Summa Cum Laude, Inc.", "SCL", 76543.21));
    list.add(new CustomerBean("333333", "Yabba Dabba Doo", "YDD",  333.33));
    pageContext.setAttribute("ListBean", list,
                             PageContext.SESSION_SCOPE);
  }

%>

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>

    <%@ taglib uri="http://java.sun.com/j2ee/html_basic/" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core/" prefix="f" %>

    <BODY>
        <H3> JSF Basic Components Test Page </H3>

<f:usefaces>
<h:form formName="basicForm" id="basicForm">

  <TABLE BORDER="1">

      <tr>
         <td>
                 <h:panel_grid id="logonPanel1" columns="2"
                    border="1" cellpadding="3" cellspacing="3"
                     summary="Grid with hardcoded data"
                     title="Grid with hardcoded data" >

                     <h:panel_group id="form_header2">
                        <h:output_text id="A2" value="Logon&nbsp;"/>
                        <h:output_text id="B2" value="Form"/>
                     </h:panel_group>

                     <!-- Panel data elements -->

                    <h:output_text id="text1" value="Username:"/>

                    <h:input_text id="username1" inputClass="inputClass" value="JavaServerFaces" />

                    <h:output_text id="text2" outputClass="outputClass" value="Password:"/>

                    <h:input_secret inputClass="secretClass" id="password1" />

                    <h:command_button id="submit1" type="SUBMIT"
                           commandClass="commandClass" commandName="submit">
                      <h:output_text id="submit1_label" value="Login" />
                    </h:command_button>

                    <h:command_button id="reset1" type="RESET" commandName="reset">
                      <h:output_text id="reset1_label" value="Reset" />
                    </h:command_button>

                </h:panel_grid>
             </td>
           </tr>

             <tr><td>
                 <h:panel_list id="list2"
                  border="1" cellpadding="3" cellspacing="3"
                     summary="List with HTML attributes."
                     title="List with no stylesheets" >

                  <!-- List Data -->

                  <h:panel_data    id="listData2" var="customer"
                       modelReference="ListBean">
                    <h:output_text id="accountId2"
                       modelReference="customer.accountId"/>
                    <h:output_text id="name2"
                       modelReference="customer.name"/>
                    <h:output_text id="symbol2"
                       modelReference="customer.symbol"/>
                    <h:output_text id="totalSales2"
                       modelReference="customer.totalSales"/>
                  </h:panel_data>

                 </h:panel_list> 
             </td>

            </tr>

      <TR>

	<TD>

            <h:command_button id="pushButton" type="button"
                     commandName="push" disabled = "true" >
                 <h:output_text id="buttonLabel" value="This is a push button " />
                 <h:graphic_image id="buttonImage" url="/duke.gif" />
                 <!-- this isn't valid HTML, but it works for our label case -->
                 <h:output_label id="pushButtonLabel" 
	             outputClass="outputClass"
                     lang="en_US" accesskey="Z" 
                     style="12pt" for="../pushButton">
                 Label for PushButon
                 </h:output_label>
             </h:command_button>
	</TD>

      </TR>


      <TR>

	<TD>

            <h:command_button id="imageOnlyButton" type="submit"
                     commandName="login"  >
                 <h:graphic_image id="buttonImage1" url="/duke.gif" 
                        graphicClass="buttonImage1"/>
             </h:command_button>
	</TD>

      </TR>

      <TR>

	<TD>

	      <h:command_hyperlink id="link" target="hello.html"
                           commandClass="hyperlinkClass"
				       label="link text"/>

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:command_hyperlink id="imageLink" target="hello.html"
				       image="duke.gif"/>

	</TD>

      </TR>

      <TR>

	<TD>

	      <h:output_text id="userLabel" value="Output Text" />

	</TD>

      </TR>

      <TR>

	<TD>

            <h:selectboolean_checkbox id="validUser" 
                   selectbooleanClass="selectbooleanClass"/>
	</TD>

      </TR>

      <TR>

	<TD>

	     <h:selectone_listbox id="appleQuantity" selectoneClass="selectoneClass"
                     title="Select Quantity"
                     accesskey="N" tabindex="20" >

                <h:selectitem  disabled="true" itemValue="0" itemLabel="0"/>
                <h:selectitem  itemValue="1" itemLabel="1" title="One"/>
                <h:selectitem  itemValue="2" itemLabel="2" title="Two" />
                <h:selectitem  itemValue="3" itemLabel="3" title="Three" />
                <h:selectitem  itemValue="4" itemLabel="4" title="Four" selected="true"/>
                <h:selectitem  itemValue="5" itemLabel="5" title="Five" />
                <h:selectitem  itemValue="6" itemLabel="6" title="Six" />
                <h:selectitem  itemValue="7" itemLabel="7" title="Seven" />
                <h:selectitem  itemValue="8" itemLabel="8" title="Eight" />
                <h:selectitem  itemValue="9" itemLabel="9" title="nine" />

              </h:selectone_listbox>

	</TD>

      </TR>

      <TR>

	<TD>

	    <h:selectone_radio id="shipType" layout="LINE_DIRECTION">

                <h:selectitem itemValue="nextDay" itemLabel="Next Day"
                      tabindex="30" title="Next day shipment"/>
                <h:selectitem itemValue="nextWeek" itemLabel="Next Week" title="Next week shipment"
                                  tabindex="40" selected="true" />
                <h:selectitem itemValue="nextMonth" itemLabel="Next Month"
                        tabindex="50" title="Next month shipment"/>

              </h:selectone_radio>

	</TD>

      </TR>

      <TR>

	<TD>
            <h:selectone_radio id="verticalRadio" 
                                            layout="PAGE_DIRECTION" border="1" >

                <h:selectitem itemValue="nextDay" itemLabel="Next Day"
                                  selected="true" />
                <h:selectitem itemValue="nextWeek" itemLabel="Next Week"  />
                <h:selectitem itemValue="nextMonth" itemLabel="Next Month" />

           </h:selectone_radio>

	</TD>

      </TR>

      <TR>

        <TD>Date: 
        </TD>

	<TD><h:input_date id="date1" value="Wed, Jul 10, 1996"
                              inputClass="inputClass"
                              dateStyle="LONG" />
	</TD>

      </TR>


      <TR>

        <TD>Disabled Date: 
        </TD>

	<TD><h:input_date id="date2" value="Thu, Jul 11, 1996"
                        dateStyle="LONG" disabled="true"
                        size="3" maxlength="20" tabindex="1" accesskey="D"/>
	</TD>

      </TR>

      <TR>

        <TD>DateTime: 
        </TD>

	<TD><h:input_datetime id="date3" 
                          value="Wed, Jul 10, 1996 AD at 12:31:31 PM"
                          formatPattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a" />
	</TD>

      </TR>


      <tr>
          <td>
                   <h:input_number id="testPattern" formatPattern="####"
                        value="9999.98765" size="3" maxlength="20" 
                        inputClass="inputClass" 
                        tabindex="2" accesskey="D"/>
              </td>

      </tr>

      <tr>
          <td> <h:output_text outputClass="outputClass" id="percentLabel" value="OUTPUT-PERCENT" /> </td>
              <td>
                   <h:output_number id="testPercent" numberStyle="PERCENT"
                        value="45%"/>
              </td>
      </tr>

      <TR>

        <TD>OutputDate: 
        </TD>

	<TD><h:output_date id="date4" value="Wed, Jul 10, 1996"
                              outputClass="outputClass" dateStyle="LONG" />
	</TD>

      </TR>

      <TR>

        <TD>OutputDateTime: 
        </TD>

	<TD><h:output_datetime id="date5" 
                          value="Wed, Jul 10, 1996 AD at 12:31:31 PM"
                          formatPattern="EEE, MMM d, yyyy G 'at' hh:mm:ss a" />
	</TD>

      </TR>

      <TR>

        <TD>InputTime: 
        </TD>

	<TD><h:input_time id="date6" 
                          value="12:31:31 PM"
                          timeStyle="MEDIUM" />
	</TD>

      </TR>

      <TR>

        <TD>OutputTime: 
        </TD>

	<TD><h:output_time id="date7" 
                          value="12:31:31 PM"
                          timeStyle="MEDIUM" />
	</TD>

      </TR>

           <tr>

             <td>

               <h:input_date id="input_date1" dateStyle="MEDIUM"
                                 value="Jan 12, 1952" 
                                 readonly="true"
                                 size="10" maxlength="20"
                                 alt="input_date MEDIUM readonly"
                                 accesskey="D" 
                               title="input_date MEDIUM readonly"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:input_date id="input_date2" dateStyle="MEDIUM"
                                 value="Jan 12, 1952" 
                                 alt="input_date MEDIUM"
                                  title="input_date MEDIUM"/>

             </td>

            </tr>

           <tr>

             <td>

               <h:input_date id="input_date3" dateStyle="SHORT"
                                 value="01/12/1952" 
                                 alt="input_date SHORT"
                                  title="input_date SHORT"/>


             </td>

            </tr>

           <tr>

             <td>

               <h:input_date id="input_date4" dateStyle="LONG" 
                                 value="January 12, 1952" 
                                 size="20" maxlength="40"
                                 alt="input_date LONG"
                                 accesskey="d"
                               title="input_date LONG"/>


             </td>

            </tr>

<tr>
					<TD>Multi-select menu:</TD>
					<TD><h:selectmany_menu id="ManyApples" selectmanyClass="selectmanyClass">
						<h:selectitem itemValue="0" itemLabel="zero" />
						<h:selectitem itemValue="1" itemLabel="one" />
						<h:selectitem itemValue="2" itemLabel="two" />
						<h:selectitem itemValue="3" itemLabel="three" />
						<h:selectitem itemValue="4" itemLabel="four" selected="true" />
						<h:selectitem itemValue="5" itemLabel="five" />
						<h:selectitem itemValue="6" itemLabel="six" />
						<h:selectitem itemValue="7" itemLabel="seven" selected="true" />
						<h:selectitem itemValue="8" itemLabel="eight" />
						<h:selectitem itemValue="9" itemLabel="nine" />
					</h:selectmany_menu></TD>

</tr>

<tr>
					<TD>Multi-select listbox:</TD>
					<TD><h:selectmany_listbox id="ManyApples2">
						<h:selectitem itemValue="0" itemLabel="zero" />
						<h:selectitem itemValue="1" itemLabel="one" />
						<h:selectitem itemValue="2" itemLabel="two" />
						<h:selectitem itemValue="3" itemLabel="three" />
						<h:selectitem itemValue="4" itemLabel="four" selected="true" />
						<h:selectitem itemValue="5" itemLabel="five" />
						<h:selectitem itemValue="6" itemLabel="six" />
						<h:selectitem itemValue="7" itemLabel="seven" selected="true" />
						<h:selectitem itemValue="8" itemLabel="eight" />
						<h:selectitem itemValue="9" itemLabel="nine" />
					</h:selectmany_listbox></TD>
</tr>

<tr>
					<TD><h:selectmany_checkboxlist id="ManyApples3" 
                                                selectmanyClass="selectmanyClass">
						<h:selectitem itemValue="0" itemLabel="zero" />
						<h:selectitem itemValue="1" itemLabel="one" />
						<h:selectitem itemValue="2" itemLabel="two" />
						<h:selectitem itemValue="3" itemLabel="three" />
						<h:selectitem itemValue="4" itemLabel="four" selected="true" />
						<h:selectitem itemValue="5" itemLabel="five" />
						<h:selectitem itemValue="6" itemLabel="six" />
						<h:selectitem itemValue="7" itemLabel="seven" selected="true" />
						<h:selectitem itemValue="8" itemLabel="eight" />
						<h:selectitem itemValue="9" itemLabel="nine" />
					</h:selectmany_checkboxlist></TD>
</tr>


  <TABLE>

</h:form>
</f:usefaces>

    </BODY>
</HTML>
