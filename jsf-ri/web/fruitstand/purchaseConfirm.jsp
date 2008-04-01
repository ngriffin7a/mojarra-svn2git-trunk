<html>
<head>
<title>FruitStand.com</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<%@ taglib uri='WEB-INF/html_basic.tld' prefix='faces' %>
<h2><font color="#0000FF"> FruitStand.com </font></h2>
<hr>
<font size="4" color="#0000FF">Here are the items you selected.</font>
<faces:useFaces>
<table cellpadding="10">
  <th>Item</th>
  <th>Amount(lbs)</th>
  <th>Price/lb.($)</th>
  <th>Total($)</th>
  <tr> 
    <td><faces:Output_Text name='apple_item' value='appple' /></td>
    <td><faces:Output_Text name='apple_amount' value= '2.0'/></td>
    <td><faces:Output_Text name='apple_price' value= '0.29'/></td>
    <td><faces:Output_Text name='apple_total' value= '0.58'/></td>
  </tr>
  <tr> 
    <td><faces:Output_Text name='banana_item' value='banana' /></td>
    <td><faces:Output_Text name='banana_amount' value='3.0' /></td>
    <td><faces:Output_Text name='banana_price' value='0.59' /></td>
    <td><faces:Output_Text name='banana_total' value='1.77' /></td>
  </tr>
  <tr> 
    <td><faces:Output_Text name='grapes_item' value='grapes' /></td>
    <td><faces:Output_Text name='grapes_amount' value='3.0' /></td>
    <td><faces:Output_Text name='grapes_price' value='0.79' /></td>
    <td><faces:Output_Text name='grapes_total' value='2.37' /></td>
  </tr>
  <tr> 
    <td colspan="4"><hr /></td>
    
  </tr>
  <tr>
    <td colspan="3">Total</td>
    <td> <faces:Output_Text name='total_price' value='6.14' /></td>
  </tr>
</table>
<br>
<font size="4" color="#0000FF">
The items listed above will be billed to: <br>
<br>

<faces:Output_Text name='cust_name' value='Jordan Niles' /> <br>
<faces:Output_Text name='cust_address' value='24978 Court Madera' /><br>
<faces:Output_Text name='cust_city' value='Santa Clara, CA' /><br>
<faces:Output_Text name='cust_country' value='USA' />

<form name="form1" method="post" action="purchaseAction.jsp">
  <input type="submit" name="Submit" value="Purchase Items Listed Above">
</form>
<hr>

</font>
<p>Thanks for shopping with FruitStand.com. </p></html>

</faces:useFaces>
