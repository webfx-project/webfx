package webfx.platform.gwt.services.windowhistory;

import webfx.platform.gwt.services.windowhistory.jsinterop.Window;
import webfx.platform.gwt.services.json.GwtJsonObject;
import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.web.services.windowhistory.JsWindowHistory;
import webfx.platforms.core.util.async.Handler;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public class GwtJsWindowHistory implements JsWindowHistory {

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
