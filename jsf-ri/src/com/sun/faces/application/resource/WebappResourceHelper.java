package com.sun.faces.application.resource;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

/**
 * <p>
 * A {@link ResourceHelper} implementation for finding/serving resources
 * found within <code>&lt;contextroot&gt;/resources</code> directory of a
 * web application.
 * </p>
 *
 * @since 2.0
 */
public class WebappResourceHelper extends ResourceHelper {

    private static final WebappResourceHelper INSTANCE = new WebappResourceHelper();

    private static final String BASE_RESOURCE_PATH = "/resources";


    // ------------------------------------------------------------ Constructors


    protected WebappResourceHelper() { }


    // ---------------------------------------------------------- Public Methods


    public static WebappResourceHelper getInstance() {

        return INSTANCE;

    }


    // --------------------------------------------- Methods from ResourceHelper


    /**
     * @see com.sun.faces.application.resource.ResourceHelper#getBaseResourcePath()
     */
    public String getBaseResourcePath() {

        return BASE_RESOURCE_PATH;

    }


    /**
     * @see ResourceHelper#getInputStream(String, javax.faces.context.FacesContext)
     */
    public InputStream getInputStream(String path, FacesContext ctx) {

        return ctx.getExternalContext().getResourceAsStream(path);

    }


    /**
     * @see ResourceHelper#getURL(String, javax.faces.context.FacesContext)
     */
    public URL getURL(String path, FacesContext ctx) {

        try {
            return ctx.getExternalContext().getResource(path);
        } catch (MalformedURLException e) {
            return null;
        }

    }


    /**
     * @see ResourceHelper#findLibrary(String, String, javax.faces.context.FacesContext)
     */
    public LibraryInfo findLibrary(String libraryName,
                                   String localePrefix,
                                   FacesContext ctx) {

        String path;
        if (localePrefix == null) {
            path = getBaseResourcePath() + '/' + libraryName;
        } else {
            path = getBaseResourcePath()
                   + '/'
                   + localePrefix
                   + '/'
                   + libraryName;
        }
        Set<String> resourcePaths =
              ctx.getExternalContext().getResourcePaths(path);
        // it could be possible that there exists an empty directory
        // that is representing the library, but if it's empty, treat it
        // as non-existant and return null.
        if (resourcePaths != null && !resourcePaths.isEmpty()) {
            String version = getVersion(resourcePaths);
                return new LibraryInfo(libraryName, version, this);
        }

        return null;
    }


    /**
     * @see ResourceHelper#findResource(LibraryInfo, String, String, javax.faces.context.FacesContext)
     */
    public ResourceInfo findResource(LibraryInfo library,
                                     String resourceName,
                                     String localePrefix,
                                     FacesContext ctx) {

        String basePath;
        if (library != null) {
            basePath = library.getPath() + '/' + resourceName;
        } else {
            if (localePrefix == null) {
                basePath = getBaseResourcePath() + '/' + resourceName;
            } else {
                basePath = getBaseResourcePath()
                           + '/'
                           + localePrefix
                           + '/'
                           + resourceName;
            }
        }

        // first check to see if the resource exists, if not, return null.  Let
        // the caller decide what to do.
        try {
            if (ctx.getExternalContext().getResource(basePath) == null) {
                return null;
            }
        } catch (MalformedURLException e) {
            throw new FacesException(e);
        }

        // we got to hear, so we know the resource exists (either as a directory
        // or file)
        Set<String> resourcePaths =
              ctx.getExternalContext().getResourcePaths(basePath);
        // if getResourcePaths returns null or an empty set, this means that we have
        // a non-directory resource, therefor, this resource isn't versioned.
        if (resourcePaths == null || resourcePaths.size() == 0) {
            if (library != null) {
                return new ResourceInfo(library, resourceName, null);
            } else {
                return new ResourceInfo(resourceName, null, this);
            }
        } else {
            // ok, subdirectories exist, so find the latest 'version' directory
            String version = getVersion(resourcePaths);
            if (version == null) {
                // Problem here - they aren't following the property format
                // RELEASE_PENDING (rlubke) i18n
                throw new MalformedResourcePathException("Resource '"
                                                         + resourceName
                                                         + "' represents a directory, but there are no discernable versions within said directory");
            }
            if (library != null) {
                return new ResourceInfo(library, resourceName, version);
            } else {
                return new ResourceInfo(resourceName, version, this);
            }
        }

    }


    // --------------------------------------------------------- Private Methods



}
