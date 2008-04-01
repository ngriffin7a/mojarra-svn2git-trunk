/*
 * $Id: CommandEvent.java,v 1.1 2002/05/17 04:55:38 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>CommandEvent</strong> is a subclass of {@link FacesEvent} that
 * indicates that a particular {@link UICommand} was selected by the user.
 * It is queued to the application, for processing during the
 * <em>Invoke Application</em> phase of the request processing lifecycle.</p>
 */

public class CommandEvent extends FacesEvent {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new event object from the specified source component.</p>
     *
     * @param component Source {@link UIComponent} for this event (if any)
     * @param commandName Command name of the command this event signifies
     */
    public CommandEvent(UIComponent source, String commandName) {

        super(source);
        this.commandName = commandName;

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>The command namd whose selection this event signifies.</p>
     */
    private String commandName = null;


    /**
     * <p>Return the command name of the {@link UICommand} whose selection
     * this event signifies.</p>
     */
    public String getCommandName() {

        return (commandName);

    }


}
