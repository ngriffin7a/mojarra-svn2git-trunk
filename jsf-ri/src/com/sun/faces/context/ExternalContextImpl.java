/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.context;

import java.io.OutputStream;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.TypedCollections;
import com.sun.faces.util.Util;
import com.sun.faces.context.UrlBuilder;

/**
 * <p>This implementation of {@link ExternalContext} is specific to the
 * servlet implementation.
 *
 * @author Brendan Murray
 * @version $Id: ExternalContextImpl.java,v 1.68 2008/01/28 20:55:36 rlubke Exp $
 */
public class ExternalContextImpl extends ExternalContext {

    private ServletContext servletContext = null;
    private ServletRequest request = null;
    private ServletResponse response = null;

    private Map<String,Object> applicationMap = null;
    private Map<String,Object> sessionMap = null;
    private Map<String,Object> requestMap = null;
    private Map<String,String> requestParameterMap = null;
    private Map<String,String[]> requestParameterValuesMap = null;
    private Map<String,String> requestHeaderMap = null;
    private Map<String,String[]> requestHeaderValuesMap = null;
    private Map<String,Object> cookieMap = null;
    private Map<String,String> initParameterMap = null;

    private enum ALLOWABLE_COOKIE_PROPERTIES {
        domain,
        maxAge,
        path,
        secure
    }

    static final Class theUnmodifiableMapClass =
        Collections.unmodifiableMap(new HashMap<Object,Object>()).getClass();


    // ------------------------------------------------------------ Constructors

    
    public ExternalContextImpl(ServletContext sc,
                               ServletRequest request,
                               ServletResponse response) {

        // Validate the incoming parameters
        try {
            Util.notNull("sc", sc);
            Util.notNull("request", request);
            Util.notNull("response", response);
        } catch (Exception e) {
            throw new FacesException(
                MessageUtils.getExceptionMessageString(
                    MessageUtils.FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID));
        }

        // Save references to our context, request, and response
        this.servletContext = sc;
        this.request = request;        
        this.response = response;
        WebConfiguration config = WebConfiguration.getInstance(sc);
        if (config
              .isOptionEnabled(BooleanWebContextInitParameter.SendPoweredByHeader)) {
            ((HttpServletResponse) response)
                  .addHeader("X-Powered-By", "JSF/2.0");
        }
        
    }


    // -------------------------------------------- Methods from ExternalContext


    /**
     * @see ExternalContext#getSession(boolean)
     */
    public Object getSession(boolean create) {
        return (((HttpServletRequest) request).getSession(create));
    }


    /**
     * @see javax.faces.context.ExternalContext#getContext()
     */
    public Object getContext() {
        return this.servletContext;
    }


