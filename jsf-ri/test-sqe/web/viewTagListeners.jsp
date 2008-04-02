<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view beforePhase="#{phaseListener.beforePhase}"
        afterPhase="#{phaseListener.afterPhase}">
  <html>
    <head>
      <title>listener methods on f:view</title>
    </head>
    <body>
      <h:form>

        <h2>About this test</h2>

	  <p>The first time this page is visited, we'll only see output
	  on the beforePhaseEvent below.  That's because the page will
	  be done rendering by the time the afterPhase listener gets
	  called, and thus the outputText's below will already have
	  rendered their content to the page by the time the phase
	  listener is called.</p>

          <p>When the page is re-displayed any number of times by
          pressing the redisplay button below, we'll see the apply,
          process, update, invoke, and render phases on the
          beforePhaseEvent, and we'll see apply, process, update, and
          invoke on the afterPhaseEvent.  The former is correct because
          it's impossible to see a restore-view event by using a view
          scoped listener.  The latter is correct because we see
          everything but the after render event because the outputText's
          below render their output before the after event is sent.</p>

       <h2>Output from the PhaseListener</h2>

        <p>beforePhaseEvent: <h:outputText value="#{beforePhaseEvent}"/>.</p>

        <p>afterPhaseEvent: <h:outputText value="#{afterPhaseEvent}"/>.</p>

        <p><h:commandButton value="redisplay" /></p>
        
      </h:form>
     
    </body>
  </html>
</f:view>

