
/*
 * $Id: ELFlash.java,v 1.6 2005/12/16 21:32:36 edburns Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */


package com.sun.faces.context.flash;

import com.sun.faces.util.FacesLogger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.PhaseId;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>How this implementation works</p>

 * <p>This class is an application singleton.  It has one ivar, {@link
 * #innerMap}.  Entries are added to and removed from this map as needed
 * according to how the flash scope is defined in the spec.  This
 * implementation never touches the session, nor does it cause the
 * session to be created.</p>

 * <p>Most of the hairy logic is encapsulated with in the inner class
 * {@link #PreviousNextFlashInfoManager}.  An instance of this class is
 * obtained by calling one of the variants of getCurrentFlashManager().
 * When the instance is no longer needed for this request, call
 * releaseCurrentFlashManager().</p>

 * <p>Two very important methods are {@link getPhaseMapForWriting} and
 * {@link getPhaseMapForReading}.  These methods are the basis for the
 * Map implementation methods.  Methods that need to write to the map
 * use getPhaseMapForWriting(), those that need to read use
 * getPhaseMapForReading().  These methods allow for the laziness that
 * allows us to only incur a cost when the flash is actually written
 * to.</p>

 * <p>The operation of this class is intimately tied to the request
 * processing lifecycle.  Let's break down every run thru the request
 * processing lifecycle into two parts called "previous" and "next".  We
 * use the names "previous" and "next" to indicate the persistence and
 * timing of the data that is stored in the flash.  Consider two runs
 * through the requset processing lifecle: N and N+1.  On request N,
 * there is no "previous" request.  On Request N, any writes to the
 * flash that happen during RENDER RESPONSE go to the "next" flash map.
 * This means they are available for the ENTIRE run though the request
 * processing lifecycle on request N+1.  Any entries put into the "next"
 * flash map on request N will be expired at the end of request N+1.
 * Now, when we get into request N+1 what was considered "next" on
 * request N, is now called "previous" from the perspective of request
 * N+1.  Any reads from the flash during request N+1 come from the
 * "previous" map.  Any writes to the flash before RENDER RESPONSE go to
 * the "previous" map.  Any writes to the flash during RENDER RESPNOSE
 * go to the "next" map.</p>
 */

public class ELFlash extends Flash {

    // <editor-fold defaultstate="collapsed" desc="ivars">

    /**
     * <p>Keys in this map are the string version of sequence numbers
     * obtained via calls to {@link #getNewSequenceNumber}.  Values are
     * the actual Map instances that back the actual Map methods on this
     * class.  All writes to and reads from this map are done by the
     * {@link PreviousNextFlashInfoManager} inner class.</p>
     * 
     */
    private Map<String,Map<String, Object>> innerMap = null;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="class vars">

    private static final Logger LOGGER = FacesLogger.CONTEXT.getLogger();

    /**
     * <p>These constants are referenced from other source files in this
     * package.  This one is a disambiguator prefix.</p>
     */
    static final String PREFIX = "csfcf";

    /**
     * <p>This constant is used as the key in the application map that
     * stores the singleton ELFlash instance.</p>
     */
    static final String FLASH_ATTRIBUTE_NAME = PREFIX + "f";

    /**
     * <p>This constant is used as the name of the cookie sent to the
     * client.</p>
     */
    static final String FLASH_COOKIE_NAME = PREFIX + "c";


    /**
     * <p>This constant is used as the key the request map used, in the
     * FlashELResolver, to convey the name of the property being
     * accessed via 'now'.</p>
     */
    static final String FLASH_NOW_REQUEST_KEY = FLASH_ATTRIBUTE_NAME + "n";

    private enum CONSTANTS {

	/**
	 * The key in the FacesContext attributes map (hereafter
	 * referred to as contextMap) for the request scoped {@link
	 * PreviousNextFlashInfoManager}.
	 */

        RequestFlashManager,

	/**
	 * At the beginning of every phase, we save the value of the
	 * facesContext.getResponseComplete() into the contextMap under
	 * this key.  We check this value after the phase to see if this
	 * is the phase where the user called responseComplete().  This
	 * is important to cover cases when the user does some funny
	 * lifecycle stuff.
	 */

        SavedResponseCompleteFlagValue,

        /**
	 * This is used as the key in the flash itself to store the messages
	 * if they are being tracked.
	 */

        FacesMessageAttributeName,

        /**
	 * This is used as the key in the flash itself to track whether or not
	 * messages are being saved across request/response boundaries.
	 */

        KeepAllMessagesAttributeName,

        /**
         * This key is used in the contextMap to indicate that the next
         * get should be treated as a keep.
         *
         */
        KeepFlagAttributeName;

    }

    private static final AtomicLong sequenceNumber = new AtomicLong(0);

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors and instance accessors">

    /** Creates a new instance of ELFlash */
    private ELFlash() {
        innerMap = new ConcurrentHashMap<String,Map<String, Object>>();
    }

    /**
     * <p>Returns the flash <code>Map</code> for this application.  This is
     * a convenience method that calls
     * <code>FacesContext.getCurrentInstance()</code> and then calls the
     * overloaded <code>getFlash()</code> that takes a
     * <code>FacesContext</code> with it.</p>
     * 
     * @return The flash <code>Map</code> for this session.
     */
    
    public static Map<String,Object> getFlash() {
        FacesContext context = FacesContext.getCurrentInstance();
        return getFlash(context.getExternalContext(), true);
    }

    /**
     *
     * @param extContext the <code>ExternalContext</code> for this request.
     *
     * @param create <code>true</code> to create a new instance for this request if 
     * necessary; <code>false</code> to return <code>null</code> if there's no 
     * instance in the current <code>session</code>.
     * 
     * @return The flash <code>Map</code> for this session.
     */
    
    public static ELFlash getFlash(ExternalContext extContext, boolean create) {
        Map<String, Object> appMap = extContext.getApplicationMap();
        ELFlash flash = (ELFlash) 
            appMap.get(FLASH_ATTRIBUTE_NAME);
        if (null == flash && create) {
            synchronized (extContext.getContext()) {
                if (null == (flash = (ELFlash)
                    appMap.get(FLASH_ATTRIBUTE_NAME))) {
                    flash = new ELFlash();
                    appMap.put(FLASH_ATTRIBUTE_NAME, flash);
                }
            }
        }
        return flash;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Abstract class overrides">

    
    public boolean isKeepMessages() {
        boolean result = false;
        Map<String, Object> phaseMap;

        if (null != (phaseMap = getPhaseMapForReading())) {
            Object value = phaseMap.get(CONSTANTS.KeepAllMessagesAttributeName.toString());
            result = (null != value) ? (Boolean) value : false;
        }
        
        return result;
    }
    
    
    public void setKeepMessages(boolean newValue) {

        getPhaseMapForWriting().put(CONSTANTS.KeepAllMessagesAttributeName.toString(),
                Boolean.valueOf(newValue));

    }
    
    
    public boolean isRedirect() {
        boolean result = false;

        FacesContext context = FacesContext.getCurrentInstance();
        Map<Object, Object> contextMap = context.getAttributes();
        PreviousNextFlashInfoManager flashManager;
        if (null != (flashManager = getCurrentFlashManager(contextMap, false))) {
            result = flashManager.getPreviousRequestFlashInfo().isIsRedirect();
        }

        return result;
    }
    

    // PENDING(edburns): I'm going to make an entry to the errata.  This
    // method can't be implemented because the decision of whether or
    // not to redirect is made by the navigationHandler.
    public void setRedirect(boolean newValue) {
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Map overrides">

    
    @SuppressWarnings("element-type-mismatch")
    public Object get(Object key) {
        Object result = null;

        if (null != key) {
            if (key.equals("keepMessages")) {
                result = this.isKeepMessages();
            } else if (key.equals("redirect")) {
                result = this.isRedirect();
            } else {
                Map<Object, Object> contextMap = FacesContext.getCurrentInstance().getAttributes();
                Boolean keepFlagIsTrue;
                if (null != (keepFlagIsTrue = (Boolean) contextMap.get(CONSTANTS.KeepFlagAttributeName)) &&
                    (boolean) keepFlagIsTrue) {
                    keep(key.toString());
                }

            }

        }
        
        if (null == result) {
            result = getPhaseMapForReading().get(key);
        }

        return result;
    }


    public Object put(String key, Object value) {
        Boolean b = null;
        Object result = null;

        if (null != key) {
            if (key.equals("keepMessages")) {
                this.setKeepMessages(b = Boolean.parseBoolean((String) value));
            }
            if (key.equals("redirect")) {
                this.setRedirect(b = Boolean.parseBoolean((String) value));
            }
        }
        result = (null == b) ? getPhaseMapForWriting().put(key, value) : b;

        return result;
    }

    @SuppressWarnings("element-type-mismatch")
    public Object remove(Object key) {
        Object result = null;

        result = getPhaseMapForWriting().remove(key);

        return result;
    }

    
    @SuppressWarnings("element-type-mismatch")
    public boolean containsKey(Object key) {
        boolean result = false;

        result = getPhaseMapForReading().containsKey(key);

        return result;
    }

    
    public boolean containsValue(Object value) {
        boolean result = false;

        result = getPhaseMapForReading().containsValue(value);

        return result;
    }

    
    public void putAll(Map<? extends String, ?> t) {

        getPhaseMapForWriting().putAll(t);

    }

    
    public Collection<Object> values() {
        Collection<Object> result = null;

        result = getPhaseMapForReading().values();

        return result;
    }

    
    public int size() {
        int result = 0;

        result = getPhaseMapForReading().size();

        return result;
    }

    
    public void clear() {

        getPhaseMapForWriting().clear();

    }

    
    @SuppressWarnings({"CloneDoesntCallSuperClone"})
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    
    public Set<Map.Entry<String, Object>> entrySet() {
        Set<Map.Entry<String, Object>> result = null;

        result = getPhaseMapForWriting().entrySet();

        return result;
    }

    
    public boolean isEmpty() {
        boolean result = false;

        result = getPhaseMapForReading().isEmpty();

        return result;
    }

    
    public Set<String> keySet() {
        Set<String> result = null;

        result = getPhaseMapForWriting().keySet();

        return result;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Flash overrides">

    
    public void keep(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Map<Object, Object> contextMap = context.getAttributes();
        PreviousNextFlashInfoManager flashManager;
        if (null != (flashManager = getCurrentFlashManager(contextMap, true))) {
            Object toKeep;

            if (null == (toKeep = requestMap.remove(key))) {
                FlashInfo flashInfo = null;
                if (null != (flashInfo = flashManager.getPreviousRequestFlashInfo())) {
                    toKeep = flashInfo.getFlashMap().remove(key);
                }
            }

            if (null != toKeep) {
                getPhaseMapForWriting().put(key, toKeep);
            }
        }


    }

    
    public void putNow(String key, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<Object, Object> contextMap = context.getAttributes();
        PreviousNextFlashInfoManager flashManager;
        if (null != (flashManager = getCurrentFlashManager(contextMap, true))) {
            FlashInfo flashInfo = null;
            if (null != (flashInfo = flashManager.getPreviousRequestFlashInfo())) {
                flashInfo.getFlashMap().put(key, value);
            }
        }
    }
    
    public void doPrePhaseActions(FacesContext context) {
        PhaseId currentPhase = context.getCurrentPhaseId();
        Map<Object, Object> contextMap = context.getAttributes();
        contextMap.put(CONSTANTS.SavedResponseCompleteFlagValue,
                context.getResponseComplete());
        PreviousNextFlashInfoManager flashManager = null;

        if (currentPhase.equals(PhaseId.RESTORE_VIEW)) {
            Cookie cookie = null;

            if (null != (cookie = getCookie(context.getExternalContext()))) {
                getCurrentFlashManager(contextMap, cookie);
            }

            if (this.isKeepMessages()) {
                this.restoreAllMessages(context);
            }
        } 
    }

    public void doPostPhaseActions(FacesContext context) {
        PhaseId currentPhase = context.getCurrentPhaseId();
        Map<Object, Object> contextMap = context.getAttributes();
        boolean
                responseCompleteJustSetTrue = responseCompleteWasJustSetTrue(context, contextMap),
                lastPhaseForThisRequest = responseCompleteJustSetTrue ||
                                          currentPhase == PhaseId.RENDER_RESPONSE;
        if (lastPhaseForThisRequest) {
            doLastPhaseActions(context, false);
        }
    }

    /**
     * <p>This is the most magic of methods.  There are several scenarios
     * in which this method can be called, but the first time it is
     * called for a request it takes action, while on subsequent times
     * it returns without taking action.  This is due to the call to
     * {@link #releaseCurrentFlashManager}.  After this call, any calls
     * to {@link #getCurrentFlashManager} will return null.</p>

     * <p>Scenario 1: normal request ending.  This will be called after
     * the RENDER_RESPONSE phase executes.  outgoingResponseIsRedirect will be false.</p>

     * <p>Scenario 2: navigationHandler asks extContext for redirect.
     * In this case, extContext calls this method directly,
     * outgoingResponseIsRedirect will be true.</p>

     * <p>Scenario 3: extContext.flushBuffer(): As far as I can tell,
     * this is only called in the JSP case, but it's good to call it
     * from there anyway, because we need to write our cookie before the
     * response is committed.  outgoingResponseIsRedirect is false.</p>

     * <p>Scenario 4: after rendering the response in JSP, but before
     * the buffer is flushed.  In the JSP case, I've found this necessary
     * because the call to extContext.flushBuffer() is too late, the
     * response has already been committed by that
     * point. outgoingResponseIsRedirect is false.</p>
     */

    public void doLastPhaseActions(FacesContext context, boolean outgoingResponseIsRedirect) {
        ExternalContext extContext = context.getExternalContext();
        Map<Object, Object> contextMap = context.getAttributes();
        PreviousNextFlashInfoManager flashManager = getCurrentFlashManager(contextMap, false);
        if (null == flashManager) {
            return;
        }
        if (this.isKeepMessages()) {
            this.saveAllMessages(context);
        }
        releaseCurrentFlashManager(contextMap);

	// What we do in this if-else statement has consequences for
	// PreviousNextFlashInfoManager.decode().
        
        if (outgoingResponseIsRedirect) {
            FlashInfo previousRequestFlashInfo = flashManager.getPreviousRequestFlashInfo();
	    // Next two methods are VITALLY IMPORTANT!
            previousRequestFlashInfo.setIsRedirect(true);
            flashManager.expireNext_MovePreviousToNext();
        } else {
            FlashInfo flashInfo = flashManager.getPreviousRequestFlashInfo();
            if (null != flashInfo && flashInfo.getLifetimeMarker() ==
                LifetimeMarker.SecondTimeThru) {
                flashManager.expirePrevious();
            }
        }


        setCookie(extContext, flashManager.encode());

    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Helpers">


    void setKeepFlag(FacesContext context) {
        context.getAttributes().put(CONSTANTS.KeepFlagAttributeName, Boolean.TRUE);
    }

    private static long getNewSequenceNumber() {
        long result = sequenceNumber.incrementAndGet();

        if (result == Long.MAX_VALUE) {
            result = 1;
            sequenceNumber.set(1);
        }

        return result;
    }

    private boolean responseCompleteWasJustSetTrue(FacesContext context,
            Map<Object, Object> contextMap) {
        boolean result = false;

        // If it was false, but it's now true, return true
        result = (Boolean.FALSE == contextMap.get(CONSTANTS.SavedResponseCompleteFlagValue) &&
                 context.getResponseComplete());

        return result;
    }

    /**
     * <p>If the current phase is earlier than RENDER_RESPONSE, return
     * the map for the "previous" request.  Otherwise, return the map
     * for the "next" request.  Note that we use
     * getCurrentFlashManager(contextMap,true).  This is because if this
     * method is being called, we know we actually need the map, so we
     * have to ensure the underlying data structure is present before
     * trying to access it.</p>
     */

    private Map<String, Object> getPhaseMapForWriting() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> result = null;
        PhaseId currentPhase = context.getCurrentPhaseId();
        Map<Object, Object> contextMap = context.getAttributes();

        PreviousNextFlashInfoManager flashManager;
        if (null != (flashManager = getCurrentFlashManager(contextMap, true))) {
            FlashInfo flashInfo;

            if (currentPhase.getOrdinal() < PhaseId.RENDER_RESPONSE.getOrdinal()) {
                flashInfo = flashManager.getPreviousRequestFlashInfo();
            } else {
                flashInfo = flashManager.getNextRequestFlashInfo(true);
            }
            result = flashInfo.getFlashMap();
        }

        return result;
    }

    /**
     * <p>Always return the map for the "previous" request.  Note that
     * we use getCurrentFlashManager(contextMap,false).  This is because
     * if this method is being called, and there is pre-existing data in
     * the flash from a previous write, then the
     * PreviousNextFlashInfoManager will already have been created.  If
     * there is not pre-existing data, we don't create the
     * PreviousNextFlashInfoManager, and therefore just return the empty
     * map.</p>
     */

    private Map<String, Object> getPhaseMapForReading() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> result = Collections.emptyMap();
        Map<Object, Object> contextMap = context.getAttributes();

        PreviousNextFlashInfoManager flashManager;
        if (null != (flashManager = getCurrentFlashManager(contextMap, false))) {
            FlashInfo flashInfo;

            if (null != (flashInfo = flashManager.getPreviousRequestFlashInfo())) {
                result = flashInfo.getFlashMap();
            }
        }

        return result;
    }

    void saveAllMessages(FacesContext context) {
        // take no action on the GET that comes after a REDIRECT
        Map<Object, Object> contextMap = context.getAttributes();
        PreviousNextFlashInfoManager flashManager;
        if (null == (flashManager = getCurrentFlashManager(contextMap, true))) {
            return;
        }
        if (flashManager.getPreviousRequestFlashInfo().isIsRedirect()) {
            return;
        }

        Iterator<String> messageClientIds = context.getClientIdsWithMessages();
        List<FacesMessage> facesMessages;
        Map<String, List<FacesMessage>> allFacesMessages = null;
        Iterator<FacesMessage> messageIter;
        String curMessageId;

        // Save all the FacesMessages into a Map, which we store in the flash.

        // Step 1, go through the FacesMessage instances for each clientId
        // in the messageClientIds list.
        while (messageClientIds.hasNext()) {
            curMessageId = messageClientIds.next();
            // Get the messages for this clientId
            messageIter = context.getMessages(curMessageId);
            facesMessages = new ArrayList<FacesMessage>();
            while (messageIter.hasNext()) {
                facesMessages.add(messageIter.next());
            }
            // Add the list to the map
            if (null == allFacesMessages) {
                allFacesMessages = new HashMap<String, List<FacesMessage>>();
            }
            allFacesMessages.put(curMessageId, facesMessages);
        }
        facesMessages = null;

        // Step 2, go through the FacesMessages that do not have a client
        // id associated with them.
        messageIter = context.getMessages(null);
        while (messageIter.hasNext()) {
            // Make sure to overwrite the previous facesMessages list
            facesMessages = new ArrayList<FacesMessage>();
            facesMessages.add(messageIter.next());
        }
        if (null != facesMessages) {
            // Add the list to the map
            if (null == allFacesMessages) {
                allFacesMessages = new HashMap<String, List<FacesMessage>>();
            }
            allFacesMessages.put(null, facesMessages);
        }
        getPhaseMapForWriting().put(CONSTANTS.FacesMessageAttributeName.toString(),
                allFacesMessages);

    }

    @SuppressWarnings("element-type-mismatch")
    void restoreAllMessages(FacesContext context) {
        Map<String, List<FacesMessage>> allFacesMessages;
        Map<String, Object> phaseMap = getPhaseMapForReading();
        List<FacesMessage> facesMessages;


        if (null != (allFacesMessages = (Map<String, List<FacesMessage>>)
                phaseMap.get(CONSTANTS.FacesMessageAttributeName.toString()))) {
            for (Map.Entry<String, List<FacesMessage>> cur : allFacesMessages.entrySet()) {
                if (null != (facesMessages = allFacesMessages.get(cur.getKey()))) {
                    for (FacesMessage curMessage : facesMessages) {
                        context.addMessage(cur.getKey(), curMessage);
                    }
                }
            }
            phaseMap.remove(CONSTANTS.FacesMessageAttributeName);
        }
    }


    /**
     * <p>Return the cookie that came from the browser, if any.</p>
     */
    private Cookie getCookie(ExternalContext extContext) {
        Cookie result = null;

        result = (Cookie) extContext.getRequestCookieMap().get(FLASH_COOKIE_NAME);

        return result;
    }

    /** 
     * <p>Set the cookie iff the response was not yet committed.  If the response
     * was committed, log a warning.</p>
     */

    private void setCookie(ExternalContext extContext, Cookie toSet) {
        HttpServletResponse resp = (HttpServletResponse) extContext.getResponse();
	if (resp.isCommitted()) {
	    if (LOGGER.isLoggable(Level.WARNING)) {
		LOGGER.log(Level.WARNING,
			   "jsf.externalcontext.flash.response.already.committed");
	    }
	} else {
	    resp.addCookie(toSet);
	}
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Inner classes">

    private enum LifetimeMarker {

        // these must be unique
        
        FirstTimeThru("f"),
        SecondTimeThru("s"),
        IsRedirect("r"),
        IsNormal("n");

        private static char FIRST_TIME_THRU = 'f';
        private static char SECOND_TIME_THRU = 's';
        private static char IS_REDIRECT = 'r';
        private static char IS_NORMAL = 'n';

        private String name;

        private LifetimeMarker(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
        
        public char encode() {
            return name.charAt(0);
        }

        public static LifetimeMarker decode(char c) {
            LifetimeMarker result = FirstTimeThru;

            if (FIRST_TIME_THRU == c) {
                result = FirstTimeThru;
            } else if (SECOND_TIME_THRU == c) {
                result = SecondTimeThru;
            } else if (IS_REDIRECT == c) {
                result = IsRedirect;
            } else if (IS_NORMAL == c) {
                result = IsNormal;
            } else {
                throw new IllegalStateException("class invariant failed: invalid lifetime marker");
            }

            return result;
        }
                
    }

    private void releaseCurrentFlashManager(Map<Object, Object> contextMap) {
        contextMap.remove(CONSTANTS.RequestFlashManager);
    }

    /**
     * <p>Called when you need to get access to the flashManager.  If
     * argument create is true, and no instance of the FlashManager
     * exists for this request, create it and store it in the
     * contextMap.</p>
     */
    private PreviousNextFlashInfoManager getCurrentFlashManager(Map<Object, Object> contextMap,
            boolean create) {
        PreviousNextFlashInfoManager result = (PreviousNextFlashInfoManager)
                contextMap.get(CONSTANTS.RequestFlashManager);

        if (null == result && create) {
            result = new PreviousNextFlashInfoManager();
            result.initializeBaseCase();
            contextMap.put(CONSTANTS.RequestFlashManager, result);

        }
        return result;
    }

    /**
     * <p>Called on the preRestoreView phase if the browser sent us a
     * cookie.  If no instance of the FlashManager exists for this
     * request, create it and store it in the contextMap.</p>
     */
    private PreviousNextFlashInfoManager getCurrentFlashManager(Map<Object, Object> contextMap,
            Cookie cookie) {
        PreviousNextFlashInfoManager result = (PreviousNextFlashInfoManager)
                contextMap.get(CONSTANTS.RequestFlashManager);

        if (null == result) {
            result = new PreviousNextFlashInfoManager();
            result.decode(cookie);
            contextMap.put(CONSTANTS.RequestFlashManager, result);

        }
        return result;
    }

    /**
     * <p>On any given request, there are actually two maps behind the
     * flash.  Which one is actually used on a given Map method depends
     * on the current lifecycle phase at the time the method is invoked.
     * There is a "next" map, and a "previous" map.  This class manages
     * the complexities of dealing with these two maps, and does so by
     * relying on another inner class, FlashInfo.</p>

     * <p>The "next" map is used in only one case, which happens to be a
     * VERY common case: write operations to the flash that happen
     * during render response.</p>

     * <p>The "previous" map is used for write operations that happen
     * before render response, and for all read operations.</p>

     * <p>This class knows how to "decode" its state from an incoming
     * cookie, written by a previous call to "encode".</p>

     * <p>See the docs for FlashInfo for more information.</p>
     */

    private final class PreviousNextFlashInfoManager {

        private FlashInfo previousRequestFlashInfo;

        private FlashInfo nextRequestFlashInfo;

        private boolean incomingCookieCameFromRedirect = false;

        private PreviousNextFlashInfoManager() {

        }

        @Override
        public String toString() {
            String result = null;

            result = "previousRequestSequenceNumber: " +
                    ((null != previousRequestFlashInfo) ? previousRequestFlashInfo.getSequenceNumber() : "null") +
                    " nextRequestSequenceNumber: " +
                    ((null != nextRequestFlashInfo) ? nextRequestFlashInfo.getSequenceNumber() : "null");

            return result;
        }

        void initializeBaseCase() {
            Map<String, Object> flashMap = null;

            previousRequestFlashInfo = new FlashInfo(getNewSequenceNumber(),
                    LifetimeMarker.FirstTimeThru, false);
            innerMap.put(previousRequestFlashInfo.getSequenceNumber() + "",
                    flashMap = new HashMap<String, Object>());
            previousRequestFlashInfo.setFlashMap(flashMap);

            nextRequestFlashInfo = new FlashInfo(getNewSequenceNumber(),
                    LifetimeMarker.FirstTimeThru, false);
            innerMap.put(nextRequestFlashInfo.getSequenceNumber() + "",
                    flashMap = new HashMap<String, Object>());
            nextRequestFlashInfo.setFlashMap(flashMap);
        }

        void expirePrevious() {
            // expire previous
            if (null != previousRequestFlashInfo) {
                Map<String, Object> flashMap;
                // clear the old map
                if (null != (flashMap = previousRequestFlashInfo.getFlashMap())) {
                    flashMap.clear();
                }
                // remove it from the flash
                innerMap.remove(previousRequestFlashInfo.getSequenceNumber() + "");
                previousRequestFlashInfo = null;
            }
        }

        void expireNext_MovePreviousToNext() {
            if (null != nextRequestFlashInfo) {
                // clear the old map
                nextRequestFlashInfo.getFlashMap().clear();
                // remove it from the flash
                innerMap.remove(nextRequestFlashInfo.getSequenceNumber() + "");
                nextRequestFlashInfo = null;
            }

            nextRequestFlashInfo = previousRequestFlashInfo;
            previousRequestFlashInfo = null;
        }

	/**
	 * <p>Decode the state of the PreviousNextFlashInfoManager from
	 * a Cookie.  This entire method is wrapped in a try-catch block
	 * to prevent any errors from malformed cookies from polluting
	 * the system.  When any error occurs, the flash is not usable
	 * for this request, and a nice error message is logged.</p>

	 * <p>This method is where the LifetimeMarker is incremeted,
	 * UNLESS the incoming request is the GET after the REDIRECT
	 * after POST, in which case we don't increment it because the
	 * system will expire the entries in the doLastPhaseActions.</p>
	 *
	 */

        void decode(Cookie cookie) {
            String temp, value = cookie.getValue();
            try {
                int i = value.indexOf("_");

                // IMPORTANT: what was "next" when the cookie was
                // encoded is now "previous".  Therefore decode "next" first.
                temp = value.substring(0, i++);

                if (0 < temp.length()) {
                    nextRequestFlashInfo = new FlashInfo();
                    nextRequestFlashInfo.decode(temp);
                }
                // invariant we must always have something after the _
                previousRequestFlashInfo = new FlashInfo();
                previousRequestFlashInfo.decode(value.substring(i));

		// handle the consequences of action taken on doLastPhaseActions
                if (previousRequestFlashInfo.isIsRedirect()) {
                    this.setIncomingCookieCameFromRedirect(true);
                    previousRequestFlashInfo.setIsRedirect(false);
                } else {
                    previousRequestFlashInfo.setLifetimeMarker(LifetimeMarker.SecondTimeThru);
                }
                Map<String, Object> flashMap;
                // If the browser sent a cookie that is valid, but
                // doesn't correspond to a map in memory...
                if (null == (flashMap = innerMap.get(previousRequestFlashInfo.getSequenceNumber() + ""))) {
                    // create a new map
                    previousRequestFlashInfo = new FlashInfo();
                    previousRequestFlashInfo.setSequenceNumber(getNewSequenceNumber());
                    previousRequestFlashInfo.setLifetimeMarker(LifetimeMarker.FirstTimeThru);
                    previousRequestFlashInfo.setIsRedirect(false);
                    // put it in the flash
                    innerMap.put(previousRequestFlashInfo.getSequenceNumber() + "",
                            flashMap = new HashMap<String, Object>());
                }
                previousRequestFlashInfo.setFlashMap(flashMap);
                if (null != nextRequestFlashInfo) {
                    if (null == (flashMap = innerMap.get(nextRequestFlashInfo.getSequenceNumber() + ""))) {
                        // create a new map
                        nextRequestFlashInfo = new FlashInfo();
                        nextRequestFlashInfo.setSequenceNumber(getNewSequenceNumber());
                        nextRequestFlashInfo.setLifetimeMarker(LifetimeMarker.FirstTimeThru);
                        nextRequestFlashInfo.setIsRedirect(false);
                        // put it in the flash
                        innerMap.put(nextRequestFlashInfo.getSequenceNumber() + "",
                                flashMap = new HashMap<String, Object>());
                    }
                    nextRequestFlashInfo.setFlashMap(flashMap);
                }
            } catch (Throwable t) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                            "jsf.externalcontext.flash.bad.cookie",
                            new Object [] { value });
                }
            }

        }

	/**
	 * <p>Encode the current state of the
	 * PreviousNextFlashInfoManager to the cookie.</p>
	 */
        Cookie encode() {
            Cookie result = null;

            String value = ((null != previousRequestFlashInfo) ? previousRequestFlashInfo.encode() : "")  + "_" +
                           ((null != nextRequestFlashInfo) ? nextRequestFlashInfo.encode() : "");
            result = new Cookie(FLASH_COOKIE_NAME, value);
            if (1 == value.length()) {
                result.setMaxAge(0);
                result.setPath("/");
            }

            return result;
        }

        FlashInfo getPreviousRequestFlashInfo() {
            return previousRequestFlashInfo;
        }

        void setPreviousRequestFlashInfo(FlashInfo thisRequestFlashInfo) {
            this.previousRequestFlashInfo = thisRequestFlashInfo;
        }

        FlashInfo getNextRequestFlashInfo() {
            return nextRequestFlashInfo;
        }

        FlashInfo getNextRequestFlashInfo(boolean create) {
            if (create && null == nextRequestFlashInfo) {
                nextRequestFlashInfo = new FlashInfo();
                nextRequestFlashInfo.setSequenceNumber(getNewSequenceNumber());
                nextRequestFlashInfo.setLifetimeMarker(LifetimeMarker.FirstTimeThru);
                nextRequestFlashInfo.setIsRedirect(false);
                // put it in the flash
                Map<String, Object> flashMap = null;
                innerMap.put(nextRequestFlashInfo.getSequenceNumber() + "",
                        flashMap = new HashMap<String, Object>());
                nextRequestFlashInfo.setFlashMap(flashMap);
            }
            return nextRequestFlashInfo;
        }

        void setNextRequestFlashInfo(FlashInfo nextRequestFlashInfo) {
            this.nextRequestFlashInfo = nextRequestFlashInfo;
        }

        boolean isIncomingCookieCameFromRedirect() {
            return incomingCookieCameFromRedirect;
        }

        void setIncomingCookieCameFromRedirect(boolean incomingCookieCameFromRedirect) {
            this.incomingCookieCameFromRedirect = incomingCookieCameFromRedirect;
        }

    }

    /**
     * <p>Encapsulate one of the two maps that back the flash for the
     * current request.</p>
     */
    private static final class FlashInfo {

	/**
	 * <p>Set to true by the Flash when the extContext tells us
	 * there is a redirect.</p>
	 */
        private boolean isRedirect;

	/**
	 * <p>How many times has this map been through the lifecycle?</p>
	 */
        private LifetimeMarker lifetimeMarker;

	/**
	 * <p>Application Unique key in the innerMap.</p>
	 */

        private long sequenceNumber;

	/**
	 * <p>The Map that stores the data. This map itself is stored in
	 * innerMap under the key given by the value of
	 * sequenceNumber.</p>
	 */
        private Map<String, Object> flashMap;

        private FlashInfo() {

        }

        FlashInfo(long sequenceNumber, LifetimeMarker lifetimeMarker,
                boolean isRedirect) {
            setSequenceNumber(sequenceNumber);
            setLifetimeMarker(lifetimeMarker);
            setIsRedirect(isRedirect);
        }

        void decode(String value) {
            if (null == value || 0 == value.length()) {

                // PENDING(edburns): REMOVE THIS
                return;
            }

            int i = value.indexOf('X');

            // decode the sequence number
            setSequenceNumber(Long.parseLong(value.substring(0, i++)));

            // decode the lifetime marker
            setLifetimeMarker(LifetimeMarker.decode(value.charAt(i++)));
            
            // decode the redirect flag
            setIsRedirect(LifetimeMarker.IsRedirect ==
                    LifetimeMarker.decode(value.charAt(i++)));
        }

        String encode() {
            String value = null;

            // The cookie value is an encoding of the sequence number, the
            // lifetime marker, and the redirect flag
            if (isIsRedirect()) {
                value = Long.toString(getSequenceNumber()) + "X" +
                        getLifetimeMarker().encode() +
                        LifetimeMarker.IsRedirect.encode();
            } else {
                value = Long.toString(getSequenceNumber()) + "X" +
                        getLifetimeMarker().encode() +
                        LifetimeMarker.IsNormal.encode();
            }

            return value;
        }

        boolean isIsRedirect() {
            return isRedirect;
        }

        void setIsRedirect(boolean isRedirect) {
            this.isRedirect = isRedirect;
        }

        long getSequenceNumber() {
            return sequenceNumber;
        }

        void setSequenceNumber(long sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
        }

        LifetimeMarker getLifetimeMarker() {
            return lifetimeMarker;
        }

        void setLifetimeMarker(LifetimeMarker lifetimeMarker) {
            this.lifetimeMarker = lifetimeMarker;
        }

        Map<String, Object> getFlashMap() {
            return flashMap;
        }

        void setFlashMap(Map<String, Object> flashMap) {
            this.flashMap = flashMap;
        }

        

    }

    // </editor-fold>


}
