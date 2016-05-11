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
     * Set a given key to the given element.
     */
    void setNativeElement(String key, Object element);

    /**
     * Set a given key to the given value.
     */
    default void set(String key, Object value) {
        setNativeElement(key, anyJavaToNative(value));
    }

    /**
     * Set a given key to the given object.
     */
    default void setObject(String key, JsonObject object) { setNativeElement(key, javaToNativeJsonObject(object)); }

    /**
     * Set a given key to the given array.
     */
    default void setArray(String key, JsonArray array) { setNativeElement(key, javaToNativeJsonArray(array)); }

    /**
     * Set a given key to the given element.
     */
    default void setScalar(String key, Object scalar) { setNativeElement(key, javaToNativeScalar(scalar)); }

    default void set(String key, boolean value) { setScalar(key, value); }

    default void set(String key, int value) { setScalar(key, value); }

    default void set(String key, double value) { setScalar(key, value); }

}
