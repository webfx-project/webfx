package naga.providers.platform.server.vertx.json;

import io.vertx.core.json.JsonObject;
import naga.platform.json.spi.WritableJsonObject;
import naga.platform.json.listmap.MapBasedJsonObject;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class VertxJsonObject extends MapBasedJsonObject implements VertxJsonElement, WritableJsonObject {

    private JsonObject vertxObject;

    public VertxJsonObject() { // super constructor will call recreateEmptyNativeObject() to initialize the map
    }

    VertxJsonObject(Map<String, Object> map) {
        setMap(map);
    }

    VertxJsonObject(JsonObject vertxObject) {
        this.vertxObject = vertxObject;
    }

    @Override
    public Map<String, Object> getMap() {
        return vertxObject.getMap();
    }

    @Override
    protected void setMap(Map<String, Object> map) {
        vertxObject = new JsonObject(map);
    }

    @Override
    public JsonObject getNativeElement() {
        return vertxObject;
    }

    @Override
    protected void deepCopyNativeObject() {
        vertxObject = vertxObject.copy();
    }

    @Override
    public String toJsonString() {
        return vertxObject.encode();
    }
}

