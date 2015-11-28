package naga.core.spi.sock.gwt;

import naga.core.spi.json.JsonObject;
import naga.core.spi.sock.WebSocketFactory;

/**
 * @author Bruno Salmon
 */
public final class GwtWebSocketFactory implements WebSocketFactory {

    @Override
    public native GwtWebSocket createWebSocket(String url, JsonObject options) /*-{
        return new $wnd.SockJS(url, undefined, options);
    }-*/;

}
