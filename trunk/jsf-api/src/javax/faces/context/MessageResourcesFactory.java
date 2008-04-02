/*
 * $Id: MessageResourcesFactory.java,v 1.4 2003/02/20 22:46:21 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;


import java.util.Iterator;
import javax.faces.FacesException;


/**
 * <p><strong>MessageResourcesFactory</strong> is a Factory object that creates
 * (if needed) and returns {@link MessageResources} instances.  Implementations
 * of JavaServer Faces must provide {@link MessageResources} instances for the
 * following standard message resources identifiers:</p>
 * <ul>
 * <li><strong>MessageResourcesFactory.FACES_API_MESSAGES</strong> - Messages
 *     whose message identifiers are defined in the JavaServer Faces
 *     specification, for messages generated by <code>javax.faces.*</code>
 *     concrete classes.</li>
 * <li><strong>MessageResourcesFactory.FACES_IMPL_MESSAGES</strong> - Messages
 *     whose message identifiers are defined by the JavaServer Faces
 *     implementation being utilized.</li>
 * </ul>
 *
 * <p>There must be one {@link MessageResourcesFactory} instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 * <pre>
 *   MessageResourcesFactory factory = (MessageResourcesFactory)
 *    FactoryFinder.getFactory(FactoryFinder.MESSAGE_RESOURCES_FACTORY);
 * </pre>
 */

public abstract class MessageResourcesFactory {


    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>Message resources identifier for a {@link MessageResources} instance
     * containing messages whose message identifiers are defined in the
     * JavaServer Faces specification.</p>
     */
    public static final String FACES_API_MESSAGES =
        "javax.faces.context.FACES_API_MESSAGES";


    /**
     * <p>Message resources identifier for a {@link MessageResources} instance
     * containing messages whose message identifiers are defined by the
     * JavaServer Faces implementation being used.</p>
     */
    public static final String FACES_IMPL_MESSAGES =
        "javax.faces.context.FACES_IMPL_MESSAGES";


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Register a new {@link MessageResources} instance, associated with
     * the specified <code>messageResourcesId</code>, to be supported by this
     * {@link MessageResourcesFactory}.  This method may be called at
     * any time, and makes the corresponding {@link MessageResources} instance
     * available throughout the remaining lifetime of this web application.
     * </p>
     *
     * @param messageResourcesId Identifier of the new {@link MessageResources}
     * @param messageResources {@link MessageResources} instance that
     *  we are registering
     *
     * @exception IllegalArgumentException if <code>messageResourcesId</code>
     *  is already registered in this <code>MessageResourcesFactory</code>
     * @exception NullPointerException if <code>messageResourcesId</code>
     *  or <code>messageResources</code> is <code>null</code>
     */
    public abstract void addMessageResources
        (String messageResourcesId, MessageResources messageResources);


    /**
     * <p>Create (if needed) and return a {@link MessageResources} instance
     * for the specified message resources identifier.  The set of available
     * message resources identifiers is available via the
     * <code>getMessageResourcesIds()</code> method.</p>
     *
     * <p>Each call to <code>getMessageResources()</code> for the same
     * <code>messageResourcesId</code>, from within the same web application,
     * must return the same <code>MessageResources</code> instance.</p>
     *
     * @param messageResourcesId Identifier of the requested
     *  {@link MessageResources} instance
     *
     * @exception IllegalArgumentException if no {@link MessageResources}
     *  instance can be returned for the specified identifier
     * @exception NullPointerException if <code>messageResourcesId</code>
     *  is <code>null</code>
     */
    public abstract MessageResources getMessageResources
        (String messageResourcesId);


    /**
     * <p>Return an <code>Iterator</code> over the set of message resource
     * identifiers supported by this factory.  This <code>Iterator</code>
     * must include the standard identifiers described above.</p>
     */
    public abstract Iterator getMessageResourcesIds();


}
