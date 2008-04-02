/*
 * $Id: TestRenderers_2.java,v 1.72 2003/11/11 06:45:02 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderers_2.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UISelectItems;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIMessage;
import javax.faces.component.UIMessages;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.apache.cactus.WebRequest;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.TestBean;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_2.java,v 1.72 2003/11/11 06:45:02 horwat Exp $
 * 
 *
 */

public class TestRenderers_2 extends JspFacesTestCase
{
    //
    // Instance Variables
    //
    private Application application;

    //
    // Protected Constants
    //

    public static String DATE_STR = "Jan 12, 1952";
    public static String DATE_STR_LONG = "Sat, Jan 12, 1952 AD at 12:31:31 PM";
    
    public static String TIME_STR = "12:31:31 PM";
    public static String NUMBER_STR = "47%";
    public static String NUMBER_STR_PATTERN = "1999.8765432";
        
    public boolean sendWriterToFile() {
        return true;
    }    

    public String getExpectedOutputFilename() {
        return "CorrectRenderersResponse_2";
    }

    public String [] getLinesToIgnore() {
        String[] lines =  {
	    "<img id=\"myGraphicImage\" src=\"/test/nonModelReferenceImage.gif;jsessionid=CE3C052680005E352476DAD1A410AAC9\" /><img id=\"id0\" src=\"/test/foo/modelReferenceImage.gif;jsessionid=CE3C052680005E352476DAD1A410AAC9\" />My name is Bobby Orr"
};
        return lines;
    }   
 
    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private FacesContextFactory  facesContextFactory = null;

    // Attribute Instance Variables
    // Relationship Instance Variables
    //
    // Constructors and Initializers    
    //

    public TestRenderers_2() {super("TestRenderers_2");}
    public TestRenderers_2(String name) {super(name);}

    //
    // Class methods
    //

