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
        return new VertxJsonArray();
    }

    @Override
    public VertxJsonArray createArray(io.vertx.core.json.JsonArray nativeArray) {
        return nativeArray == null ? null : new VertxJsonArray(nativeArray);
    }

    @Override
    public VertxJsonObject createObject() {
        return new VertxJsonObject();
    }

    @Override
    public VertxJsonObject createObject(io.vertx.core.json.JsonObject nativeObject) {
        return nativeObject == null ? null : new VertxJsonObject(nativeObject);
    }

    @Override
    public <T extends JsonElement> T parse(String jsonString) {
        Object value = Json.decodeValue(jsonString, Object.class);
        if (value instanceof Map) {
            io.vertx.core.json.JsonObject vjo = new io.vertx.core.json.JsonObject((Map<String, Object>) value);
            return (T) (vjo == null ? null : new VertxJsonObject(vjo));
        }
        if (value instanceof List) {
            io.vertx.core.json.JsonArray vja = new io.vertx.core.json.JsonArray((List<Object>) value);
            return (T) (vja == null ? null : new VertxJsonArray(vja));
        }
        return (T) value;
    }
}
