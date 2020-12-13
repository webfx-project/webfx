package dev.webfx.platform.client.services.windowhistory.spi;

import dev.webfx.platform.client.services.windowlocation.spi.PathStateLocation;

/**
 * A location answers two important (philosophical) questions:
 * Where am I?
 * How did I get here?
 * New locations are typically created each time the URL changes.
 *
 * A location object is conceptually similar to document.location in web browsers, with a few extra goodies.
 * Location objects have the following properties:
 *  - pathname      The pathname portion of the URL, without query string
 *  - search        The query string portion of the URL, including the ?
 *  - state         An object of data tied to this location
 *  - action        One of PUSH, REPLACE, or POP
 *  - key           A unique identifier for this location
 *
 * @author Bruno Salmon
 */
public interface BrowsingHistoryLocation extends PathStateLocation {

    /**
     * Returns an history event that describes the type of change that has been done to the URL to get there.
     *
     * @return an BrowsingHistoryEvent object
     */
    BrowsingHistoryEvent getEvent();

    /**
     * Returns a location key of the current URL. A location key is a unique string that identifies a location.
     * It is the one piece of data that most accurately answers the question "Where am I?".
     */
    String getLocationKey();

}
