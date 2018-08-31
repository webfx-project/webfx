package webfx.providers.platform.client.gwt.services.websocket;

import webfx.platform.services.json.JsonObject;
import webfx.platform.services.websocket.spi.WebSocketServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class GwtWebSocketServiceProviderImpl implements WebSocketServiceProvider {

    @Override
    public native GwtWebSocket createWebSocket(String url, JsonObject options) /*-{
        // Code for the case the "sockjs-quickstart.js" script was included in index.html
        var sockJS = $wnd.quickStartSockJS;
        if (sockJS) { // Yes the script was included, so a sockJS has already been started
            $wnd.quickStartSockJS = null; // Used only once on first call
            if ($wnd.quickStartSockJSUrl === url && !options) // Checking the parameters are the same
                return sockJS; // Yes! The connection is probably already established (we gained a few seconds!)
            sockJS.close(); // The started connection is not the requested one! We close it.
        }
        // Otherwise we create a brand new SockJS connection
        return new $wnd.SockJS(url, undefined, options);
    }-*/;

}
