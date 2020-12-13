package dev.webfx.platform.client.services.windowlocation.spi.impl;

import dev.webfx.platform.client.services.windowlocation.spi.PathLocation;
import dev.webfx.platform.shared.services.json.JsonObject;
import dev.webfx.platform.shared.util.Objects;
import dev.webfx.platform.shared.util.Strings;

/**
 * @author Bruno Salmon
 */
public class PathLocationImpl implements PathLocation {

    private final String pathname;
    private final String queryString;
    private final String fragment;

    public PathLocationImpl(String path) {
        String pathname = path;
        String queryString = null;
        String fragment = null;
        int p = pathname.indexOf('#');
        if (p != -1) {
            fragment = pathname.substring(p + 1);
            pathname = pathname.substring(0, p);
        }
        p = pathname.indexOf('?');
        if (p != -1) {
            queryString = pathname.substring(p + 1);
            pathname = pathname.substring(0, p);
        }
        this.pathname = pathname;
        this.queryString = queryString;
        this.fragment = fragment;
    }

    public PathLocationImpl(PathLocation pathLocation) {
        this(pathLocation.getPathname(), pathLocation.getQueryString(), pathLocation.getFragment());
    }

    public PathLocationImpl(String pathname, String queryString, String fragment) {
        this.pathname = pathname;
        this.queryString = Strings.removePrefix(queryString, "?");
        this.fragment = Strings.removePrefix(fragment, "#");
    }

    public String getPathname() {
        return pathname;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    @Override
    public String getFragment() {
        return fragment;
    }

    public static PathLocationImpl fromJson(JsonObject json) {
        String path = json.getString("path");
        if (path != null)
            return new PathLocationImpl(path);
        String pathname = json.getString("pathname");
        if (pathname != null) {
            String queryString = Objects.coalesce(json.getString("querystring"), json.getString("queryString"), json.getString("search"));
            String fragment = Objects.coalesce(json.getString("fragment"), json.getString("hash"));
            return new PathLocationImpl(pathname, queryString, fragment);
        }
        return null;
    }

    /**
     *  Implementing equals() and hashCode() so a location can be identified in the history from a path location.
     *  So we allow super classes and consider only the pathname, the queryString and the fragment.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PathLocationImpl)) return false;

        PathLocationImpl that = (PathLocationImpl) o;

        if (pathname != null ? !pathname.equals(that.pathname) : that.pathname != null) return false;
        if (queryString != null ? !queryString.equals(that.queryString) : that.queryString != null) return false;
        return fragment != null ? fragment.equals(that.fragment) : that.fragment == null;

    }

    @Override
    public int hashCode() {
        int result = pathname != null ? pathname.hashCode() : 0;
        result = 31 * result + (queryString != null ? queryString.hashCode() : 0);
        result = 31 * result + (fragment != null ? fragment.hashCode() : 0);
        return result;
    }
}
