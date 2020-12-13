package dev.webfx.platform.client.services.windowlocation.spi;

import dev.webfx.platform.shared.services.json.JsonObject;

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

public interface PathStateLocation extends PathLocation {

    /**
     * A location state is an arbitrary object of data associated with a particular location.
     * This is basically a way to tie extra state to a location that is not contained in the URL.
     * This type gets its name from the first argument to HTML5's pushState and replaceState methods.
     *
     * @return a json object representing the location state
     */
    JsonObject getState();

}
