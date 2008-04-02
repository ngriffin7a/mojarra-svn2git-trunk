/*
 * $Id: ConfigParser.java,v 1.2 2003/04/29 20:51:34 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import com.sun.faces.application.ApplicationImpl;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;

import javax.servlet.ServletContext;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.mozilla.util.Assert;

import org.xml.sax.Attributes;

/**
 * <p>Configuration File processing.</p>
 */

public class ConfigParser {


    //
    // Instance Variables
    //

    // The public identifier of our DTD
    protected String CONFIG_DTD_PUBLIC_ID =
        "-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.0//EN";

    /**
     * The Digester instance we will use to parse configuration files.
     */
    protected Digester digester = null;

    protected ConfigBase base = null;


    //
    // Constructors and Initializers
    //

    /**
     * Construct a new instance of this configuration parser.
     *
     */
    public ConfigParser(ServletContext servletContext) {

        super();

        digester = createDigester();
        configureRules(digester);

        base = parseConfig("/WEB-INF/faces-config.xml", servletContext);
        Assert.assert_it(null != base);
    }

    /**
     * Constuct a new instance of this configuration parser with a specified path.
     *
     */
    public ConfigParser(String configPath, ServletContext servletContext) {
        super();

        digester = createDigester();
        configureRules(digester);

        base = parseConfig(configPath, servletContext);
        Assert.assert_it(null != base);
    }

    public ConfigBase getConfigBase() {
        return base;
    }            

    // Parse the configuration file at the specified path; 
    protected ConfigBase parseConfig(String configPath, ServletContext servletContext) { 

        // Clear before parsing..

        ApplicationFactory aFactory = (ApplicationFactory)FactoryFinder.getFactory(
            FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl)aFactory.getApplication();
        application.clearManagedBeanFactories();

        InputStream input = null;
        ConfigBase base = null;

        try {
            input = servletContext.getResourceAsStream(configPath);
        } catch (Throwable t) {
            throw new RuntimeException("Error Opening File:"+configPath);
        }
        try {
            digester.clear();
            digester.push(new ConfigBase());
            base = (ConfigBase) digester.parse(input);
        } catch (Throwable t) {
            throw new RuntimeException("Unable to parse file:"+configPath+":"+
                t.getMessage());
        }
            
        try {
            input.close();
        } catch(Throwable t) {
        }

        return base;
    }


    // Create a Digester instance with no rules yet
    protected Digester createDigester() {

        Digester digester = new Digester();
        try {
            URL url = this.getClass().getResource("/com/sun/faces/config/faces-config.dtd");
            digester.register(CONFIG_DTD_PUBLIC_ID, url.toString());
            digester.setValidating(true);
        } catch (Throwable t) {
            throw new RuntimeException("Error registering DTD:"+t.getMessage());
        }
        return (digester);

    }


    // Configure the matching rules for the specified Digester instance
    // Rules assume that a ConfigBase bean is pushed on the stack first
    protected void configureRules(Digester digester) {

        configureRulesApplication(digester);
        configureRulesConverter(digester);
        configureRulesComponent(digester);
        configureRulesValidator(digester);
        configureRulesManagedBean(digester);

    }


    // Configure the rules for an <application> element
    protected void configureRulesApplication(Digester digester) {

        digester.addCallMethod("faces-config/application/action-listener",
                               "setActionListener", 0);
        digester.addCallMethod("faces-config/application/navigation-handler",
                               "setNavigationHandler", 0);
        digester.addCallMethod("faces-config/application/property-resolver",
                               "setPropertyResolver", 0);
        digester.addCallMethod("faces-config/application/variable-resolver",
                               "setVariableResolver", 0);

    }


    // Configure the rules for a <attribute> element
    protected void configureRulesAttribute(Digester digester, String prefix) {

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigAttribute");
        digester.addSetNext(prefix, "addAttribute", "com.sun.faces.config.ConfigAttribute");
        configureRulesFeature(digester, prefix);
        digester.addCallMethod(prefix + "/attribute-name",
                               "setAttributeName", 0);
        digester.addCallMethod(prefix + "/attribute-class",
                               "setAttributeClass", 0);

    }


