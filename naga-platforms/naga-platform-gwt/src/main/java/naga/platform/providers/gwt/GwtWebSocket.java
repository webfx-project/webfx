package naga.platform.providers.gwt;

import com.google.gwt.core.client.JavaScriptObject;
import naga.commons.websocket.spi.WebSocket;
import naga.commons.websocket.spi.WebSocketListener;

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
    public native void setListener(WebSocketListener listener) /*-{
        this.onopen =    listener.@naga.commons.websocket.spi.WebSocketListener::onOpen().bind(listener);
        this.onmessage = function(event) { listener.@naga.commons.websocket.spi.WebSocketListener::onMessage(Ljava/lang/String;)(event.data)};
        this.onerror =   function(event) { listener.@naga.commons.websocket.spi.WebSocketListener::onError(Ljava/lang/String;)(event.data)};
        this.onclose =   listener.@naga.commons.websocket.spi.WebSocketListener::onClose(Lnaga/commons/json/spi/JsonObject;).bind(listener);
    }-*/;

    @Override
    public native void close() /*-{ this.close(); }-*/;

    private native JavaScriptObject getSockJSState() /*-{ return this.readyState; }-*/;
    private static native JavaScriptObject OPEN() /*-{ return $wnd.SockJS.OPEN; }-*/;
    private static native JavaScriptObject CONNECTING() /*-{ return $wnd.SockJS.CONNECTING; }-*/;
    private static native JavaScriptObject CLOSING() /*-{ return $wnd.SockJS.CLOSING; }-*/;
    private static native JavaScriptObject CLOSED() /*-{ return $wnd.SockJS.CLOSED; }-*/;
}
