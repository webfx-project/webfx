package naga.core.composite;

/**
 * @author Bruno Salmon
 */
public interface WritableCompositeObject extends CompositeObject {

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
    default <V> void set(String key, V value) {
        setNativeElement(key, anyCompositeToNative(value));
    }

    /**
     * Set a given key to the given object.
     */
    default void setObject(String key, CompositeObject object) { setNativeElement(key, compositeToNativeObject(object)); }

    /**
     * Set a given key to the given array.
     */
    default void setArray(String key, CompositeArray array) { setNativeElement(key, compositeToNativeArray(array)); }

    /**
     * Set a given key to the given element.
     */
    default void setScalar(String key, Object scalar) { setNativeElement(key, compositeToNativeScalar(scalar)); }

    default void set(String key, boolean value) { setScalar(key, value); }

    default void set(String key, double value) { setScalar(key, value); }

}
