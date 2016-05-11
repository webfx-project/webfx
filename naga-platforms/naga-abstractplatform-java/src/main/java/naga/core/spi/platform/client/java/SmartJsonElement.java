package naga.core.spi.platform.client.java;

import naga.core.json.WritableJsonArray;
import naga.core.json.WritableJsonObject;
import naga.core.json.listmap.ListMapJsonElement;
import net.minidev.json.JSONValue;

import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
interface SmartJsonElement extends ListMapJsonElement {

    @Override
    default Object parseNativeObject(String text) {
        return JSONValue.parse(text);
    }

    @Override
    default Object parseNativeArray(String text) {
        return JSONValue.parse(text);
    }

    @Override
    default WritableJsonObject nativeToCompositeObject(Object nativeObject) {
        if (nativeObject == null || nativeObject instanceof WritableJsonObject)
            return (WritableJsonObject) nativeObject;
        return new SmartJsonObject((Map) nativeObject);
    }

    @Override
    default WritableJsonArray nativeToCompositeArray(Object nativeArray) {
        if (nativeArray == null || nativeArray instanceof WritableJsonArray)
            return (WritableJsonArray) nativeArray;
        return new SmartJsonArray((List) nativeArray);
    }

}
