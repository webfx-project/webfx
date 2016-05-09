package naga.core.spi.json.java.jackson;

import naga.core.composite.WritableCompositeArray;
import naga.core.composite.WritableCompositeObject;
import naga.core.spi.json.listmap.ListMapCompositeElement;
import net.minidev.json.JSONValue;

import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public interface JacksonJsonElement extends ListMapCompositeElement {

    @Override
    default Object parseNativeObject(String text) {
        return JSONValue.parse(text);
    }

    @Override
    default Object parseNativeArray(String text) {
        return JSONValue.parse(text);
    }

    @Override
    default WritableCompositeObject nativeToCompositeObject(Object nativeObject) {
        if (nativeObject == null || nativeObject instanceof WritableCompositeObject)
            return (WritableCompositeObject) nativeObject;
        return new JacksonJsonObject((Map) nativeObject);
    }

    @Override
    default WritableCompositeArray nativeToCompositeArray(Object nativeArray) {
        if (nativeArray == null || nativeArray instanceof WritableCompositeArray)
            return (WritableCompositeArray) nativeArray;
        return new JacksonJsonArray((List) nativeArray);
    }
}
