package naga.core.valuesobject;

import naga.core.util.Booleans;
import naga.core.util.Numbers;
import naga.core.util.Objects;
import naga.core.util.Strings;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public interface ValuesObject extends ValuesElement {

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
    Collection<String> keys();

    /**
     * Return the element as it is stored (unwrapped) in the underlying structure (so either a value or an unwrapped object/array).
     */
    Object getRaw(String key);

    /**
     * Return the element as a value or wrapped object/array.
     */
    default <T> T get(String key) { return wrapAny(getRaw(key)); }


    /**
     * Return the element as a ValuesObject. If the type is not an object, this can result in runtime errors.
     */
    default <T extends ValuesObject> T getObject(String key) { return wrapValuesObject(getRaw(key)); }

    /**
     * Return the element as a ValuesArray. If the type is not an array, this can result in runtime errors.
     */
    default <T extends ValuesArray> T getArray(String key) { return wrapValuesArray(getRaw(key)); }

    default <T> T getScalar(String key) {
        return wrapScalar(getRaw(key));
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
    default boolean getBoolean(String key, boolean defaultValue) { return Booleans.booleanValue(getScalar(key, unwrapAny(defaultValue))); }

    /**
     * Return the element as a String. If the type is not a String, this can result in runtime errors.
     */
    default String getString(String key) { return Strings.stringValue(getRaw(key)); }

    /**
     * Return the element as a String. If the type is not a String, this can result in runtime errors.
     */
    default String getString(String key, String defaultValue) { return Strings.stringValue(getScalar(key, defaultValue)); }

    /**
     * Return the element as a int. If the type is not a int, this can result in runtime errors.
     */
    default int getInt(String key) { return Numbers.intValue(getRaw(key)); }

    /**
     * Return the element as a int. If the type is not a int, this can result in runtime errors.
     */
    default int getInt(String key, int defaultValue) { return Numbers.intValue(getScalar(key, defaultValue)); }

    /**
     * Return the element as a long. If the type is not a long, this can result in runtime errors.
     */
    default long getLong(String key) { return Numbers.longValue(getRaw(key)); }

    /**
     * Return the element as a long. If the type is not a long, this can result in runtime errors.
     */
    default long getLong(String key, long defaultValue) { return Numbers.longValue(getScalar(key, defaultValue)); }

    /**
     * Return the element as a double. If the type is not a double, this can result in runtime errors.
     */
    default double getDouble(String key) { return Numbers.doubleValue(getRaw(key)); }

    /**
     * Return the element as a double. If the type is not a double, this can result in runtime errors.
     */
    default double getDouble(String key, double defaultValue) { return  Numbers.doubleValue(getScalar(key, defaultValue)); }
}
