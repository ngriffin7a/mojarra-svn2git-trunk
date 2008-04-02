<%@ page contentType="text/html"
%><%@ page import="javax.faces.FactoryFinder"
%><%@ page import="javax.faces.application.Application"
%><%@ page import="javax.faces.application.ApplicationFactory"
%><%@ page import="javax.faces.context.FacesContext"
%><%@ page import="javax.el.ValueExpression"
%><%@ page import="com.sun.faces.systest.model.TestBean"
%><%

// This test exercices the config system's ability to load information
// from a faces configuration file specified as a servlet context
// initialization parameter, in addition to one that is specified
// under WEB-INF.

  // Acquire our Application instance
  ApplicationFactory afactory = (ApplicationFactory)
   FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
  Application appl = afactory.getApplication();

  // Acquire the FacesContext instance for this request
  FacesContext facesContext = FacesContext.getCurrentInstance();
  if (facesContext == null) {
    out.println("/configd01.jsp FAILED - No FacesContext returned");
    return;
  }

  // Acquire a ValueExpression for the bean to be created
  // "mybean" exists in a Faces configuration file specified as
  // as a servlet context init parameter.
  //
  ValueExpression valueExpression = appl.getExpressionFactory().
      createValueExpression(facesContext.getELContext(),"#{mybean}", Object.class);
  if (valueExpression == null) {
    out.println("/config01.jsp FAILED - No ValueExpression returned");
    return;
  }

  // Evaluate the value binding and check for bean creation
  Object result = valueExpression.getValue(facesContext.getELContext());
  if (result == null) {
    out.println("/config01.jsp FAILED - getValue() returned null");
    return;
  }

  Object scoped = request.getAttribute("mybean");
  if (scoped == null) {
    out.println("/config01.jsp FAILED - not created in request scope");
    return;
  }

  // Acquire a ValueExpression for the bean to be created
  // "test1" exists in a Faces configuration file under WEB-INF. 
  //
  valueExpression = appl.getExpressionFactory().createValueExpression(facesContext.getELContext(),"#{test1}", 
     Object.class);
  if (valueExpression == null) {
    out.println("/config01.jsp FAILED - No ValueExpression returned");
    return;
  }

  // Evaluate the value binding and check for bean creation
  result = valueExpression.getValue(facesContext.getELContext());
  if (result == null) {
    out.println("/config01.jsp FAILED - getValue() returned null");
    return;
  }

  scoped = request.getAttribute("test1");
  if (scoped == null) {
    out.println("/config01.jsp FAILED - not created in request scope");
    return;
  }

  out.println("/config01.jsp PASSED");
%>