    // Configure the rules for a <component> element
    protected void configureRulesComponent(Digester digester) {

        String prefix = "faces-config/component";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigComponent");
        digester.addSetNext(prefix, "addComponent", "com.sun.faces.config.ConfigComponent");
        configureRulesFeature(digester, prefix);
        digester.addCallMethod(prefix + "/component-type",
                               "setComponentType", 0);
        digester.addCallMethod(prefix + "/component-class",
                               "setComponentClass", 0);
        configureRulesAttribute(digester, prefix + "/attribute");
        configureRulesProperty(digester, prefix + "/property");

    }


    // Configure the rules for a <converter> element
    protected void configureRulesConverter(Digester digester) {

        String prefix = "faces-config/converter";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigConverter");
        digester.addSetNext(prefix, "addConverter", "com.sun.faces.config.ConfigConverter");
        configureRulesFeature(digester, prefix);
        digester.addCallMethod(prefix + "/converter-id",
                               "setConverterId", 0);
        digester.addCallMethod(prefix + "/converter-class",
                               "setConverterClass", 0);
        configureRulesAttribute(digester, prefix + "/attribute");
        configureRulesProperty(digester, prefix + "/property");

    }


    // Configure the generic feature rules for the specified prefix
    protected void configureRulesFeature(Digester digester, String prefix) {

        digester.addCallMethod(prefix + "/description",
                               "setDescription", 0);
        digester.addCallMethod(prefix + "/display-name",
                               "setDisplayName", 0);
        digester.addCallMethod(prefix + "/icon/large-icon",
                               "setLargeIcon", 0);
        digester.addCallMethod(prefix + "/icon/small-icon",
                               "setSmallIcon", 0);

    }


    // Configure the rules for a <property> element
    protected void configureRulesProperty(Digester digester, String prefix) {

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigProperty");
        digester.addSetNext(prefix, "addProperty", "com.sun.faces.config.ConfigProperty");
        configureRulesFeature(digester, prefix);
        digester.addCallMethod(prefix + "/property-name",
                               "setPropertyName", 0);
        digester.addCallMethod(prefix + "/property-class",
                               "setPropertyClass", 0);

    }


    // Configure the rules for a <validator> element
    protected void configureRulesValidator(Digester digester) {

        String prefix = "faces-config/validator";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigValidator");
        digester.addSetNext(prefix, "addValidator", "com.sun.faces.config.ConfigValidator");
        configureRulesFeature(digester, prefix);
        digester.addCallMethod(prefix + "/validator-id",
                               "setValidatorId", 0);
        digester.addCallMethod(prefix + "/validator-class",
                               "setValidatorClass", 0);
        configureRulesAttribute(digester, prefix + "/attribute");
        configureRulesProperty(digester, prefix + "/property");

    }

    // Configure the rules for a <managed-bean> element
    protected void configureRulesManagedBean(Digester digester) {

        String prefix = "faces-config/managed-bean";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigManagedBean");
        digester.addSetNext(prefix, "addManagedBean", "com.sun.faces.config.ConfigManagedBean");
        configureRulesFeature(digester, prefix);
        digester.addCallMethod(prefix + "/managed-bean-name", "setManagedBeanId", 0);
        digester.addCallMethod(prefix + "/managed-bean-class", "setManagedBeanClass", 0);
        digester.addCallMethod(prefix + "/managed-bean-scope", "setManagedBeanScope", 0);
        configureRulesManagedBeanProperty(digester, prefix + "/managed-property");
    }

