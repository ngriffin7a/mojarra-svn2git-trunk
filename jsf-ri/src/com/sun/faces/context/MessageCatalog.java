/*
 * $Id: MessageCatalog.java,v 1.4 2003/04/29 20:51:41 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.context;

import java.util.HashMap;
import java.util.Locale;

import javax.faces.application.Message;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>MessageCatalog</B> maintains a list of messages for a particular locale.
 *
 * @version $Id: MessageCatalog.java,v 1.4 2003/04/29 20:51:41 eburns Exp $
 * 
 * @see com.sun.faces.context.MessageTemplate
 *
 */

public class MessageCatalog extends Object
{
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private     Locale  locale;
    private     HashMap messages;
    
    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public MessageCatalog(Locale locale) {
        super();
        ParameterCheck.nonNull(locale);
        this.locale = locale;
        messages = new HashMap();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    public void addMessage(MessageTemplate msg) {
        ParameterCheck.nonNull(msg);
        String msgId = msg.getMessageId();
        ParameterCheck.nonNull(msgId);
        msg.setLocale(locale);
        synchronized( messages) {
            messages.put(msgId, msg);
        }    
    }

    public MessageTemplate get(Object msgId) {
        ParameterCheck.nonNull(msgId);
        synchronized(messages) {
            return (MessageTemplate)messages.get(msgId);
        }    
    }
    
    // The testcase for this class is TestMessageListImpl.java 


} // end of class MessageCatalog
