package naga.core.spi.platform.web;

/**
 * @author Bruno Salmon
 */
public interface WindowLocation {

    /**
     * Ex: https://developer.mozilla.org/en-US/search?q=URL#search-results-close-container
     * @return A String containing the entire URL.
     */
    String getHref();

    /**
     * Ex: https: in https://developer.mozilla.org/en-US/search?q=URL#search-results-close-container
     * @return A String containing the protocol scheme of the URL, including the final ':'
     */
    String getProtocol();

    /**
     * Ex: developer.mozilla.org:443 in https://developer.mozilla.org:443/en-US/search?q=URL#search-results-close-container
     * @return A String containing the host, that is the hostname, a ':', and the port of the URL.
     */
    String getHost();

    /**
     * Ex: developer.mozilla.org in https://developer.mozilla.org:443/en-US/search?q=URL#search-results-close-container
     * @return A String containing the domain of the URL.
     */
    String getHostName();

    /**
     * Ex: 443 in https://developer.mozilla.org:443/en-US/search?q=URL#search-results-close-container
     * @return A String containing the port number of the URL (or blank if not specified)
     */
    String getPort();

    /**
     * Ex: /en-US/search in https://developer.mozilla.org/en-US/search?q=URL#search-results-close-container
     * @return A String containing an initial '/' followed by the path of the URL.
     */
    String getPathName();

    /**
     * Ex: ?q=URL in https://developer.mozilla.org/en-US/search?q=URL#search-results-close-container
     * @return A String containing a '?' followed by the parameters of the URL. Also known as "querystring".
     */
    String getSearch();

    /**
     * Ex: #search-results-close-container in https://developer.mozilla.org/en-US/search?q=URL#search-results-close-container
     * @return A String containing a '#' followed by the fragment identifier of the URL.
     */
    String getHash();

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

}
