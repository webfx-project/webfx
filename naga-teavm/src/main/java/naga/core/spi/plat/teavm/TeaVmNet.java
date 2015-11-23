package naga.core.spi.plat.teavm;

import naga.core.spi.json.JsonObject;
import naga.core.spi.plat.Net;
import naga.core.spi.plat.WebSocket;

/**
 * @author Bruno Salmon
 */
class TeaVmNet implements Net {
    @Override
    public WebSocket createWebSocket(String url, JsonObject options) {
        return new TeaVmWebSocket(url, options);
    }
}
