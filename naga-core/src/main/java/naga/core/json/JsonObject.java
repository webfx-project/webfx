package naga.core.json;

import naga.core.util.Booleans;
import naga.core.util.Numbers;
import naga.core.util.Objects;
import naga.core.util.Strings;

/**
 * @author Bruno Salmon
 */
public interface JsonObject extends JsonElement {

    /**
     * Return true if it is an array.
     */
    default boolean isArray() { return false; }

    /**
     * Return true if it is an object.
     */
    default boolean isObject() { return true; }

    /**
     * Test whether a given key has present.
     */
    default boolean has(String key) {
        return get(key) != null;
    }

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
    default <T> T get(String key) { return anyNativeToComposite(getNativeElement(key)); }


    /**
     * Return the element as a JsonObject. If the type is not an object, this can result in runtime errors.
     */
    default JsonObject getObject(String key) { return nativeToCompositeObject(getNativeElement(key)); }

    /**
     * Return the element as a JsonArray. If the type is not an array, this can result in runtime errors.
     */
    default JsonArray getArray(String key) { return nativeToCompositeArray(getNativeElement(key)); }

    default <T> T getScalar(String key) {
        return nativeToCompositeScalar(getNativeElement(key));
    }

    default <T> T getScalar(String key, T defaultValue) {
        return Objects.coalesce(getScalar(key), defaultValue);
    }

    /**
     * Return the element as a boolean. If the type is not a boolean, this can result in runtime errors.
     */
    default boolean getBoolean(String key) { return Booleans.booleanValue(getScalar(key)); }

    /**
     * Return the element as a boolean. If the type is not a boolean, this can result in runtime errors.
     */
    default boolean getBoolean(String key, boolean defaultValue) { return Booleans.booleanValue(getScalar(key, anyCompositeToNative(defaultValue))); }

    /**
     * Return the element as a String. If the type is not a String, this can result in runtime errors.
     */
    default String getString(String key) { return Strings.stringValue(getNativeElement(key)); }

    /**
     * Return the element as a String. If the type is not a String, this can result in runtime errors.
     */
    default String getString(String key, String defaultValue) { return Strings.stringValue(getScalar(key, defaultValue)); }

    /**
     * Return the element as a int. If the type is not a int, this can result in runtime errors.
     */
    default int getInt(String key) { return Numbers.intValue(getNativeElement(key)); }

    /**
     * Return the element as a int. If the type is not a int, this can result in runtime errors.
     */
    default int getInt(String key, int defaultValue) { return Numbers.intValue(getScalar(key, defaultValue)); }

    /**
     * Return the element as a long. If the type is not a long, this can result in runtime errors.
     */
    default long getLong(String key) { return Numbers.longValue(getNativeElement(key)); }

    /**
     * Return the element as a long. If the type is not a long, this can result in runtime errors.
     */
    default long getLong(String key, long defaultValue) { return Numbers.longValue(getScalar(key, defaultValue)); }

    /**
     * Return the element as a double. If the type is not a double, this can result in runtime errors.
     */
    default double getDouble(String key) { return Numbers.doubleValue(getNativeElement(key)); }

    /**
     * Return the element as a double. If the type is not a double, this can result in runtime errors.
     */
    default double getDouble(String key, double defaultValue) { return  Numbers.doubleValue(getScalar(key, defaultValue)); }
}
