package naga.core.spi.platform.client.gwt;

import naga.core.json.JsonObject;
import naga.core.client.bus.WebSocketFactory;

/**
 * @author Bruno Salmon
 */
final class GwtWebSocketFactory implements WebSocketFactory {

    @Override
    public native GwtWebSocket createWebSocket(String url, JsonObject options) /*-{
        return new $wnd.SockJS(url, undefined, options);
    }-*/;

}
