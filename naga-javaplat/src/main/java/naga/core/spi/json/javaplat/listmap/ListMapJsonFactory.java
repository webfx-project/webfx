package naga.core.spi.json.javaplat.listmap;

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonElement;
import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class ListMapJsonFactory implements JsonFactory<List, Map<String, Object>> {

    @Override
    public JsonArray createArray(List nativeArray) {
        return nativeArray == null ? null : createNonNullArray(nativeArray);
    }

    protected abstract JsonArray createNonNullArray(List nativeArray);

    @Override
    public JsonObject createObject(Map<String, Object> nativeObject) {
        return nativeObject == null ? null : createNonNullObject(nativeObject);
    }

    protected abstract JsonObject createNonNullObject(Map<String, Object> nativeObject);

    @Override
    public <T extends JsonElement> T parse(String jsonString) {
        return ListMapUtil.wrap(parseNative(jsonString));
    }

    protected abstract Object parseNative(String jsonString);

}