    // Configure the rules for a <managed-bean><managed-property> element
    protected void configureRulesManagedBeanProperty(Digester digester, String prefix) {

        // these rules set the value category of the individual property value
        // the category can be one of: value, value-ref, value-class, null-value

        ConfigManagedBeanPropertyValueRule cpvRule = new ConfigManagedBeanPropertyValueRule();
        ConfigManagedBeanPropertyValueRefRule cpvrRule = new ConfigManagedBeanPropertyValueRefRule();
        ConfigManagedBeanPropertyValueTypeRule cpvtRule = new ConfigManagedBeanPropertyValueTypeRule();
        ConfigManagedBeanPropertyValueNullRule cpvnRule = new ConfigManagedBeanPropertyValueNullRule();

        // these rules set the value category of the individual property map entries 
        // the category can be one of: value, value-ref, null-value

        ConfigManagedPropertyMapValueRule cpmvRule = new ConfigManagedPropertyMapValueRule();
        ConfigManagedPropertyMapRefRule cpmrRule = new ConfigManagedPropertyMapRefRule();
        ConfigManagedPropertyMapNullRule cpmnRule = new ConfigManagedPropertyMapNullRule();

        // these method calls create a property, set the property name, add
        // the property to the parent "ConfigManagedBean" object

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigManagedBeanProperty");
        digester.addSetNext(prefix, "addProperty", "com.sun.faces.config.ConfigManagedBeanProperty");
        digester.addCallMethod(prefix + "/property-name", "setPropertyName", 0);
        digester.addCallMethod(prefix + "/map-entries/key-class", "setMapKeyClass", 0);
        digester.addCallMethod(prefix + "/map-entries/value-class", "setMapValueClass", 0);

        // for "simple" values

        configureRulesManagedBeanPropertyValue(digester, prefix + "/value");
        digester.addRule(prefix+"/value", cpvRule);

        // for "simple" value-ref

        configureRulesManagedBeanPropertyValueRef(digester, prefix + "/value-ref");
        digester.addRule(prefix+"/value-ref", cpvrRule);

        // for "simple" null-value

        configureRulesManagedBeanPropertyValueRef(digester, prefix + "/null-value");
        digester.addRule(prefix+"/null-value", cpvnRule);

        // for value arrays

        configureRulesManagedBeanPropertyValueArr(digester, prefix + "/values/value");
        configureRulesManagedBeanPropertyValueArr(digester, prefix + "/values/value-ref");
        configureRulesManagedBeanPropertyValueArr(digester, prefix + "/values/value-class");
        configureRulesManagedBeanPropertyValueArr(digester, prefix + "/values/null-value");
        digester.addRule(prefix+"/values/value-class", cpvtRule);
        digester.addRule(prefix+"/values/value", cpvRule);
        digester.addRule(prefix+"/values/value-ref", cpvrRule);
        digester.addRule(prefix+"/values/null-value", cpvnRule);

        // for map entries
    
        configureRulesManagedPropertyMap(digester, prefix + "/map-entries/map-entry");
        digester.addRule(prefix+"/map-entries/map-entry/value", cpmvRule);
        digester.addRule(prefix+"/map-entries/map-entry/value-ref", cpmrRule);
        digester.addRule(prefix+"/map-entries/map-entry/null-value", cpmnRule);
    }

    // Configure the rules for the values of a <managed-bean><property><value> element
    // This method creates property value object, sets the value, and sets it in the property object

    protected void configureRulesManagedBeanPropertyValue(Digester digester, String prefix) {
        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigManagedBeanPropertyValue");
        digester.addSetNext(prefix, "setValue", "com.sun.faces.config.ConfigManagedBeanPropertyValue");
        digester.addCallMethod(prefix, "setValue", 0);
    }

    // Configure the rules for the values of a <managed-bean><property><value-ref> element
    // This method creates property value object, sets the value, and sets it in the property object

    protected void configureRulesManagedBeanPropertyValueRef(Digester digester, String prefix) {
        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigManagedBeanPropertyValue");
        digester.addSetNext(prefix, "setValue", "com.sun.faces.config.ConfigManagedBeanPropertyValue");
        digester.addCallMethod(prefix, "setValue", 0);
    }

    // Configure the rules for the values of <managed-bean><property><values><value>,
    // <managed-bean><property><values><value-ref>,
    // <managed-bean><property><values><value-class>
    // <managed-bean><property><values><null-value> elements.
    // This method creates property value object, sets the value, and adds it to
    // the property values array in the property object
 
