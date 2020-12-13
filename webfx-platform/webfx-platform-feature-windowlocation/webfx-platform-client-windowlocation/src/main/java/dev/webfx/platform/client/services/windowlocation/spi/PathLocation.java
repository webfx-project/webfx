package dev.webfx.platform.client.services.windowlocation.spi;

import dev.webfx.platform.shared.util.Strings;

/**
 * @author Bruno Salmon
 */
public interface PathLocation {

    default String getPath() { return Strings.concat(getPathname(), getSearch(), getHash());}

    /**
     * A pathname is the portion of a URL that describes a hierarchical path, including the preceding /.
     * For example, in http://example.com/the/path?the=query#thehash, /the/path is the pathname.
     * It is synonymous with window.location.pathname in web browsers.
     *
     * @return A String containing an initial '/' followed by the path of the URL
     */
    String getPathname();

    /**
     * A search is the portion of the URL that follows the pathname, including any preceding ?.
     * For example, in http://example.com/the/path?the=query#thehash, ?the=query is the search.
     * It is synonymous with window.location.search in web browsers.
     *
     * @return A String containing a '?' followed by the parameters of the URL.
     */
    default String getSearch() { return Strings.isEmpty(getQueryString()) ? "" : Strings.concat("?", getQueryString()); }

    /**
     * The queryString is the part of the URL after the '?'. This is often used for parameter passing.
     * @return the queryString
     */
    String getQueryString();

    /**
     * A hash is the portion of the URL that follows the search, including any preceding #.
     * For example, in http://example.com/the/path?the=query#thehash, #thehash is the hash.
     * @return A String containing a '#' followed by the fragment identifier of the URL.
     */
    default String getHash() { return Strings.isEmpty(getFragment()) ? "" : Strings.concat("#", getFragment()); }

    /**
     * The fragment is the part of the URL after the '#'. This is often used for parameter passing.
     * @return the fragment
     */
    String getFragment();

}
