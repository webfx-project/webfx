package naga.core.spi.json.javaplat.smart;

import naga.core.spi.json.javaplat.listmap.MapJsonObject;
import net.minidev.json.JSONObject;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
final class SmartJsonObject extends MapJsonObject {

    public SmartJsonObject() {
    }

    public SmartJsonObject(Map map) {
        super(map);
    }

    @Override
    public String toJsonString() {
        return JSONObject.toJSONString(getMap());
    }
}
