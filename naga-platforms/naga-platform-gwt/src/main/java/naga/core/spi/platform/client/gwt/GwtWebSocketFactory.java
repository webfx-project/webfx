package naga.core.spi.platform.client.gwt;

import naga.core.composite.CompositeObject;
import naga.core.spi.platform.client.WebSocketFactory;

/**
 * @author Bruno Salmon
 */
final class GwtWebSocketFactory implements WebSocketFactory {

    @Override
    public native GwtWebSocket createWebSocket(String url, CompositeObject options) /*-{
        return new $wnd.SockJS(url, undefined, options);
    }-*/;

}
