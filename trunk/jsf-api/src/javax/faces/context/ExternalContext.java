/*
 * $Id: ExternalContext.java,v 1.9 2003/10/22 20:43:33 eburns Exp $
 */
 
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002, 2003 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

package javax.faces.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.Map;

import javax.faces.FacesException;
import javax.servlet.http.Cookie;


/**
   
* <p>This class allows the Faces API to be unaware of the nature of its
* containing environment.  For example, this class allows Faces to run
* in either a Servlet or a Portlet.  This class can be seen as an
* extension of FacesContext.</p>

* @author Brendan Murray
* @author Ed Burns <ed.burns@sun.com>
* @version 0.1
* 
*/
public abstract class ExternalContext {
    

    /**
     * <p>String identifier for BASIC authentication.</p>
     */
    public static final String BASIC_AUTH = "BASIC";


    /**
     * <p>String identifier for CLIENT_CERT authentication.</p>
     */
    public static final String CLIENT_CERT_AUTH = "CLIENT_CERT";


    /**
     * <p>String identifier for DIGEST authentication.</p>
     */
    public static final String DIGEST_AUTH = "DIGEST";


    /**
     * <p>String identifier for FORM authentication.</p>
     */
    public static final String FORM_AUTH = "FORM";


    /**
     * @return the <code>HttpSession</code> instance for the session
     * associated with the current request (if any); otherwise, return
     * <code>null</code>.
     
     * @param create true - to create a new session for this request if
     * necessary; false to return null if there's no current session
     
    */
    public abstract Object getSession(boolean create);
    
    /**
     * @return the <code>ServletContext</code> or
     * <code>PortletContext</code> object for the web application
     * associated with this request.
     */
    public abstract Object getContext();
    
    
    /**
     * @return the <code>ServletRequest</code> or
     * <code>PortletRequest</code> object representing the current
     * request that is being processed.
     */
    public abstract Object getRequest();
    
    
    /**
     * @return the <code>ServletResponse</code>
     * <code>PortletResponse</code> object representing the current
     * response that is being rendered.
     */
    public abstract Object getResponse();
    
    /**
     *       
     * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
     * from this method is equivalent to getting the
     * <code>ServletContext</code> for this request and calling
     * <code>getAttribute(key)</code> on it.</p>
     *
     * <p>The following methods of <code>Map</code> must be supported.</p>
     *
     * <ul><p><code>get, put, equals, remove, entrySet</code>, and all
     * of the <code>Map</code> methods from
     * <code>java.util.AbstractMap</code>, except for below
     * exclusions.</p></ul>
     *
     * <p>The following methods of <code>Map</code> must not be supported.</p>
     *
     * <ul><p><code>clear</code>, and <code>putAll</code>.</p></ul>
     *
     * @return a <code>Map</code> that wraps the ServletContext's
     * attribute set.
     *
     */
    public abstract Map getApplicationMap();
    
    /**
     *     
     * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
     * from this method is equivalent to getting the
     * <code>HttpSession</code> for this request and calling
     * <code>getAttribute(key)</code> on it.</p>
     *
     * <p>The following methods of <code>Map</code> must be supported.</p>
     *
     * <ul><p><code>get, put, equals, remove, entrySet</code>, and all
     * of the <code>Map</code> methods from
     * <code>java.util.AbstractMap</code>, except for below
     * exclusions.</p></ul>
     *
     * <p>The following methods of <code>Map</code> must not be supported.</p>
     *
     * <ul><p><code>clear</code>, and <code>putAll</code>.</p></ul>
     *
     * @return a <code>Map</code> that wraps the HttpSession's
     * attribute set.
     *
     */
    
    public abstract Map getSessionMap();
    
    /**
     *     
     * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
     * from this method is equivalent to getting the
     * <code>ServletRequest</code> for this request and calling
     * <code>getAttribute(key)</code> on it.</p>
     *
     * <p>The following methods of <code>Map</code> must be supported.</p>
     *
     * <ul><p><code>get, put, equals, remove, entrySet</code>, and all
     * of the <code>Map</code> methods from
     * <code>java.util.AbstractMap</code>, except for below
     * exclusions.</p></ul>
     *
     * <p>The following methods of <code>Map</code> must not be supported.</p>
     *
     * <ul><p><code>clear</code>, and <code>putAll</code>.</p></ul>
     *
     * @return a <code>Map</code> that wraps the Request's
     * attribute set.
     */
    public abstract Map getRequestMap();
    
