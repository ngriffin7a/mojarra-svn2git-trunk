<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
   <title>Non-JSP Examples</title>
</head>
<body>
<h1>Non-JSP Examples</h1>

<p>

<h2>Overview:</h2>

<p>
This is a collection of examples written in XUL demonstrating 
the usage of different Java Server Faces components. These components
are rendered by the Java Server Faces framework and are not dependent
on a JSP container<br><br>
These examples are important because they are a technology demonstrator
showing that the Java Server Faces framework can be used and rendered 
independently of a JSP container.
</p>

<h2>How this Non-JSP Web Application works:</h2>
<p>
<ol>
  <li>
    This web application works by first creating a 
    <code>XulServletContextListener</code>.<br>
    This context listener creates a new <code>XulViewHandler</code> 
    when the context is initialized and makes it the default 
    View Handler.
  </li>
  <li>
    The <code>XulViewHandler</code> handles how the XUL view is rendered. It goes through the following steps in order to render the page:
    <ul>
      <li>
        <b>Parsing Xml Rule Set</b><br>
        A Rule Set is created which maps the XUL elements to Java Server
        Faces objects. The XUL page is then parsed using this Rule Set
        and a tree of Java Server Faces objects is created.
      </li>
      <li>
        <b>Setting the Tree</b><br>
        The object tree that is created is set within the FacesContext
        along with the appropriate ResponseWriter and ContentType.
      </li>
      <li>
        <b>Call component encode methods</b><br>
        The response is rendered by traversing the tree and calling the
        individual object's encode methods.
      </li>
      <li>
        <b>Saving the state</b><br>
        The tree is saved in the session to allow for navigation between
        different XUL pages. This allows ActionEvents such as the
        demo's <code>ButtonAction</code> to be written.<br>
        During the parsing phase an ActionRule was written that creates an
        ActionListener on an object. The ActionListener implementation
        class then handles the event. In the <code>ButtonAction</code>
        case the event handler redirects to another XUL page.
      </li>
    </ul>
  </li>
</ol>

<p>
<hr>

<table BORDER=0 CELLSPACING=5 WIDTH="85%" >
  <tr VALIGN=TOP>
    <td>Simple Label Example&nbsp;</td>

    <td VALIGN=TOP WIDTH="30%">
      <a href="faces/label.xul">
        <img SRC="images/execute.gif" HSPACE=4 BORDER=0 align=TOP>
      </a>
      <a href="faces/label.xul">Execute</a>
    </td>

    <td WIDTH="30%">
      <a href="ShowSource.jsp?filename=/label.xul">
        <img SRC="images/code.gif" HSPACE=4 BORDER=0 height=24 width=24 align=TOP>
      </a>
      <a href="ShowSource.jsp?filename=/label.xul">Source</a>
    </td>
  </tr>

  <tr VALIGN=TOP>
    <td>Text Field Example&nbsp;</td>

    <td VALIGN=TOP WIDTH="30%">
      <a href="faces/textField.xul">
        <img SRC="images/execute.gif" HSPACE=4 BORDER=0 align=TOP>
      </a>
      <a href="faces/textField.xul">Execute</a>
    </td>

    <td WIDTH="30%">
      <a href="ShowSource.jsp?filename=/textField.xul">
        <img SRC="images/code.gif" HSPACE=4 BORDER=0 height=24 width=24 align=TOP>
      </a>
      <a href="ShowSource.jsp?filename=/textField.xul">Source</a>
    </td>
  </tr>

  <tr VALIGN=TOP>
    <td>Hello Duke Example&nbsp;</td>

    <td VALIGN=TOP WIDTH="30%">
      <a href="faces/helloDuke.xul">
        <img SRC="images/execute.gif" HSPACE=4 BORDER=0 align=TOP>
      </a>
      <a href="faces/helloDuke.xul">Execute</a>
    </td>

    <td WIDTH="30%">
      <a href="ShowSource.jsp?filename=/helloDuke.xul">
        <img SRC="images/code.gif" HSPACE=4 BORDER=0 height=24 width=24 align=TOP>
      </a>
      <a href="ShowSource.jsp?filename=/helloDuke.xul">Source</a>
    </td>
  </tr>

</table>

</body>
</html>
