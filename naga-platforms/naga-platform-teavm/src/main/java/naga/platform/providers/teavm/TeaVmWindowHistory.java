package naga.platform.providers.teavm;

import naga.commons.json.spi.JsonObject;
import naga.platform.providers.abstr.web.WindowHistory;
import naga.commons.util.async.Handler;
import naga.commons.util.function.Function;
import org.teavm.jso.JSBody;
import org.teavm.jso.browser.History;
import org.teavm.jso.browser.Window;

/**
 * @author Bruno Salmon
 */
public class TeaVmWindowHistory implements WindowHistory {

    private final History jsoHistory;

    private TeaVmWindowHistory(History jsoHistory) {
        this.jsoHistory = jsoHistory;
    }

    public static TeaVmWindowHistory current() {
        return new TeaVmWindowHistory(History.current());
    }

    @Override
    public int length() {
        return jsoHistory.getLength();
    }

    @Override
    public void go(int offset) {
        jsoHistory.go(offset);
    }

    @Override
    public boolean supportsStates() {
        return hasWindowHistoryPushState();
    }

    @JSBody(params = {}, script = "return typeof window.history.pushState === 'function';")
    private static native boolean hasWindowHistoryPushState();

    @Override
    public JsonObject state() {
        return TeaVmJsonObject.create(jsoHistory.getState());
    }

    @Override
    public void pushState(JsonObject stateObj, String title, String url) {
        jsoHistory.pushState(((TeaVmJsonObject) stateObj).getNativeElement(), title, url);
    }

    @Override
    public void replaceState(JsonObject stateObj, String title, String url) {
        jsoHistory.replaceState(((TeaVmJsonObject) stateObj).getNativeElement(), title, url);
    }

    @Override
    public void onPopState(Handler<JsonObject> stateListener) {
        Window.current().addEventListener("popstate", evt -> stateListener.handle(TeaVmJsonObject.create(evt).getObject("state")));
    }

    @Override
    public void onBeforeUnload(Function<JsonObject, String> listener) {
        Window.current().listenBeforeOnload(evt -> listener.apply(TeaVmJsonObject.create(evt)));
    }
}
