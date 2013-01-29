/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
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
package com.sun.faces.facelets.flow;

import com.sun.faces.RIConstants;
import com.sun.faces.facelets.tag.TagHandlerImpl;
import com.sun.faces.flow.SwitchCaseImpl;
import com.sun.faces.flow.FlowImpl;
import com.sun.faces.flow.ParameterImpl;
import com.sun.faces.flow.ReturnNodeImpl;
import com.sun.faces.flow.ViewNodeImpl;
import com.sun.faces.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowCallNode;
import javax.faces.flow.Flow;
import javax.faces.flow.FlowHandler;
import javax.faces.flow.MethodCallNode;
import javax.faces.flow.Parameter;
import javax.faces.flow.ReturnNode;
import javax.faces.flow.SwitchNode;
import javax.faces.flow.ViewNode;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;

public class FacesFlowDefinitionTagHandler extends TagHandlerImpl {
    
    private static final String FLOW_DATA_MAP_ATTR_NAME = FacesFlowDefinitionTagHandler.class.getPackage().getName() + ".FLOW_DATA";
    
    enum FlowDataKeys {
        FlowReturnNavigationCase,
        NavigationCases,
        Views,
        VDLDocument,
        Initializer,
        Finalizer,
        StartNodeId,
        WithinFacesFlowReturn,
        WithinSwitch,
        WithinMethodCall,
        WithinFacesFlowCall,
        CurrentNavigationCase,
        CurrentMethodCall,
        CurrentFacesFlowReference,
        CurrentParameter,
        InboundParameters,
        OutboundParameters,
        Parameters,
        MethodCalls,
        SwitchNavigationCases,
        SwitchDefaultCase,
        Switches,
        FacesFlowCalls,
        
    } 
    
    public static List<Parameter> getInboundParameters(FaceletContext ctx) {
        List<Parameter> result = null;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = (List<Parameter>) flowData.get(FacesFlowDefinitionTagHandler.FlowDataKeys.InboundParameters);
        if (null == result) {
            result = Collections.synchronizedList(new ArrayList<Parameter>());
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.InboundParameters, result);
        }
        
