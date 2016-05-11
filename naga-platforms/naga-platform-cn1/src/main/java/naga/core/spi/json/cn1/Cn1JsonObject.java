package naga.core.spi.json.cn1;

import naga.core.json.listmap.MapJsonObject;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class Cn1JsonObject extends MapJsonObject implements Cn1JsonElement {

    public Cn1JsonObject() {
    }

    public Cn1JsonObject(Map map) {
        super(map);
    }
}
