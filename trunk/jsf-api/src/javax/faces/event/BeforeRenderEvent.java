/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.event;

import javax.faces.component.UIComponent;

/**
 *
 * <div class="changed_added_2_0">
 *
 * <p>When an instance of this event is passed to {@link
 * SystemEventListener#processEvent} or {@link
 * ComponentSystemEventListener#processEvent}, the listener implementation
 * may assume that the <code>source</code> of this event instance is the
 * {@link UIComponent} instance that is about to be rendered just added
 * to its parent and that it is safe to call {@link
 * UIComponent#getParent}, {@link UIComponent#getClientId}, and other
 * methods that depend upon the component instance being added into the
 * view.</p>

 * </div>

 */
public class BeforeRenderEvent extends ComponentSystemEvent {

    public BeforeRenderEvent(UIComponent component) {
        super(component);
    }

}
