package naga.core.spi.json.vertx;

import naga.core.spi.json.listmap.MapBasedJsonObject;
import naga.core.valuesobject.RawType;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
final class VertxJsonObject extends MapBasedJsonObject {

    private io.vertx.core.json.JsonObject vertxObject;

    VertxJsonObject() { // super constructor will call recreateEmptyNativeObject() to initialize the map
    }

    VertxJsonObject(io.vertx.core.json.JsonObject vertxObject) {
        this.vertxObject = vertxObject;
    }

    @Override
    public RawType getRawType(Object rawValue) {
        if (rawValue instanceof io.vertx.core.json.JsonObject)
            return RawType.RAW_VALUES_OBJECT;
        return super.getRawType(rawValue);
    }

    @Override
    public MapBasedJsonObject wrapValuesObject(Object rawObject) {
        if (rawObject instanceof io.vertx.core.json.JsonObject)
            return new VertxJsonObject((io.vertx.core.json.JsonObject) rawObject);
        return super.wrapValuesObject(rawObject);
    }

    @Override
    public Map<String, Object> getMap() {
        return vertxObject.getMap();
    }

    @Override
    public Object getNativeObject() {
        return vertxObject;
    }

    @Override
    protected void recreateEmptyNativeObject() {
        vertxObject = new io.vertx.core.json.JsonObject();
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