    /**
     *
     * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
     * from this method is equivalent to getting the
     * <code>ServletRequest</code> for this request and calling
     * <code>getParameter(key)</code> on it.</p>
     *
     * <p>The following methods of <code>Map</code> must be supported.</p>
     *
     * <ul><p><code>get, equals, entrySet</code>, and all of
     * the <code>Map</code> methods from
     * <code>java.util.AbstractMap</code>, except for below
     * exclusions.</p></ul>
     *
     * <p>The following methods of <code>Map</code> must not be supported.</p>
     *
     * <ul><p><code>put, remove, clear</code>, and
     * <code>putAll</code>.</p></ul>
     *
     * @return a <code>Map</code> of all the parameters sent with this request.
     *
     */
    
    public abstract Map getRequestParameterMap();
    
    /**
     *       
     * <p>Similar to <code>getRequestParameterMap</code>, but useful for those
     * parameters that can have multiple values with one key.</p>
     *
     * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
     * from this method is equivalent to getting the
     * <code>ServletRequest</code> for this request and calling
     * <code>getParameterValues(key)</code> on it.</p>
     *
     * <p>The following methods of <code>Map</code> must be supported.</p>
     *
     * <ul><p><code>get, equals, entrySet</code>, and all of the
     * <code>Map</code> methods from <code>java.util.AbstractMap</code>,
     * except for below exclusions.</p></ul>
     *
     * <p>The following methods of <code>Map</code> must not be supported.</p>
     *
     * <ul><p><code>put, remove, clear</code>, and
     * <code>putAll</code>.</p></ul>
     *
     * @return a <code>Map</code> that wraps the getParameterValues()
     * result set.
     *
     */
    
    public abstract Map getRequestParameterValuesMap();

    /**

    * This is simply a wrapper for
    * <code>ServletRequest.getParameterNames()</code>.  We took the
    * liberty of returning an <code>Iterator</code> instead of an
    * <code>Enumeration</code>.

    * @return an Iterator of the names of the parameters for this request.

    */
    
    public abstract Iterator getRequestParameterNames();

    /**
     *
     * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
     * from this method is equivalent to getting the
     * <code>ServletRequest</code> for this request and calling
     * <code>getHeader(key)</code> on it.</p>
     *
     * <p>The following methods of <code>Map</code> must be supported.</p>
     *
     * <ul><p><code>get, equals, entrySet</code>, and all of the
     * <code>Map</code> methods from <code>java.util.AbstractMap</code>,
     * except for below exclusions.</p></ul>
     *
     * <p>The following methods of <code>Map</code> must not be supported.</p>
     *
     * <ul><p><code>put, remove, clear</code>, and
     * <code>putAll</code>.</p></ul>
     *
     * @return a <code>Map</code> that wraps the Header set for this
     * request.
     *
     */ 
    
    public abstract Map getRequestHeaderMap();

    /**
     *
     * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
     * from this method is equivalent to getting the
     * <code>ServletRequest</code> for this request and calling
     * <code>getHeader<em>s</em>(key)</code> on it.</p>
     *
     * <p>The following methods of <code>Map</code> must be supported.</p>
     *
     * <ul><p><code>get, equals, entrySet</code>, and all of the
     * <code>Map</code> methods from <code>java.util.AbstractMap</code>,
     * except for below exclusions.</p></ul>
     *
     * <p>The following methods of <code>Map</code> must not be supported.</p>
     *
     * <ul><p><code>put, remove, clear</code>, and
     * <code>putAll</code>.</p></ul>
     *
     * @return a <code>Map</code> that wraps the Header set for this
     * request, but calls <code>getHeader<em>s</em>()</code> instead of
     * <code>getHeader()</code>.
     */ 
    
    public abstract Map getRequestHeaderValuesMap();

    /**
     *
     * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
     * from this method is equivalent to getting the
     * <code>ServletRequest</code> for this request and looking through
     * the <code>Cookie[]</code> returned by calling
     * <code>getCookies()</code> and finding a cookie with the name of
     * <code>key</code>.</p>
     *
     * <p>The following methods of <code>Map</code> must be supported.</p>
     *
     * <ul><p><code>get, equals, entrySet</code>, and all of the
     * <code>Map</code> methods from <code>java.util.AbstractMap</code>,
     * except for below exclusions.</p></ul>
     *
     * <p>The following methods of <code>Map</code> must not be supported.</p>
     *
     * <ul><p><code>put, remove, clear</code>, and
     * <code>putAll</code>.</p></ul>
     *
     * @return a <code>Map</code> that wraps the Cookie set for this
     * request.
     *
     */ 

    public abstract Map getRequestCookieMap();

    /**

    * <p>A wrapper for <code>ServletRequest.getLocale()</code>.</p>

    */
    
