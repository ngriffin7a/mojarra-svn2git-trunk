/*
 * $Id: TestUtil_messages.java,v 1.18 2003/09/03 18:53:42 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestUtil_messages.java

package com.sun.faces.util;


import com.sun.faces.ServletFacesTestCase;

import java.util.Locale;



/**
 *
 *  <B>TestUtil_messages.java</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestUtil_messages.java,v 1.18 2003/09/03 18:53:42 rlubke Exp $
 */

public class TestUtil_messages extends ServletFacesTestCase {

//
// Protected Constants
//

// Class Variables
//

//
// Instance Variables
//


// README - Add message info to this array as {message key, "number of params"}
// If message has no parameters entry should be {message key, "0"}

    public String[][] messageInfo = {
        {Util.CONVERSION_ERROR_MESSAGE_ID, "2"},
        {Util.MODEL_UPDATE_ERROR_MESSAGE_ID, "2"},
        {Util.FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_COMPONENT_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_REQUEST_VIEW_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_RESPONSE_VIEW_ERROR_MESSAGE_ID, "0"},
        {Util.REQUEST_VIEW_ALREADY_SET_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_MESSAGE_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_PARAMETERS_ERROR_MESSAGE_ID, "0"},
        {Util.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {Util.NULL_RESPONSE_STREAM_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_RESPONSE_WRITER_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_EVENT_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_HANDLER_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_CONTEXT_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_LOCALE_ERROR_MESSAGE_ID, "0"},
        {Util.SUPPORTS_COMPONENT_ERROR_MESSAGE_ID, "1"},
        {Util.MISSING_RESOURCE_ERROR_MESSAGE_ID, "0"},
        {Util.MISSING_CLASS_ERROR_MESSAGE_ID, "1"},
        {Util.COMPONENT_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {Util.LIFECYCLE_ID_ALREADY_ADDED_ID, "1"},
        {Util.LIFECYCLE_ID_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {Util.PHASE_ID_OUT_OF_BOUNDS_ERROR_MESSAGE_ID, "1"},
        {Util.CANT_CREATE_LIFECYCLE_ERROR_MESSAGE_ID, "1"},
        {Util.ILLEGAL_MODEL_REFERENCE_ID, "1"},
        {Util.ATTRIBUTE_NOT_SUPORTED_ERROR_MESSAGE_ID, "2"},
        {Util.FILE_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, "1"},
        {Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, "1"},
        {Util.ILLEGAL_CHARACTERS_ERROR_MESSAGE_ID, "0"},
        {Util.NOT_NESTED_IN_FACES_TAG_ERROR_MESSAGE_ID, "1"},
        {Util.NULL_BODY_CONTENT_ERROR_MESSAGE_ID, "1"},
        {Util.SAVING_STATE_ERROR_MESSAGE_ID, "2"},
        {Util.RENDERER_ALREADY_EXISTS_ERROR_MESSAGE_ID, "1"},
        {Util.RENDERER_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {Util.MAXIMUM_EVENTS_REACHED_ERROR_MESSAGE_ID, "1"},
        {Util.NO_ACTION_FROM_ACTIONREF_ERROR_MESSAGE_ID, "1"},
        {Util.NULL_CONFIGURATION_ERROR_MESSAGE_ID, "0"},
        {Util.ERROR_OPENING_FILE_ERROR_MESSAGE_ID, "1"},
        {Util.ERROR_REGISTERING_DTD_ERROR_MESSAGE_ID, "1"},
        {Util.INVALID_INIT_PARAM_ERROR_MESSAGE_ID, "0"},
        {Util.ERROR_SETTING_BEAN_PROPERTY_ERROR_MESSAGE_ID, "1"},
        {Util.ERROR_GETTING_VALUE_BINDING_ERROR_MESSAGE_ID, "1"},
        {Util.ERROR_GETTING_VALUEREF_VALUE_ERROR_MESSAGE_ID, "1"},
        {Util.CANT_INTROSPECT_CLASS_ERROR_MESSAGE_ID, "1"},
        {Util.CANT_CONVERT_VALUE_ERROR_MESSAGE_ID, "2"},
        {Util.INVALID_SCOPE_LIFESPAN_ERROR_MESSAGE_ID, "1"},
        {Util.CONVERTER_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {Util.VALIDATOR_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {Util.ENCODING_ERROR_MESSAGE_ID, "0"},
        {Util.ILLEGAL_IDENTIFIER_LVALUE_MODE_ID, "1"},
        {Util.VALIDATION_ID_ERROR_ID, "1"},
        {Util.VALIDATION_EL_ERROR_ID, "1"},
        {Util.VALIDATION_COMMAND_ERROR_ID, "1"},
        {Util.CONTENT_TYPE_ERROR_MESSAGE_ID, "0"},
        {Util.COMPONENT_NOT_FOUND_IN_VIEW_WARNING_ID, "1"},
        {Util.ILLEGAL_ATTEMPT_SETTING_VIEWHANDLER_ID, "0"},
        {Util.INVALID_MESSAGE_SEVERITY_IN_CONFIG_ID, "1"}
        
    };

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestUtil_messages() {super("TestUtil_messages.java");}
    public TestUtil_messages(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//


    public void testVerifyMessages() {

        // English Language
        System.out.println("Verifying English Messages...");
        Locale locale = new Locale("en", "US");
        getFacesContext().setLocale(locale);
        verifyParamsInMessages(messageInfo);

        // French Language
        System.out.println("Verifying French Messages...");
        locale = new Locale("fr", "");
        getFacesContext().setLocale(locale);
        verifyParamsInMessages(messageInfo);

        // German Language
        System.out.println("Verifying German Messages...");
        locale = new Locale("de", "");
        getFacesContext().setLocale(locale);
        verifyParamsInMessages(messageInfo);

        // Spanish Language
        System.out.println("Verifying Spanish Messages...");
        locale = new Locale("es", "");
        getFacesContext().setLocale(locale);
        verifyParamsInMessages(messageInfo);
    }

    private void verifyParamsInMessages(String[][] messageInfo) {
        int numParams = 0;

        for (int i=0; i<messageInfo.length; i++) {
            try {
                numParams = Integer.parseInt(messageInfo[i][1]);
            } catch (NumberFormatException e) {
                 System.out.println("Invalid param number specifier!");
                 assertTrue(false);
            }
            if (numParams == 0) {
                String message = Util.getExceptionMessage(messageInfo[i][0]);
                assertTrue(message != null);
            } else if (numParams > 0) {
                Object[] params = generateParams(numParams);
                String message = Util.getExceptionMessage(messageInfo[i][0], params);
                assertTrue(message != null);
                for (int j=0; j<params.length; j++) {
                    assertTrue(message.indexOf((String)params[j])!=-1);
                }
            }
        }
    }

    private Object[] generateParams(int numParams) {
        Object[] params = new String[numParams];
        for (int i=0; i<numParams; i++) {
            params[i] = "param_"+i;
        }
        return params;
    }
        
} // end of class TestUtil_messages
