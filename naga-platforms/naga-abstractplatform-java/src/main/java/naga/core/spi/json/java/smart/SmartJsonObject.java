package naga.core.spi.json.java.smart;

import naga.core.json.WritableJsonObject;
import naga.core.json.listmap.MapJsonObject;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class SmartJsonObject extends MapJsonObject implements SmartJsonElement, WritableJsonObject {

    public SmartJsonObject() {
    }

    public SmartJsonObject(Map map) {
        super(map);
    }
}
