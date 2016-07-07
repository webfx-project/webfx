package naga.platform.providers.gwt;

import naga.commons.json.spi.JsonObject;
import naga.commons.websocket.spi.WebSocketFactory;

/**
 * @author Bruno Salmon
 */
final class GwtWebSocketFactory implements WebSocketFactory {

    @Override
    public native GwtWebSocket createWebSocket(String url, JsonObject options) /*-{
        return new $wnd.SockJS(url, undefined, options);
    }-*/;

}
