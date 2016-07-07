package naga.platform.providers.vertx;

import io.vertx.core.json.Json;
import naga.commons.json.spi.ElementType;
import naga.commons.json.spi.JsonArray;
import naga.commons.json.spi.JsonObject;
import naga.commons.json.listmap.ListMapBasedJsonElement;
import naga.commons.json.listmap.MapBasedJsonObject;
import naga.commons.json.spi.WritableJsonArray;
import naga.commons.util.Numbers;

import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
interface VertxJsonElement extends ListMapBasedJsonElement {

    @Override
    default io.vertx.core.json.JsonObject parseNativeObject(String text) {
        return Json.decodeValue(text, io.vertx.core.json.JsonObject.class);
    }

    @Override
    default io.vertx.core.json.JsonArray parseNativeArray(String text) {
        return Json.decodeValue(text, io.vertx.core.json.JsonArray.class);
    }

    @Override
    default ElementType getNativeElementType(Object nativeElement) {
        if (nativeElement == null)
            return ElementType.NULL;
        if (nativeElement instanceof Map || nativeElement instanceof JsonObject || nativeElement instanceof io.vertx.core.json.JsonObject)
            return ElementType.OBJECT;
        if (nativeElement instanceof List || nativeElement instanceof JsonArray || nativeElement instanceof io.vertx.core.json.JsonArray)
            return ElementType.ARRAY;
        if (nativeElement instanceof Boolean)
            return ElementType.NUMBER;
        if (nativeElement instanceof String)
            return ElementType.STRING;
        if (Numbers.isNumber(nativeElement))
            return ElementType.NUMBER;
        return ElementType.UNDEFINED;
    }

    @Override
    default MapBasedJsonObject nativeToJavaJsonObject(Object nativeObject) {
        if (nativeObject == null || nativeObject instanceof MapBasedJsonObject)
            return (MapBasedJsonObject) nativeObject;
        if (nativeObject instanceof io.vertx.core.json.JsonObject)
            return new VertxJsonObject((io.vertx.core.json.JsonObject) nativeObject);
        return new VertxJsonObject((Map) nativeObject);
    }

    @Override
    default WritableJsonArray nativeToJavaJsonArray(Object nativeArray) {
        if (nativeArray == null || nativeArray instanceof WritableJsonArray)
            return (WritableJsonArray) nativeArray;
        if (nativeArray instanceof io.vertx.core.json.JsonArray)
            return new VertxJsonArray((io.vertx.core.json.JsonArray) nativeArray);
        return new VertxJsonArray((List) nativeArray);
    }
}

