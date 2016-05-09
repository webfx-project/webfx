package naga.core.spi.json.java.smart;

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonObject;
import naga.core.composite.listmap.ListMapJsonFactory;
import net.minidev.json.JSONValue;

import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class SmartJsonFactory extends ListMapJsonFactory {

    @Override
    public SmartJsonArray createArray() {
        return new SmartJsonArray();
    }

    @Override
    protected JsonArray createNewArray(List nativeArray) {
        return new SmartJsonArray(nativeArray);
    }

    @Override
    public SmartJsonObject createObject() {
        return new SmartJsonObject();
    }

    @Override
    protected JsonObject createNewObject(Map<String, Object> nativeObject) {
        return new SmartJsonObject(nativeObject);
    }

    @Override
    public Object parseNative(String jsonString) {
        return JSONValue.parse(jsonString);
    }
}
