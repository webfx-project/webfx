package naga.core.spi.json.java.smart;

import naga.core.composite.WritableCompositeObject;
import naga.core.composite.listmap.MapCompositeObject;
import net.minidev.json.JSONObject;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class SmartJsonObject extends MapCompositeObject implements SmartJsonElement, WritableCompositeObject {

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
