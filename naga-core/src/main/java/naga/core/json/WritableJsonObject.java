package naga.core.json;

/**
 * @author Bruno Salmon
 */
public interface WritableJsonObject extends JsonObject {

    /**
     * Remove a given key and associated value from the object.
     */
    <V> V remove(String key);

    /**
     * Set a given key to the given element. Fluent API (return this).
     */
    WritableJsonObject setNativeElement(String key, Object element);

    /**
     * Set a given key to the given value.
     */
    default WritableJsonObject set(String key, Object value) {
        return setNativeElement(key, anyJavaToNative(value));
    }

    /**
     * Set a given key to the given object.
     */
    default WritableJsonObject setObject(String key, JsonObject object) { return setNativeElement(key, javaToNativeJsonObject(object)); }

    /**
     * Set a given key to the given array.
     */
    default WritableJsonObject setArray(String key, JsonArray array) { return setNativeElement(key, javaToNativeJsonArray(array)); }

    /**
     * Set a given key to the given element.
     */
    default WritableJsonObject setScalar(String key, Object scalar) { return setNativeElement(key, javaToNativeScalar(scalar)); }

    default WritableJsonObject set(String key, boolean value) { return setScalar(key, value); }

    default WritableJsonObject set(String key, int value) { return setScalar(key, value); }

    default WritableJsonObject set(String key, double value) { return setScalar(key, value); }

}
