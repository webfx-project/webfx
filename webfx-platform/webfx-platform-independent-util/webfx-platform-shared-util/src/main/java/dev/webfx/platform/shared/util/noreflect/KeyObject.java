package dev.webfx.platform.shared.util.noreflect;

import dev.webfx.platform.shared.util.*;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public interface KeyObject {

    /**
     * Test whether a given key has present.
     */
    default boolean has(String key) {
        return get(key) != null;
    }

    /**
     * All keys of the object.
     */
    IndexedArray keys();

    /**
     * Return the element as a value or wrapped object/array.
     */
    <T> T get(String key);

    /**
     * Return the element as a JsonObject. If the type is not an object, this can result in runtime errors.
     */
    default KeyObject getObject(String key) { return get(key); }

    /**
     * Return the element as a JsonArray. If the type is not an array, this can result in runtime errors.
     */
    default IndexedArray getArray(String key) { return get(key); }

    default <T> T getScalar(String key) {
        return get(key);
    }

    default <T> T getScalar(String key, T defaultValue) {
        return Objects.coalesce(getScalar(key), defaultValue);
    }

    default boolean isTrue(String key) {
        return Boolean.TRUE.equals(getBoolean(key));
    }
    /**
     * Return the element as a boolean. If the type is not a boolean, this can result in runtime errors.
     */
    default Boolean getBoolean(String key) { return Booleans.toBoolean(getScalar(key)); }

    /**
     * Return the element as a boolean. If the type is not a boolean, this can result in runtime errors.
     */
    default Boolean getBoolean(String key, Boolean defaultValue) { return Booleans.toBoolean(getScalar(key, defaultValue)); }

    /**
     * Return the element as a String. If the type is not a String, this can result in runtime errors.
     */
    default String getString(String key) { return Strings.toString(getScalar(key)); }

    /**
     * Return the element as a String. If the type is not a String, this can result in runtime errors.
     */
    default String getString(String key, String defaultValue) { return Strings.toString(getScalar(key, defaultValue)); }

    /**
     * Return the element as a int. If the type is not a int, this can result in runtime errors.
     */
    default Integer getInteger(String key) { return Numbers.toInteger(getScalar(key)); }

    /**
     * Return the element as a int. If the type is not a int, this can result in runtime errors.
     */
    default Integer getInteger(String key, Integer defaultValue) { return Numbers.toInteger(getScalar(key, defaultValue)); }

    /**
     * Return the element as a long. If the type is not a long, this can result in runtime errors.
     */
    default Long getLong(String key) { return Numbers.toLong(getScalar(key)); }

    /**
     * Return the element as a long. If the type is not a long, this can result in runtime errors.
     */
    default Long getLong(String key, Long defaultValue) { return Numbers.toLong(getScalar(key, defaultValue)); }

    /**
     * Return the element as a double. If the type is not a double, this can result in runtime errors.
     */
    default Double getDouble(String key) { return Numbers.toDouble(getScalar(key)); }

    /**
     * Return the element as a double. If the type is not a double, this can result in runtime errors.
     */
    default Double getDouble(String key, Double defaultValue) { return Numbers.toDouble(getScalar(key, defaultValue)); }

    /**
     * Return the element as an instant. If the type is not an instant, this can result in runtime errors.
     */
    default Instant getInstant(String key) { return Dates.toInstant(getScalar(key)); }

    /**
     * Return the element as an instant. If the type is not an instant, this can result in runtime errors.
     */
    default Instant getInstant(String key, Instant defaultValue) { return Dates.toInstant(getScalar(key, defaultValue)); }
}

