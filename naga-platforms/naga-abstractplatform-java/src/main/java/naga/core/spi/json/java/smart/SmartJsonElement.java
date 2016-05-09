package naga.core.spi.json.java.smart;

import naga.core.composite.WritableCompositeArray;
import naga.core.composite.WritableCompositeObject;
import naga.core.spi.json.listmap.ListMapCompositeElement;
import net.minidev.json.JSONValue;

import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public interface SmartJsonElement extends ListMapCompositeElement {

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
        return new SmartJsonObject((Map) nativeObject);
    }

    @Override
    default WritableCompositeArray nativeToCompositeArray(Object nativeArray) {
        if (nativeArray == null || nativeArray instanceof WritableCompositeArray)
            return (WritableCompositeArray) nativeArray;
        return new SmartJsonArray((List) nativeArray);
    }

}
