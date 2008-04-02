/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package renderkits.renderkit.svg;

import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * <p><B>SVGRenderKit</B> is a class that houses a collection of <code>SVG</code>
 * renderers.  It also creates the <code>ResponseWriter</code> used to write
 * <code>SVG</code> markup.</p> 
 */
public class SVGRenderKit extends RenderKit {

//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    // used for ResponseWriter creation;
    private static String SVG_CONTENT_TYPE = "image/svg+xml";
    private static String APPLICATION_XML_CONTENT_TYPE = "application/xml";
    private static String TEXT_XML_CONTENT_TYPE = "text/xml";
    
    private static String CHAR_ENCODING = "ISO-8859-1";
    private static String CONTENT_TYPE_IS_SVG = "ContentTypeIsSVG";
//
// Ivars used during actual client lifetime
//

// Relationship Instance Variables

    /**
     * Keys are String renderer family.  Values are HashMaps.  Nested
     * HashMap keys are Strings for the rendererType, and values are the
     * Renderer instances themselves.
     */

    private HashMap rendererFamilies;

    private ResponseStateManager responseStateManager = null;
//
// Constructors and Initializers    
//

    public SVGRenderKit() {
        super();
	rendererFamilies = new HashMap();
    }


    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From RenderKit
    //

    public void addRenderer(String family, String rendererType,
                            Renderer renderer) {
        if (family == null || rendererType == null || renderer == null) {
            // PENDING - i18n
            String message = "Argument Error: One or more parameters are null."; 
            message = message + " family " + family + " rendererType " +
                rendererType + " renderer " + renderer;
            throw new NullPointerException(message);
                
        }
	HashMap renderers = null;

        synchronized (rendererFamilies) {
	    if (null == (renderers = (HashMap) rendererFamilies.get(family))) {
		rendererFamilies.put(family, renderers = new HashMap());
	    }
            renderers.put(rendererType, renderer);
        }
    }


    public Renderer getRenderer(String family, String rendererType) {

        if (rendererType == null || family == null) {
            // PENDING - i18n
            String message = "Argument Error: One or more parameters are null."; 
            message = message + " family " + family + " rendererType " +
                rendererType;
            throw new NullPointerException(message);
        }

	HashMap renderers = null;
        Renderer renderer = null;

	if (null != (renderers = (HashMap) rendererFamilies.get(family))) {
	    renderer = (Renderer) renderers.get(rendererType);
	}
	
        return renderer;
    }


    public synchronized ResponseStateManager getResponseStateManager() {
        if (responseStateManager == null) {
            responseStateManager = new SVGResponseStateManager();
        }
        return responseStateManager;
    }


    public ResponseWriter createResponseWriter(Writer writer, 
					       String desiredContentTypeList,
                                               String characterEncoding) {
        if (writer == null) {
            return null;
        }
        String contentType = null;
	FacesContext context = FacesContext.getCurrentInstance();
        
        String [] supportedTypes = { SVG_CONTENT_TYPE, APPLICATION_XML_CONTENT_TYPE,
            TEXT_XML_CONTENT_TYPE};
        String [] desiredTypes = null;
            

        // Obtain the desired content type list
	// first crack is the passed in list
	if (null == desiredContentTypeList) {
	    // second crack is the response content type
	    desiredContentTypeList = 
                    context.getExternalContext().getResponseContentType();
	}
        if (null == desiredContentTypeList) {
            // third crack is the Accept header.
            desiredContentTypeList = (String)
	      context.getExternalContext().getRequestHeaderMap().get("Accept");
        }
        // fourth, default to text/html
        if (null == desiredContentTypeList) {
            desiredContentTypeList = "text/html";
        }

	if (null != desiredContentTypeList) {
	    Map requestMap = context.getExternalContext().getRequestMap();
	    
	    desiredTypes = contentTypeSplit(desiredContentTypeList);
	    String curContentType = null, curDesiredType = null;                       
            
            // For each entry in the desiredTypes array, look for a match in 
            // the supportedTypes array
	    for (int i = 0; i < desiredTypes.length; i++) {
                curDesiredType = desiredTypes[i];
                for (int j = 0; j < supportedTypes.length; j++) {
                    curContentType = supportedTypes[j].trim();
                    if (-1 != curDesiredType.indexOf(curContentType)) {
                        contentType = curDesiredType;
                        /*
                        if (-1 != curContentType.indexOf(SVG_CONTENT_TYPE)) {
                            contentType = SVG_CONTENT_TYPE;
                            requestMap.put(CONTENT_TYPE_IS_SVG, Boolean.TRUE);
                        }
                        else if (-1 != curContentType.indexOf(XHTML_CONTENT_TYPE) ||
                                 -1 != curContentType.indexOf(APPLICATION_XML_CONTENT_TYPE) ||
                                 -1 != curContentType.indexOf(TEXT_XML_CONTENT_TYPE)) {
                            contentType = XHTML_CONTENT_TYPE;
                            requestMap.put(RIConstants.CONTENT_TYPE_IS_XHTML,
                                	   Boolean.TRUE);
                        }*/        
                        break;
                    }
                }
                if (null != contentType) {
                    break;
                }
	    }
	    // If none of the contentTypes about which we know was in
	    // desiredContentTypeList
	    if (null == contentType) {
                // PENDING - i18n
                throw new IllegalArgumentException("Unrecognized Content Type.");
	    }
	}
	else {
	    // there was no argument contentType list, or Accept header
	    contentType = SVG_CONTENT_TYPE;
	}

        if (characterEncoding == null) {
            characterEncoding = CHAR_ENCODING;
        }

        return new SVGResponseWriter(writer, contentType, characterEncoding);
    }
    
    private String[] contentTypeSplit(String contentTypeString) {
        String [] result = contentTypeString.split(",");
        for (int i = 0; i < result.length; i++) {
            int semicolon = result[i].indexOf(";");
            if (-1 != semicolon) {
                result[i] = result[i].substring(0,semicolon);
            }
        }
        return result;
    }


    public ResponseStream createResponseStream(OutputStream out) {
        final OutputStream output = out;
        return new ResponseStream() {
            public void write(int b) throws IOException {
                output.write(b);
            }


            public void write(byte b[]) throws IOException {
                output.write(b);
            }


            public void write(byte b[], int off, int len) throws IOException {
                output.write(b, off, len);
            }


            public void flush() throws IOException {
                output.flush();
            }


            public void close() throws IOException {
                output.close();
            }
        };
    }       

} // end of class SVGRenderKit

