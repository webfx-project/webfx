package naga.core.routing.history;

import naga.core.json.JsonObject;

/**
 * A location descriptor is the pushable analogue of a location.
 * The history object uses locations to tell its listeners where they are, while history users use location descriptors
 * to tell the history object where to go.
 * The object signature is compatible with that of location, differing only in ignoring the internally-generated
 * action and key fields. This allows you to build a location descriptor from an existing location, which can be used to
 * change only specific fields on the location.
 *
 * @author Bruno Salmon
 */

public interface HistoryLocationDescriptor {

    /**
     * A pathname is the portion of a URL that describes a hierarchical path, including the preceding /.
     * For example, in http://example.com/the/path?the=query#thehash, /the/path is the pathname.
     * It is synonymous with window.location.pathname in web browsers.
     *
     * @return the pathname
     */
    String getPathName();

    /**
     * A search is the portion of the URL that follows the pathname, including any preceding ?.
     * For example, in http://example.com/the/path?the=query#thehash, ?the=query is the search.
     * It is synonymous with window.location.search in web browsers.
     *
     * @return the search
     */
    String getSearch();

    /**
     * A hash is the portion of the URL that follows the pathname, including any preceding ?.
     * For example, in http://example.com/the/path?the=query#thehash, #thehash is the hash.
     * @return A String containing a '#' followed by the fragment identifier of the URL.
     */
    String getHash();

    /**
     * A location state is an arbitrary object of data associated with a particular location.
     * This is basically a way to tie extra state to a location that is not contained in the URL.
     * This type gets its name from the first argument to HTML5's pushState and replaceState methods.
     *
     * @return a json object representing the location state
     */
    JsonObject getState();

}
