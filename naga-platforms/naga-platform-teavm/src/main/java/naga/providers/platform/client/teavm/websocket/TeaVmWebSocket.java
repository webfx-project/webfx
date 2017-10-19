package naga.providers.platform.client.teavm.websocket;

import naga.platform.json.spi.JsonObject;
import naga.platform.client.websocket.spi.WebSocket;
import naga.platform.client.websocket.spi.WebSocketListener;
import naga.providers.platform.client.teavm.json.TeaVmJsonElement;
import naga.providers.platform.client.teavm.json.TeaVmJsonObject;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

/**
 * @author Bruno Salmon
 */
public final class TeaVmWebSocket implements WebSocket {

    private final SockJS sockJS;

    public TeaVmWebSocket(String url, JsonObject options) {
        sockJS = createSockJS(url, options == null ? null : ((TeaVmJsonObject) options).getNativeElement());
    }

    @Override
    public State getReadyState() {
        JSObject jsState = sockJS.getReadyState();
        if (jsState == SockJS_OPEN())
            return State.OPEN;
        if (jsState == SockJS_CONNECTING())
            return State.CONNECTING;
        if (jsState == SockJS_CLOSING())
            return State.CLOSING;
        if (jsState == SockJS_CLOSED())
            return State.CLOSED;
        throw new IllegalStateException("SockJS.readyState");
    }

    @Override
    public void send(String data) {
        sockJS.send(data);
    }

    @Override
    public void setListener(WebSocketListener listener) {
        sockJS.setOnopen(listener::onOpen);
        sockJS.setOnclose(jsReason -> listener.onClose(TeaVmJsonObject.create(jsReason)));
        sockJS.setOnmessage(jsMessage -> listener.onMessage(TeaVmJsonElement.js2String(TeaVmJsonElement.getJSValue(jsMessage, "data"))));
        sockJS.setOnerror(jsError -> listener.onError(TeaVmJsonElement.js2String(TeaVmJsonElement.getJSValue(jsError, "data"))));
    }

    @Override
    public void close() {
        sockJS.close();
    }

    @JSBody(params = {"url", "options"}, script = "return new SockJS(url, undefined, options);")
    private static native SockJS createSockJS(String url, JSObject options);

    @JSBody(params = {}, script = "return SockJS.OPEN;")
    private static native JSObject SockJS_OPEN();

    @JSBody(params = {}, script = "return SockJS.CONNECTING;")
    private static native JSObject SockJS_CONNECTING();

    @JSBody(params = {}, script = "return SockJS.CLOSING;")
    private static native JSObject SockJS_CLOSING();

    @JSBody(params = {}, script = "return SockJS.CLOSED;")
    private static native JSObject SockJS_CLOSED();
}
