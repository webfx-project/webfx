package naga.core.spi.plat;

import naga.core.spi.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public class Cn1Net implements Net {

    @Override
    public WebSocket createWebSocket(String url, JsonObject options) {
        return null;
    }
}
