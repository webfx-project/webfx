package naga.core.json.listmap;

import naga.core.json.JsonArray;
import naga.core.json.JsonObject;
import naga.core.json.ElementType;
import naga.core.json.WritableJsonElement;
import naga.core.util.Numbers;

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
    default Object parseNativeObject(String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Object parseNativeArray(String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    default ElementType getNativeElementType(Object nativeElement) {
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
        return ElementType.UNKNOWN;
    }
}
