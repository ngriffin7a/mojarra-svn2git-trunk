/*
 * $Id: RenderKit.java,v 1.36 2007/04/27 22:00:10 ofung Exp $
 */

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

package javax.faces.render;


import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseStream;
import java.io.Writer;
import java.io.OutputStream;
import java.util.Iterator;


/**
 * <p><span
 * class="changed_modified_2_0"><strong>RenderKit</strong></span>
 * represents a collection of {@link Renderer} instances that, together,
 * know how to render JavaServer Faces {@link UIComponent} instances for
 * a specific client.  Typically, {@link RenderKit}s are specialized for
 * some combination of client device type, markup language, and/or user
 * <code>Locale</code>.  A {@link RenderKit} also acts as a Factory for
 * associated {@link Renderer} instances, which perform the actual
 * rendering process for each component.</p>
 *
 * <p>A typical JavaServer Faces implementation will configure one or
 * more {@link RenderKit} instances at web application startup.  They
 * are made available through calls to the <code>getRenderKit()</code>
 * methods of {@link RenderKitFactory}.  Because {@link RenderKit}
 * instances are shared, they must be implemented in a thread-safe
 * manner.  Due to limitations in the current specification having
 * multiple <code>RenderKit</code> instances at play in the same
 * application requires a custom {@link
 * javax.faces.application.ViewHandler} instance that is aware of how to
 * deal with this case.  This limitation will be lifted in a future
 * version of the spec.</p>
 *
 * <p>The <code>RenderKit</code> instance must also vend a {@link
 * ResponseStateManager} instance, which is used in the process of
 * saving and restoring tree structure and state.</p>
 */
public abstract class RenderKit {


    /**
     * <p>Register the specified {@link Renderer} instance, associated with the
     * specified component <code>family</code> and <code>rendererType</code>,
     * to the set of {@link Renderer}s registered with this {@link RenderKit},
     * replacing any previously registered {@link Renderer} for this
     * combination of identifiers.</p>
     *
     * @param family Component family of the {@link Renderer} to register
     * @param rendererType Renderer type of the {@link Renderer} to register
     * @param renderer {@link Renderer} instance we are registering
     *
     * @throws NullPointerException if <code>family</code> or
     *  <code>rendererType</code> or <code>renderer</code> is null
     */
    public abstract void addRenderer(String family, String rendererType,
                                     Renderer renderer);


    /**
     * <p>Return the {@link Renderer} instance most recently registered for
     * the specified component <code>family</code> and
     * <code>rendererType</code>, if any; otherwise, return
     * <code>null</code>.</p>
     *
     * @param family Component family of the requested
     *  {@link Renderer} instance
     * @param rendererType Renderer type of the requested
     *  {@link Renderer} instance
     *
     * @throws NullPointerException if <code>family</code> or
     *  <code>rendererType</code> is <code>null</code>
     */
    public abstract Renderer getRenderer(String family, String rendererType);


    /**
     * <p>Return an instance of {@link ResponseStateManager} to handle
     * rendering technology specific state management decisions.</p>
     */
    public abstract ResponseStateManager getResponseStateManager();


    /**
     * <p>Use the provided <code>Writer</code> to create a new {@link
     * ResponseWriter} instance for the specified (optional) content
     * type, and character encoding.</p>
     *
     * <p>Implementors are advised to consult the
     * <code>getCharacterEncoding()</code> method of class {@link
     * javax.servlet.ServletResponse} to get the required value for the
     * characterEncoding for this method.  Since the <code>Writer</code>
     * for this response will already have been obtained (due to it
     * ultimately being passed to this method), we know that the
     * character encoding cannot change during the rendering of the
     * response.</p>
     *
     * @param writer the Writer around which this {@link ResponseWriter}
     * must be built.
     *
     * @param contentTypeList an "Accept header style" list of content
     * types for this response, or <code>null</code> if the RenderKit
     * should choose the best fit.  As of the current version, the
     * values accepted by the Standard render-kit for this parameter
     * include any valid "Accept header style" String that includes the
     * String <code>text/html</code>,
     * <code>application/xhtml+xml</code>, <code>application/xml</code>
     * or <code>text/xml</code>.  This may change in a future version.
     * The RenderKit must support a value for this argument that comes
     * straight from the <code>Accept</code> HTTP header, and therefore
     * requires parsing according to the specification of the
     * <code>Accept</code> header.  Please see <a
     * href="http://www.ietf.org/rfc/rfc2616.txt?number=2616">Section
     * 14.1 of RFC 2616</a> for the specification of the
     * <code>Accept</code> header.
     *
     * @param characterEncoding such as "ISO-8859-1" for this
     * ResponseWriter, or <code>null</code> if the
     * <code>RenderKit</code> should choose the best fit.  Please see <a
     * href="http://www.iana.org/assignments/character-sets">the
     * IANA</a> for a list of character encodings.
     *
     * @return a new {@link ResponseWriter}.
     *
     * @throws IllegalArgumentException if no matching content type
     * can be found in <code>contentTypeList</code>, no appropriate
     * content type can be found with the implementation dependent best
     * fit algorithm, or no matching character encoding can be found for
     * the argument <code>characterEncoding</code>.
     *
     */
    public abstract ResponseWriter createResponseWriter(Writer writer,
							String contentTypeList,
							String characterEncoding);


    /** 
     * <p>Use the provided <code>OutputStream</code> to create a new
     * {@link ResponseStream} instance.</p>
     *
     */ 
    public abstract ResponseStream createResponseStream(OutputStream out);


    // PENDING(rlubke) implementation
    /**
     * <p class="changed_added_2_0">Return an <code>Iterator</code> over
     * the component-family entries supported by this
     * <code>RenderKit</code> instance.</p>
     *
     * @since 2.0
     *
     */

    public Iterator<String> getComponentFamilies() {

        throw new UnsupportedOperationException();

    }
    
    // PENDING(rlubke): implementation
    /**
     * <p class="changed_added_2_0">Return an <code>Iterator</code> over
     * the renderer-type entries for the given component-family.</p>
     *
     * @param componentFamily one of the members of the
     * <code>Iterator</code> returned by {@link #getComponentFamilies}.
     *
     * @since 2.0
     */

    public Iterator<String> getRendererTypes(String componentFamily) {

        throw new UnsupportedOperationException();

    }

}
