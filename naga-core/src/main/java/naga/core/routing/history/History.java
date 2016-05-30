package naga.core.routing.history;

import naga.core.json.JsonObject;
import naga.core.util.async.Future;
import naga.core.util.async.Handler;
import naga.core.util.function.Function;

/**
 * Inspired from https://github.com/mjackson/history/blob/master/docs/Glossary.md#history
 *
 * @author Bruno Salmon
 */
public interface History {

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
    HistoryLocation getCurrentLocation();

    /**
     * Push a new entry onto the history stack.
     * @param location A location descriptor object that uses a combination of pathname, search, hash, and state properties
     */
    void push(HistoryLocationDescriptor location);

    /**
     * Push a new entry onto the history stack.
     * @param path A path string that represents a complete URL path. Path = Pathname + Search + Hash
     */
    void push(String path);

    /**
     * Replace the current entry on the history stack.
     * @param location A location descriptor object that uses a combination of pathname, search, hash, and state properties
     */
    void replace(HistoryLocationDescriptor location);

    /**
     * Replace the current entry on the history stack.
     * @param path A path string that represents a complete URL path. Path = Pathname + Search + Hash
     */
    void replace(String path);

    void transitionTo(HistoryLocation location);

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
    void listen(Handler<HistoryLocation> listener);

    /**
     * Sometimes you may want to prevent the user from going to a different page.
     * For example, if they are halfway finished filling out a long form, and they click the back button, you may want to
     * prompt them to confirm they actually want to leave the page before they lose the information they've already entered.
     *
     * @param transitionHook a hook function that takes the location and returns a boolean that tells if the transition
     *                       is accepted (true) or prevented (false)
     */
    void listenBefore(Function<HistoryLocation, Boolean> transitionHook);

    /**
     * If your transition hook needs to execute asynchronously, you can return a future boolean.
     *
     * Note: If you do provide a callback argument, the transition will not proceed until you call it (i.e. listeners will
     * not be notified of the new location)! If your app is slow for some reason, this could lead to a non-responsive UI.
     *
     * @param transitionHook a hook asynchronous function that takes the location and returns a future boolean that will
     *                       tell if the transition  accepted (true) or prevented (false)
     */
    void listenBeforeAsync(Function<HistoryLocation, Future<Boolean>> transitionHook);

    /**
     * A before unload hook is a function that is used in web browsers to prevent the user from navigating away from
     * the page or closing the window.
     *      history.listenBeforeUnload(() -> 'Are you sure you want to leave this page?');
     * Note that because of the nature of the beforeunload event all hooks must return synchronously.
     * History runs all hooks in the order they were registered and displays the first message that is returned.
     *
     * @param transitionHook
     */
    void listenBeforeUnload(Function<HistoryLocation, Boolean> transitionHook);

    /********************************************* Creating URLs *******************************************************
     *
     * Additionally, history objects can be used to create URL paths and/or hrefs for <a> tags that link to various
     * places in your app. This is useful when using hash history to prefix URLs with a # or when using query support
     * to automatically build query strings.
     *      String href = history.createHref('/the/path')
     *
     ******************************************************************************************************************/

    String createHref(HistoryLocationDescriptor location);

    String createPath(HistoryLocationDescriptor location); // Path = Pathname + Search + Hash;

    HistoryLocationDescriptor createLocationDescriptor(String path, JsonObject state);

    HistoryLocation createLocation(String path, JsonObject state);

    /*

    int historyLength();

    History createHistory();

    History createHashHistory();

    History createMemoryHistory();

    */

}
