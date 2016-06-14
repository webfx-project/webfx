package naga.core.spi.platform.gwt;

import naga.core.json.JsonObject;
import naga.core.spi.platform.gwt.jsinterop.Window;
import naga.core.spi.platform.web.WindowHistory;
import naga.core.util.async.Handler;
import naga.core.util.function.Function;

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
