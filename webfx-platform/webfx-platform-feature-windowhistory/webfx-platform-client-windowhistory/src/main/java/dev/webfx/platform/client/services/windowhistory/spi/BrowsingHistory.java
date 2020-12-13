package dev.webfx.platform.client.services.windowhistory.spi;

import dev.webfx.platform.client.services.windowlocation.spi.PathStateLocation;
import dev.webfx.platform.shared.util.async.Future;
import dev.webfx.platform.shared.util.async.Handler;
import dev.webfx.platform.shared.services.json.JsonObject;

import java.util.function.Function;

/**
 * Inspired from https://github.com/mjackson/history/blob/master/docs/Glossary.md#history
 *
 * @author Bruno Salmon
 */
public interface BrowsingHistory {

    /***************************************** Navigation management ***************************************************
     *
     * You can also use the history to programmatically change the current location using the following methods:
     * push(), replace(), go(), goBack(), goForward()
     *
     ******************************************************************************************************************/

    /**
     *
     * @return the current location
     */
    BrowsingHistoryLocation getCurrentLocation();

    /**
     * Push a new entry onto the history stack.
     * @param path A path string that represents a complete URL path. Path = Pathname + Search + Hash
     */
    default void push(String path) { push(path, null); }

    /**
     * Push a new entry onto the history stack.
     * @param path A path string that represents a complete URL path. Path = Pathname + Search + Hash
     * @param state A state object associated with this history entry
     */
    default void push(String path, JsonObject state) { push(createPathStateLocation(path, state)); }

    /**
     * Push a new entry onto the history stack.
     * @param location A location descriptor object that uses a combination of pathname, search, hash, and state properties
     */
    void push(PathStateLocation location);

    /**
     * Replace the current entry on the history stack.
     * @param path A path string that represents a complete URL path. Path = Pathname + Search + Hash
     */
    default void replace(String path) { replace(path, null); }

    /**
     * Replace the current entry on the history stack.
     * @param path A path string that represents a complete URL path. Path = Pathname + Search + Hash
     * @param state A state object associated with this history entry
     */
    default void replace(String path, JsonObject state) { replace(createPathStateLocation(path, state)); }

    /**
     * Replace the current entry on the history stack.
     * @param location A location descriptor object that uses a combination of pathname, search, hash, and state properties
     */
    void replace(PathStateLocation location);

    void transitionTo(BrowsingHistoryLocation location);

    /**
     * Differential navigation method.
     * @param offset the differential step from the current location.
     */
    void go(int offset);

    /**
     * Go back to the previous history entry. Same as go(-1).
     */
    default void goBack() { go(-1); }

    /**
     * Go forward to the next history entry. Same as go(1).
     */
    default void goForward() { go(1); }


    /**************************************** Transitions management ***************************************************
     *
     *  A transition is the process of notifying listeners when the location changes.
     *  It is not an API; rather, it is a concept. Transitions may be interrupted by transition hooks
     *  Note: A transition does not refer to the exact moment the URL actually changes. For example, in web browsers
     *  the user may click the back button or otherwise directly manipulate the URL by typing into the address bar.
     *  This is not a transition, but a history object will start a transition as a result of the URL changing.
     *
     ******************************************************************************************************************/

    /**
     * Listen for changes to the current location.
     */
    void listen(Handler<BrowsingHistoryLocation> listener);

    /**
     * Sometimes you may want to prevent the user from going to a different page.
     * For example, if they are halfway finished filling out a long form, and they click the back button, you may want to
     * prompt them to confirm they actually want to leave the page before they lose the information they've already entered.
     *
     * @param transitionHook a hook function that takes the location and returns a boolean that tells if the transition
     *                       is accepted (true) or prevented (false)
     */
    default void listenBefore(Function<BrowsingHistoryLocation, Boolean> transitionHook) { listenBeforeAsync(location -> Future.succeededFuture(transitionHook.apply(location))); }

    /**
     * If your transition hook needs to execute asynchronously, you can return a future boolean.
     *
     * Note: If you do provide a callback argument, the transition will not proceed until you call it (i.e. listeners will
     * not be notified of the new location)! If your app is slow for some reason, this could lead to a non-responsive UI.
     *
     * @param transitionHook a hook asynchronous function that takes the location and returns a future boolean that will
     *                       tell if the transition  accepted (true) or prevented (false)
     */
    void listenBeforeAsync(Function<BrowsingHistoryLocation, Future<Boolean>> transitionHook);

    /**
     * A before unload hook is a function that is used in web browsers to prevent the user from navigating away from
     * the page or closing the window.
     *      history.listenBeforeUnload(() -> 'Are you sure you want to leave this page?');
     * Note that because of the nature of the beforeunload event all hooks must return synchronously.
     * BrowsingHistory runs all hooks in the order they were registered and displays the first message that is returned.
     *
     * @param transitionHook
     */
    void listenBeforeUnload(Function<BrowsingHistoryLocation, Boolean> transitionHook);

    /********************************************* Creating URLs *******************************************************
     *
     * Additionally, history objects can be used to create URL paths and/or hrefs for <a> tags that link to various
     * places in your app. This is useful when using hash history to prefix URLs with a # or when using query support
     * to automatically build query strings.
     *      String href = history.createHref('/the/path')
     *
     ******************************************************************************************************************/

    String getPath(PathStateLocation location); // Path = Pathname + Search + Hash;

    PathStateLocation createPathStateLocation(String path, JsonObject state);

    BrowsingHistoryLocation createHistoryLocation(String path, JsonObject state);

}