    //
    // Methods from TestCase
    //
    public void setUp() {
	super.setUp();
        ApplicationFactory aFactory = 
	    (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = aFactory.getApplication();
        UIViewRoot page = new UIViewRoot();
        page.setViewId("viewId");       
        getFacesContext().setViewRoot(page);
	assertTrue(null != getFacesContext().getResponseWriter());
    }     

    public void beginRenderers(WebRequest theRequest) {
        // for CheckboxRenderer
        theRequest.addParameter("myCheckboxOn", "on");
        theRequest.addParameter("myCheckboxYes", "yes");
        theRequest.addParameter("myCheckboxTrue", "true");
  
        // for LinkRenderer
        theRequest.addParameter("action", "command");
        theRequest.addParameter("myCommand", "LinkRenderer");
        // for Listbox
        theRequest.addParameter("myListbox", "100");
        // for TextEntry_Secret
        theRequest.addParameter("mySecret", "secret");
        // for Text
        theRequest.addParameter("myInputText", "text");

        theRequest.addParameter("myOutputText", "text");

        theRequest.addParameter("myTextarea", "TextareaRenderer");

        theRequest.addParameter("myGraphicImage", "graphicimage");

        theRequest.addParameter("myOutputMessage", "outputmessage");
    } 

    public void testRenderers() throws Exception {

        // create a dummy root for the tree.
        UIViewRoot root = new UIViewRoot();
        root.setId("root");

        testCheckboxRenderer(root);
        // PENDING (visvan) revisit this test case once HyperLinkRenderer
        // is fixed.
        // testLinkRenderer(root);
        getFacesContext().getResponseWriter().startDocument();
        testListboxRenderer(root);
        testSecretRenderer(root);
        testInputTextRenderer(root);
        testOutputTextRenderer(root);
        testTextareaRenderer(root);
        testGraphicImageRenderer(root);            
        testOutputMessageRenderer(root);
        testMessageRenderer(root);
        testMessagesRenderer(root);
        getFacesContext().getResponseWriter().endDocument();
        assertTrue(verifyExpectedOutput());

    }
    
    //
    // General Methods
    //
    public void testCheckboxRenderer(UIComponent root) throws IOException {
        System.out.println("Testing CheckboxRenderer");
        UISelectBoolean selectBoolean = new UISelectBoolean();
        selectBoolean.setValue(null);
        selectBoolean.setId("myCheckbox");
        root.getChildren().add(selectBoolean);
             
        CheckboxRenderer checkboxRenderer = new CheckboxRenderer();

        // First test no parameter coming in - (the checkbox
        // is not checked)
         
        // test decode method

        System.out.println("    Testing decode method - no parameter");
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        Boolean val = (Boolean)selectBoolean.getValue();
        assertTrue(!val.booleanValue());

        // Test parameter coming in - (the checkbox has been checked)

        // test decode method

        System.out.println("    Testing decode method - parameter (on)");
	selectBoolean = new UISelectBoolean();
        selectBoolean.setId("myCheckboxOn");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(getFacesContext(), selectBoolean); 
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
        

        // test decode method

        System.out.println("    Testing decode method - parameter (yes)");
	selectBoolean = new UISelectBoolean();
        selectBoolean.setId("myCheckboxYes");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
       
        // test decode method

        System.out.println("    Testing decode method - parameter (true)");
	selectBoolean = new UISelectBoolean();
        selectBoolean.setId("myCheckboxTrue");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
        
        // test decode method
        
        System.out.println("    Testing decode method - parameter (true)");
	selectBoolean = new UISelectBoolean();
        selectBoolean.setId("myCheckboxTrue");
        selectBoolean.setValue(null);
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        assertTrue(val.booleanValue());
        
         
        // test decode method with checkbox disabled.
        System.out.println("    Testing decode method - parameter (yes)");
	selectBoolean = new UISelectBoolean();
        selectBoolean.setId("mycheckboxDisabled");
        selectBoolean.getAttributes().put("disabled", "true");
        selectBoolean.setValue(Boolean.TRUE);
        checkboxRenderer.decode(getFacesContext(), selectBoolean);
        val = (Boolean)selectBoolean.getValue();
        // make sure the value wasn't set to false. Bug id  4883159
        assertTrue(val.booleanValue());
        selectBoolean.getAttributes().remove("disabled");
        
        // test encode method
        System.out.println("    Testing encode method - rendering checked");
	selectBoolean = new UISelectBoolean();
        selectBoolean.setId("myCheckbox");
        selectBoolean.setSelected(true);
        checkboxRenderer.encodeBegin(getFacesContext(), selectBoolean);
        checkboxRenderer.encodeEnd(getFacesContext(), selectBoolean);
        getFacesContext().getResponseWriter().writeText("\n", null);

        System.out.println("    Testing encode method - rendering unchecked");
        selectBoolean.setSelected(false);
        checkboxRenderer.encodeBegin(getFacesContext(), selectBoolean);
        checkboxRenderer.encodeEnd(getFacesContext(), selectBoolean);
        getFacesContext().getResponseWriter().writeText("\n", null);

        System.out.println("    Testing encode method - rendering unchecked with label");
        checkboxRenderer.encodeBegin(getFacesContext(), selectBoolean);
        checkboxRenderer.encodeEnd(getFacesContext(), selectBoolean);
        getFacesContext().getResponseWriter().writeText("\n", null);
    }

    public void testLinkRenderer(UIComponent root) throws IOException {
        System.out.println("Testing LinkRenderer");
        UICommand command = new UICommand();
        command.setId("myCommand");
        command.setRendererType("Link");
        root.getChildren().add(command);

        LinkRenderer hyperlinkRenderer = new LinkRenderer();

        System.out.println("    Testing decode method...");
        hyperlinkRenderer.decode(getFacesContext(), command);

        // Verify command event was set for the application..
        System.out.println("    Testing added application event (commandEvent)..");
        // PENDING FIX
      /*  Iterator iter = getFacesContext().getFacesEvents();
        assertTrue(iter.hasNext()); */

        // Test encode method

        System.out.println("    Testing encode method...");
        hyperlinkRenderer.encodeBegin(getFacesContext(), command);
        hyperlinkRenderer.encodeEnd(getFacesContext(), command);
        getFacesContext().getResponseWriter().writeText("\n", null);
    }

    public void testListboxRenderer(UIComponent root) throws IOException {
        System.out.println("Testing ListboxRenderer");
        UISelectOne selectOne = new UISelectOne();
	UISelectItems uiSelectItems = new UISelectItems();
        selectOne.setValue(null);
        selectOne.setId("myListbox");
        SelectItem item1 = new SelectItem(new Long(100), "Long1", null);
        SelectItem item2 = new SelectItem(new Long(101), "Long2", null);
        SelectItem item3 = new SelectItem(new Long(102), "Long3", null);
        SelectItem item4 = new SelectItem(new Long(103), "Long4", null);
        SelectItem[] selectItems = {item1, item2, item3, item4};
        uiSelectItems.setValue(selectItems);
	uiSelectItems.setId("items");
	Converter converter = application.createConverter("Number");
	selectOne.setConverter(converter);
        selectOne.getChildren().add(uiSelectItems);
        root.getChildren().add(selectOne);

        ListboxRenderer listboxRenderer = new ListboxRenderer();

        // test decode method
        System.out.println("    Testing decode method... ");
        listboxRenderer.decode(getFacesContext(), selectOne);
        Object value = selectOne.getValue();
        assertTrue(value.equals(new Long(100)));

        // test encode method
        System.out.println("    Testing encode method... ");
        //selectOne.setId("myListbox");
        listboxRenderer.encodeBegin(getFacesContext(), selectOne);
        listboxRenderer.encodeEnd(getFacesContext(), selectOne);
        getFacesContext().getResponseWriter().writeText("\n", null);
    }

    public void testSecretRenderer(UIComponent root) throws IOException {
        System.out.println("Testing SecretRenderer");
        UIInput textEntry = new UIInput();
        textEntry.setValue(null);
        textEntry.setId("mySecret");
        root.getChildren().add(textEntry);

        SecretRenderer secretRenderer = new SecretRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        secretRenderer.decode(getFacesContext(), textEntry);
        assertTrue(((String)textEntry.getValue()).equals("secret"));

        // test encode method

        System.out.println("    Testing encode method...");
        secretRenderer.encodeBegin(getFacesContext(), textEntry);
        secretRenderer.encodeEnd(getFacesContext(), textEntry);
        getFacesContext().getResponseWriter().writeText("\n", null);
    }

    public void testInputTextRenderer(UIComponent root) throws IOException {
        System.out.println("Testing InputTextRenderer");
        UIInput text = new UIInput();
        text.setValue(null);
        text.setId("myInputText");
        root.getChildren().add(text);

        TextRenderer textRenderer = new TextRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        textRenderer.decode(getFacesContext(), text);
        assertTrue(((String)text.getValue()).equals("text"));

        // test encode method

        System.out.println("    Testing encode method...");
        textRenderer.encodeBegin(getFacesContext(), text);
        textRenderer.encodeEnd(getFacesContext(), text);
    }

    public void testOutputTextRenderer(UIComponent root) throws IOException {
        System.out.println("Testing OutputTextRenderer");
        UIOutput text = new UIOutput();
        text.setValue(null);
        text.setId("myOutputText");
        root.getChildren().add(text);

        TextRenderer textRenderer = new TextRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        textRenderer.decode(getFacesContext(), text);

        // test encode method

        System.out.println("    Testing encode method...");
        textRenderer.encodeBegin(getFacesContext(), text);
        textRenderer.encodeEnd(getFacesContext(), text);
    }

    public void testGraphicImageRenderer(UIComponent root) throws IOException {
        System.out.println("Testing GraphicImageRenderer");
        UIGraphic img = new UIGraphic();
        img.setURL("/nonModelReferenceImage.gif");
        img.setId("myGraphicImage");
        img.getAttributes().put("ismap", new Boolean(true));
        img.getAttributes().put("usemap", "usemap");
        root.getChildren().add(img);

        ImageRenderer imageRenderer = new ImageRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        imageRenderer.decode(getFacesContext(), img);

        // test encode method

        System.out.println("    Testing encode method...");
        imageRenderer.encodeBegin(getFacesContext(), img);
        imageRenderer.encodeEnd(getFacesContext(), img);

        System.out.println("    Testing graphic support of modelReference...");
	root.getChildren().remove(img);
	img = new UIGraphic();
        img.getAttributes().put("ismap", new Boolean(true));
        img.getAttributes().put("usemap", "usemap");
	root.getChildren().add(img);
	TestBean testBean = (TestBean) 
	    (Util.getValueBinding("TestBean")).getValue(getFacesContext());
	assertTrue(null != testBean); // set in FacesTestCaseService
	testBean.setImagePath("/foo/modelReferenceImage.gif");
	img.setValueBinding("value", Util.getValueBinding("#{TestBean.imagePath}"));

        imageRenderer.encodeBegin(getFacesContext(), img);
        imageRenderer.encodeEnd(getFacesContext(), img);
    }

    public void testOutputMessageRenderer(UIComponent root) throws IOException {	System.out.println("Testing OutputMessageRenderer");
	UIOutput output = new UIOutput();
	output.setId("myOutputMessage");
	output.setValue("My name is {0} {1}");
	UIParameter param1, param2 = null;
	param1 = new UIParameter();
	param1.setId("p1");
	param2 = new UIParameter();
	param2.setId("p2");
		param1.setValue("Bobby");
	param2.setValue("Orr");
	output.getChildren().add(param1);
	output.getChildren().add(param2);
	root.getChildren().add(output);

	OutputMessageRenderer outputMessageRenderer = new OutputMessageRenderer();
	// test encode method

	System.out.println("	Testing encode method...");

	outputMessageRenderer.encodeBegin(getFacesContext(), output);
	outputMessageRenderer.encodeEnd(getFacesContext(), output);
    }


    public void testMessageRenderer(UIComponent root) throws IOException {
        System.out.println("Testing MessageRenderer");
        UIMessage message = new UIMessage();
        message.setId("myMessage_0");
        message.setFor("myMessage_0");
        root.getChildren().add(message);

        ResponseWriter originalWriter = getFacesContext().getResponseWriter();
        UIViewRoot originalRoot = getFacesContext().getViewRoot();

        getFacesContext().setViewRoot((UIViewRoot) root);

        // setup a new HtmlResponseWriter using a StringWriter.
        // This allows us to capture the output and check for
        // correctness without using a goldenfile.
        StringWriter writer = new StringWriter();
        HtmlResponseWriter htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);

        MessageRenderer messageRenderer = new MessageRenderer();

        // populate facescontext with some errors
        getFacesContext().addMessage(message.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_INFO,
            "global message summary_0", "global message detail_0"));

