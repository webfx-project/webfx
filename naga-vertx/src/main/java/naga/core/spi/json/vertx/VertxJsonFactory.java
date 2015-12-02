package naga.core.spi.json.vertx;

import io.vertx.core.json.Json;
import naga.core.spi.json.JsonElement;
import naga.core.spi.json.JsonFactory;

import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class VertxJsonFactory implements JsonFactory<io.vertx.core.json.JsonArray, io.vertx.core.json.JsonObject> {

    @Override
    public VertxJsonArray createArray() {
        return VertxJsonArray.create();
    }

    @Override
    public VertxJsonArray createArray(io.vertx.core.json.JsonArray nativeArray) {
        return VertxJsonArray.create(nativeArray);
    }

    @Override
    public VertxJsonObject createObject() {
        return VertxJsonObject.create();
    }

    @Override
    public VertxJsonObject createObject(io.vertx.core.json.JsonObject nativeObject) {
        return VertxJsonObject.create(nativeObject);
    }

    @Override
    public <T extends JsonElement> T parse(String jsonString) {
        Object value = Json.decodeValue(jsonString, Object.class);
        if (value instanceof Map)
            return (T) VertxJsonObject.create(new io.vertx.core.json.JsonObject((Map<String, Object>) value));
        if (value instanceof List)
            return (T) VertxJsonArray.create(new io.vertx.core.json.JsonArray((List<Object>) value));
        return (T) value;
    }
}
