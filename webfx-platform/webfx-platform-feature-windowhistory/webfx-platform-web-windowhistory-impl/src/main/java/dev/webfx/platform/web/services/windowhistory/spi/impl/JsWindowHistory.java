package dev.webfx.platform.web.services.windowhistory.spi.impl;

import dev.webfx.platform.shared.services.json.JsonObject;
import dev.webfx.platform.shared.util.async.Handler;
import dev.webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * Wrapper interface to the javascript window history.
 *
 * @author Bruno Salmon
 */
public interface JsWindowHistory {

    static JsWindowHistory get() {
        return SingleServiceProvider.getProvider(JsWindowHistory.class, () -> ServiceLoader.load(JsWindowHistory.class));
    }

    /**
     *
     * @return the number of entries in the current session history.
     */
    int length();

    /**
     * Allows to walk through history. The argument passed is the offset the current position. If you pass 0, the
     * current page will be updated. If the index goes beyond the history, nothing happens.
     * @param offset the offset to the current position.
     */
    void go(int offset);

    boolean supportsStates();

    /**
     *
     * @return the current history object.
     */
    JsonObject state();

    /**
     * Adds an element to the history.
     * @param stateObj
     * @param title
     * @param url
     */
    void pushState(JsonObject stateObj, String title, String url);

    /**
     * Updates the current element of history
     * @param stateObj
     * @param title
     * @param url
     */
    void replaceState(JsonObject stateObj, String title, String url);

    /**
     * An event handler for the popstate event on the window.
     * A popstate event is dispatched to the window every time the active history entry changes between two history
     * entries for the same document. If the history entry being activated was created by a call to history.pushState()
     * or was affected by a call to history.replaceState(), the popstate event's state property contains a copy of the
     * history entry's state object.
     * Note that just calling history.pushState() or history.replaceState() won't trigger a popstate event. The popstate
     * event is only triggered by doing a browser action such as clicking on the back button (or calling history.back()
     * in JavaScript). And the event is only triggered when the user navigates between two history entries for the same document.
     * Browsers tend to handle the popstate event differently on page load. Chrome (prior to v34) and Safari always emit
     * a popstate event on page load, but Firefox doesn't.
     * @param stateListener
     */
    void onPopState(Handler<JsonObject> stateListener);

    void onBeforeUnload(Function<JsonObject, String> listener);
}
