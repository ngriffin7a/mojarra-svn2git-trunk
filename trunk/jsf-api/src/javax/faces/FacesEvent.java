package javax.faces;

import java.util.EventObject;
import javax.servlet.ServletRequest;

/**
 * The base-class for JavaServer Faces event objects which
 * are generated by user-interface components.  An Event object
 * encapsulates any state associated with a particular event.
 */
public abstract class FacesEvent extends EventObject {
    private String sourceName;
    private ServletRequest request;

    /**
     * Initializes the request and sourceName properties of
     * this FacesEvent object.
     * @param request the ServletRequest object which this event was derived
     * @param sourceName the name of the component where this event originated
     * @throws NullPointerException if sourceName is null
     */
    protected FacesEvent(ServletRequest request, String sourceName) {
	super(sourceName);
	this.request = request;
	this.sourceName = sourceName;
    }

    /**
     * @return WComponent instance representing the component where
     *         this event originated
     */
    public Object getSource() {
	//return (WComponent)ObjectManager.get(request, sourceName);
	return null;
    }

    /**
     * @return String containing the name of the component where
     *         this event originated
     */
    public String getSourceName() { 
	return sourceName;
    }

    /**
     * @return ServletRequest object representing the request this
     *         event was derived from or null if this event did
     *         not originate from a request.
     */
    public ServletRequest getRequest() {
        return request;
    }
}
