<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

            <tr>
              <td><b><h:output_text id="dateconvertLabel"
                         value="Date [MMM DD, YYYY Format]: " /></b></td>
              <td><h:input_hidden id="datetextHidden" valueRef="LoginBean.date">
                      <f:convert_datetime type="date"/>
                  </h:input_hidden></td>
              <td><h:messages id="dateErr"
                      for="datetextHidden"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="timeconvertLabel"
                         value="Time [HH:MI:SS (AM/PM) Format]: " /></b></td>
              <td><h:input_hidden id="timetextHidden" 
                      valueRef="LoginBean.time">
                      <f:convert_datetime type="time" /> 
                  </h:input_hidden></td>
              <td><h:messages id="timeErr"
                      for="timetextHidden"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="datetimeconvertLabel"
                         value="Date/Time [MMM DD, YYYY HH:MI:SS (AM/PM) Format]: " /></b></td>
              <td><h:input_hidden id="datetimetextHidden" 
                      valueRef="LoginBean.dateTime">
                      <f:convert_datetime type="both"/>
                  </h:input_hidden></td>
              <td><h:messages id="datetimeErr"
                      for="datetimetextHidden"/> </td>
            </tr>
            <tr>
              <td><b><h:output_text id="boolconvertLabel"
                         value="Boolean [true/false]: " /></b></td>
              <td><h:input_hidden id="booltextHidden" value="false"/></td>
            </tr>
            <tr>
                <td><b><h:output_text id="currencylabel"
                           value="Currency Format:"/></b></td>
                <td>
                    <h:input_hidden id="currencyinputHidden"
                        valueRef="LoginBean.double">                        
                        <f:convert_number type="currency"/>
                    </h:input_hidden>
                </td>
            </tr>
            <tr>
                <td><b><h:output_text id="percentlabel"
                           value="Percent Format:"/></b></td>
                <td>
                    <h:input_hidden id="percentinputHidden" value="54%">
                        <f:convert_number type="percent"/>
                    </h:input_hidden>
                </td>
            </tr>
            <tr>
                <td><b><h:output_text id="patternlabel"
                           value="Pattern Format [####]:"/></b></td>
                <td>
                    <h:input_hidden id="patterninputHidden"
                        value="1999.65">
                        <f:convert_number pattern="####"/>
                    </h:input_hidden>
                </td>
            </tr>
