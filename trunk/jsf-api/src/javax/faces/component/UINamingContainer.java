/*
 * $Id: UINamingContainer.java,v 1.9 2003/09/30 17:05:00 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UINamingContainer</strong> is a convenience base class for
 * components that wish to implement {@link NamingContainer} functionality.</p>
 */

public class UINamingContainer extends UIComponentBase
    implements NamingContainer {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UINamingContainer} instance with default property
     * values.</p>
     */
    public UINamingContainer() {

        super();
        setRendererType(null);

    }

 
    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The {@link NamingContainer} implementation that we delegate to
     */
    private NamingContainerSupport namespace = new NamingContainerSupport();


    // ------------------------------------------------- NamingContainer Methods

    /**
     * @exception IllegalArgumentException {@inheritDoc}
     * @exception IllegalStateException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}     
     */ 
    public void addComponentToNamespace(UIComponent namedComponent) {

	namespace.addComponentToNamespace(namedComponent);

    }

    /**
     * @exception IllegalArgumentException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}
     */ 
    public UIComponent findComponentInNamespace(String name) {

	return namespace.findComponentInNamespace(name);

    }


    public synchronized String generateClientId() {

	return namespace.generateClientId();

    }

    /**
     * @exception IllegalArgumentException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}     
     */ 
    public void removeComponentFromNamespace(UIComponent namedComponent) {

	namespace.removeComponentFromNamespace(namedComponent);

    }


}
