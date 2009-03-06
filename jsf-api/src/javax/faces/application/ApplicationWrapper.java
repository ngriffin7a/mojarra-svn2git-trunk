/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

package javax.faces.application;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.el.ELContextListener;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.FacesWrapper;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.validator.Validator;


/**
 * <p class="changed_added_2_0">Provides a simple implementation of
 * {@link Application} that can be subclassed by developers wishing
 * to provide specialized behavior to an existing {@link
 * ResourceHandler} instance.  The default implementation of all methods
 * is to call through to the wrapped {@link ResourceHandler}.</p>
 *
 * <div class="changed_added_2_0">
 *
 * <p>Usage: extend this class and override {@link #getWrapped} to
 * return the instance we are wrapping.</p>
 *
 * </div>
 *
 * @since 2.0
 */
public abstract class ApplicationWrapper extends Application implements FacesWrapper<Application> {

    public abstract Application getWrapped();

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getActionListener} on the
     * wrapped {@link Application} object.</p>
     */
    @Override
    public ActionListener getActionListener() {
        return getWrapped().getActionListener();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setActionListener} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void setActionListener(ActionListener listener) {
        getWrapped().setActionListener(listener);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getDefaultLocale} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public Locale getDefaultLocale() {
        return getWrapped().getDefaultLocale();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setDefaultLocale} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void setDefaultLocale(Locale locale) {
        getWrapped().setDefaultLocale(locale);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getDefaultRenderKitId} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public String getDefaultRenderKitId() {
        return getWrapped().getDefaultRenderKitId();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#addDefaultValidatorId} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void addDefaultValidatorId(String validatorId) {
        getWrapped().addDefaultValidatorId(validatorId);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#String} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public Map<String, String> getDefaultValidatorInfo() {
        return getWrapped().getDefaultValidatorInfo();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setDefaultRenderKitId} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void setDefaultRenderKitId(String renderKitId) {
        getWrapped().setDefaultRenderKitId(renderKitId);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getMessageBundle} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public String getMessageBundle() {
        return getWrapped().getMessageBundle();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setMessageBundle} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void setMessageBundle(String bundle) {
        getWrapped().setMessageBundle(bundle);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getNavigationHandler} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public NavigationHandler getNavigationHandler() {
        return getWrapped().getNavigationHandler();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setNavigationHandler} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void setNavigationHandler(NavigationHandler handler) {
        getWrapped().setNavigationHandler(handler);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getPropertyResolver} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public PropertyResolver getPropertyResolver() {
        return getWrapped().getPropertyResolver();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setPropertyResolver} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void setPropertyResolver(PropertyResolver resolver) {
        getWrapped().setPropertyResolver(resolver);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getVariableResolver} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public VariableResolver getVariableResolver() {
        return getWrapped().getVariableResolver();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setVariableResolver} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void setVariableResolver(VariableResolver resolver) {
        getWrapped().setVariableResolver(resolver);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getViewHandler} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public ViewHandler getViewHandler() {
        return getWrapped().getViewHandler();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setViewHandler} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void setViewHandler(ViewHandler handler) {
        getWrapped().setViewHandler(handler);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getStateManager} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public StateManager getStateManager() {
        return getWrapped().getStateManager();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setStateManager} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void setStateManager(StateManager manager) {
        getWrapped().setStateManager(manager);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#addComponent} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void addComponent(String componentType, String componentClass) {
        getWrapped().addComponent(componentType, componentClass);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createComponent} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public UIComponent createComponent(String componentType)
          throws FacesException {
        return getWrapped().createComponent(componentType);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createComponent} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public UIComponent createComponent(ValueBinding componentBinding,
                                       FacesContext context,
                                       String componentType)
          throws FacesException {
        return getWrapped().createComponent(componentBinding,
                                            context,
                                            componentType);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#String} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public Iterator<String> getComponentTypes() {
        return getWrapped().getComponentTypes();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#addConverter} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void addConverter(String converterId, String converterClass) {
        getWrapped().addConverter(converterId, converterClass);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#addConverter} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void addConverter(Class<?> targetClass, String converterClass) {
        getWrapped().addConverter(targetClass, converterClass);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createConverter} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public Converter createConverter(String converterId) {
        return getWrapped().createConverter(converterId);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createConverter} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public Converter createConverter(Class<?> targetClass) {
        return getWrapped().createConverter(targetClass);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#String} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public Iterator<String> getConverterIds() {
        return getWrapped().getConverterIds();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#Class} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public Iterator<Class<?>> getConverterTypes() {
        return getWrapped().getConverterTypes();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createMethodBinding} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public MethodBinding createMethodBinding(String ref, Class<?>[] params)
          throws ReferenceSyntaxException {
        return getWrapped().createMethodBinding(ref, params);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#Locale} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public Iterator<Locale> getSupportedLocales() {
        return getWrapped().getSupportedLocales();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setSupportedLocales} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void setSupportedLocales(Collection<Locale> locales) {
        getWrapped().setSupportedLocales(locales);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#addBehavior} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void addBehavior(String behaviorId, String behaviorClass) {
        getWrapped().addBehavior(behaviorId, behaviorClass);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createBehavior} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public Behavior createBehavior(String behaviorId) throws FacesException {
        return getWrapped().createBehavior(behaviorId);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#String} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public Iterator<String> getBehaviorIds() {
        return getWrapped().getBehaviorIds();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#addValidator} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void addValidator(String validatorId, String validatorClass) {
        getWrapped().addValidator(validatorId, validatorClass);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createValidator} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public Validator createValidator(String validatorId) throws FacesException {
        return getWrapped().createValidator(validatorId);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#String} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public Iterator<String> getValidatorIds() {
        return getWrapped().getValidatorIds();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createValueBinding} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public ValueBinding createValueBinding(String ref)
          throws ReferenceSyntaxException {
        return getWrapped().createValueBinding(ref);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getResourceHandler} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public ResourceHandler getResourceHandler() {
        return getWrapped().getResourceHandler();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#setResourceHandler} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void setResourceHandler(ResourceHandler resourceHandler) {
        getWrapped().setResourceHandler(resourceHandler);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getResourceBundle} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public ResourceBundle getResourceBundle(FacesContext ctx, String name) {
        return getWrapped().getResourceBundle(ctx, name);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getProjectStage} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public ProjectStage getProjectStage() {
        return getWrapped().getProjectStage();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#addELResolver} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void addELResolver(ELResolver resolver) {
        getWrapped().addELResolver(resolver);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getELResolver} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public ELResolver getELResolver() {
        return getWrapped().getELResolver();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createComponent} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public UIComponent createComponent(ValueExpression componentExpression,
                                       FacesContext context,
                                       String componentType)
          throws FacesException {
        return getWrapped().createComponent(componentExpression, context, componentType);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createComponent} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public UIComponent createComponent(ValueExpression componentExpression,
                                       FacesContext context,
                                       String componentType,
                                       String rendererType) {
        return getWrapped().createComponent(componentExpression, context, componentType, rendererType);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createComponent} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public UIComponent createComponent(FacesContext context,
                                       String componentType,
                                       String rendererType) {
        return getWrapped().createComponent(context, componentType, rendererType);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#createComponent} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public UIComponent createComponent(FacesContext context,
                                       Resource componentResource) {
        return getWrapped().createComponent(context, componentResource);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#getExpressionFactory} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public ExpressionFactory getExpressionFactory() {
        return getWrapped().getExpressionFactory();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application# T} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public <T> T evaluateExpressionGet(FacesContext context,
                                       String expression,
                                       Class<? extends T> expectedType)
          throws ELException {
        return getWrapped().evaluateExpressionGet(context, expression, expectedType);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#addELContextListener} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void addELContextListener(ELContextListener listener) {
        getWrapped().addELContextListener(listener);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#removeELContextListener} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void removeELContextListener(ELContextListener listener) {
        getWrapped().removeELContextListener(listener);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#] getELContextListeners} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public ELContextListener[] getELContextListeners() {
        return getWrapped().getELContextListeners();
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#publishEvent} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void publishEvent(Class<? extends SystemEvent> systemEventClass,
                             Object source) {
        getWrapped().publishEvent(systemEventClass, source);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#publishEvent} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void publishEvent(Class<? extends SystemEvent> systemEventClass,
                             Class<?> sourceBaseType,
                             Object source) {
        getWrapped().publishEvent(systemEventClass, sourceBaseType, source);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#subscribeToEvent} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass,
                                 Class<?> sourceClass,
                                 SystemEventListener listener) {
        getWrapped().subscribeToEvent(systemEventClass, sourceClass, listener);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#subscribeToEvent} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass,
                                 SystemEventListener listener) {
        getWrapped().subscribeToEvent(systemEventClass, listener);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#unsubscribeFromEvent} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void unsubscribeFromEvent(Class<? extends SystemEvent> systemEventClass,
                                     Class<?> sourceClass,
                                     SystemEventListener listener) {
        getWrapped().unsubscribeFromEvent(systemEventClass, sourceClass, listener);
    }

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link Application#unsubscribeFromEvent} on the
     * wrapped {@link Application} object.</p>
     *
     * @since 2.0
     */
    @Override
    public void unsubscribeFromEvent(Class<? extends SystemEvent> systemEventClass,
                                     SystemEventListener listener) {
        getWrapped().unsubscribeFromEvent(systemEventClass, listener);
    }
}
