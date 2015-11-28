package naga.core.spi.sock.teavm;

import naga.core.spi.json.JsonObject;
import naga.core.spi.json.teavm.JSUtil;
import naga.core.spi.json.teavm.TeaVmJsonObject;
import naga.core.spi.sock.WebSocket;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

/**
 * @author Bruno Salmon
 */
final class TeaVmWebSocket implements WebSocket {

    private final SockJS sockJS;

    public TeaVmWebSocket(String url, JsonObject options) {
        sockJS = createSockJS(url, options == null ? null : ((TeaVmJsonObject) options).getJsValue());
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
    public void setListen(WebSocketHandler handler) {
        sockJS.setOnopen(handler::onOpen);
        sockJS.setOnclose(jsReason -> handler.onClose(TeaVmJsonObject.create(jsReason)));
        sockJS.setOnmessage(jsMessage -> handler.onMessage(JSUtil.js2String(JSUtil.getJSValue(jsMessage, "data"))));
        sockJS.setOnerror(jsError -> handler.onError(JSUtil.js2String(JSUtil.getJSValue(jsError, "data"))));
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
