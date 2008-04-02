/*
 * $Id: CarDemoServletContextListener.java,v 1.3 2003/02/05 00:45:20 jenball Exp $
 */
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

package cardemo;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.faces.FactoryFinder;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.convert.ConverterFactory;

import com.sun.faces.context.MessageResourcesImpl;
import javax.faces.context.MessageResourcesFactory;
import javax.faces.context.MessageResources;

import javax.faces.FactoryFinder;
import javax.faces.render.*;

/**
 *
 *  <B>CarDemoServletContextListener</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *

 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class CarDemoServletContextListener implements ServletContextListener
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

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public CarDemoServletContextListener()
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


        RenderKitFactory rkFactory = 
		(RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	RenderKit defaultRenderKit =
		rkFactory.getRenderKit(RenderKitFactory.DEFAULT_RENDER_KIT);
	defaultRenderKit.addRenderer("Area", new AreaRenderer());

        ApplicationHandler handler = new CarDemoApplicationHandler();
        LifecycleFactory factory = (LifecycleFactory)
            FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle =
            factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        lifecycle.setApplicationHandler(handler); 

        // register CreditCardConverter
        ConverterFactory convertFactory =
                (ConverterFactory) FactoryFinder.getFactory(
                FactoryFinder.CONVERTER_FACTORY);
        convertFactory.addConverter("creditcard", new CreditCardConverter());

        // register CarDemo MessageResources.
        MessageResourcesFactory mrFactory =
                (MessageResourcesFactory) FactoryFinder.getFactory(
                FactoryFinder.MESSAGE_RESOURCES_FACTORY);
        MessageResourcesImpl carResource = 
                new MessageResourcesImpl("carResources", 
                "cardemo/CarDemoResources");
        mrFactory.addMessageResources("carResources", carResource);
    }

    public void contextDestroyed(ServletContextEvent e)
    {
    }

} // end of class CarDemoServletContextListener
