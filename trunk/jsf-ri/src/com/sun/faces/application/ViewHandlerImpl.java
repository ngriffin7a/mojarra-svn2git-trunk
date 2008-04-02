/* 
 * $Id: ViewHandlerImpl.java,v 1.28 2004/01/15 21:34:02 eburns Exp $ 
 */ 


/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


// ViewHandlerImpl.java 

package com.sun.faces.application; 

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.jsp.jstl.core.Config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Iterator;
import java.util.Enumeration;

/** 
 * <B>ViewHandlerImpl</B> is the default implementation class for ViewHandler. 
 * @version $Id: ViewHandlerImpl.java,v 1.28 2004/01/15 21:34:02 eburns Exp $ 
 * 
 * @see javax.faces.application.ViewHandler 
 * 
 */ 
public class ViewHandlerImpl extends Object
        implements ViewHandler { 
    
    // 
    // Private/Protected Constants
    //
    private static final Log log = LogFactory.getLog(ViewHandlerImpl.class);
    
    /**
     * <p>The <code>request</code> scoped attribute to store the
     * {@link javax.faces.webapp.FacesServlet} path of the original
     * request.</p>
     */ 
    private static final String INVOCATION_PATH =
        RIConstants.FACES_PREFIX + "INVOCATION_PATH";        

    //
    // Relationship Instance Variables
    // 

    /**
     * <p>List of url-patterns defined for the FacesServlet.</p>
     */ 
    protected List facesServletMappings;
    
    /**
     * <p>If there are multiple extension mappings, then the
     * mapping returned will be arbitrary.  Return this value
     * in that case.</p>
     */ 
    private String defaultMappingExtension;
    
    /**
     * <p>Store the value of <code>DEFAULT_SUFFIX_PARAM_NAME</code>
     * or, if that isn't defined, the value of <code>DEFAULT_SUFFIX</code>
     */ 
    private String contextDefaultSuffix;
        

    public ViewHandlerImpl() {
        if (log.isDebugEnabled()) {
            log.debug("Created ViewHandler instance ");
        }
    }

    public void renderView(FacesContext context, 
			   UIViewRoot viewToRender) throws IOException, 
             FacesException { 

        if (null == context || null == viewToRender) { 
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        } 
        Application application = context.getApplication();
        if (application instanceof ApplicationImpl) {
            ((ApplicationImpl) application).responseRendered();
        }
        String requestURI = viewToRender.getViewId();       
        if (log.isDebugEnabled()) {
            log.debug("About to render view " + requestURI);
        }
        
        String mapping = getFacesMapping(context);
        String newViewId = requestURI;
        // If we have a valid mapping (meaning we were invoked via the
        // FacesServlet) and we're extension mapped, do the replacement.
        if (mapping != null && !isPrefixMapped(mapping)) {            
            if (log.isDebugEnabled()) {
                log.debug("Found URL pattern mapping to FacesServlet " + mapping);
            }
            newViewId = convertViewId(context, requestURI);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Found no URL patterns mapping to FacesServlet ");
            }
        }
        

        viewToRender.setViewId(newViewId);

        // update the JSTL locale attribute in request scope so that JSTL
        // picks up the locale from viewRoot. This attribute must be updated
        // before the JSTL setBundle tag is called because that is when the
        // new LocalizationContext object is created based on the locale.
        Config.set((ServletRequest) context.getExternalContext().getRequest(), 
                Config.FMT_LOCALE, context.getViewRoot().getLocale());
        if (log.isTraceEnabled()) {
            log.trace("Before dispacthMessage to newViewId " + newViewId);
        }
        context.getExternalContext().dispatchMessage(newViewId);
        if (log.isTraceEnabled()) {
            log.trace("After dispacthMessage to newViewId " + newViewId);
        }

    }

    
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        if (context == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }        

        ExternalContext extContext = context.getExternalContext();

	// set the request character encoding. NOTE! This MUST be done
	// before any request praameter is accessed.
	HttpSession session = null;
	HttpServletRequest request = 
	    (HttpServletRequest) extContext.getRequest();
	String 
	    contentType = null,
	    charEnc = null;

	// look for a charset in the Content-Type header first.
	if (null != (contentType = request.getHeader("Content-Type"))) {
	    // see if this header had a charset
	    String charsetStr = "charset=";
	    int 
		len = charsetStr.length(),
		i = 0;
	    
	    // if we have a charset in this Content-Type header AND it
	    // has a non-zero length.
	    if (-1 != (i = contentType.indexOf(charsetStr)) &&
		(i + len < contentType.length())) {
		charEnc = contentType.substring(i + len);
	    }
	}
	// failing that, look in the session for a previously saved one
	if (null == charEnc) {
	    if (null != (session=(HttpSession)extContext.getSession(false))) {
		charEnc =(String) session.getAttribute(CHARACTER_ENCODING_KEY);
	    }
	}
	if (null != charEnc) {
	    try {
                if (log.isTraceEnabled()) {
                    log.trace("set character encoding on request to " + charEnc);
                }
		request.setCharacterEncoding(charEnc);
	    }
	    catch (java.io.UnsupportedEncodingException uee) {
                if (log.isErrorEnabled()) {
                    log.error(uee.getMessage(), uee);
                }
		throw new FacesException(uee);
	    }
	}

        String mapping = getFacesMapping(context);
        UIViewRoot viewRoot = null;
        
        if (mapping != null && !isPrefixMapped(mapping)) {
            viewId = convertViewId(context, viewId);
        }
        
        // maping could be null if a non-faces request triggered
        // this response.
        if (extContext.getRequestPathInfo() == null && mapping != null &&
            isPrefixMapped(mapping)) {                                   
            // this was probably an initial request
            // send them off to the root of the web application
            try {
                Object response = extContext.getResponse();
                context.responseComplete();
                if (log.isDebugEnabled()) {
                    log.debug("Response Complete for" + viewId);
                }
                // PENDING -- Need to consider Portlets
                if (response instanceof HttpServletResponse) {
                    ((HttpServletResponse) response).sendRedirect(
                        extContext.getRequestContextPath());
                }
            } catch (IOException ioe) {
                throw new FacesException(ioe);
            }           
        } 
	else {
	    viewRoot = Util.getStateManager(context).restoreView(context, viewId);
        }        
	
        return viewRoot;
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
        if (context == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
        Locale locale = null;
        String renderKitId = null;

        // use the locale from the previous view if is was one which will be
        // the case if this is called from NavigationHandler. There wouldn't be 
        // one for the initial case.
        if (context.getViewRoot() != null ) {
            locale = context.getViewRoot().getLocale();
            renderKitId = context.getViewRoot().getRenderKitId();
        }
	UIViewRoot result = new UIViewRoot();
	result.setViewId(viewId);
        if (log.isDebugEnabled()) {
            log.debug("Created new view for " + viewId);
        }
	// PENDING(): not sure if we should set the RenderKitId here.
	// The UIViewRoot ctor sets the renderKitId to the default
	// one.
        // if there was no locale from the previous view, calculate the locale 
        // for this view.
        if (locale == null) {
            locale = calculateLocale(context);
            if (log.isDebugEnabled()) {
                log.debug("Locale for this view as determined by calculateLocale " 
                        + locale.toString());
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Using locale from previous view " + locale.toString());
            }
        }

        if (renderKitId == null) {
            renderKitId = calculateRenderKitId(context);
            if (log.isDebugEnabled()) {
                log.debug("RenderKitId for this view as determined by calculateRenderKitId " 
                        + renderKitId);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Using renderKitId from previous view " + renderKitId);
            }
        }
          
        result.setLocale(locale);
        result.setRenderKitId(renderKitId);

	return result;
    }
    
    public Locale calculateLocale(FacesContext context) {
        
        if (context == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
        
        Locale result = null;
        // determine the locales that are acceptable to the client based on the 
        // Accept-Language header and the find the best match among the 
        // supported locales specified by the client.
        Enumeration enum = ((ServletRequest)
                context.getExternalContext().getRequest()).getLocales();
        while (enum.hasMoreElements()) {
            Locale perf = (Locale) enum.nextElement();
            result = findMatch(context, perf);
            if (result != null) {
                break;
            }
        }
        // no match is found.
        if ( result == null ) {
            if (context.getApplication().getDefaultLocale() == null ) {
                result = Locale.getDefault();
            } else {
                result = context.getApplication().getDefaultLocale();
            }
        }
        return result;
    }


    public String calculateRenderKitId(FacesContext context) {
        
        if (context == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
        
        return RenderKitFactory.DEFAULT_RENDER_KIT;
    }


    /**
     * Attempts to find a matching locale based on <code>perf></code> and 
     * list of supported locales, using the matching algorithm
     * as described in JSTL 8.3.2.
     */
    protected Locale findMatch(FacesContext context, Locale perf) {
        Locale result = null;
        Iterator it = context.getApplication().getSupportedLocales();
        while (it.hasNext()) {
            Locale supportedLocale = (Locale)it.next();
            
            if ( perf.equals(supportedLocale)) {
                // exact match
                result = supportedLocale;
                break;
            } else {
                // Make sure the preferred locale doesn't have  country set, when 
                // doing a language match, For ex., if the preferred locale is
                // "en-US", if one of supported locales is "en-UK", even though 
                // its language matches that of the preferred locale, we must 
                // ignore it.
                if (perf.getLanguage().equals(supportedLocale.getLanguage()) &&
                    supportedLocale.getCountry().equals("")) {
                    result = supportedLocale;
                }
            }
        }
        return result;
    }
    
    public void writeState(FacesContext context) throws IOException {
        if (context == null) {
            throw new NullPointerException(Util.getExceptionMessage(
								    Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin writing state to response for viewId" + 
                    context.getViewRoot().getViewId());
        }
	if (Util.getStateManager(context).isSavingStateInClient(context)) {
	    context.getResponseWriter().writeText(RIConstants.SAVESTATE_FIELD_MARKER,null);
	}
        if (log.isTraceEnabled()) {
            log.trace("End writing state to response for viewId" + 
                    context.getViewRoot().getViewId());
        }
    }
    
    
    public String getViewIdPath(FacesContext context, String viewId) {             
        
        if (context == null || viewId == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }               
        
        if (viewId.charAt(0) != '/') {
            String message = Util.getExceptionMessage(Util.ILLEGAL_VIEW_ID_ID,
                new Object[] { viewId });
            if (log.isErrorEnabled()) {
                log.error(message + " " + viewId);
            }
            throw new IllegalArgumentException(message);
        }
        
        // Check our cache for a processed result        
        String mapping = getFacesMapping(context);
        
        
        // No previous processed value
        StringBuffer buffer = new StringBuffer(viewId);        
        
        // If mapping was null, then this was a response
        // generated by a non-faces request.
        
        if (mapping != null) {
            if (isPrefixMapped(mapping)) {
                // prefix path mapping
            
                // if the mapping returned is only "/*", then the 
                // return the viewId with no modifications
                if (mapping.equals("/*")) {
                    if (log.isTraceEnabled()) {
                        log.trace("viewId returned with no modifications " + viewId);
                    }
                    return viewId;
                } else {
                    buffer.insert(0, mapping);
                }
            } else {
                // extension mapping            
            
                // only replace or append if the viewId doesn't already
                // end with the mapping.
                if (!viewId.endsWith(mapping)) {
                    int extIdx = viewId.lastIndexOf('.');
                    if (extIdx != -1) {
                        buffer.replace(extIdx, viewId.length(), mapping);
                    } else {
                        // no extension in the provided viewId, append
                        // the current value
                        buffer.append(mapping);
                    }
                    if (log.isTraceEnabled()) {
                        log.trace("viewId after modifications " + buffer.toString());
                    }
                }
            }
        }
        
        return buffer.toString();
    }

    /**
     * <p>Specifies a <code>List</code> of one or more URL patterns 
     * mapped to one or more {@link javax.faces.webapp.FacesServlet} 
     * instances.</p>
     * 
     * @param mappings the URL patterns of the 
     *  defined {@link javax.faces.webapp.FacesServlet}   
     * 
     * @exception NullPointerException if <code>mappings</code> is null 
     */ 
    public void setFacesMapping(List mappings) {
        
        if (mappings == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }  
	
        if (log.isTraceEnabled()) {
             log.debug("Setting a list of URL patterns mappings");
        }
	synchronized (this) {
	    facesServletMappings = 
		Collections.unmodifiableList(new ArrayList(mappings));
        
	    int size = facesServletMappings.size();
	    if (facesServletMappings.size() > 1) {
		for (int i = 0; i < size; i++) {
		    String mapping = (String) facesServletMappings.get(i);
		    if (mapping.charAt(0) == '.') {
			defaultMappingExtension = mapping;
			break;
		    }
		}
	    }
	}
    }

    /**
     * <p>Returns the URL pattern of the 
     * {@link javax.faces.webapp.FacesServlet} that
     * is executing the current request.  If there are multiple
     * URL patterns, the value returned by 
     * <code>HttpServletRequest.getServletPath()</code> and
     * <code>HttpServletRequest.getPathInfo()</code> is 
     * used to determine which mapping to return.</p>
     * If no mapping can be determined, it most likely means
     * that this particular request wasn't dispatched through
     * the {@link javax.faces.webapp.FacesServlet}.
     * 
     * @param context the {@link FacesContext} of the current request
     *  
     * @return the URL pattern of the {@link javax.faces.webapp.FacesServlet}
     *  or <code>null</code> if no mapping can be determined
     * 
     * @exception NullPointerException if <code>context</code> is null
     */ 
    private String getFacesMapping(FacesContext context) {
        // PENDING (rlubke) Need to handle the Portlet case
        
        if (context == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }                
        
        // Check for a previously stored mapping   
        ExternalContext extContext = context.getExternalContext();
        String mapping = 
            (String) extContext.getRequestMap().get(INVOCATION_PATH);
        
        if (mapping == null) {

            Object request = extContext.getRequest();
            String servletPath = null;
            String pathInfo = null;            
        
            // first check for javax.servlet.forward.servlet_path
            // and javax.servlet.forward.path_info for non-null
            // values.  if either is non-null, use this
            // information to generate determine the mapping.
        
            if (request instanceof HttpServletRequest) {
                servletPath = extContext.getRequestServletPath();
                pathInfo = extContext.getRequestPathInfo();
            }


            mapping = getMappingForRequest(servletPath, pathInfo);
            if (mapping == null) {
                String message = Util.getExceptionMessage(
                    Util.FACES_SERVLET_MAPPING_CANNOT_BE_DETERMINED_ID,
                    new Object[]{servletPath});
                if (log.isWarnEnabled()) {
                    log.warn(message);
                }            
                //throw new FacesException(message);
            }
        }
        
        if (mapping != null) {
            extContext.getRequestMap().put(INVOCATION_PATH, mapping);
        }
        if (log.isDebugEnabled()) {
            log.debug("URL pattern of the FacesServlet executing the current request "
                + mapping);
        }
        return mapping;        
    }
    
    /**
     * <p>Return the appropriate {@link javax.faces.webapp.FacesServlet} mapping
     * based on the servlet path of the current request.</p>
     * 
     * @param servletPath the servlet path of the request
     * @param pathInfo the path info of the request
     * 
     * @see HttpServletRequest#getServletPath()
     */ 
    private String getMappingForRequest(String servletPath, String pathInfo) {
        
        if (servletPath == null) {
            return null;
        }                
        if (log.isTraceEnabled()) {
            log.trace("servletPath "+ servletPath);
            log.trace("pathInfo " + pathInfo);
        }                  
        String mapping = null;
        
        // If the path returned by HttpServletRequest.getServletPath()
        // returns a zero-length String, then the FacesServlet has
        // been mapped to '/*'.
        if (servletPath.length() == 0) {
            int idx = facesServletMappings.indexOf("/*");
            if (idx != -1) {
                return "/*";
            } else {
                // Shouldn't happen...
                if (log.isWarnEnabled()) {
                    log.warn("Error: FacesServlet mapping does not contain \"/*\" ");
                }
                return null;
            }
        }
        
        // presence of path info means we were invoked
        // using a prefix path mapping
        if (pathInfo != null) {
            for (int i = 0, size = facesServletMappings.size();
                 i < size; i++) {

                String temp = (String) facesServletMappings.get(i);

                if (servletPath.equals(temp)) {
                    mapping = temp;
                    break;
                }
            }   
        } else {
            // Servlet could have been invoked using extension mapping. 
            // Check the mappings in our List for a matching
            // extension mapping.       
            for (int i = 0, size = facesServletMappings.size();
                 i < size; i++) {
                String temp = (String) facesServletMappings.get(i);
                if (servletPath.endsWith(temp)) {
                    mapping = temp;
                    break;
                } else if (servletPath.equals(temp)) {
                    // The request that came in was in fact
                    // prefix path matched, but was sent to the
                    // servlet with no path info, i.e.
                    // /<context_root>/faces or /<context_root>/faces/
                    mapping = temp;
                    break;
                }
            }

            // no exact extension mapping found, return the
            // default mapping extension.
            if (mapping == null) {
                mapping = defaultMappingExtension;
                if (log.isDebugEnabled()) {
                    log.debug("Using default mapping extension "+ mapping);
                }
            }
        }
            
        return mapping;
    }
    
    /**
     * <p>Returns true if the provided <code>url-mapping</code> is
     * a prefix path mapping (starts with <code>/</code>).</p>
     * @param mapping a <code>url-pattern</code>
     * @return true if the mapping starts with <code>/</code>
     */    
    private static boolean isPrefixMapped(String mapping) {
        return (mapping.charAt(0) == '/');
    }
    
    /**
     * <p>Adjust the viewID per the requirements of {@link #renderView}.</p>
     * @param context current {@link FacesContext}
     * @param viewId incoming view ID
     * @return the view ID with an altered suffix mapping (if necessary)
     */ 
    private String convertViewId(FacesContext context, String viewId) {
        synchronized (this) {
            if (contextDefaultSuffix == null) {
                contextDefaultSuffix = context.getExternalContext().
                        getInitParameter(ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
                if (contextDefaultSuffix == null) {
                    contextDefaultSuffix = ViewHandler.DEFAULT_SUFFIX;
                }
                if (log.isDebugEnabled()) {
                    log.debug("contextDefaultSuffix " + contextDefaultSuffix);
                }
            }
        }
        String convertedViewId = viewId;    
        // if the viewId doesn't already use the above suffix,
        // replace or append.
        if (!convertedViewId.endsWith(contextDefaultSuffix)) {
            StringBuffer buffer = new StringBuffer(convertedViewId);
            int extIdx = convertedViewId.lastIndexOf('.');
            if (extIdx != -1) {
                buffer.replace(extIdx, convertedViewId.length(),
                        contextDefaultSuffix);
            } else {
                // no extension in the provided viewId, append the suffix
                buffer.append(contextDefaultSuffix);
            }
            convertedViewId = buffer.toString();
            if (log.isDebugEnabled()) {
                log.debug("viewId after appending the context suffix " +
                        convertedViewId);
            }

        }
        return convertedViewId;
    }

        
} 