    public abstract Locale getRequestLocale();
    
    /**

    * <p>A wrapper for <code>ServletRequest.getPathInfo()</code>.</p>

    */
    
    public abstract String getRequestPathInfo();
    
    /**

    * <p>A wrapper for <code>HttpServletRequest.getContextPath()</code>.</p>

    */
    
    public abstract String getRequestContextPath();

    /**
     * <p>A wrapper for <code>HttpServletRequest.getServletPath()</code></p>
     *
     */
    public abstract String getRequestServletPath();

    /**

    * <p>A wrapper for <code>HttpServletRequest.getCookies()</code>.</p>

    */
    
    public abstract Cookie[] getRequestCookies();

    /**

    * <p>A wrapper for <code>ServletContext.getInitParameter()</code>.</p>

    */
    
    public abstract String getInitParameter(String name);

    /**
     *
     * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
     * from this method is equivalent to getting the
     * <code>ServletContext</code> for this request and calling
     * <code>getInitParam(key)</code> on it.</p>
     *
     * <p>The following methods of <code>Map</code> must be supported.</p>
     *
     * <ul><p><code>get, equals, entrySet</code>, and all of the
     * <code>Map</code> methods from <code>java.util.AbstractMap</code>,
     * except for below exclusions.</p></ul>
     *
     * <p>The following methods of <code>Map</code> must not be supported.</p>
     *
     * <ul><p><code>put, remove, clear</code>, and
     * <code>putAll</code>.</p></ul>
     *
     * @return a <code>Map</code> that wraps the ServletContext's
     * init parameter set.
     */

    public abstract Map getInitParameterMap();

    /**

    * <p>A wrapper for <code>ServletContext.getResourcePaths()</code>.</p>

    */
    
    public abstract Set getResourcePaths(String path);

    /**

    * <p>A wrapper for <code>ServletContext.getResourceAsStream()</code>.</p>

    */
    
    public abstract InputStream getResourceAsStream(String path);
    

    /**
     * <p>Return the name of the authentication scheme used to authenticate
     * the current user.  If the current user is not authenticated,
     * return <code>null</code> instead.  For standard authentication schemes,
     * the returned value will match one of the following constants:
     * <code>BASIC_AUTH</code>, <code>CLIENT_CERT_AUTH</code>,
     * <code>DIGEST_AUTH</code>, or <code>FORM_AUTH</code>.</p>
     */
    public abstract String getAuthType();


    /**
     * <p>Return the login name of the current user, if any.  If the current
     * user is not logged in, return <code>null</code> instead.</p>
     */
    public abstract String getRemoteUser();


    /**
     * <p>Return <code>true</code> if the current user has been logged in,
     * and is included in the specified logical "role".  If the current user
     * is not logged in, or is not included in the specified role, return
     * <code>false</code> instead.</p>
     *
     * @param role Logical role name to be checked
     */
    public abstract boolean isUserInRole(String role);


    /**
     * <p>Return a <code>java.security.Principal</code> object containing
     * the name of the currently authenticated user, if any.  If the current
     * user has not been authenticated, return <code>null</code> instead.</p>
     */
    public abstract java.security.Principal getUserPrincipal();



    /**
     * <p>Force any URL that causes an action to work within a portal/portlet. 
     * This causes the URL to have the required redirection for the specific
     * portal to be included</p>
     *
     * @param sb The input URL to be reformatted
     */
    public abstract String encodeActionURL(String sb);
    
    /**
     * <p>Force any URL that references a resource to work within a
     * portal/portlet. This causes the URL to have the required
     * redirection for the specific portal to be included. In reality,
     * it simply returns an absolute URL.</p>
     *
     * @param sb The input URL to be reformatted
     */

    public abstract String encodeResourceURL(String sb);

    /**

    * PENDING(edburns): this does nothing for Servlets.  What does it to
    * for Portlets?

    */ 

    public abstract String encodeNamespace(String aValue);

    /**
      * <p>Dispatch a request to the apropriate context. In the
      * case of servlets, this is done via "forward", but for
      * portlets, it must use "include".</p>
      *
      * @param requestURI The input URI of the request view.
      */

    public abstract void dispatchMessage(String requestURI) throws IOException, FacesException;
    

    /**
     * <p>Log the specified message to the appropriate context.</p>
     *
     * @param message Message to be logged
     */
    public abstract void log(String message);


    /**
     * <p>Log the specified message, and a stack trace for the specified
     * exception, to the appropriate context.</p>
     *
     * @param message Message to be logged
     * @param throwable Exception to be logged
     */
    public abstract void log(String message, Throwable throwable);


}
