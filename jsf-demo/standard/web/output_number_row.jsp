<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="outputNumber1Label"
                     value="output_number number"/>

             </td>


             <td>

               <h:output_text id="outputNumber1" value="#{LoginBean.floater}"/>

             </td>
             <td>

             <h:messages id="outputNumber1Errors"
                          for="outputNumber1" />

             </td>
            </tr>

           <tr>

             <td>

               <h:output_text id="outputNumber2Label" 
                     value="output_number currency"/>

             </td>


             <td>

               <h:output_text id="outputNumber2" value="#{LoginBean.floater}">
                   <f:convertNumber type="currency"/>
               </h:output_text>
             </td>

	      <td>

		<h:messages id="outputNumber2Errors" 
			  for="outputNumber2" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="outputNumber3Label" 
                     value="output_number percent"/>

             </td>


             <td>

               <h:output_text id="outputNumber3" value="#{LoginBean.floater}">
                   <f:convertNumber type="percent"/>
               </h:output_text>

             </td>

	      <td>

		<h:messages id="outputNumber3Errors" 
			  for="outputNumber3" />

	      </td>

            </tr>

           <tr>

             <td>

               <h:output_text id="outputNumber4Label" 
                     value="output_number PATTERN "/>

             </td>


             <td>

               <h:output_text id="outputNumber4" value="#{LoginBean.floater}">
                   <f:convertNumber pattern="####"/>
               </h:output_text>
             </td>

	      <td>

		<h:messages id="outputNumber4Errors" 
			  for="outputNumber4" />

	      </td>

            </tr>

            <tr>

             <td>

               <h:output_text id="outputNumber5Label"
                     value="output_number integer with valueRef"/>

             </td>


             <td>

               <h:output_text id="outputNumber5" value="#{LoginBean.char}"/>
             </td>

              <td>

                <h:messages id="outputNumber5Errors"
                          for="outputNumber5" />

              </td>

            </tr>


