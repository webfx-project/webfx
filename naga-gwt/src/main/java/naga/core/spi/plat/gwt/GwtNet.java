package naga.core.spi.plat.gwt;

import naga.core.spi.json.JsonObject;
import naga.core.spi.plat.Net;

/**
 * @author Bruno Salmon
 */
class GwtNet implements Net {

    @Override
    public native GwtWebSocket createWebSocket(String url, JsonObject options) /*-{
        return new $wnd.SockJS(url, undefined, options);
    }-*/;

}
