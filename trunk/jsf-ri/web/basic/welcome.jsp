<HTML>
    <HEAD> <TITLE> JSF Basic Components Test Page </TITLE> </HEAD>
    <%@ taglib uri='WEB-INF/html_basic.tld' prefix='faces' %>
    <H3> JSF Basic Components Test Page </H3>
    <hr>
    <faces:UseFaces>
        <faces:Form name='basicForm' >

            <faces:Output_Text name='hello_label' value='Login Successful' />
             <P></P>
        </faces:Form>
    </faces:UseFaces>
</HTML>
