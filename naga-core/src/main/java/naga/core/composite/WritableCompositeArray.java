package naga.core.composite;

/**
 * @author Bruno Salmon
 */
public interface WritableCompositeArray extends CompositeArray, WritableCompositeElement {

    /**
     * Remove a given index and associated value from the object.
     */
    <V> V remove(int index);

    /**
     * Pushes the given element onto the end of the array.
     */
    void pushNativeElement(Object element);

    /**
     * Pushes the given element onto the end of the array. Most consuming call.
     */
    default void push(Object element) {
        pushNativeElement(anyCompositeToNative(element));
    }

    /**
     * Set a given index to the given object.
     */
    default void push(CompositeObject object) { pushNativeElement(compositeToNativeObject(object)); }

    /**
     * Set a given index to the given array.
     */
    default void push(CompositeArray array) { pushNativeElement(compositeToNativeArray(array)); }

    /**
     * Set a given index to the given element.
     */
    default void pushScalar(Object scalar) { pushNativeElement(compositeToNativeScalar(scalar)); }

    /**
     * Pushes the given boolean string onto the end of the array.
     */
    default void push(String value) { pushScalar(value); }

    /**
     * Pushes the given boolean value onto the end of the array.
     */
    default void push(boolean value) { pushScalar(value); }

    /**
     * Pushes the given double value onto the end of the array.
     */
    default void push(double value) { pushScalar(value); }

    /**
     * Set a given index to the given value.
     */
    void setNativeElement(int index, Object value);

    /**
     * Set a given index to the given value. Most consuming call.
     */
    default void set(int index, Object value) {
        setNativeElement(index, anyCompositeToNative(value));
    }

    /**
     * Set a given index to the given object.
     */
    default void set(int index, CompositeObject object) {
        setNativeElement(index, compositeToNativeObject(object));
    }

    /**
     * Set a given index to the given array.
     */
    default void set(int index, CompositeArray array) {
        setNativeElement(index, compositeToNativeArray(array));
    }

    /**
     * Set a given index to the given scalar.
     */
    default void setScalar(int index, Object scalar) {
        setNativeElement(index, compositeToNativeScalar(scalar));
    }

    /**
     * Set a given index to the given string.
     */
    default void set(int index, String value) {
        setScalar(index, value);
    }

    /**
     * Set a given index to the given boolean.
     */
    default void set(int index, boolean value) {
        setScalar(index, value);
    }

    /**
     * Set a given index to the given double.
     */
    default void set(int index, double value) {
        setScalar(index, value);
    }

}
