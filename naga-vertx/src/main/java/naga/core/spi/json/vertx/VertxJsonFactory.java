package naga.core.spi.json.vertx;

import io.vertx.core.json.Json;
import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class VertxJsonFactory implements JsonFactory {

    @Override
    public JsonArray createArray() {
        return VertxJsonArray.create();
    }

    @Override
    public JsonObject createObject() {
        return VertxJsonObject.create();
    }

    @Override
    public <T> T parse(String jsonString) {
        Object value = Json.decodeValue(jsonString, Object.class);
        if (value instanceof Map)
            return (T) VertxJsonObject.create(new io.vertx.core.json.JsonObject((Map<String, Object>) value));
        if (value instanceof List)
            return (T) VertxJsonArray.create(new io.vertx.core.json.JsonArray((List<Object>) value));
        return (T) value;
    }
}
