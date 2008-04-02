/*
 * $Id: LogValueChangedListener.java,v 1.3 2003/10/27 04:15:55 craigmcc Exp $
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

package standard;


import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;


/**
 * <p>Log the occurrence of this event.</p>
 */

public class LogValueChangedListener implements ValueChangeListener {


    // --------------------------------------------- ValueChangeListener Methods


    /**
     * <p>Specify the phase(s) we are interested in being notified for.</p>
     */
    public PhaseId getPhaseId() {

	return (PhaseId.PROCESS_VALIDATIONS);

    }


    /**
     * <p>Log the event.</p>
     *
     * @param event {@link ValueChangeEvent} that is being processed
     */
    public void processValueChange(ValueChangeEvent event) {

	FacesContext context = FacesContext.getCurrentInstance();
	append(context, "ValueChangeEvent(" +
	       event.getComponent().getClientId(context) + "," +
	       event.getOldValue() + "," + event.getNewValue() + ")");

    }


    private void append(FacesContext context, String value) {

	String message = (String)
	    context.getExternalContext().getRequestMap().get("message");
	if (message == null) {
	    message = "";
	}
	message += "<li>" + value + "</li>";
	context.getExternalContext().getRequestMap().put("message", message);

    }


}
