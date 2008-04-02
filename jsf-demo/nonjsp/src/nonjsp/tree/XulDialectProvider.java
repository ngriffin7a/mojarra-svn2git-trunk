/*
 * $Id: XulDialectProvider.java,v 1.1 2003/02/13 23:34:25 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// XulDialectProvider.java

package nonjsp.tree;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import org.apache.commons.digester.RuleSetBase;

/**
 *
 * <B>XulDialectProvider</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: XulDialectProvider.java,v 1.1 2003/02/13 23:34:25 horwat Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class XulDialectProvider extends Object implements XmlDialectProvider
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

public XulDialectProvider()
{
    super();
}

//
// Class methods
//

//
// General Methods
//

//
// Methods from XmlDialectProvider
//

public RuleSetBase getRuleSet()
{
    RuleSetBase result = null;

    result = new XmlXulRuleSet(new BuildComponentFromTagImpl());

    return result;
}

public String getSuffix() { return ".xul"; }

// The testcase for this class is TestXmlTreeFactoryImpl.java 


} // end of class XulDialectProvider
