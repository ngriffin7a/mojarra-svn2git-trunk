/*
 * $Id: EventContextImpl.java,v 1.4 2002/03/13 18:04:20 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// EventContextImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import java.util.EventObject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;

import javax.faces.FacesEvent;
import javax.faces.EventContext;
import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.EventQueue;
import javax.faces.EventDispatcher;
import javax.faces.NavigationHandler;
import javax.faces.ClientCapabilities;
import javax.faces.ObjectManager;
import javax.faces.ObjectAccessor;
import com.sun.faces.NavigationHandlerFactory;
import javax.faces.EventQueueFactory;
import javax.faces.UIForm;
import javax.faces.NavigationMap;
import javax.faces.TreeNavigator;

/**
 *
 *  <B>EventContextImpl</B> is created in FacesServlet.processRequest()
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: EventContextImpl.java,v 1.4 2002/03/13 18:04:20 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class EventContextImpl extends EventContext
{
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    private RenderContext renderContext = null;
    private ServletRequest request = null;
    private ServletResponse response = null;

    private EventQueue eventQueue = null;
    private String formId = null;
    private NavigationHandler navHandler = null;
    private NavigationHandlerFactory navHandlerFactory = null;
    
    /**

    * Convenience ivar.  Owning reference is in RenderContext implementation.

    */

    private ObjectManager objectManager = null;

    //
    // Constructors and Initializers    
    //

    public EventContextImpl(RenderContext yourRenderContext, 
                            ServletRequest yourRequest,
                            ServletResponse yourResponse)
    {
        super();
        ParameterCheck.nonNull(yourRenderContext);
        renderContext = yourRenderContext;
        request = yourRequest;
        response = yourResponse;
        objectManager = renderContext.getObjectManager();
        
        // get the navigationHandler Factory
        navHandlerFactory = (NavigationHandlerFactory)
                    objectManager.get(Constants.REF_NAVIGATIONHANDLERFACTORY);
        Assert.assert_it(null != navHandlerFactory);
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    // 
    // Methods from RenderContext
    //

    public ClientCapabilities getClientCapabilities() {
        return renderContext.getClientCapabilities();
    }

    /**
     * @return ServletRequest object representing the client request
     */
    public ServletRequest getRequest() {
        return request;
    }

    /**
     * @return ServletResponse object used to write response to the client
     *         request
     */
    public ServletResponse getResponse() {
        return response;
    }

    /**

    * PRECONDITION: EventQueueFactory is in ObjectManager.

    *

     * @see javax.faces.EventContext#getEventQueue
     */
    public EventQueue getEventQueue() {
        EventQueueFactory eventQueueFactory;

        if (null == eventQueue) {
            eventQueue = (EventQueue)objectManager.get(request, 
                                               Constants.REF_EVENTQUEUE);
            if (eventQueue == null) {
                eventQueueFactory = (EventQueueFactory)
                    objectManager.get(Constants.REF_EVENTQUEUEFACTORY);
                Assert.assert_it(null != eventQueueFactory);
                try {
                    eventQueue = eventQueueFactory.newEventQueue();
                } catch (FacesException e) {
                    // PENDING(edburns): log message
                    System.out.println("Exception getEventQueue: " + 
                                       e.getMessage());
                    e.printStackTrace();
                    Assert.assert_it(false);
                }
                Assert.assert_it(null != eventQueue); 
                // PENDING ( visvan ) is EventQueue in request scope ??
                objectManager.put(request, Constants.REF_EVENTQUEUE, eventQueue);
            }
        }
        return eventQueue;
    }

    /**
     * @throws NullPointerException if event is null
     * @return EventDispatcher object used to obtain an object which can
     *         dispatched the specified event
     */
    public EventDispatcher getEventDispatcher(EventObject event) {

        ParameterCheck.nonNull(event);

        EventDispatcher result = null;
        FacesEvent fe = (FacesEvent) event;
        result = (EventDispatcher) fe.getSource();
        Assert.assert_it(result != null);
        return result;
    }

    /**
     * @return NavigationHandler object used to configure the navigational
     *         result of processing events originating from this request
     */
    public NavigationHandler getNavigationHandler() {
        
        if (navHandler != null) {
            return navHandler;
        }    
        // get NavigationMap from ObjectManager
        // and pass it to constructor. For this we need to look up the
        // navigationMap id in UIForm. UIForm can be obtained from 
        // objectManager
        String formId=(String)request.getParameter(Constants.REF_UIFORMID);
        if ( formId != null) {
	    TreeNavigator treeNav = (TreeNavigator)objectManager.get(request,
						  Constants.REF_TREENAVIGATOR);
	    Assert.assert_it(null != treeNav);
	    
	    UIForm form_obj = (UIForm) treeNav.findComponentForId(formId);
            // PENDING ( visvan ) Form object should not be null. If it is
            // request scoped ??
            if ( form_obj != null ) {
                NavigationMap navMap = form_obj.getNavigationMap(renderContext); 
                // if navigationMap is not specifed then the navMap will
                // null. // PENDING ( visvan ) is that an error ?
                if ( navMap != null ) { 
                    try {
                        navHandler = navHandlerFactory.newNavigationHandler(navMap);
                    } catch (FacesException e) {
                        // PENDING(edburns): log message
                        System.out.println("Exception getEventQueue: " + 
                                   e.getMessage());
                        e.printStackTrace();
                        Assert.assert_it(false);
                    }   
                }
            }    
        }
        return navHandler;
    }

    /**
     * @return ObjectManager used to manage application objects in scoped
     *         namespace
     */
    public ObjectManager getObjectManager() {
        return renderContext.getObjectManager();
    }

    /**
     * @return ObjectAccessor used to resolve object-reference Strings to
     *         objects
     */
    public ObjectAccessor getObjectAccessor() {
        return renderContext.getObjectAccessor();
    }
    
    
// The testcase for this class is TestEventContext.java 


} // end of class EventContextImpl
