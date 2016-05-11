package naga.core.spi.platform.client.java;

import naga.core.json.WritableJsonObject;
import naga.core.json.listmap.MapJsonObject;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
final class SmartJsonObject extends MapJsonObject implements SmartJsonElement, WritableJsonObject {

    SmartJsonObject() {
    }

    SmartJsonObject(Map map) {
        super(map);
    }
}
