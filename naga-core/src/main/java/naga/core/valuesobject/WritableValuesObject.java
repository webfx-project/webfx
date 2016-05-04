package naga.core.valuesobject;

/**
 * @author Bruno Salmon
 */
public interface WritableValuesObject extends ValuesObject {

    /**
     * Remove a given key and associated value from the object.
     */
    <V> V remove(String key);

    /**
     * Set a given key to the given value.
     */
    void setRaw(String key, Object value);

    /**
     * Set a given key to the given value.
     */
    default <V> void set(String key, V value) {
        setRaw(key, unwrapAny(value));
    }

    /**
     * Set a given key to the given object.
     */
    default void setObject(String key, ValuesObject object) { setRaw(key, unwrapObject(object)); }

    /**
     * Set a given key to the given array.
     */
    default void setArray(String key, ValuesArray array) { setRaw(key, unwrapArray(array)); }

    /**
     * Set a given key to the given element.
     */
    default void setScalar(String key, Object scalar) { setRaw(key, unwrapScalar(scalar)); }

    default void set(String key, boolean value) { setScalar(key, value); }

    default void set(String key, double value) { setScalar(key, value); }

}