    protected void configureRulesManagedBeanPropertyValueArr(Digester digester, String prefix) {
        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigManagedBeanPropertyValue");
        digester.addCallMethod(prefix, "setValue", 0);
        digester.addSetNext(prefix, "addValue", "com.sun.faces.config.ConfigManagedBeanPropertyValue");
    }

    protected void configureRulesManagedPropertyMap(Digester digester, String prefix) {
        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigManagedPropertyMap");
        digester.addCallMethod(prefix+"/key", "setKey", 0);
        digester.addCallMethod(prefix+"/value", "setValue", 0);
        digester.addCallMethod(prefix+"/value-ref", "setValue", 0);
        digester.addCallMethod(prefix+"/null-value", "setValue", 0);
        digester.addSetNext(prefix, "addMapEntry", "com.sun.faces.config.ConfigManagedPropertyMap");
    }

    // Return the URL of the specified path, relative to our base directory
    protected URL relativeURL(String relativePath) throws Exception {

        File file = new File(System.getProperty("base.dir"), relativePath);
        return (file.toURL());

    }
}

// These specialized rules set the appropriate value category (value,value-ref,null-value,value-class)
// on the managed bean property value object;
 
final class ConfigManagedBeanPropertyValueRule extends Rule {
    public ConfigManagedBeanPropertyValueRule() {
        super();
    }
    public void begin(Attributes attributes) throws Exception {
        ConfigManagedBeanPropertyValue cpv = (ConfigManagedBeanPropertyValue)digester.peek();
        cpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE);
    }
}

final class ConfigManagedBeanPropertyValueRefRule extends Rule {
    public ConfigManagedBeanPropertyValueRefRule() {
        super();
    }
    public void begin(Attributes attributes) throws Exception {
        ConfigManagedBeanPropertyValue cpv = (ConfigManagedBeanPropertyValue)digester.peek();
        cpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE_REF);
    }
}

final class ConfigManagedBeanPropertyValueTypeRule extends Rule {
    public ConfigManagedBeanPropertyValueTypeRule() {
        super();
    }
    public void begin(Attributes attributes) throws Exception {
        ConfigManagedBeanPropertyValue cpv = (ConfigManagedBeanPropertyValue)digester.peek();
        cpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE_CLASS);
    }
}

final class ConfigManagedBeanPropertyValueNullRule extends Rule {
    public ConfigManagedBeanPropertyValueNullRule() {
        super();
    }
    public void begin(Attributes attributes) throws Exception {
        ConfigManagedBeanPropertyValue cpv = (ConfigManagedBeanPropertyValue)digester.peek();
        cpv.setValueCategory(ConfigManagedBeanPropertyValue.NULL_VALUE);
    }
}

// These specialized rules set the appropriate value category (value,value-ref,null-value)
// on the managed bean property map object;

final class ConfigManagedPropertyMapValueRule extends Rule {
    public ConfigManagedPropertyMapValueRule() {
        super();
    }
    public void begin(Attributes attributes) throws Exception {
        ConfigManagedPropertyMap cpm = (ConfigManagedPropertyMap)digester.peek();
        cpm.setValueCategory(ConfigManagedPropertyMap.VALUE);
    }
}

final class ConfigManagedPropertyMapRefRule extends Rule {
    public ConfigManagedPropertyMapRefRule() {
        super();
    }
    public void begin(Attributes attributes) throws Exception {
        ConfigManagedPropertyMap cpm = (ConfigManagedPropertyMap)digester.peek();
        cpm.setValueCategory(ConfigManagedPropertyMap.VALUE_REF);
    }
}

final class ConfigManagedPropertyMapNullRule extends Rule {
    public ConfigManagedPropertyMapNullRule() {
        super();
    }
    public void begin(Attributes attributes) throws Exception {
        ConfigManagedPropertyMap cpm = (ConfigManagedPropertyMap)digester.peek();
        cpm.setValueCategory(ConfigManagedPropertyMap.NULL_VALUE);
    }
}
