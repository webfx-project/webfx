package naga.core.spi.sock.gwt;

import com.google.gwt.core.client.JavaScriptObject;
import naga.core.spi.sock.WebSocket;

/**
 * @author Bruno Salmon
 */
final class GwtWebSocket extends JavaScriptObject implements WebSocket {

    protected GwtWebSocket() {}

    @Override
    public State getReadyState() {
        JavaScriptObject jsState = getSockJSState();
        if (jsState == OPEN())
            return State.OPEN;
        if (jsState == CONNECTING())
            return State.CONNECTING;
        if (jsState == CLOSING())
            return State.CLOSING;
        if (jsState == CLOSED())
            return State.CLOSED;
        throw new IllegalStateException("SockJS.readyState");
    }

    @Override
    public native void send(String data) /*-{ this.send(data); }-*/;

    @Override
    public native void setListen(WebSocketHandler handler) /*-{
        this.onopen =    handler.@naga.core.spi.plat.WebSocket.WebSocketHandler::onOpen().bind(handler);
        this.onmessage = function(event) { handler.@naga.core.spi.plat.WebSocket.WebSocketHandler::onMessage(Ljava/lang/String;)(event.data)};
        this.onerror =   function(event) { handler.@naga.core.spi.plat.WebSocket.WebSocketHandler::onError(Ljava/lang/String;)(event.data)};
        this.onclose =   handler.@naga.core.spi.plat.WebSocket.WebSocketHandler::onClose(Lnaga/core/spi/json/JsonObject;).bind(handler);
    }-*/;

    @Override
    public native void close() /*-{ this.close(); }-*/;

    private native JavaScriptObject getSockJSState() /*-{ return this.readyState; }-*/;
    private static native JavaScriptObject OPEN() /*-{ return $wnd.SockJS.OPEN; }-*/;
    private static native JavaScriptObject CONNECTING() /*-{ return $wnd.SockJS.CONNECTING; }-*/;
    private static native JavaScriptObject CLOSING() /*-{ return $wnd.SockJS.CLOSING; }-*/;
    private static native JavaScriptObject CLOSED() /*-{ return $wnd.SockJS.CLOSED; }-*/;
}
