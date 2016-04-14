package naga.core.spi.json.vertx;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import naga.core.spi.json.listmap.ListMapBasedJsonFactory;

/**
 * @author Bruno Salmon
 */
public final class VertxJsonFactory extends ListMapBasedJsonFactory<JsonArray, JsonObject> {

    @Override
    public VertxJsonArray createArray() {
        return new VertxJsonArray();
    }

    @Override
    protected VertxJsonArray createNewArray(JsonArray vertxArray) {
        return new VertxJsonArray(vertxArray);
    }

    @Override
    public VertxJsonObject createObject() {
        return new VertxJsonObject();
    }

    @Override
    protected VertxJsonObject createNewObject(JsonObject vertxObject) {
        return new VertxJsonObject(vertxObject);
    }

    @Override
    protected Object parseNative(String jsonString) {
        return Json.decodeValue(jsonString, Object.class);
    }
}
