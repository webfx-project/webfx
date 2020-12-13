package dev.webfx.platform.gwt.services.windowhistory.spi.impl;

import dev.webfx.platform.gwt.services.windowhistory.spi.impl.jsinterop.Window;
import dev.webfx.platform.gwt.services.json.spi.impl.GwtJsonObject;
import dev.webfx.platform.shared.services.json.JsonObject;
import dev.webfx.platform.web.services.windowhistory.spi.impl.JsWindowHistory;
import dev.webfx.platform.shared.util.async.Handler;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public final class GwtJsWindowHistory implements JsWindowHistory {

    public GwtJsWindowHistory() {
    }

    @Override
    public int length() {
        return Window.history.length;
    }

    @Override
    public void go(int offset) {
        Window.history.go(offset);
    }

    @Override
    public final native boolean supportsStates() /*-{
        return typeof window.history.pushState === 'function';
    }-*/;

    @Override
    public JsonObject state() {
        return Window.history.state.cast();
    }

    @Override
    public void pushState(JsonObject stateObj, String title, String url) {
        Window.history.pushState((GwtJsonObject) stateObj, title, url);
    }

    @Override
    public void replaceState(JsonObject stateObj, String title, String url) {
        Window.history.replaceState((GwtJsonObject) stateObj, title, url);
    }

    @Override
    public void onPopState(Handler<JsonObject> stateListener) {
        Window.onpopstate = event -> stateListener.handle((GwtJsonObject) event.state);
    }

    @Override
    public void onBeforeUnload(Function<JsonObject, String> listener) {
        Window.onbeforeunload = event -> listener.apply((GwtJsonObject) event);
    }

}
