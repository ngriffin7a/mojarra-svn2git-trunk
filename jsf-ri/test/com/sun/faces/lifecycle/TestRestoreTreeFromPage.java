/*
 * $Id: TestRestoreTreeFromPage.java,v 1.6 2003/01/21 23:23:25 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRestoreTreeFromPage.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIComponent;

import javax.faces.tree.TreeFactory;
import javax.faces.tree.Tree;
import com.sun.faces.RIConstants;

import com.sun.faces.ServletFacesTestCase;
import javax.servlet.http.HttpSession;
import javax.faces.render.RenderKitFactory;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import com.sun.faces.CompareFiles;
import com.sun.faces.FileOutputResponseWriter;

/**
 *
 *  <B>TestReconstituteRequestTreePhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRestoreTreeFromPage.java,v 1.6 2003/01/21 23:23:25 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRestoreTreeFromPage extends ServletFacesTestCase
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
public static final String TEST_URI = "/TestSaveState.jsp";
public static final String  RESTORE_TREE_OUTPUT_FILE = FileOutputResponseWriter.FACES_RESPONSE_ROOT + 
    "RestoreTree_output";
public static final String  RESTORE_TREE_CORRECT_FILE = FileOutputResponseWriter.FACES_RESPONSE_ROOT + 
    "RestoreTree_correct";

public static final String ignore[] = {
  "value=[Ljava.lang.Object;@14a18d"
};
// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestRestoreTreeFromPage() {
	super("TestRestoreTreeFromPage");
    }

    public TestRestoreTreeFromPage(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//

public void beginRestoreTreeFromPage(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
    theRequest.addParameter("com.sun.faces.TREE", 
			    "rO0ABXNyACFjb20uc3VuLmZhY2VzLnRyZWUuU2ltcGxlVHJlZUltcGx8SgHTtTLr8AIAA0wAC3JlbmRlcktpdElkdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEcm9vdHQAI0xqYXZheC9mYWNlcy9jb21wb25lbnQvVUlDb21wb25lbnQ7TAAGdHJlZUlkcQB+AAF4cgAVamF2YXguZmFjZXMudHJlZS5UcmVlPrWmwgkoaEwCAAB4cHQAB0RFRkFVTFRzcgAjY29tLnN1bi5mYWNlcy50cmVlLlNpbXBsZVRyZWVJbXBsJDGZzr1VeEns/QIAAUwABnRoaXMkMHQAI0xjb20vc3VuL2ZhY2VzL3RyZWUvU2ltcGxlVHJlZUltcGw7eHIAJWphdmF4LmZhY2VzLmNvbXBvbmVudC5VSUNvbXBvbmVudEJhc2XC5mq6oKj1YgIABEwACmF0dHJpYnV0ZXN0ABNMamF2YS91dGlsL0hhc2hNYXA7TAAIY2hpbGRyZW50ABVMamF2YS91dGlsL0FycmF5TGlzdDtMAAhoYW5kbGVyc3EAfgAKTAAKdmFsaWRhdG9yc3EAfgAKeHBwc3IAE2phdmEudXRpbC5BcnJheUxpc3R4gdIdmcdhnQMAAUkABHNpemV4cAAAAAF3BAAAAApzcgAcamF2YXguZmFjZXMuY29tcG9uZW50LlVJRm9ybd/vQSa1Eq3gAgAAeHEAfgAIc3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAACHcIAAAACwAAAAR0AAtjb21wb25lbnRJZHQACWJhc2ljRm9ybXQADHJlbmRlcmVyVHlwZXQABEZvcm10AAZwYXJlbnRxAH4AC3QABXZhbHVlcQB+ABN4c3EAfgAMAAAABXcEAAAACnNyAB1qYXZheC5mYWNlcy5jb21wb25lbnQuVUlJbnB1dKvJFthZlLUYAgAAeHEAfgAIc3EAfgAQP0AAAAAAAAh3CAAAAAsAAAAEcQB+ABJ0AAh1c2VyTmFtZXEAfgAUdAAEVGV4dHEAfgAWcQB+AA9xAH4AF3QAD0phdmFTZXJ2ZXJGYWNlc3hwcHNxAH4ADAAAAAJ3BAAAAApzcgAlamF2YXguZmFjZXMudmFsaWRhdG9yLkxlbmd0aFZhbGlkYXRvchOY3QOS02mPAgAESQAHbWF4aW11bVoACm1heGltdW1TZXRJAAdtaW5pbXVtWgAKbWluaW11bVNldHhyACNqYXZheC5mYWNlcy52YWxpZGF0b3IuVmFsaWRhdG9yQmFzZYngGzccBXsFAgAAeHAAAAAKAQAAAAYBc3IAJ2phdmF4LmZhY2VzLnZhbGlkYXRvci5SZXF1aXJlZFZhbGlkYXRvcjfoy9Dkh/T+AgAAeHEAfgAheHNyAB9qYXZheC5mYWNlcy5jb21wb25lbnQuVUlDb21tYW5kyDZjy8ASvIsCAAB4cQB+AAhzcQB+ABA/QAAAAAAACHcIAAAACwAAAAZ0AAxjb21tYW5kQ2xhc3N0AA5oeXBlcmxpbmtDbGFzc3QABWxhYmVsdAAJbGluayB0ZXh0cQB+ABJ0AARsaW5rcQB+ABR0AAlIeXBlcmxpbmtxAH4AFnEAfgAPdAAGdGFyZ2V0dAAKaGVsbG8uaHRtbHhwcHBzcgAlamF2YXguZmFjZXMuY29tcG9uZW50LlVJU2VsZWN0Qm9vbGVhbiiDmwtDyLEhAgAAeHEAfgAZc3EAfgAQP0AAAAAAAAh3CAAAAAsAAAAEdAASc2VsZWN0Ym9vbGVhbkNsYXNzcQB+ADNxAH4AEnQACXZhbGlkVXNlcnEAfgAUdAAIQ2hlY2tib3hxAH4AFnEAfgAPeHBwcHNyACFqYXZheC5mYWNlcy5jb21wb25lbnQuVUlTZWxlY3RPbmWK9BXfbmCzEgIAAHhyACJqYXZheC5mYWNlcy5jb21wb25lbnQuVUlTZWxlY3RCYXNllB");
}


public void testRestoreTreeFromPage()
{
    Phase reconstituteTree = new ReconstituteRequestTreePhase(null, 
			RIConstants.RECONSTITUTE_REQUEST_TREE_PHASE);
    int result = -1;

    try {
	result = reconstituteTree.execute(getFacesContext());
    }
    catch (Throwable e) {
        e.printStackTrace();
	assertTrue(false);
    }
    assertTrue(Phase.GOTO_NEXT == result);

    assertTrue(null != getFacesContext().getTree());
    assertTrue(null != getFacesContext().getTree().getRoot());
    assertTrue(RenderKitFactory.DEFAULT_RENDER_KIT.equals(
           getFacesContext().getTree().getRenderKitId()));
      
    assertTrue(getFacesContext().getLocale().equals(Locale.ENGLISH));
    CompareFiles cf = new CompareFiles();
    try {
        FileOutputStream os = new FileOutputStream(RESTORE_TREE_OUTPUT_FILE);
        PrintStream ps = new PrintStream(os);
        com.sun.faces.util.DebugUtil.printTree((getFacesContext().getTree()).getRoot(), ps );
        
        List ignoreList = new ArrayList();
	for (int i = 0; i < ignore.length; i++) {
	    ignoreList.add(ignore[i]);
	}
	boolean status = cf.filesIdentical(RESTORE_TREE_OUTPUT_FILE, 
                RESTORE_TREE_CORRECT_FILE, ignoreList);
        assertTrue(status);
    }
    catch (IOException e) {
	System.out.println(e.getMessage());
	e.printStackTrace();
    }

}

} // end of class TestReconstituteRequestTreePhase
