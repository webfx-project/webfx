package naga.core.spi.json.vertx;

import io.vertx.core.json.JsonObject;
import naga.core.composite.listmap.MapBasedCompositeObject;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
final class VertxJsonObject extends MapBasedCompositeObject implements VertxJsonElement, naga.core.spi.json.JsonObject {

    private JsonObject vertxObject;

    VertxJsonObject() { // super constructor will call recreateEmptyNativeObject() to initialize the map
    }

    VertxJsonObject(Map<String, Object> map) {
        super(map);
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

