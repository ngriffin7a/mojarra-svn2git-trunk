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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.util;

import javax.faces.application.FacesMessage;

import java.text.MessageFormat;

/**
 * <p>This class contains all message constants and utility methods
 * for creating <code>FacesMessage</code> instances or localized 
 * <code>String</code>s for said constants.</p> 
 */
public class MessageUtils {


    // IMPORTANT - ensure that any new message constant is properly
    // tested in test/com/sun/faces/util/TestUtil_messages (see comments
    // in test class for details on the test).    

    public static final String APPLICATION_ASSOCIATE_CTOR_WRONG_CALLSTACK_ID =
          "com.sun.faces.APPLICATION_ASSOCIATE_CTOR_WRONG_CALLSTACK";
    public static final String APPLICATION_ASSOCIATE_EXISTS_ID =
          "com.sun.faces.APPLICATION_ASSOCIATE_EXISTS";
    public static final String APPLICATION_INIT_COMPLETE_ERROR_ID =
          "com.sun.faces.APPLICATION_INIT_COMPLETE_ERROR_ID";
    public static final String ASSERTION_FAILED_ID =
          "com.sun.faces.ASSERTION_FAILED";
    public static final String ATTRIBUTE_NOT_SUPORTED_ERROR_MESSAGE_ID =
          "com.sun.faces.ATTRIBUTE_NOT_SUPORTED";
    public static final String CANT_CLOSE_INPUT_STREAM_ID =
          "com.sun.faces.CANT_CLOSE_INPUT_STREAM";
    public static final String CANT_CONVERT_VALUE_ERROR_MESSAGE_ID =
          "com.sun.faces.CANT_CONVERT_VALUE";
    public static final String CANT_CREATE_CLASS_ERROR_ID =
          "com.sun.faces.CANT_CREATE_CLASS_ERROR";
    public static final String CANT_CREATE_LIFECYCLE_ERROR_MESSAGE_ID =
          "com.sun.faces.CANT_CREATE_LIFECYCLE_ERROR";
    public static final String CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID =
          "com.sun.faces.CANT_INSTANTIATE_CLASS";
    public static final String CANT_INTROSPECT_CLASS_ERROR_MESSAGE_ID =
          "com.sun.faces.CANT_INTROSPECT_CLASS";
    public static final String CANT_LOAD_CLASS_ERROR_MESSAGE_ID =
          "com.sun.faces.CANT_INSTANTIATE_CLASS";
    public static final String CANT_PARSE_FILE_ERROR_MESSAGE_ID =
          "com.sun.faces.CANT_PARSE_FILE";
    public static final String CHILD_NOT_OF_EXPECTED_TYPE_ID =
          "com.sun.faces.CHILD_NOT_OF_EXPECTED_TYPE";
    public static final String COMMAND_LINK_NO_FORM_MESSAGE_ID =
          "com.sun.faces.COMMAND_LINK_NO_FORM_MESSAGE";
    public static final String COMPONENT_NOT_FOUND_ERROR_MESSAGE_ID =
          "com.sun.faces.COMPONENT_NOT_FOUND_ERROR";
    public static final String COMPONENT_NOT_FOUND_IN_VIEW_WARNING_ID =
          "com.sun.faces.COMPONENT_NOT_FOUND_IN_VIEW_WARNING";
    public static final String CONTENT_TYPE_ERROR_MESSAGE_ID =
          "com.sun.faces.CONTENT_TYPE_ERROR";
    public static final String CONVERSION_ERROR_MESSAGE_ID =
          "com.sun.faces.TYPECONVERSION_ERROR";
    public static final String CONVERTER_NOT_FOUND_ERROR_MESSAGE_ID =
          "com.sun.faces.CONVERTER_NOT_FOUND_ERROR";
    public static final String CYCLIC_REFERENCE_ERROR_ID =
          "com.sun.faces.CYCLIC_REFERENCE_ERROR";
    public static final String DUPLICATE_COMPONENT_ID_ERROR_ID =
          "com.sun.faces.DUPLICATE_COMPONENT_ID_ERROR";
    public static final String EL_OUT_OF_BOUNDS_ERROR_ID =
          "com.sun.faces.OUT_OF_BOUNDS_ERROR";
    public static final String EL_PROPERTY_TYPE_ERROR_ID =
          "com.sun.faces.PROPERTY_TYPE_ERROR";
    public static final String EL_SIZE_OUT_OF_BOUNDS_ERROR_ID =
          "com.sun.faces.SIZE_OUT_OF_BOUNDS_ERROR";
    public static final String EMPTY_PARAMETER_ID =
          "com.sun.faces.EMPTY_PARAMETER";
    public static final String ENCODING_ERROR_MESSAGE_ID =
          "com.sun.faces.ENCODING_ERROR";
    public static final String ERROR_GETTING_VALUEREF_VALUE_ERROR_MESSAGE_ID =
          "com.sun.faces.ERROR_GETTING_VALUEREF_VALUE";
    public static final String ERROR_GETTING_VALUE_BINDING_ERROR_MESSAGE_ID =
          "com.sun.faces.ERROR_GETTING_VALUE_BINDING";
    public static final String ERROR_OPENING_FILE_ERROR_MESSAGE_ID =
          "com.sun.faces.ERROR_OPENING_FILE";
    public static final String ERROR_REGISTERING_DTD_ERROR_MESSAGE_ID =
          "com.sun.faces.ERROR_REGISTERING_DTD";
    public static final String ERROR_SETTING_BEAN_PROPERTY_ERROR_MESSAGE_ID =
          "com.sun.faces.ERROR_SETTING_BEAN_PROPERTY";
    public static final String EVAL_ATTR_UNEXPECTED_TYPE =
          "com.sun.faces.EVAL_ATTR_UNEXPECTED_TYPE";
    public static final String FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID =
          "com.sun.faces.FACES_CONTEXT_CONSTRUCTION_ERROR";
    public static final String FACES_SERVLET_MAPPING_CANNOT_BE_DETERMINED_ID =
          "com.sun.faces.FACES_SERVLET_MAPPING_CANNOT_BE_DETERMINED";
    public static final String FILE_NOT_FOUND_ERROR_MESSAGE_ID =
          "com.sun.faces.FILE_NOT_FOUND";
    public static final String ILLEGAL_ATTEMPT_SETTING_STATEMANAGER_ID =
          "com.sun.faces.ILLEGAL_ATTEMPT_SETTING_STATEMANAGER";
    public static final String ILLEGAL_ATTEMPT_SETTING_VIEWHANDLER_ID =
          "com.sun.faces.ILLEGAL_ATTEMPT_SETTING_VIEWHANDLER";
    public static final String ILLEGAL_CHARACTERS_ERROR_MESSAGE_ID =
          "com.sun.faces.ILLEGAL_CHARACTERS_ERROR";
    public static final String ILLEGAL_IDENTIFIER_LVALUE_MODE_ID =
          "com.sun.faces.ILLEGAL_IDENTIFIER_LVALUE_MODE";
    public static final String ILLEGAL_MODEL_REFERENCE_ID =
          "com.sun.faces.ILLEGAL_MODEL_REFERENCE";
    public static final String ILLEGAL_VIEW_ID_ID =
          "com.sun.faces.ILLEGAL_VIEW_ID";
    public static final String INCORRECT_JSP_VERSION_ID =
          "com.sun.faces.INCORRECT_JSP_VERSION";
    public static final String INVALID_EXPRESSION_ID =
          "com.sun.faces.INVALID_EXPRESSION";
    public static final String INVALID_INIT_PARAM_ERROR_MESSAGE_ID =
          "com.sun.faces.INVALID_INIT_PARAM";
    public static final String INVALID_MESSAGE_SEVERITY_IN_CONFIG_ID =
          "com.sun.faces.INVALID_MESSAGE_SEVERITY_IN_CONFIG";
    public static final String INVALID_SCOPE_LIFESPAN_ERROR_MESSAGE_ID =
          "com.sun.faces.INVALID_SCOPE_LIFESPAN";
    public static final String LIFECYCLE_ID_ALREADY_ADDED_ID =
          "com.sun.faces.LIFECYCLE_ID_ALREADY_ADDED";
    public static final String LIFECYCLE_ID_NOT_FOUND_ERROR_MESSAGE_ID =
          "com.sun.faces.LIFECYCLE_ID_NOT_FOUND";
    public static final String MANAGED_BEAN_CANNOT_SET_LIST_ARRAY_PROPERTY_ID =
          "com.sun.faces.MANAGED_BEAN_CANNOT_SET_LIST_ARRAY_PROPERTY";
    public static final String MANAGED_BEAN_CANNOT_SET_MAP_PROPERTY_ID =
          "com.sun.faces.MANAGED_BEAN_CANNOT_SET_MAP_PROPERTY";
    public static final String MANAGED_BEAN_EXISTING_VALUE_NOT_LIST_ID =
          "com.sun.faces.MANAGED_BEAN_EXISTING_VALUE_NOT_LIST";
    public static final String MANAGED_BEAN_TYPE_CONVERSION_ERROR_ID =
          "com.sun.faces.MANAGED_BEAN_TYPE_CONVERSION_ERROR";
    public static final String MAXIMUM_EVENTS_REACHED_ERROR_MESSAGE_ID =
          "com.sun.faces.MAXIMUM_EVENTS_REACHED";
    public static final String MISSING_CLASS_ERROR_MESSAGE_ID =
          "com.sun.faces.MISSING_CLASS_ERROR";
    public static final String MISSING_RESOURCE_ERROR_MESSAGE_ID =
          "com.sun.faces.MISSING_RESOURCE_ERROR";
    public static final String MODEL_UPDATE_ERROR_MESSAGE_ID =
          "com.sun.faces.MODELUPDATE_ERROR";
    public static final String NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID =
          "com.sun.faces.NAMED_OBJECT_NOT_FOUND_ERROR";
    public static final String NOT_NESTED_IN_FACES_TAG_ERROR_MESSAGE_ID =
          "com.sun.faces.NOT_NESTED_IN_FACES_TAG_ERROR";
    public static final String NOT_NESTED_IN_TYPE_TAG_ERROR_MESSAGE_ID =
          "com.sun.faces.NOT_NESTED_IN_TYPE_TAG_ERROR";
    public static final String NO_DTD_FOUND_ERROR_ID =
          "com.sun.faces.NO_DTD_FOUND_ERROR";
    public static final String NULL_BODY_CONTENT_ERROR_MESSAGE_ID =
          "com.sun.faces.NULL_BODY_CONTENT_ERROR";
    public static final String NULL_COMPONENT_ERROR_MESSAGE_ID =
          "com.sun.faces.NULL_COMPONENT_ERROR";
    public static final String NULL_CONFIGURATION_ERROR_MESSAGE_ID =
          "com.sun.faces.NULL_CONFIGURATION";
    public static final String NULL_CONTEXT_ERROR_MESSAGE_ID =
          "com.sun.faces.NULL_CONTEXT_ERROR";
    public static final String NULL_EVENT_ERROR_MESSAGE_ID =
          "com.sun.faces.NULL_EVENT_ERROR";
    public static final String NULL_FORVALUE_ID =
          "com.sun.faces.NULL_FORVALUE";
    public static final String NULL_HANDLER_ERROR_MESSAGE_ID =
          "com.sun.faces.NULL_HANDLER_ERROR";
    public static final String NULL_LOCALE_ERROR_MESSAGE_ID =
          "com.sun.faces.NULL_LOCALE_ERROR";
    public static final String NULL_MESSAGE_ERROR_MESSAGE_ID =
          "com.sun.faces.NULL_MESSAGE_ERROR";
    public static final String NULL_PARAMETERS_ERROR_MESSAGE_ID =
          "com.sun.faces.NULL_PARAMETERS_ERROR";
    public static final String NULL_REQUEST_VIEW_ERROR_MESSAGE_ID =
          "com.sun.faces.NULL_REQUEST_VIEW_ERROR";
    public static final String NULL_RESPONSE_STREAM_ERROR_MESSAGE_ID =
          "com.sun.faces.NULL_RESPONSE_STREAM_ERROR";
    public static final String NULL_RESPONSE_VIEW_ERROR_MESSAGE_ID =
          "com.sun.faces.NULL_RESPONSE_VIEW_ERROR";
    public static final String NULL_RESPONSE_WRITER_ERROR_MESSAGE_ID =
          "com.sun.faces.NULL_RESPONSE_WRITER_ERROR";
    public static final String OBJECT_CREATION_ERROR_ID =
          "com.sun.faces.OBJECT_CREATION_ERROR";
    public static final String OBJECT_IS_READONLY =
          "com.sun.faces.OBJECT_IS_READONLY";
    public static final String PHASE_ID_OUT_OF_BOUNDS_ERROR_MESSAGE_ID =
          "com.sun.faces.PHASE_ID_OUT_OF_BOUNDS";
    public static final String RENDERER_NOT_FOUND_ERROR_MESSAGE_ID =
          "com.sun.faces.RENDERER_NOT_FOUND";
    public static final String REQUEST_VIEW_ALREADY_SET_ERROR_MESSAGE_ID =
          "com.sun.faces.REQUEST_VIEW_ALREADY_SET_ERROR";
    public static final String RESTORE_VIEW_ERROR_MESSAGE_ID =
          "com.sun.faces.RESTORE_VIEW_ERROR";
    public static final String SAVING_STATE_ERROR_MESSAGE_ID =
          "com.sun.faces.SAVING_STATE_ERROR";
    public static final String SUPPORTS_COMPONENT_ERROR_MESSAGE_ID =
          "com.sun.faces.SUPPORTS_COMPONENT_ERROR";
    public static final String VALIDATION_COMMAND_ERROR_ID =
          "com.sun.faces.VALIDATION_COMMAND_ERROR";
    public static final String VALIDATION_EL_ERROR_ID =
          "com.sun.faces.VALIDATION_EL_ERROR";
    public static final String VALIDATION_ID_ERROR_ID =
          "com.sun.faces.VALIDATION_ID_ERROR";
    public static final String VALIDATOR_NOT_FOUND_ERROR_MESSAGE_ID =
          "com.sun.faces.VALIDATOR_NOT_FOUND_ERROR";
    public static final String VALUE_NOT_SELECT_ITEM_ID =
          "com.sun.faces.OPTION_NOT_SELECT_ITEM";


