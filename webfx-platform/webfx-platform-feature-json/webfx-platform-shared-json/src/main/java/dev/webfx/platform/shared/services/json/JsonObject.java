package dev.webfx.platform.shared.services.json;

import dev.webfx.platform.shared.util.noreflect.KeyObject;

/**
 * @author Bruno Salmon
 */
public interface JsonObject extends KeyObject, JsonElement {

    /**
     * Return true if it is an array.
     */
    default boolean isArray() { return false; }

    /**
     * Return true if it is an object.
     */
    default boolean isObject() { return true; }

    /**
     * All keys of the object.
     */
    JsonArray keys();

    /**
     * Return the element as it is stored (unwrapped) in the underlying structure (so either a value or an unwrapped object/array).
     */
    Object getNativeElement(String key);

    /**
     * Return the element as a value or wrapped object/array.
     */
    default <T> T get(String key) { return anyNativeToJava(getNativeElement(key)); }

    /**
     * Return the element as a JsonObject. If the type is not an object, this can result in runtime errors.
     */
    default JsonObject getObject(String key) { return nativeToJavaJsonObject(getNativeElement(key)); }

    /**
     * Return the element as a JsonArray. If the type is not an array, this can result in runtime errors.
     */
    default JsonArray getArray(String key) { return nativeToJavaJsonArray(getNativeElement(key)); }

    default <T> T getScalar(String key) {
        return nativeToJavaScalar(getNativeElement(key));
    }
}
