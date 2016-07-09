package naga.providers.platform.client.gwt.websocket;

import naga.platform.json.spi.JsonObject;
import naga.platform.client.websocket.WebSocketFactory;

/**
 * @author Bruno Salmon
 */
public final class GwtWebSocketFactory implements WebSocketFactory {

    @Override
    public native GwtWebSocket createWebSocket(String url, JsonObject options) /*-{
        return new $wnd.SockJS(url, undefined, options);
    }-*/;

}