    // ------------------------------------------------------------ Constructors


    private MessageUtils() {}


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Creates a new <code>FacesMessage</code> instance using the
     * specified #messageId.</p>
     * 
     * @param messageId the message ID
     * @return a new <code>FacesMessage</code> based on the provided
     *  <code>messageId</code>
     */
    public static synchronized FacesMessage getExceptionMessage(
          String messageId) {

        return getExceptionMessage(messageId, null);

    }

    /**
     * <p>Creates a new <code>FacesMessage</code> instance using the
     * specified #messageId.</p>
     * 
     * @param messageId the message ID
     * @param params an array of substitution parameters
     * @return a new <code>FacesMessage</code> based on the provided
     *  <code>messageId</code>
     */
    public static synchronized FacesMessage getExceptionMessage(
          String messageId,
          Object[] params) {

        return MessageFactory.getMessage(messageId, params);

    }


    /**
     * <p>Returns the localized message for the specified 
     * #messageId.</p>
     * 
     * @param messageId the message ID
     * @return the localized message for the specified 
     *  <code>messageId</code>
     */
    public static synchronized String getExceptionMessageString(
          String messageId) {

        return getExceptionMessageString(messageId, null);

    }


    /**
     * <p>Returns the localized message for the specified 
     * #messageId.</p>
     * 
     * @param messageId the message ID
     * @param params an array of substitution parameters
     * @return the localized message for the specified 
     *  <code>messageId</code>
     */
    public static synchronized String getExceptionMessageString(
          String messageId,
          Object[] params) {

        String result = null;

        FacesMessage message = MessageFactory.getMessage(messageId, params);
        if (null != message) {
            result = message.getSummary();
        }


        if (null == result) {
            result = "null MessageFactory";
        } else {
            if (params != null) {
                result = MessageFormat.format(result, params);
            }
        }
        return result;

    }

} // END MessageUtils