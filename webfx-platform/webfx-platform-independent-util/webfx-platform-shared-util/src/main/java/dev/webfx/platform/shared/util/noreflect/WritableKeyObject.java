package dev.webfx.platform.shared.util.noreflect;

import dev.webfx.platform.shared.util.Dates;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public interface WritableKeyObject extends KeyObject {

    /**
     * Remove a given key and associated value from the object.
     */
    <V> V remove(String key);

    /**
     * Set a given key to the given value.
     */
    <T extends WritableKeyObject> T set(String key, Object value);

    /**
     * Set a given key to the given object.
     */
    default <T extends WritableKeyObject> T setObject(String key, KeyObject object) { return set(key, object); }

    /**
     * Set a given key to the given array.
     */
    default <T extends WritableKeyObject> T setArray(String key, IndexedArray array) { return set(key, array); }

    /**
     * Set a given key to the given element.
     */
    default <T extends WritableKeyObject> T setScalar(String key, Object scalar) { return set(key, scalar); }

    default <T extends WritableKeyObject> T set(String key, Boolean value) { return setScalar(key, value); }

    default <T extends WritableKeyObject> T set(String key, Integer value) { return setScalar(key, value); }

    default <T extends WritableKeyObject> T set(String key, Long value) { return setScalar(key, value); }

    default <T extends WritableKeyObject> T set(String key, Double value) { return setScalar(key, value); }

    default <T extends WritableKeyObject> T set(String key, String value) { return setScalar(key, value); }

    default <T extends WritableKeyObject> T set(String key, Instant value) { return setScalar(key, Dates.formatIso(value)); }

}