    /**
     * @see javax.faces.context.ExternalContext#getContextName()
     */
    public String getContextName() {

        if (servletContext.getMajorVersion() >= 3
            || (servletContext.getMajorVersion() == 2
                && servletContext.getMinorVersion() == 5)) {
            return this.servletContext.getServletContextName();
        } else {
            // for servlet 2.4 support
            return servletContext.getServletContextName();
        }
        
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequest()
     */
    public Object getRequest() {
        return this.request;
    }


    /**
     * @see ExternalContext#setRequest(Object)
     */
    public void setRequest(Object request) {
        if (request instanceof ServletRequest) {
            this.request = (ServletRequest) request;
            requestHeaderMap = null;
            requestHeaderValuesMap = null;
            requestHeaderValuesMap = null;
            requestMap = null;
            requestParameterMap = null;
            requestParameterValuesMap = null;
        }
    }


    /**
     * @see ExternalContext#setRequestCharacterEncoding(String)
     */
    public void setRequestCharacterEncoding(String encoding) throws UnsupportedEncodingException {
        request.setCharacterEncoding(encoding);
    }


    /**
     * @see javax.faces.context.ExternalContext#getResponse()
     */
    public Object getResponse() {
        return this.response;
    }


    /**
     * @see ExternalContext#setResponse(Object)
     */
    public void setResponse(Object response) {
        if (response instanceof ServletResponse) {
            this.response = (ServletResponse) response;
        }
    }


    /**
     * @see ExternalContext#setResponseCharacterEncoding(String)
     */
    public void setResponseCharacterEncoding(String encoding) {
        response.setCharacterEncoding(encoding);
    }


    /**
     * @see javax.faces.context.ExternalContext#getApplicationMap()
     */
    public Map<String,Object> getApplicationMap() {
        if (applicationMap == null) {
            applicationMap = new ApplicationMap(servletContext);
        }
        return applicationMap;
    }


    /**
     * @see javax.faces.context.ExternalContext#getSessionMap()
     */
    public Map<String,Object> getSessionMap() {
        if (sessionMap == null) {
            sessionMap = new SessionMap((HttpServletRequest) request,
                                        FacesContext.getCurrentInstance()
                                              .getApplication().getProjectStage());
        }
        return sessionMap;
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestMap()
     */
    public Map<String,Object> getRequestMap() {
        if (requestMap == null) {
            requestMap = new RequestMap(this.request);
        }
        return requestMap;
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestHeaderMap()
     */
    public Map<String,String> getRequestHeaderMap() {
        if (null == requestHeaderMap) {
            requestHeaderMap = 
                Collections.unmodifiableMap(
                    new RequestHeaderMap((HttpServletRequest) request));
        }
        return requestHeaderMap;
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestHeaderValuesMap()
     */
    public Map<String,String[]> getRequestHeaderValuesMap() {
        if (null == requestHeaderValuesMap) {
            requestHeaderValuesMap = 
                Collections.unmodifiableMap(
                    new RequestHeaderValuesMap((HttpServletRequest) request));
        }
        return requestHeaderValuesMap;
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestCookieMap()
     */
    public Map<String,Object> getRequestCookieMap() {
        if (null == cookieMap) {
            cookieMap =
                Collections.unmodifiableMap(
                    new RequestCookieMap((HttpServletRequest) request));
        }
        return cookieMap;
    }


    /**
     * @see javax.faces.context.ExternalContext#getInitParameterMap()
     */
    public Map<String,String> getInitParameterMap() {
        if (null == initParameterMap) {
            initParameterMap = 
                Collections.unmodifiableMap(
                    new InitParameterMap(servletContext));
        }
        return initParameterMap;
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestParameterMap()
     */
    public Map<String,String> getRequestParameterMap() {
        if (null == requestParameterMap) {
            requestParameterMap = 
                Collections.unmodifiableMap(
                    new RequestParameterMap(request));
        }
        return requestParameterMap;
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestParameterValuesMap()
     */
    public Map<String,String[]> getRequestParameterValuesMap() {
        if (null == requestParameterValuesMap) {
            requestParameterValuesMap = 
                Collections.unmodifiableMap(
                    new RequestParameterValuesMap(request));
        }
        return requestParameterValuesMap;
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestParameterNames()
     */
    public Iterator<String> getRequestParameterNames() {
        final Enumeration namEnum = request.getParameterNames();

        return new Iterator<String>() {
            public boolean hasNext() {
                return namEnum.hasMoreElements();
            }


            public String next() {
                return (String) namEnum.nextElement();
            }


            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestLocale()
     */
    public Locale getRequestLocale() {
        return request.getLocale();
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestLocales()
     */
    public Iterator<Locale> getRequestLocales() {
        return (new LocalesIterator(request.getLocales()));
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestPathInfo()
     */
    public String getRequestPathInfo() {
        return (((HttpServletRequest) request).getPathInfo());
    }


    /**
     * @see ExternalContext#getRealPath(String)
     */
    @Override
    public String getRealPath(String path) {
        return servletContext.getRealPath(path);
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestContextPath()
     */
    public String getRequestContextPath() {
        return (((HttpServletRequest) request).getContextPath());
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestServletPath()
     */
    public String getRequestServletPath() {
        return (((HttpServletRequest) request).getServletPath());
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestCharacterEncoding()
     */
    @Override
    public String getRequestCharacterEncoding() {
        return (request.getCharacterEncoding());
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestContentType()
     */
    @Override
    public String getRequestContentType() {
        return (request.getContentType());
    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestContentLength()
     */
    @Override
    public int getRequestContentLength() {
        return (request.getContentLength());
    }


    /**
     * @see javax.faces.context.ExternalContext#getResponseCharacterEncoding()
     */
    @Override
    public String getResponseCharacterEncoding() {
        return (response.getCharacterEncoding());
    }


    /**
     * @see javax.faces.context.ExternalContext#getResponseContentType()
     */
    public String getResponseContentType() {
        return (response.getContentType());
    }


    /**
     * @see javax.faces.context.ExternalContext#getInitParameter(String)
     */
    public String getInitParameter(String name) {
        return servletContext.getInitParameter(name);
    }


    /**
     * @see javax.faces.context.ExternalContext#getResourcePaths(String)
     */
    public Set<String> getResourcePaths(String path) {
        return TypedCollections.dynamicallyCastSet(servletContext.getResourcePaths(path), String.class);
    }


    /**
     * @see javax.faces.context.ExternalContext#getResourceAsStream(String)
     */
    public InputStream getResourceAsStream(String path) {
        return servletContext.getResourceAsStream(path);
    }


    /**
     * @see ExternalContext#getResource(String)
     */
    public URL getResource(String path) {
        URL url;
        try {
            url = servletContext.getResource(path);
        } catch (MalformedURLException e) {
            return null;
        }
        return url;
    }


    /**
     * @see ExternalContext#encodeActionURL(String)
     */
    public String encodeActionURL(String url) {
        return ((HttpServletResponse) response).encodeURL(url);
    }


    /**
     * @see ExternalContext#encodeResourceURL(String)
     */
    public String encodeResourceURL(String url) {
        return ((HttpServletResponse) response).encodeURL(url);
    }


    /**
     * @see ExternalContext#encodeNamespace(String)
     */
    public String encodeNamespace(String name) {
        return name; // Do nothing for servlets
    }


    /**
     * @see ExternalContext#dispatch(String)
     */
    public void dispatch(String requestURI) throws IOException, FacesException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(
            requestURI);
        if (requestDispatcher == null) {
            ((HttpServletResponse) response).
                    sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            requestDispatcher.forward(this.request, this.response);
        } catch (IOException ioe) {
            // e.printStackTrace();
            throw ioe;
        } catch (ServletException se) {
            throw new FacesException(se);
        }
    }


    /**
     * @see ExternalContext#redirect(String)
     */
    public void redirect(String requestURI) throws IOException {
        ((HttpServletResponse) response).sendRedirect(requestURI);
        FacesContext.getCurrentInstance().responseComplete();
    }


    /**
     * @see ExternalContext#log(String)
     */
    public void log(String message) {
        servletContext.log(message);
    }


    /**
     * @see ExternalContext#log(String, Throwable)
     */
    public void log(String message, Throwable throwable) {
        servletContext.log(message, throwable);
    }


    /**
     * @see javax.faces.context.ExternalContext#getAuthType()
     */
    public String getAuthType() {
        return ((HttpServletRequest) request).getAuthType();
    }

    /**
     * @see ExternalContext#getMimeType(String)
     */
    @Override
    public String getMimeType(String file) {
        return servletContext.getMimeType(file);
    }


    /**
     * @see javax.faces.context.ExternalContext#getRemoteUser()
     */
    public String getRemoteUser() {
        return ((HttpServletRequest) request).getRemoteUser();
    }


    /**
     * @see javax.faces.context.ExternalContext#getUserPrincipal()
     */
    public java.security.Principal getUserPrincipal() {
        return ((HttpServletRequest) request).getUserPrincipal();
    }


    /**
     * @see javax.faces.context.ExternalContext#isUserInRole(String)
     */
    public boolean isUserInRole(String role) {
        return ((HttpServletRequest) request).isUserInRole(role);
    }


    /**
     * @see javax.faces.context.ExternalContext#invalidateSession()
     */
    @Override
    public void invalidateSession() {

        HttpSession session = ((HttpServletRequest) request).getSession(false);
        if (session != null) {
            session.invalidate();
        }

    }


    /**
     * @see ExternalContext#addResponseCookie(String, String, java.util.Map)
     * @param name
     * @param value
     * @param properties
     */
    @Override
    public void addResponseCookie(String name,
                                  String value,
                                  Map<String,Object> properties) {

        HttpServletResponse res = (HttpServletResponse) response;

        Cookie cookie = new Cookie(name, value);
        if (properties != null && properties.size() != 0) {
            for (Map.Entry<String,Object> entry : properties.entrySet()) {
                String key = entry.getKey();
                ALLOWABLE_COOKIE_PROPERTIES p = ALLOWABLE_COOKIE_PROPERTIES.valueOf(key);
                Object v = entry.getValue();
                switch (p) {
                    case domain:
                        cookie.setDomain((String) v);
                        break;
                    case maxAge:
                        cookie.setMaxAge((Integer) v);
                        break;
                    case path:
                        cookie.setPath((String) v);
                        break;
                    case secure:
                        cookie.setSecure((Boolean) v);
                        break;
                    default:
                        throw new IllegalStateException(); // shouldn't happen
                }
            }
        }
        res.addCookie(cookie);

    }


    /**
     * @see javax.faces.context.ExternalContext#getResponseOutputStream()
     */
    @Override
    public OutputStream getResponseOutputStream() throws IOException {

        return response.getOutputStream();
        
    }


    /**
     * @see javax.faces.context.ExternalContext#getResponseOutputWriter()
     */
    @Override
    public Writer getResponseOutputWriter() throws IOException {

        return response.getWriter();
        
    }

    /**
     * @see javax.faces.context.ExternalContext#getRequestScheme()
     */
    @Override
    public String getRequestScheme() {

        return request.getScheme();

    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestServerName()
     */
    @Override
    public String getRequestServerName() {

        return request.getServerName();

    }


    /**
     * @see javax.faces.context.ExternalContext#getRequestServerPort()
     */
    @Override
    public int getRequestServerPort() {

        return request.getServerPort();

    }


    /**
     * @see ExternalContext#setResponseContentType(String)
     */
    @Override
    public void setResponseContentType(String contentType) {

        response.setContentType(contentType);
        
    }


    /**
     * @see ExternalContext#setResponseHeader(String, String)
     */
    @Override
    public void setResponseHeader(String name, String value) {

        ((HttpServletResponse) response).setHeader(name, value);
        
    }


    /**
     * @see ExternalContext#addResponseHeader(String, String)
     */
    @Override
    public void addResponseHeader(String name, String value) {

        ((HttpServletResponse) response).addHeader(name, value);
        
    }


    /**
     * @see javax.faces.context.ExternalContext#setResponseBufferSize(int)
     */
    @Override
    public void setResponseBufferSize(int size) {

        response.setBufferSize(size);

    }


    /**
     * @see javax.faces.context.ExternalContext#isResponseCommitted()
     */
    @Override
    public boolean isResponseCommitted() {

        return response.isCommitted();

    }


    /**
     * @see javax.faces.context.ExternalContext#responseReset()
     */
    @Override
    public void responseReset() {

        response.reset();

    }


    /**
     * @see javax.faces.context.ExternalContext#responseSendError(int, String)
     */
    @Override
    public void responseSendError(int statusCode, String message) throws IOException {

        if (message == null) {
            ((HttpServletResponse) response).sendError(statusCode);
        } else {
            ((HttpServletResponse) response).sendError(statusCode, message);
        }
        
    }


    /**
     * @see javax.faces.context.ExternalContext#setResponseStatus(int)
     */
    @Override
    public void setResponseStatus(int statusCode) {

        ((HttpServletResponse) response).setStatus(statusCode);

    }

    
    /**
     * @see javax.faces.context.ExternalContext#responseFlushBuffer()
     */
    @Override
    public void responseFlushBuffer() throws IOException {

        response.flushBuffer();

    }

    /**
     * @see javax.faces.context.ExternalContext#setResponseContentLength(int)
     */
    @Override
    public void setResponseContentLength(int length) {

        response.setContentLength(length);

    }

    /**
     * @see javax.faces.context.ExternalContext#getResponseBufferSize()
     * @return
     */
    @Override
    public int getResponseBufferSize() {

        return response.getBufferSize();

    }

    @Override
    public String encodeBookmarkableURL(String baseUrl,
                                        Map<String, List<String>> parameters) {

        UrlBuilder builder = new UrlBuilder(baseUrl, getResponseCharacterEncoding());
        builder.addParameters(parameters);
        return builder.createUrl();

    }

    @Override
    public String encodeRedirectURL(String baseUrl,
                                    Map<String, List<String>> parameters) {

        UrlBuilder builder = new UrlBuilder(baseUrl, getResponseCharacterEncoding());
        builder.addParameters(parameters);
        return builder.createUrl();
        
    }


    // ----------------------------------------------------------- Inner Classes


    private static class LocalesIterator implements Iterator<Locale> {

        public LocalesIterator(Enumeration locales) {
            this.locales = locales;
        }


        private Enumeration locales;


        public boolean hasNext() {
            return locales.hasMoreElements();
        }


        public Locale next() {
            return (Locale) locales.nextElement();
        }


        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}

