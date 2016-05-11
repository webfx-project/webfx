package naga.core.spi.json.cn1;

import naga.core.composite.listmap.MapCompositeObject;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class Cn1JsonObject extends MapCompositeObject implements Cn1JsonElement {

    public Cn1JsonObject() {
    }

    public Cn1JsonObject(Map map) {
        super(map);
    }
}
