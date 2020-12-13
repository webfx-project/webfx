package dev.webfx.platform.shared.services.json.spi.impl.listmap;

import dev.webfx.platform.shared.services.json.*;
import dev.webfx.platform.shared.util.Numbers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public interface ListMapBasedJsonElement extends WritableJsonElement {

    @Override
    default Object createNativeObject() {
        return new LinkedHashMap<>();
    }

    @Override
    default Object createNativeArray() {
        return new ArrayList();
    }

    @Override
    default WritableJsonObject nativeToJavaJsonObject(Object nativeObject) {
        if (nativeObject == null || nativeObject instanceof WritableJsonObject)
            return (WritableJsonObject) nativeObject;
        return new MapJsonObject((Map) nativeObject);
    }

    @Override
    default WritableJsonArray nativeToJavaJsonArray(Object nativeArray) {
        if (nativeArray == null || nativeArray instanceof WritableJsonArray)
            return (WritableJsonArray) nativeArray;
        return new ListJsonArray((List) nativeArray);
    }

    @Override
    default ElementType getNativeElementType(Object nativeElement) {
        if (nativeElement == null)
            return ElementType.NULL;
        if (nativeElement instanceof Map || nativeElement instanceof JsonObject)
            return ElementType.OBJECT;
        if (nativeElement instanceof List || nativeElement instanceof JsonArray)
            return ElementType.ARRAY;
        if (nativeElement instanceof Boolean)
            return ElementType.NUMBER;
        if (nativeElement instanceof String)
            return ElementType.STRING;
        if (Numbers.isNumber(nativeElement))
            return ElementType.NUMBER;
        return ElementType.UNDEFINED;
    }
}
