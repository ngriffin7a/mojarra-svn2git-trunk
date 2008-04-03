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

/*
Copyright (c) 2007, Yahoo! Inc. All rights reserved.
Code licensed under the BSD License:
http://developer.yahoo.net/yui/license.txt
version: 2.2.0
*/

YAHOO.util.Attribute=function(hash,owner){if(owner){this.owner=owner;this.configure(hash,true);}};YAHOO.util.Attribute.prototype={name:undefined,value:null,owner:null,readOnly:false,writeOnce:false,_initialConfig:null,_written:false,method:null,validator:null,getValue:function(){return this.value;},setValue:function(value,silent){var beforeRetVal;var owner=this.owner;var name=this.name;var event={type:name,prevValue:this.getValue(),newValue:value};if(this.readOnly||(this.writeOnce&&this._written)){return false;}
if(this.validator&&!this.validator.call(owner,value)){return false;}
if(!silent){beforeRetVal=owner.fireBeforeChangeEvent(event);if(beforeRetVal===false){return false;}}
if(this.method){this.method.call(owner,value);}
this.value=value;this._written=true;event.type=name;if(!silent){this.owner.fireChangeEvent(event);}
return true;},configure:function(map,init){map=map||{};this._written=false;this._initialConfig=this._initialConfig||{};for(var key in map){if(key&&YAHOO.lang.hasOwnProperty(map,key)){this[key]=map[key];if(init){this._initialConfig[key]=map[key];}}}},resetValue:function(){return this.setValue(this._initialConfig.value);},resetConfig:function(){this.configure(this._initialConfig);},refresh:function(silent){this.setValue(this.value,silent);}};(function(){var Lang=YAHOO.util.Lang;YAHOO.util.AttributeProvider=function(){};YAHOO.util.AttributeProvider.prototype={_configs:null,get:function(key){var configs=this._configs||{};var config=configs[key];if(!config){return undefined;}
return config.value;},set:function(key,value,silent){var configs=this._configs||{};var config=configs[key];if(!config){return false;}
return config.setValue(value,silent);},getAttributeKeys:function(){var configs=this._configs;var keys=[];var config;for(var key in configs){config=configs[key];if(Lang.hasOwnProperty(configs,key)&&!Lang.isUndefined(config)){keys[keys.length]=key;}}
return keys;},setAttributes:function(map,silent){for(var key in map){if(Lang.hasOwnProperty(map,key)){this.set(key,map[key],silent);}}},resetValue:function(key,silent){var configs=this._configs||{};if(configs[key]){this.set(key,configs[key]._initialConfig.value,silent);return true;}
return false;},refresh:function(key,silent){var configs=this._configs;key=((Lang.isString(key))?[key]:key)||this.getAttributeKeys();for(var i=0,len=key.length;i<len;++i){if(configs[key[i]]&&!Lang.isUndefined(configs[key[i]].value)&&!Lang.isNull(configs[key[i]].value)){configs[key[i]].refresh(silent);}}},register:function(key,map){this.setAttributeConfig(key,map);},getAttributeConfig:function(key){var configs=this._configs||{};var config=configs[key]||{};var map={};for(key in config){if(Lang.hasOwnProperty(config,key)){map[key]=config[key];}}
return map;},setAttributeConfig:function(key,map,init){var configs=this._configs||{};map=map||{};if(!configs[key]){map.name=key;configs[key]=new YAHOO.util.Attribute(map,this);}else{configs[key].configure(map,init);}},configureAttribute:function(key,map,init){this.setAttributeConfig(key,map,init);},resetAttributeConfig:function(key){var configs=this._configs||{};configs[key].resetConfig();},fireBeforeChangeEvent:function(e){var type='before';type+=e.type.charAt(0).toUpperCase()+e.type.substr(1)+'Change';e.type=type;return this.fireEvent(e.type,e);},fireChangeEvent:function(e){e.type+='Change';return this.fireEvent(e.type,e);}};YAHOO.augment(YAHOO.util.AttributeProvider,YAHOO.util.EventProvider);})();(function(){var Dom=YAHOO.util.Dom,EventPublisher=YAHOO.util.EventPublisher,AttributeProvider=YAHOO.util.AttributeProvider;YAHOO.util.Element=function(el,map){if(arguments.length){this.init(el,map);}};YAHOO.util.Element.prototype={DOM_EVENTS:null,appendChild:function(child){child=child.get?child.get('element'):child;this.get('element').appendChild(child);},getElementsByTagName:function(tag){return this.get('element').getElementsByTagName(tag);},hasChildNodes:function(){return this.get('element').hasChildNodes();},insertBefore:function(element,before){element=element.get?element.get('element'):element;before=(before&&before.get)?before.get('element'):before;this.get('element').insertBefore(element,before);},removeChild:function(child){child=child.get?child.get('element'):child;this.get('element').removeChild(child);return true;},replaceChild:function(newNode,oldNode){newNode=newNode.get?newNode.get('element'):newNode;oldNode=oldNode.get?oldNode.get('element'):oldNode;return this.get('element').replaceChild(newNode,oldNode);},initAttributes:function(map){map=map||{};var element=Dom.get(map.element)||null;this.setAttributeConfig('element',{value:element,readOnly:true});},addListener:function(type,fn,obj,scope){var el=this.get('element');var scope=scope||this;el=this.get('id')||el;if(!this._events[type]){if(this.DOM_EVENTS[type]){YAHOO.util.Event.addListener(el,type,function(e){if(e.srcElement&&!e.target){e.target=e.srcElement;}
this.fireEvent(type,e);},obj,scope);}
this.createEvent(type,this);this._events[type]=true;}
this.subscribe.apply(this,arguments);},on:function(){this.addListener.apply(this,arguments);},removeListener:function(type,fn){this.unsubscribe.apply(this,arguments);},addClass:function(className){Dom.addClass(this.get('element'),className);},getElementsByClassName:function(className,tag){return Dom.getElementsByClassName(className,tag,this.get('element'));},hasClass:function(className){return Dom.hasClass(this.get('element'),className);},removeClass:function(className){return Dom.removeClass(this.get('element'),className);},replaceClass:function(oldClassName,newClassName){return Dom.replaceClass(this.get('element'),oldClassName,newClassName);},setStyle:function(property,value){var el=this.get('element');if(!el){this._queue[this._queue.length]=['setStyle',arguments];return false;}
return Dom.setStyle(this.get('element'),property,value);},getStyle:function(property){return Dom.getStyle(this.get('element'),property);},fireQueue:function(){var queue=this._queue;for(var i=0,len=queue.length;i<len;++i){this[queue[i][0]].apply(this,queue[i][1]);}},appendTo:function(parent,before){parent=(parent.get)?parent.get('element'):Dom.get(parent);this.fireEvent('beforeAppendTo',{type:'beforeAppendTo',target:parent});before=(before&&before.get)?before.get('element'):Dom.get(before);var element=this.get('element');if(!element){return false;}
if(!parent){return false;}
if(element.parent!=parent){if(before){parent.insertBefore(element,before);}else{parent.appendChild(element);}}
this.fireEvent('appendTo',{type:'appendTo',target:parent});},get:function(key){var configs=this._configs||{};var el=configs.element;if(el&&!configs[key]&&!YAHOO.lang.isUndefined(el.value[key])){return el.value[key];}
return AttributeProvider.prototype.get.call(this,key);},set:function(key,value,silent){var el=this.get('element');if(!el){this._queue[this._queue.length]=['set',arguments];if(this._configs[key]){this._configs[key].value=value;}
return;}
if(!this._configs[key]&&!YAHOO.lang.isUndefined(el[key])){_registerHTMLAttr.call(this,key);}
return AttributeProvider.prototype.set.apply(this,arguments);},setAttributeConfig:function(key,map,init){var el=this.get('element');if(el&&!this._configs[key]&&!YAHOO.lang.isUndefined(el[key])){_registerHTMLAttr.call(this,key,map);}else{AttributeProvider.prototype.setAttributeConfig.apply(this,arguments);}},getAttributeKeys:function(){var el=this.get('element');var keys=AttributeProvider.prototype.getAttributeKeys.call(this);for(var key in el){if(!this._configs[key]){keys[key]=keys[key]||el[key];}}
return keys;},init:function(el,attr){this._queue=this._queue||[];this._events=this._events||{};this._configs=this._configs||{};attr=attr||{};attr.element=attr.element||el||null;this.DOM_EVENTS={'click':true,'keydown':true,'keypress':true,'keyup':true,'mousedown':true,'mousemove':true,'mouseout':true,'mouseover':true,'mouseup':true};var readyHandler=function(){this.initAttributes(attr);this.setAttributes(attr,true);this.fireQueue();this.fireEvent('contentReady',{type:'contentReady',target:attr.element});};if(YAHOO.lang.isString(el)){_registerHTMLAttr.call(this,'id',{value:el});YAHOO.util.Event.onAvailable(el,function(){attr.element=Dom.get(el);this.fireEvent('available',{type:'available',target:attr.element});},this,true);YAHOO.util.Event.onContentReady(el,function(){readyHandler.call(this);},this,true);}else{readyHandler.call(this);}}};var _registerHTMLAttr=function(key,map){var el=this.get('element');map=map||{};map.name=key;map.method=map.method||function(value){el[key]=value;};map.value=map.value||el[key];this._configs[key]=new YAHOO.util.Attribute(map,this);};YAHOO.augment(YAHOO.util.Element,AttributeProvider);})();YAHOO.register("element",YAHOO.util.Element,{version:"2.2.0",build:"127"});