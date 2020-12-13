package dev.webfx.platform.client.services.windowlocation.spi;

import dev.webfx.platform.shared.util.Strings;

/**
 * @author Bruno Salmon
 */
public interface BrowsingLocation extends PathLocation {

    /**
     * Ex: https://developer.mozilla.org
     * @return a String containing the origin of the URL, that is its scheme, its domain and its port.
     * */
    default String getOrigin() {
        return Strings.concat(getProtocol(), "://", getHost());
    }

    /**
     * Ex: https://developer.mozilla.org/en-US/search?q=URL#search-results-close-container
     * @return A String containing the entire URL.
     */
    default String getHref() {
        return Strings.concat(getOrigin(), getPath());
    }

    /**
     * Ex: https in https://developer.mozilla.org/en-US/search?q=URL#search-results-close-container
     * @return A String containing the protocol scheme of the URL.
     */
    String getProtocol();

    /**
     * Ex: developer.mozilla.org:443 in https://developer.mozilla.org:443/en-US/search?q=URL#search-results-close-container
     * @return A String containing the host, that is the hostname, a ':', and the port of the URL.
     */
    default String getHost() {
        return Strings.isEmpty(getPort()) ? getHostname() : Strings.concat(getHostname(), ":", getPort());
    }

    /**
     * Ex: developer.mozilla.org in https://developer.mozilla.org:443/en-US/search?q=URL#search-results-close-container
     * @return A String containing the domain of the URL.
     */
    String getHostname();

    /**
     * Ex: 443 in https://developer.mozilla.org:443/en-US/search?q=URL#search-results-close-container
     * @return A String containing the port number of the URL (or blank if not specified)
     */
    String getPort();

    /**
     * @return A String containing the username specified before the domain name.
     */
    //String getUsername();

    /**
     * @return A String containing the password specified before the domain name.
     */
    //String getPassword();

    /**
     * Ex: https://developer.mozilla.org in https://developer.mozilla.org/en-US/search?q=URL#search-results-close-container
     * @return A String containing the canonical form of the origin of the specific location.
     */
    //String getOrigin();

    default void assignHref(String href) {throw new UnsupportedOperationException();}

    default void replaceHref(String href) {throw new UnsupportedOperationException();}
}
