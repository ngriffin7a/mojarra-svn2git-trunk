/*
 * $Id: MockRenderKitFactory.java,v 1.1 2003/07/20 00:41:46 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;


public class MockRenderKitFactory extends RenderKitFactory {


    private Map renderKits = new HashMap();


    public void addRenderKit(String renderKitId, RenderKit renderKit) {
        if ((renderKitId == null) || (renderKit == null)) {
            throw new NullPointerException();
        }
        synchronized (renderKits) {
            if (renderKits.containsKey(renderKitId)) {
                throw new IllegalArgumentException(renderKitId);
            }
            renderKits.put(renderKitId, renderKit);
        }
    }


    public RenderKit getRenderKit(String renderKitId) {
        if (renderKitId == null) {
            throw new NullPointerException();
        }
        synchronized (renderKits) {
            RenderKit renderKit = (RenderKit) renderKits.get(renderKitId);
            if (renderKit == null) {
                throw new IllegalArgumentException(renderKitId);
            }
            return (renderKit);
        }
    }


    public RenderKit getRenderKit(String renderKitId, FacesContext context) {
        if ((renderKitId == null) || (context == null)) {
            throw new NullPointerException();
        }
        synchronized (renderKits) {
            RenderKit renderKit = (RenderKit) renderKits.get(renderKitId);
            if (renderKit == null) {
                throw new IllegalArgumentException(renderKitId);
            }
            return (renderKit);
        }
    }


    public Iterator getRenderKitIds() {
        synchronized (renderKits) {
            return (renderKits.keySet().iterator());
        }
    }


}
