package naga.core.spi.json.java.smart;

import naga.core.spi.json.JsonObject;
import naga.core.composite.listmap.MapCompositeObject;
import net.minidev.json.JSONObject;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
final class SmartJsonObject extends MapCompositeObject implements SmartJsonElement, JsonObject {

    public SmartJsonObject() {
    }

    public SmartJsonObject(Map map) {
        super(map);
    }

    @Override
    public String toJsonString() {
        return JSONObject.toJSONString(getMap());
    }

    @Override
    public String toString() {
        return toJsonString();
    }
}
