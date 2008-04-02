/*
 *
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
 * 
 */


package imageMap;

import java.util.*;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.RequestEventHandler;
import javax.faces.event.RequestEvent;
import com.sun.faces.RIConstants;

/**
 * The listener interface for handling the request event generated
 * by the map component and received by the output_text component.
 */
public class ImageMapEventHandler extends RequestEventHandler {

    Hashtable localeTable = new Hashtable();

    public ImageMapEventHandler ( ) {

    	localeTable.put("NAmericas", Locale.ENGLISH);
	localeTable.put("SAmericas", new Locale("es"));
	localeTable.put("Germany", Locale.GERMAN);
	localeTable.put("Finland", new Locale("fi"));
	localeTable.put("France", Locale.FRENCH); 	

    }

// Processes the event queued on the specified component.
    public boolean processEvent(FacesContext context,
				UIComponent component,
				RequestEvent event) {

	UIMap map = (UIMap)event.getSource();
	String value = (String) map.getAttribute("currentArea");
	UIOutput welcome = (UIOutput) component;
	Locale curLocale = (Locale) localeTable.get(value);
	context.setLocale(curLocale);
	context.getHttpSession().setAttribute(RIConstants.REQUEST_LOCALE, curLocale);
	return true;
    }
    
}