        return result;
    }
    
    public static ParameterImpl getCurrentParameter(FaceletContext ctx) {
        ParameterImpl result = null;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = (ParameterImpl) flowData.get(FacesFlowDefinitionTagHandler.FlowDataKeys.CurrentParameter);
        if (null == result) {
            result = new ParameterImpl();
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.CurrentParameter, result);
        }
        
        return result;
    }
    
    public static void clearCurrentParameter(FaceletContext ctx) {

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        flowData.remove(FacesFlowDefinitionTagHandler.FlowDataKeys.CurrentParameter);
    }
    
    static Map<FlowDataKeys,Object> getFlowData(FaceletContext ctx) {
        Map<Object, Object> attrs = ctx.getFacesContext().getAttributes();
        Map<FlowDataKeys, Object> result = null;
        if (!attrs.containsKey(FLOW_DATA_MAP_ATTR_NAME)) {
            // Because Facelets is single threaded, this need not be concurrent.
            result = new EnumMap<FlowDataKeys, Object>(FlowDataKeys.class);
            attrs.put(FLOW_DATA_MAP_ATTR_NAME, result);
        } else {
            result = (Map<FlowDataKeys, Object>) attrs.get(FLOW_DATA_MAP_ATTR_NAME);
        }
        
        return result;
        
    }
    
    public static SwitchCaseImpl getCurrentNavigationCase(FaceletContext ctx) {
        SwitchCaseImpl result = null;

        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        result = (SwitchCaseImpl) flowData.get(FacesFlowDefinitionTagHandler.FlowDataKeys.CurrentNavigationCase);
        if (null == result) {
            result = new SwitchCaseImpl();
            flowData.put(FacesFlowDefinitionTagHandler.FlowDataKeys.CurrentNavigationCase, result);
        }
        
        return result;
    }
    
    public static void removeCurrentNavigationCase(FaceletContext ctx) {
        Map<FacesFlowDefinitionTagHandler.FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        flowData.remove(FacesFlowDefinitionTagHandler.FlowDataKeys.CurrentNavigationCase);
        
    }
    
    private String getFlowId(FaceletContext ctx) {
        String id = null;
        
        TagAttribute flowIdAttr = this.getAttribute("id");
        if (null != flowIdAttr) {
            id = flowIdAttr.getValue(ctx);
        } else {
            id = this.tag.getLocation().getPath();

            int i = id.indexOf(RIConstants.FLOW_IN_JAR_PREFIX);
            if (-1 != i) { 
                id = id.substring(i + RIConstants.FLOW_IN_JAR_PREFIX_LENGTH);
            }
            
            if (id.charAt(0) == '/') {
                id = id.substring(1);
            }
            
            if (id.startsWith("WEB-INF/")) {
                id = id.substring(8);
            }
            
            int slash = id.lastIndexOf("/");
            if (-1 != slash) {
                id = id.substring(0, slash);
            }
        }
        
        
        return id;
    }
    
    private void clearFlowData(FaceletContext ctx) {
        getFlowData(ctx).clear();
    }

    public FacesFlowDefinitionTagHandler(TagConfig config) {
        super(config);
    }
    
    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        
        // PENDING(edburns): we should explicitly allow or disallow nested <j:flow-definition> handlers.
        // I'm leaning toward disallow.
        
        clearFlowData(ctx);
        this.nextHandler.apply(ctx, parent);
        FacesContext context = ctx.getFacesContext();
        FlowHandler flowHandler = context.getApplication().getFlowHandler();
        if (null == flowHandler) {
            return;
        }
        
        Map<FlowDataKeys, Object> flowData = FacesFlowDefinitionTagHandler.getFlowData(ctx);
        String flowId = getFlowId(ctx);
        Flow existingFlow = flowHandler.getFlow(context, "", flowId);
        FlowImpl newFlow = null;
        boolean addFlow = false;
        
        if (null == existingFlow) {
            
            // In the current implementation, this is the only place where a flow 
            // is instantiated.

            newFlow = new FlowImpl();
            newFlow.setId("", flowId);
            addFlow = true;

        } else {
            
            // Inspect the flow for a view corresponding to this page.
            if (null != existingFlow.getNode(getMyNodeId())) {
                // If we have one, take no further action.
                return;
            } else {
                // else we have a flow, but no view corresponding to this page.
                if (existingFlow instanceof FlowImpl) {
                    newFlow = (FlowImpl) existingFlow;
                } else {
                    // we have an existing flow, but we are not able to configure it
                    // because it was not created by this implementation
                    throw new TagException(tag, "Unable to configure Flow " + 
                            existingFlow.getClass().getName());
                }
            }
            
        }
        
        if (flowData.isEmpty()) {
            List<ViewNode> viewsInFlow = null;
        
            // <editor-fold defaultstate="collapsed" desc="No flow data, create the entire flow now using conventions">       
            
            // Create a simple flow like this
            
            // <flow-definition id=this tag's id attribute>
            //   <default-node>the name of this page without any extension</default-node>
            //   <view id="the name of this page without any extension">
            //     <vdl-document>the name of this page with the extension</vdl-document>
            //   </view>
            // </flow-definition>
            
            //
            // <default-node>
            //
            if (null == newFlow.getStartNodeId()) {
                newFlow.setStartNodeId(getMyNodeId());
            } 
            
            //
            // <view>s
            //
            String myNodeId = getMyNodeId();
            if (null == newFlow._getViews()) {
                viewsInFlow = synthesizeViews();
                newFlow._getViews().addAll(viewsInFlow);
            } else if (null == newFlow.getNode(myNodeId)) {
                ViewNode viewNode = new ViewNodeImpl(myNodeId, this.tag.getLocation().getPath());
                newFlow._getViews().add(viewNode);
            }
            
            // </editor-fold>
 
        } else {
            
            // <editor-fold defaultstate="collapsed" desc="Some flow data, create the flow now with that data, using conventions for gaps">       

            if (null == newFlow.getStartNodeId()) {
                //
                // <default-node>
                //
                // If we have some flow data, we must have a start-node.
                String startNodeId = (String) flowData.get(FlowDataKeys.StartNodeId);
                if (null != startNodeId) {
                    newFlow.setStartNodeId(startNodeId);
                } else {
                    newFlow.setStartNodeId(getMyNodeId());
                }
            }
            
            //
            // <view>s
            //
            // We may or may not have views.
            List<ViewNode> viewsFromTag = (List<ViewNode>) flowData.get(FlowDataKeys.Views);
            if (null == viewsFromTag) {
                // If not, make one from this view.
                viewsFromTag = synthesizeViews();
            }
            newFlow._getViews().addAll(viewsFromTag);
            
            //
            // <faces-flow-return>
            //
            List<SwitchCaseImpl> facesFlowReturns = FacesFlowReturnTagHandler.getNavigationCases(ctx);
            if (null != facesFlowReturns) {
                Map<String, ReturnNode> returns = newFlow._getReturns();
                for (SwitchCaseImpl cur : facesFlowReturns) {
                    String returnId = cur.getEnclosingId();
                    if (!returns.containsKey(returnId)) {
                        ReturnNodeImpl newReturn = new ReturnNodeImpl(returnId);
                        newReturn.setFromOutcome(cur.getFromOutcome());
                        returns.put(returnId, newReturn);
                    }
                }
            }
            
            //
            // <inbound-parameters>
            //
            List<Parameter> inboundParametersFromConfig = FacesFlowDefinitionTagHandler.getInboundParameters(ctx);
            Map<String, Parameter> inboundParameters = newFlow._getInboundParameters();
            if (null != inboundParametersFromConfig && !inboundParametersFromConfig.isEmpty()) {
                for (Parameter cur : inboundParametersFromConfig) {
                    inboundParameters.put(cur.getName(), cur);
                }
            }
            
            //
            // <switch>
            //
            Map<String, SwitchNode> switchElement = SwitchNodeTagHandler.getSwitches(ctx);
            if (null != switchElement) {
                Map<String, SwitchNode> switches = newFlow._getSwitches();
                for (Map.Entry<String, SwitchNode> cur : switchElement.entrySet()) {
                    switches.put(cur.getKey(), cur.getValue());
                }
            }
            
            //
            // <method-call>
            //
            List<MethodCallNode> methodCalls = MethodCallTagHandler.getMethodCalls(ctx);
            newFlow._getMethodCalls().addAll(methodCalls);
            
            //
            // <faces-flow-call>
            //
            Map<String,FlowCallNode> facesFlowCallElement = FacesFlowCallTagHandler.getFacesFlowCalls(ctx);
            if (null != facesFlowCallElement) {
                Map<String,FlowCallNode> facesFlowCalls = newFlow._getFlowCalls();
                for (Map.Entry<String, FlowCallNode> cur : facesFlowCallElement.entrySet()) {
                    facesFlowCalls.put(cur.getKey(), cur.getValue());
                }
            }
            
            
            //
            // <initializer>
            //
            String meStr = (String) flowData.get(FlowDataKeys.Initializer);
            MethodExpression me = null;
            ExpressionFactory ef = context.getApplication().getExpressionFactory();
            final Class argTypes[] = new Class [0]; // PENDING(edburns): arguments must be supported.
            if (null != meStr) {
                me = ef.createMethodExpression(context.getELContext(), meStr, null, argTypes);
                newFlow.setInitializer(me);
            }

            //
            // <finalizer>
            //
            meStr = (String) flowData.get(FlowDataKeys.Finalizer);
            me = null;
            if (null != meStr) {
                me = ef.createMethodExpression(context.getELContext(), meStr, null, argTypes);
                newFlow.setFinalizer(me);
            }

            
            
            
            // </editor-fold>
            
        }

        // This needs to be done at the end so that the flow is fully populated.
        if (addFlow) {
            newFlow.init(context);
            flowHandler.addFlow(context, newFlow);

        }
        
    }
    
    private List<ViewNode> synthesizeViews() {
        List<ViewNode> viewsInFlow = null;
        String myNodeId = getMyNodeId();
        
        String path = this.tag.getLocation().getPath();
        if (path.endsWith(RIConstants.FLOW_DEFINITION_ID_SUFFIX)) {
            int i = path.indexOf(RIConstants.FLOW_DEFINITION_ID_SUFFIX);
            path = path.substring(0, i) + ".xhtml";
        }
        
        
        ViewNode viewNode = new ViewNodeImpl(myNodeId, path);
        viewsInFlow = Collections.synchronizedList(new ArrayList<ViewNode>());
        viewsInFlow.add(viewNode);
        
        return viewsInFlow;
    }
    
    private String getMyNodeId() {
        String myViewId = Util.removeAllButLastSlashPathSegment(this.tag.getLocation().getPath());
        if (myViewId.endsWith(RIConstants.FLOW_DEFINITION_ID_SUFFIX)) {
            myViewId = myViewId.substring(0, myViewId.length() - RIConstants.FLOW_DEFINITION_ID_SUFFIX_LENGTH);
        } else {

            int dot = myViewId.indexOf(".");
            myViewId = myViewId.substring(0, dot);
        }
        return myViewId;
        
    }
    
}
