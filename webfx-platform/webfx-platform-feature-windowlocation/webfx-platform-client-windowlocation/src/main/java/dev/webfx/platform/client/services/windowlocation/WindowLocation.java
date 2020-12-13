package dev.webfx.platform.client.services.windowlocation;

import dev.webfx.platform.client.services.windowlocation.spi.WindowLocationProvider;
import dev.webfx.platform.shared.util.Strings;
import dev.webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class WindowLocation {

    public static WindowLocationProvider getProvider() { // Returns the browser window location
        return SingleServiceProvider.getProvider(WindowLocationProvider.class, () -> ServiceLoader.load(WindowLocationProvider.class));
    }

    /**
     * Ex: https://developer.mozilla.org
     * @return a String containing the origin of the URL, that is its scheme, its domain and its port.
     * */
    public static String getOrigin() {
        return getProvider().getOrigin();
    }

    /**
     * Ex: https://developer.mozilla.org/en-US/search?q=URL#search-results-close-container
     * @return A String containing the entire URL.
     */
    public static String getHref() {
        return getProvider().getHref();
    }

    /**
     * Ex: https in https://developer.mozilla.org/en-US/search?q=URL#search-results-close-container
     * @return A String containing the protocol scheme of the URL.
     */
    public static String getProtocol() {
        return getProvider().getProtocol();
    }

    /**
     * Ex: developer.mozilla.org:443 in https://developer.mozilla.org:443/en-US/search?q=URL#search-results-close-container
     * @return A String containing the host, that is the hostname, a ':', and the port of the URL.
     */
    public static String getHost() {
        return getProvider().getHost();
    }

    /**
     * Ex: developer.mozilla.org in https://developer.mozilla.org:443/en-US/search?q=URL#search-results-close-container
     * @return A String containing the domain of the URL.
     */
    public static String getHostname() {
        return getProvider().getHostname();
    }

    /**
     * Ex: 443 in https://developer.mozilla.org:443/en-US/search?q=URL#search-results-close-container
     * @return A String containing the port number of the URL (or blank if not specified)
     */
    public static String getPort() {
        return getProvider().getPort();
    }

    public static void assignHref(String href) {
        getProvider().assignHref(href);
    }

    public static void replaceHref(String href) {
        getProvider().replaceHref(href);
    }


    public static String getPath() { return Strings.concat(getPathname(), getSearch(), getHash());}

    /**
     * A pathname is the portion of a URL that describes a hierarchical path, including the preceding /.
     * For example, in http://example.com/the/path?the=query#thehash, /the/path is the pathname.
     * It is synonymous with window.location.pathname in web browsers.
     *
     * @return A String containing an initial '/' followed by the path of the URL
     */
    public static String getPathname() {
        return getProvider().getPathname();
    }

    /**
     * A search is the portion of the URL that follows the pathname, including any preceding ?.
     * For example, in http://example.com/the/path?the=query#thehash, ?the=query is the search.
     * It is synonymous with window.location.search in web browsers.
     *
     * @return A String containing a '?' followed by the parameters of the URL.
     */
    public static String getSearch() {
        return getProvider().getSearch();
    }

    /**
     * The queryString is the part of the URL after the '?'. This is often used for parameter passing.
     * @return the queryString
     */
    public static String getQueryString() {
        return getProvider().getQueryString();
    }

    /**
     * A hash is the portion of the URL that follows the search, including any preceding #.
     * For example, in http://example.com/the/path?the=query#thehash, #thehash is the hash.
     * @return A String containing a '#' followed by the fragment identifier of the URL.
     */
    public static String getHash() {
        return getProvider().getHash();
    }

    /**
     * The fragment is the part of the URL after the '#'. This is often used for parameter passing.
     * @return the fragment
     */
    public static String getFragment() {
        return getProvider().getFragment();
    }

}
