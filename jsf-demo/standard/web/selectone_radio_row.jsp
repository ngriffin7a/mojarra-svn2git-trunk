<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

                 <h:output_text id="shipmentLabel" 
                     value="Radio with hard-coded options laid out horizontally" />
             </td>


             <td>
                 <h:selectone_radio id="shipType" layout="LINE_DIRECTION" 
                    value="nextDay" tabindex="30" title="shipType">
                <f:selectitem itemValue="nextDay" itemLabel="Next Day"/>
                <f:selectitem itemValue="nextWeek" itemLabel="Next Week" />
                <f:selectitem itemValue="nextMonth" itemLabel="Next Month"/>
                </h:selectone_radio>

             </td>

            </tr>

            <tr>

             <td>
                 <h:output_text id="verticalLabel" 
                     value="Radio with hard-coded options laid out vertically" />

             </td>


             <td>
                <h:selectone_radio id="verticalRadio" layout="PAGE_DIRECTION" border="1" >
                 <f:selectitem itemValue="nextDay" itemLabel="Next Day"/>
                <f:selectitem itemValue="nextWeek" itemLabel="Next Week"  />
                <f:selectitem itemValue="nextMonth" itemLabel="Next Month" />

                </h:selectone_radio>

             </td>

            </tr>

             <tr>

             <td>
                 <h:output_text id="modelLabel" value="Radio with options from model " />

             </td>


             <td>
                 <h:selectone_radio id="radioFromModel"
                       valueRef="LoginBean.currentOption"
                       title="options come from model"
                       layout="LINE_DIRECTION" >

                 <f:selectitems id="radioOptions" valueRef="LoginBean.options"/>
                </h:selectone_radio>

             </td>

            </tr>

             <tr>

             <td>
                 <h:output_text id="modelLongLabel" value="Radio with options 
                     from model of type java.lang.Long" />

             </td>


             <td>
                 <h:selectone_radio id="radioLongOptions"
                       valueRef="LoginBean.currentLongOption"
                       title="options come from model"
                       layout="LINE_DIRECTION">
                 
                <f:selectitems id="longItemOptions" valueRef="LoginBean.longList"/>
                </h:selectone_radio>

             </td>

            </tr>

            <tr>
	         <td>Single-select radiomodel - modelType Boolean:</td>
	         <td><h:selectone_radio id="oneLongradiomodel"
                            valueRef="LoginBean.currentBooleanOption">
                           
		         <f:selectitems id="oneRadiomodelitems"
				valueRef="LoginBean.booleanList" />
		    </h:selectone_radio></td>
	   </tr>



