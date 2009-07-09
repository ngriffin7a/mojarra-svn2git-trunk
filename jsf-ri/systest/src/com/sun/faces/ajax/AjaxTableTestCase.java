/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.ajax;

import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.*;

public class AjaxTableTestCase extends AbstractTestCase {

    public AjaxTableTestCase(String name) {
        super(name);
    }

    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {
        super.setUp();
    }


    /*
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(AjaxTableTestCase.class));
    }


    /*
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    /*
       Test each component to see that it behaves correctly when used with an Ajax tag
     */
    public void testAjaxTable() throws Exception {
        getPage("/faces/ajax/ajaxTable.xhtml");
        System.out.println("Start ajax table test");

        assertTrue(check("table:2:inCity","Boston"));

        // Check on the text field
        HtmlTextInput intext = ((HtmlTextInput)lastpage.getHtmlElementById("table:2:inCity"));
        intext.setValueAttribute("");
        intext.focus();
        intext.type("test");
        intext.blur();

        checkTrue("table:2:inCity","test");
        System.out.println("Text Checked");

        // Check on the checkbox

        checkTrue("table:3:cheesepref","Eww");

        HtmlCheckBoxInput checked = ((HtmlCheckBoxInput)lastpage.getHtmlElementById("table:3:cheesecheck"));
        lastpage = (HtmlPage)checked.setChecked(true);

        checkTrue("table:3:cheesepref","Cheese Please");
        System.out.println("Boolean Checkbox Checked");

        checkTrue("table:4:count", "4");
        HtmlAnchor countlink = (HtmlAnchor) lastpage.getHtmlElementById("table:4:countlink");
        lastpage = countlink.click();

        checkTrue("table:4:count", "5");
        checkTrue("count","1");


        HtmlSubmitInput button = (HtmlSubmitInput)lastpage.getHtmlElementById("submitButton");
        lastpage = button.click();
        checkTrue("table:0:count", "6");
        checkTrue("count","1");
        
    }

}