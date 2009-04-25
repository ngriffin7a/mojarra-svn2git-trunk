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

package javax.faces.context;

import javax.faces.FacesException;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;


/**
 * <p class="changed_added_2_0"><strong>ExceptionHandler</strong> is the
 * central point for handling <em>unexpected</em>
 * <code>Exception</code>s that are thrown during the Faces
 * lifecycle. The <code>ExceptionHandler</code> must not be notified of
 * any <code>Exception</code>s that occur during application startup or
 * shutdown.</p>

 * <div class="changed_added_2_0">
 *
 * <p>See the specification prose document for the requirements for the
 * default implementation.  <code>Exception</code>s may be passed to the
 * <code>ExceptionHandler</code> in one of two ways:</p>
 *
 * <ul>
 *
 * <li><p>by ensuring that <code>Exception</code>s are not caught, or
 * are caught and re-thrown.</p>
 *
 *         <p>This approach allows the <code>ExceptionHandler</code>
 *         facility specified in section JSF.6.2 to operate on the
 *         <code>Exception</code>.</p>
 *
 *         </li>
 *
 * <li><p>By using the system event facility to publish an {@link
 * ExceptionQueuedEvent} that wraps the <code>Exception</code>.</p>
 *
 *         <p>This approach requires manually publishing the {@link
 *         ExceptionQueuedEvent}, but allows more information about the
 *         <code>Exception</code>to be stored in the event.  The
 *         following code is an example of how to do this.</p>
 *
 * <pre><code>
 *
 * //...
 * } catch (Exception e) {
 *   FacesContext ctx = FacesContext.getCurrentInstance();
 *   ExceptionQueuedEventContext eventContext = new ExceptionQueuedEventContext(ctx, e);
 *   eventContext.getAttributes().put("key", "value");
 *   ctx.getApplication().publishEvent(ExceptionQueuedEvent.class, eventContext);
 * }
 *
 * </code></pre>
 *
 *            <p>Because the <code>Exception</code> must not be re-thrown
 *            when using this approach, lifecycle processing may continue
 *            as normal, allowing more <code>Exception</code>s to be
 *            published if necessary.</p>
 *
 *         </li>
 * </ul>
 *
 * <p>With either approach, any <code>ExceptionQueuedEvent</code> instances
 * that are published in this way are accessible to the {@link #handle}
 * method, which is called at the end of each lifecycle phase, as
 * specified in section JSF.6.2.</p>

 * <p>Instances of this class are request scoped and are created by
 * virtue of {@link FacesContextFactory#getFacesContext} calling {@link
 * ExceptionHandlerFactory#getExceptionHandler}.</p>
 *
 * </div>
 *
 * @since 2.0
 */
public abstract class ExceptionHandler implements SystemEventListener {

   /**
    * <p class="changed_added_2_0">Take action to handle the
    * <code>Exception</code> instances residing inside the {@link
    * ExceptionQueuedEvent} instances that have been queued by calls to
    * <code>Application().publishEvent(ExceptionQueuedEvent.class,
    * <em>eventContext</em>)</code>.  The requirements of the default
    * implementation are detailed in section JSF.6.2.1.</p>

    * @throws FacesException if and only if a problem occurs while
    * performing the algorithm to handle the <code>Exception</code>, not
    * as a means of conveying a handled <code>Exception</code> itself.
    *
    * @since 2.0
    */
    public abstract void handle() throws FacesException;


    /**
     * <p class="changed_added_2_0">Return the first
     * <code>ExceptionQueuedEvent</code> handled by this handler.</p>
     */
    public abstract ExceptionQueuedEvent getHandledExceptionQueuedEvent();


    /**
     * <p class="changed_added_2_0">Return an <code>Iterable</code> over
     * all <code>ExceptionQueuedEvent</code>s that have not yet been handled
     * by the {@link #handle} method.</p>
     */
    public abstract Iterable<ExceptionQueuedEvent> getUnhandledExceptionQueuedEvents();


    /**
     * <p class="changed_added_2_0">The default implementation must
     * return an <code>Iterable</code> over all
     * <code>ExceptionQueuedEvent</code>s that have been handled by the {@link
     * #handle} method.</p>
     */
    public abstract Iterable<ExceptionQueuedEvent> getHandledExceptionQueuedEvents();


    /**
     * {@inheritDoc}
     */
    public abstract void processEvent(SystemEvent exceptionQueuedEvent) throws AbortProcessingException;


    /**
     * {@inheritDoc}
     */
    public abstract boolean isListenerForSource(Object source);

    
    /**
     * <p class="changed_added_2_0">Unwrap the argument <code>t</code>
     * until the unwrapping encounters an Object whose
     * <code>getClass()</code> is not equal to
     * <code>FacesException.class</code> or
     * <code>javax.el.ELException.class</code>.  If there is no root cause, <code>null</code> is returned.</p>

     * @throws NullPointerException if argument <code>t</code> is
     * <code>null</code>.

     * @since 2.0
     */
    public abstract Throwable getRootCause(Throwable t);

}

