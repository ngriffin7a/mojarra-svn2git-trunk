<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <%@ taglib uri="WEB-INF/lib/basic.tld" prefix="basic" %>

    <H3> JSF NumberFormat Renderer Test Page </H3>
    <hr>
       <fmt:setBundle
	    basename="basic.Resources"
	    scope="session" var="basicBundle"/>

       <jsp:useBean id="LoginBean" class="basic.LoginBean" scope="session" />

       <f:use_faces>  
        <h:form id="numberForm" formName="numberForm" >

            <table>

            <tr>
              <td> <h:output_text id="outputLabel" value="DISPLAY-ONLY" /> </td>
              <td> <h:output_number id="outputNumber" formatPattern="####.##"
                       value="9989.456987"/> </td>
            </tr> 

            <tr>
              <td> <h:output_text id="numberLabel" value="number" /> </td>
              <td> <h:input_number id="testNumber" numberStyle="number"
                       value="1239989.6079"/> </td>
            </tr>

            <tr> 
              <td> <h:output_text id="currLabel" value="CURRENCY" /> </td>
              <td> <h:input_number id="testcurrency" numberStyle="currency"  
                       value="$1234789.60"/> </td>
            </tr>

             <tr>
              <td> <h:output_text id="percentLabel" value="PERCENT" /> </td>
              <td> 
                   <h:input_number id="testPercent" numberStyle="percent" 
                        value="45%"/>
              </td>

            </tr>

            <tr>
              <td> <h:output_text id="patternLabel" value="PATTERN" /> </td>
              <td>
                   <h:input_number id="testPattern" formatPattern="####"
                        value="9999.98765"/>
              </td>
           
            </tr>

            <tr>
              <td> <h:output_text id="byteLabel" value="BYTE" /> </td>
              <td>
                   <h:input_number id="byteInput" numberStyle="integer"
                        valueRef="LoginBean.byte"/>
              </td>
              <td> <h:output_errors id="errByte" for="byteInput"/> </td>
             </tr>

             <tr>
              <td> <h:output_text id="doubleLabel" value="DOUBLE" /> </td>
              <td>
                   <h:input_number id="doubleInput" numberStyle="number"
                        valueRef="LoginBean.double"/>
              </td>
              <td> <h:output_errors id="errDouble" for="doubleInput"/> </td>
             </tr>

             <tr>
              <td> <h:output_text id="floatLabel" value="FLOAT" /> </td>
              <td>
                   <h:input_number id="floatInput" numberStyle="number"
                        valueRef="LoginBean.float"/>
              </td>
              <td> <h:output_errors id="errFloat" for="floatInput"/> </td>
             </tr>

             <tr>
              <td> <h:output_text id="intLabel" value="integer" /> </td>
              <td>
                   <h:input_number id="intInput" numberStyle="integer"
                        valueRef="LoginBean.int"/>
              </td>
              <td> <h:output_errors id="errInt" for="intInput"/> </td>
             </tr>

             <tr>
              <td> <h:output_text id="longLabel" value="LONG" /> </td>
              <td>
                   <h:input_number id="longInput" numberStyle="number"
                        valueRef="LoginBean.long"/>
              </td>
              <td> <h:output_errors id="errLong" for="longInput"/> </td>
             </tr>

              <tr>
              <td> <h:output_text id="shortLabel" value="SHORT" /> </td>
              <td>
                   <h:input_number id="shortInput" numberStyle="number"
                        valueRef="LoginBean.short"/>
              </td>
              <td> <h:output_errors id="errShort" for="shortInput"/> </td>
             </tr>

             <tr>
              <td> <h:output_text id="charLabel" value="CHARACTER" /> </td>
              <td>
                   <h:input_number id="charInput" numberStyle="integer"
                        valueRef="LoginBean.char"/>
              </td>
              <td> <h:output_errors id="errChar" for="charInput"/> </td>
             </tr>

            <tr>
                <td>
                <h:command_button id="numberlogin" commandName="login"
                    label="Login">
                </h:command_button>

                </td> 
            </tr>
          </table>

        </h:form>
       </f:use_faces>

</HTML>
