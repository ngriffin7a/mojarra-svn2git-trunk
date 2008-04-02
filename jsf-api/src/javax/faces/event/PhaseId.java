/*
 * $Id: PhaseId.java,v 1.19 2006/08/30 19:14:19 edburns Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.event;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * <p>Typesafe enumeration of the legal values that may be returned by the
 * <code>getPhaseId()</code> method of the {@link FacesEvent} interface.
 */

public class PhaseId implements Comparable {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Private constructor to disable the creation of new instances.</p>
     */
    private PhaseId(String newPhaseName) {
	phaseName = newPhaseName;
    }


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The ordinal value assigned to this instance.</p>
     */
    private final int ordinal = nextOrdinal++;

    /**

    * <p>The (optional) name for this phase.</p>

    */

    private String phaseName = null;


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Compare this {@link PhaseId} instance to the specified one.
     * Returns a negative integer, zero, or a positive integer if this
     * object is less than, equal to, or greater than the specified object.</p>
     *
     * @param other The other object to be compared to
     */
    public int compareTo(Object other) {

        return this.ordinal - ((PhaseId) other).ordinal;

    }


    /**
     * <p>Return the ordinal value of this {@link PhaseId} instance.</p>
     */
    public int getOrdinal() {

        return (this.ordinal);

    }


    /**
     * <p>Return a String representation of this {@link PhaseId} instance.</p>
     */
    public String toString() {
	if (null == phaseName) {
	    return ("" + this.ordinal);
	}
	
	return ("" + this.phaseName + " " + this.ordinal);
    }


    // ------------------------------------------------------- Static Variables


    /**
     * <p>Static counter returning the ordinal value to be assigned to the
     * next instance that is created.</p>
     */
    private static int nextOrdinal = 0;


    // ------------------------------------------------------ Create Instances


    // Any new Phase values must go at the end of the list, or we will break
    // backwards compatibility on serialized instances


    private static final String ANY_PHASE_NAME = "ANY";
    /**
     * <p>Identifier that indicates an interest in events, no matter
     * which request processing phase is being performed.</p>
     */
    public static final PhaseId ANY_PHASE = new PhaseId(ANY_PHASE_NAME);


    private static final String RESTORE_VIEW_NAME = "RESTORE_VIEW";
    /**
     * <p>Identifier that indicates an interest in events queued for
     * the <em>Restore View</em> phase of the request
     * processing lifecycle.</p>
     */
    public static final PhaseId RESTORE_VIEW = new PhaseId(RESTORE_VIEW_NAME);


    private static final String APPLY_REQUEST_VALUES_NAME = "APPLY_REQUEST_VALUES";
    /**
     * <p>Identifier that indicates an interest in events queued for
     * the <em>Apply Request Values</em> phase of the request
     * processing lifecycle.</p>
     */
    public static final PhaseId APPLY_REQUEST_VALUES = new PhaseId(APPLY_REQUEST_VALUES_NAME);


    private static final String PROCESS_VALIDATIONS_NAME = "PROCESS_VALIDATIONS";
    /**
     * <p>Identifier that indicates an interest in events queued for
     * the <em>Process Validations</em> phase of the request
     * processing lifecycle.</p>
     */
    public static final PhaseId PROCESS_VALIDATIONS = new PhaseId(PROCESS_VALIDATIONS_NAME);


    private static final String UPDATE_MODEL_VALUES_NAME = "UPDATE_MODEL_VALUES";
    /**
     * <p>Identifier that indicates an interest in events queued for
     * the <em>Update Model Values</em> phase of the request
     * processing lifecycle.</p>
     */
    public static final PhaseId UPDATE_MODEL_VALUES = new PhaseId(UPDATE_MODEL_VALUES_NAME);


    private static final String INVOKE_APPLICATION_NAME = "INVOKE_APPLICATION";
    /**
     * <p>Identifier that indicates an interest in events queued for
     * the <em>Invoke Application</em> phase of the request
     * processing lifecycle.</p>
     */
    public static final PhaseId INVOKE_APPLICATION = new PhaseId(INVOKE_APPLICATION_NAME);

    private static final String RENDER_RESPONSE_NAME = "RENDER_RESPONSE";
    /**
     * <p>Identifier for the <em>Render Response</em> phase of the
     * request processing lifecycle.</p>
     */
    public static final PhaseId RENDER_RESPONSE = new PhaseId(RENDER_RESPONSE_NAME);



    /**
     * <p>Array of all defined values, ascending order of ordinal value.
     *  Be sure you include any new instances created above, in the
     * same order.</p>
     */
    private static final PhaseId[] values =
    { ANY_PHASE, RESTORE_VIEW, APPLY_REQUEST_VALUES,
      PROCESS_VALIDATIONS, UPDATE_MODEL_VALUES, INVOKE_APPLICATION, RENDER_RESPONSE };


    /**
     * <p>List of valid {@link PhaseId} instances, in ascending order
     * of their ordinal value.</p>
     */
    public static final List<PhaseId> VALUES =
        Collections.unmodifiableList(Arrays.asList(values));


}
