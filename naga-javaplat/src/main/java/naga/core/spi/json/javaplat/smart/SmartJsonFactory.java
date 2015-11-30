package naga.core.spi.json.javaplat.smart;

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.JsonObject;
import net.minidev.json.JSONValue;

import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class SmartJsonFactory implements JsonFactory {
    @Override
    public JsonArray createArray() {
        return new SmartJsonArray();
    }

    @Override
    public JsonObject createObject() {
        return new SmartJsonObject();
    }

    @Override
    public <T> T parse(String jsonString) {
        Object value = JSONValue.parse(jsonString);
        if (value instanceof Map)
            return (T) new SmartJsonObject((Map<String, Object>) value);
        if (value instanceof List)
            return (T) new SmartJsonArray((List) value);
        return (T) value;
    }
}
