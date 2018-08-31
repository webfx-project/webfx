package webfx.providers.platform.client.gwt.url.history;

import webfx.platform.services.json.JsonObject;
import webfx.providers.platform.abstr.web.WindowHistory;
import webfx.providers.platform.client.gwt.jsinterop.Window;
import webfx.util.async.Handler;
import webfx.util.function.Function;
import webfx.providers.platform.client.gwt.services.json.GwtJsonObject;

/**
 * @author Bruno Salmon
 */
public class GwtWindowHistory implements WindowHistory {

    public static GwtWindowHistory SINGLETON = new GwtWindowHistory();

    private GwtWindowHistory() {
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
