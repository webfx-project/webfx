package naga.core.valuesobject;

/**
 * @author Bruno Salmon
 */
public interface WritableValuesArray extends ValuesArray, WritableValuesElement {

    /**
     * Remove a given index and associated value from the object.
     */
    <V> V remove(int index);

    /**
     * Pushes the given value onto the end of the array.
     */
    void pushRaw(Object value);

    /**
     * Pushes the given value onto the end of the array. Most consuming call.
     */
    default void push(Object value) {
        pushRaw(unwrapAny(value));
    }

    /**
     * Set a given index to the given object.
     */
    default void push(ValuesObject object) { pushRaw(unwrapObject(object)); }

    /**
     * Set a given index to the given array.
     */
    default void push(ValuesArray array) { pushRaw(unwrapArray(array)); }

    /**
     * Set a given index to the given element.
     */
    default void pushScalar(Object scalar) { pushRaw(unwrapScalar(scalar)); }

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
    void setRaw(int index, Object value);

    /**
     * Set a given index to the given value. Most consuming call.
     */
    default void set(int index, Object value) {
        setRaw(index, unwrapAny(value));
    }

    /**
     * Set a given index to the given object.
     */
    default void set(int index, ValuesObject object) {
        setRaw(index, unwrapObject(object));
    }

    /**
     * Set a given index to the given array.
     */
    default void set(int index, ValuesArray array) {
        setRaw(index, unwrapArray(array));
    }

    /**
     * Set a given index to the given scalar.
     */
    default void setScalar(int index, Object scalar) {
        setRaw(index, unwrapScalar(scalar));
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
