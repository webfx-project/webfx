package naga.core.spi.json.vertx;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import naga.core.composite.CompositeArray;
import naga.core.composite.CompositeObject;
import naga.core.composite.ElementType;
import naga.core.composite.WritableCompositeArray;
import naga.core.composite.listmap.ListMapCompositeElement;
import naga.core.composite.listmap.MapBasedCompositeObject;
import naga.core.util.Numbers;

import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public interface VertxJsonElement extends ListMapCompositeElement {

    @Override
    default JsonObject parseNativeObject(String text) {
        return Json.decodeValue(text, JsonObject.class);
    }

    @Override
    default JsonArray parseNativeArray(String text) {
        return Json.decodeValue(text, JsonArray.class);
    }

    @Override
    default ElementType getNativeElementType(Object nativeElement) {
        if (nativeElement instanceof Map || nativeElement instanceof CompositeObject || nativeElement instanceof JsonObject)
            return ElementType.OBJECT;
        if (nativeElement instanceof List || nativeElement instanceof CompositeArray || nativeElement instanceof JsonArray)
            return ElementType.ARRAY;
        if (nativeElement instanceof Boolean)
            return ElementType.NUMBER;
        if (nativeElement instanceof String)
            return ElementType.STRING;
        if (Numbers.isNumber(nativeElement))
            return ElementType.NUMBER;
        return ElementType.UNKNOWN;
    }

    @Override
    default MapBasedCompositeObject nativeToCompositeObject(Object nativeObject) {
        if (nativeObject == null || nativeObject instanceof MapBasedCompositeObject)
            return (MapBasedCompositeObject) nativeObject;
        if (nativeObject instanceof JsonObject)
            return new VertxJsonObject((JsonObject) nativeObject);
        return new VertxJsonObject((Map) nativeObject);
    }

    @Override
    default WritableCompositeArray nativeToCompositeArray(Object nativeArray) {
        if (nativeArray == null || nativeArray instanceof WritableCompositeArray)
            return (WritableCompositeArray) nativeArray;
        if (nativeArray instanceof JsonArray)
            return new VertxJsonArray((JsonArray) nativeArray);
        return new VertxJsonArray((List) nativeArray);
    }
}

