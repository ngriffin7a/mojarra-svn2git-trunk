/*
 * $Id: ConfigListener.java,v 1.9 2003/05/04 03:08:09 eburns Exp $
 */
/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package com.sun.faces.config;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;
import com.sun.faces.application.ApplicationImpl;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import java.util.StringTokenizer;
import org.mozilla.util.Assert;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;

public class ConfigListener implements ServletContextListener
{
    //
    // Protected Constants
    //

    // Log instance for this class
    protected static Log log = LogFactory.getLog(ConfigListener.class);

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    ConfigParser configParser = null;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public ConfigListener()
    {
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods from ServletContextListener
    //

    public void contextInitialized(ServletContextEvent e) 
    {
	ServletContext servletContext = e.getServletContext();
        configParser = new ConfigParser(servletContext);
	ConfigBase configBase = null;
	String initParamFileList = null;
	InputStream jarInputStream = null;
	
	// Step 0, load our own JSF_RI_CONFIG
	jarInputStream = this.getClass().getClassLoader().
	    getResourceAsStream(RIConstants.JSF_RI_CONFIG);
	Assert.assert_it(null != jarInputStream);

        configBase = configParser.parseConfig(jarInputStream);
	Assert.assert_it(null != configBase);
	// It's an error if this doesn't load.

	// Step 1: scan the META-INF directory of all jar files in
	// "/WEB-INF/lib" for "faces-config.xml" files.
	scanJarsForConfigFiles(servletContext, configParser, configBase);

	// Step 2. If the init parameter exists, load the config from
	// there
	if (null != (initParamFileList = 
		     servletContext.getInitParameter(RIConstants.CONFIG_FILES_INITPARAM))) {
	    StringTokenizer toker = new StringTokenizer(initParamFileList, 
							",");
	    String cur;
	    while (toker.hasMoreTokens()) {
		cur = toker.nextToken();
		if (null != cur && 0 < cur.length()) {
		    cur = cur.trim();

		    try {
			configBase = configParser.parseConfig(cur,
							      servletContext,
							      configBase);
		    } catch (Throwable t) {
                        Object[] obj = new Object[1];
                        obj[0] = cur;
			throw new FacesException(Util.getExceptionMessage(
                                       Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj), t);
		    }
		}
	    }
	}
	else {
	    // Step 3, load the app's "/WEB-INF/faces-config.xml"
	    try {
		configBase = configParser.parseConfig("/WEB-INF/faces-config.xml",
						      servletContext,
						      configBase);
	    }
	    catch (Exception toIgnore) {
		// do nothing, apps are not required to have a faces-config file
	    }
	}	


	// Store the ConfigBase in the Application's AppConfig.
        ApplicationFactory aFactory = 
	    (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = 
	    (ApplicationImpl)aFactory.getApplication();
	application.getAppConfig().setConfigBase(configBase);

        servletContext.setAttribute(RIConstants.CONFIG_ATTR, 
					   configBase);
        if (log.isTraceEnabled()) {
            log.trace("CONFIG BASE SET IN CONTEXT...");
        }
    }

    public void contextDestroyed(ServletContextEvent e) {  
        e.getServletContext().removeAttribute(RIConstants.CONFIG_ATTR);
        configParser = null;
        if (log.isTraceEnabled()) {
            log.trace("CONFIG BASE REMOVED FROM CONTEXT...");
        }
    }

    /**
     * <p>Algorithm:</p>
     *
     * <p>Scan the ServletContext's resourcePaths space for all jars in
     * "/WEB-INF/lib".</p>
     *
     * <p>For each jar, look for a file called
     * "/META-INF/faces-config.xml".  If that file is present, parse it
     * into the argument configBase.</p>
     *
     *
     */

    protected void scanJarsForConfigFiles(ServletContext servletContext, 
					  ConfigParser configParser, 
					  ConfigBase configBase) {
    }
} 
