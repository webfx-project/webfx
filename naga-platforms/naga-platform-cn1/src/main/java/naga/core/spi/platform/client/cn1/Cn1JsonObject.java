package naga.core.spi.platform.client.cn1;

import naga.core.json.listmap.MapJsonObject;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
final class Cn1JsonObject extends MapJsonObject implements Cn1JsonElement {

    Cn1JsonObject() {
    }

    Cn1JsonObject(Map map) {
        super(map);
    }
}
