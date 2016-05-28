package naga.core.routing.history;

import java.util.Map;

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

public interface LocationDescriptor {

    /**
     * A pathname is the portion of a URL that describes a hierarchical path, including the preceding /.
     * For example, in http://example.com/the/path?the=query, /the/path is the pathname.
     * It is synonymous with window.location.pathname in web browsers.
     *
     * @return the pathname
     */
    String getPathName();

    /**
     * A search is the portion of the URL that follows the pathname, including any preceding ?.
     * For example, in http://example.com/the/path?the=query, ?the=query is the search.
     * It is synonymous with window.location.search in web browsers.
     *
     * @return the search
     */
    String getSearch();

    /**
     * A location state is an arbitrary object of data associated with a particular location.
     * This is basically a way to tie extra state to a location that is not contained in the URL.
     * This type gets its name from the first argument to HTML5's pushState and replaceState methods.
     *
     * @return a map representing the location state
     */
    Map getState();

}