        // test encode method

        messageRenderer.encodeBegin(getFacesContext(), message);
        messageRenderer.encodeEnd(getFacesContext(), message);

        String result = writer.toString();

        //no span should be rendered since none of the criteria was met
        assertTrue(result.indexOf("span") == -1);

        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }

        root.getChildren().remove(message);
        message = new UIMessage();
        message.setId("myMessage_1");
        message.setFor("myMessage_1");
        root.getChildren().add(message);

        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);

        messageRenderer = new MessageRenderer();

        //add a styleClass so span is rendered
	message.getAttributes().put("styleClass", "styleClass");

        // populate facescontext with some errors
        getFacesContext().addMessage(message.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_WARN,
            "global message summary_1", "global message detail_1"));

        // test encode method

        messageRenderer.encodeBegin(getFacesContext(), message);
        messageRenderer.encodeEnd(getFacesContext(), message);

        result = writer.toString();

        //Span should have class attribute for styleClass
        //Summary and detail should be in body of span separated by space
        assertTrue(result.indexOf("<span class=\"styleClass\">	global message summary_1 global message detail_1</span>") != -1);

        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }

        root.getChildren().remove(message);
        message = new UIMessage();
        message.setId("myMessage_2");
        message.setFor("myMessage_2");
        root.getChildren().add(message);

        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);

        messageRenderer = new MessageRenderer();

        //add a styleClass so span is rendered
	message.getAttributes().put("style", "style");

        // populate facescontext with some errors
        getFacesContext().addMessage(message.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "global message summary_2", "global message detail_2"));

        // test encode method

        messageRenderer.encodeBegin(getFacesContext(), message);
        messageRenderer.encodeEnd(getFacesContext(), message);

        result = writer.toString();

        //Span should have style attribute
        //Summary and detail should be in body of span separated by space
        assertTrue(result.indexOf("<span style=\"style\">	global message summary_2 global message detail_2</span>") != -1);

        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }

        root.getChildren().remove(message);
        message = new UIMessage();
        message.setId("myMessage_3");
        message.setFor("myMessage_3");
        root.getChildren().add(message);


        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);

        messageRenderer = new MessageRenderer();

        //add a styleClass so span is rendered
	message.getAttributes().put("styleClass", "styleClass");
	message.getAttributes().put("style", "style");

        // populate facescontext with some errors
        getFacesContext().addMessage(message.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_FATAL,
            "global message summary_3", "global message detail_3"));

        // test encode method

        messageRenderer.encodeBegin(getFacesContext(), message);
        messageRenderer.encodeEnd(getFacesContext(), message);

        result = writer.toString();

        //Span should have class attribute for styleClass and style attribute
        //Summary and detail should be in body of span separated by space
        assertTrue(result.indexOf("<span class=\"styleClass\" style=\"style\">	global message summary_3 global message detail_3</span>") != -1);

        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }

        root.getChildren().remove(message);
        message = new UIMessage();
        message.setId("myMessage_4");
        message.setFor("myMessage_4");
        root.getChildren().add(message);

        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);

        messageRenderer = new MessageRenderer();

        //add a styleClass so span is rendered
	message.getAttributes().put("styleClass", "styleClass");
	message.getAttributes().put("style", "style");

        //set tooltip criteria to true
	message.getAttributes().put("tooltip", new Boolean(true));
        message.setShowDetail(true);
        message.setShowSummary(true);

        // populate facescontext with some errors
        getFacesContext().addMessage(message.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_FATAL,
            "global message summary_4", "global message detail_4"));

        // test encode method

        messageRenderer.encodeBegin(getFacesContext(), message);
        messageRenderer.encodeEnd(getFacesContext(), message);

        result = writer.toString();

        //Span should containt class for styleClass, style, 
        //  and title for tooltip attributes
        //Summary should go in the title attribute and only the 
        //  detail displayed in the body of the span
        assertTrue(result.indexOf("<span class=\"styleClass\" style=\"style\" title=\"global message summary_4\">	global message detail_4</span>") != -1);

        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }

        root.getChildren().remove(message);
        message = new UIMessage();
        message.setId("myMessage_5");
        message.setFor("myMessage_5");
        root.getChildren().add(message);

        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);

        messageRenderer = new MessageRenderer();

        //add a styleClass so span is rendered
	message.getAttributes().put("styleClass", "styleClass");
	message.getAttributes().put("style", "style");

        //set tooltip criteria to true
	message.getAttributes().put("tooltip", new Boolean(true));
        message.setShowDetail(true);
        message.setShowSummary(true);

        //Set layout to table
	message.getAttributes().put("layout", "table");

        // populate facescontext with some errors
        getFacesContext().addMessage(message.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_FATAL,
            "global message summary_5", "global message detail_5"));

        // test encode method

        messageRenderer.encodeBegin(getFacesContext(), message);
        messageRenderer.encodeEnd(getFacesContext(), message);

        result = writer.toString();

        //Span should containt class for styleClass, style, 
        //  and title for tooltip attributes
        //Summary should go in the title attribute and only the 
        //  detail displayed in the body of the span
        //Should be wrapped in a table
        assertTrue(result.indexOf("<table><tr><td><span class=\"styleClass\" style=\"style\" title=\"global message summary_5\">	global message detail_5</span></td></tr></table>") != -1);

        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }

        // restore the original ResponseWriter
        getFacesContext().setResponseWriter(originalWriter);
        getFacesContext().setViewRoot(originalRoot);
    }

    public void testMessagesRenderer(UIComponent root) throws IOException {
        System.out.println("Testing MessagesRenderer");
        UIMessages messages = new UIMessages();
        messages.setId("myMessage_0");
        messages.setFor("myMessage_0");
        root.getChildren().add(messages);

        ResponseWriter originalWriter = getFacesContext().getResponseWriter();
        UIViewRoot originalRoot = getFacesContext().getViewRoot();

        getFacesContext().setViewRoot((UIViewRoot) root);

        // setup a new HtmlResponseWriter using a StringWriter.
        // This allows us to capture the output and check for
        // correctness without using a goldenfile.
        StringWriter writer = new StringWriter();
        HtmlResponseWriter htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);

        MessagesRenderer messagesRenderer = new MessagesRenderer();

        // populate facescontext with some errors
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_INFO,
            "global message summary_0.0", "global message detail_0.0"));
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_INFO,
            "global message summary_0.1", "global message detail_0.1"));

        // test encode method

        messagesRenderer.encodeBegin(getFacesContext(), messages);
        messagesRenderer.encodeEnd(getFacesContext(), messages);

        String result = writer.toString();

        //no span should be rendered since none of the criteria was met
        assertTrue(result.indexOf("span") == -1);

        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }

        root.getChildren().remove(messages);
        messages = new UIMessages();
        messages.setId("myMessage_1");
        messages.setFor("myMessage_1");
        root.getChildren().add(messages);

        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);

        messagesRenderer = new MessagesRenderer();

        //add a styleClass so span is rendered
	messages.getAttributes().put("styleClass", "styleClass");

        // populate facescontext with some errors
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_WARN,
            "global message summary_1.0", "global message detail_1.0"));
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_WARN,
            "global message summary_1.1", "global message detail_1.1"));
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_WARN,
            "global message summary_1.1", "global message detail_1.1"));

        // test encode method

        messagesRenderer.encodeBegin(getFacesContext(), messages);
        messagesRenderer.encodeEnd(getFacesContext(), messages);

        result = writer.toString();

        //Span should have class attribute for styleClass
        //Summary and detail should be in body of span separated by space
        //Verify that both messages are included
        assertTrue(result.indexOf("<span class=\"styleClass\">	global message summary_1.0 global message detail_1.0</span>") != -1);
        assertTrue(result.indexOf("<span class=\"styleClass\">	global message summary_1.1 global message detail_1.1</span>") != -1);

        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }

        root.getChildren().remove(messages);
        messages = new UIMessages();
        messages.setId("myMessage_2");
        messages.setFor("myMessage_2");
        root.getChildren().add(messages);

        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);

        messagesRenderer = new MessagesRenderer();

        //add a styleClass so span is rendered
	messages.getAttributes().put("style", "style");

        // populate facescontext with some errors
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "global message summary_2.0", "global message detail_2.0"));
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "global message summary_2.1", "global message detail_2.1"));
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "global message summary_2.2", "global message detail_2.2"));

        // test encode method

        messagesRenderer.encodeBegin(getFacesContext(), messages);
        messagesRenderer.encodeEnd(getFacesContext(), messages);

        result = writer.toString();

        //Span should have style attribute
        //Summary and detail should be in body of span separated by space
        //Verify that three messages are included
        assertTrue(result.indexOf("<span style=\"style\">	global message summary_2.0 global message detail_2.0</span>") != -1);
        assertTrue(result.indexOf("<span style=\"style\">	global message summary_2.1 global message detail_2.1</span>") != -1);
        assertTrue(result.indexOf("<span style=\"style\">	global message summary_2.2 global message detail_2.2</span>") != -1);

        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }

        root.getChildren().remove(messages);
        messages = new UIMessages();
        messages.setId("myMessage_3");
        messages.setFor("myMessage_3");
        root.getChildren().add(messages);


        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);

        messagesRenderer = new MessagesRenderer();

        //add a styleClass so span is rendered
	messages.getAttributes().put("styleClass", "styleClass");
	messages.getAttributes().put("style", "style");

        // populate facescontext with some errors
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_FATAL,
            "global message summary_3.0", "global message detail_3.0"));
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_FATAL,
            "global message summary_3.1", "global message detail_3.1"));

        // test encode method

        messagesRenderer.encodeBegin(getFacesContext(), messages);
        messagesRenderer.encodeEnd(getFacesContext(), messages);

        result = writer.toString();

        //Span should have class attribute for styleClass and style attribute
        //Summary and detail should be in body of span separated by space
        //Verify that both messages are included
        assertTrue(result.indexOf("<span class=\"styleClass\" style=\"style\">	global message summary_3.0 global message detail_3.0</span>") != -1);
        assertTrue(result.indexOf("<span class=\"styleClass\" style=\"style\">	global message summary_3.1 global message detail_3.1</span>") != -1);

        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }

        root.getChildren().remove(messages);
        messages = new UIMessages();
        messages.setId("myMessage_4");
        messages.setFor("myMessage_4");
        root.getChildren().add(messages);

        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);

        messagesRenderer = new MessagesRenderer();

        //add a styleClass so span is rendered
	messages.getAttributes().put("styleClass", "styleClass");
	messages.getAttributes().put("style", "style");

        //set tooltip criteria to true
	messages.getAttributes().put("tooltip", new Boolean(true));
        messages.setShowDetail(true);
        messages.setShowSummary(true);

        // populate facescontext with some errors
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_FATAL,
            "global message summary_4.0", "global message detail_4.0"));
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_FATAL,
            "global message summary_4.1", "global message detail_4.1"));
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_FATAL,
            "global message summary_4.2", "global message detail_4.2"));

        // test encode method

        messagesRenderer.encodeBegin(getFacesContext(), messages);
        messagesRenderer.encodeEnd(getFacesContext(), messages);

        result = writer.toString();

        //Span should containt class for styleClass, style, 
        //  and title for tooltip attributes
        //Summary should go in the title attribute and only the 
        //  detail displayed in the body of the span
        //Verify that three messages are included
       assertTrue(result.indexOf("<span class=\"styleClass\" style=\"style\" title=\"global message summary_4.0\">	global message detail_4.0</span>") != -1);
       assertTrue(result.indexOf("<span class=\"styleClass\" style=\"style\" title=\"global message summary_4.1\">	global message detail_4.1</span>") != -1);
       assertTrue(result.indexOf("<span class=\"styleClass\" style=\"style\" title=\"global message summary_4.2\">	global message detail_4.2</span>") != -1);

        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }

        root.getChildren().remove(messages);
        messages = new UIMessages();
        messages.setId("myMessage_5");
        messages.setFor("myMessage_5");
        root.getChildren().add(messages);

        writer = new StringWriter();
        htmlWriter = new HtmlResponseWriter(writer, "text/html", "ISO-8859-1");
        getFacesContext().setResponseWriter(htmlWriter);

        messagesRenderer = new MessagesRenderer();

        //add a styleClass so span is rendered
	messages.getAttributes().put("styleClass", "styleClass");
	messages.getAttributes().put("style", "style");

        //set tooltip criteria to true
	messages.getAttributes().put("tooltip", new Boolean(true));
        messages.setShowDetail(true);
        messages.setShowSummary(true);

        //Set layout to table
	messages.getAttributes().put("layout", "table");

        // populate facescontext with some errors
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_FATAL,
            "global message summary_5.0", "global message detail_5.0"));
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_FATAL,
            "global message summary_5.1", "global message detail_5.1"));
        getFacesContext().addMessage(messages.getFor(),
            new FacesMessage(FacesMessage.SEVERITY_FATAL,
            "global message summary_5.2", "global message detail_5.2"));

        // test encode method

        messagesRenderer.encodeBegin(getFacesContext(), messages);
        messagesRenderer.encodeEnd(getFacesContext(), messages);

        result = writer.toString();

        //Span should containt class for styleClass, style, 
        //  and title for tooltip attributes
        //Summary should go in the title attribute and only the 
        //  detail displayed in the body of the span
        //Verify that three messages are included
        //Should be wrapped in a table
        assertTrue(result.indexOf("<table><tr><td><span") == 0);
        assertTrue(result.indexOf("<tr><td><span class=\"styleClass\" style=\"style\" title=\"global message summary_5.0\">	global message detail_5.0</span></tr></td>") != -1);
        assertTrue(result.indexOf("<tr><td><span class=\"styleClass\" style=\"style\" title=\"global message summary_5.1\">	global message detail_5.1</span></tr></td>") != -1);
        assertTrue(result.indexOf("<tr><td><span class=\"styleClass\" style=\"style\" title=\"global message summary_5.2\">	global message detail_5.2</span></tr></td>") != -1);
        assertTrue(result.endsWith("</span></tr></td></table>"));

        try {
            writer.close();
        } catch (IOException ioe) {
            ; // ignore
        }

        // restore the original ResponseWriter
        getFacesContext().setResponseWriter(originalWriter);
        getFacesContext().setViewRoot(originalRoot);
    }


    public void testTextareaRenderer(UIComponent root) throws IOException {
        System.out.println("Testing TextareaRenderer");
        UIInput textEntry = new UIInput();
        textEntry.setValue(null);
        textEntry.setId("myTextarea");
        root.getChildren().add(textEntry);

        TextareaRenderer textAreaRenderer = new TextareaRenderer();

        // test decode method

        System.out.println("    Testing decode method...");
        textAreaRenderer.decode(getFacesContext(), textEntry);
        assertTrue(((String)textEntry.getValue()).equals("TextareaRenderer"));

        // test encode method

        System.out.println("    Testing encode method...");
        textAreaRenderer.encodeBegin(getFacesContext(), textEntry);
        textAreaRenderer.encodeEnd(getFacesContext(), textEntry);
        getFacesContext().getResponseWriter().writeText("\n", null);
    }       
    
} // end of class TestRenderers_2
