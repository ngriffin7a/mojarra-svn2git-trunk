<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
-->

				<tr>
					<td>Multi-select menu:</td>
					<td><h:selectManyMenu id="ManyApples" >
						<f:selectItem itemValue="0" itemLabel="zero" />
						<f:selectItem itemValue="1" itemLabel="one" />
						<f:selectItem itemValue="2" itemLabel="two" />
						<f:selectItem itemValue="3" itemLabel="three" />
						<f:selectItem itemValue="4" itemLabel="four"  />
						<f:selectItem itemValue="5" itemLabel="five" />
						<f:selectItem itemValue="6" itemLabel="six" />
						<f:selectItem itemValue="7" itemLabel="seven" />
						<f:selectItem itemValue="8" itemLabel="eight" />
						<f:selectItem itemValue="9" itemLabel="nine" />
					</h:selectManyMenu></td>
				</tr>
				<tr>
					<td>Multi-select menumodel:</td>
					<td><h:selectManyMenu id="menumodel" >
						<f:selectItems id="menumodelitems"
							value="#{LoginBean.options}" />
					</h:selectManyMenu></td>
				</tr>

                                <tr>
					<td>Multi-select menumodel with options of the type java.lang.Long:</td>
					<td><h:selectManyMenu id="menuLongmodel"
                                               value="#{LoginBean.currentLongOptions}">
                                               
						<f:selectItems id="menumodelonglitems"
							value="#{LoginBean.longList}" />
					</h:selectManyMenu></td>
				</tr>

                                <tr>
					<td>Multi-select menumodel Group:</td>
					<td><h:selectManyMenu id="listGroup"
                                                value="#{LoginBean.currentOptions}">
						<f:selectItems id="menumodelitemsGroup"
							value="#{LoginBean.optionsGroup}" />
					</h:selectManyMenu></td>
				</tr>

<tr>

  <td>
    <h:outputText             id="disabledsMenuLabel"
                           value="Listbox with even numbered options disabled"/>
  </td>

  <td>
    <h:selectManyMenu         id="disabledsMenu"
                           value="#{SelectItemsData.disabledSelected}">
      <f:selectItems       value="#{SelectItemsData.disableds}"/>
    </h:selectManyMenu>
  </td>

</tr>


<tr>

  <td>
    <h:outputText             id="nestedsMenuLabel"
                           value="Listbox with nested options"/>
  </td>

  <td>
    <h:selectManyMenu         id="nestedsMenu"
                           value="#{SelectItemsData.nestedSelected}">
      <f:selectItems       value="#{SelectItemsData.nesteds}"/>
    </h:selectManyMenu>
  </td>

</tr>
