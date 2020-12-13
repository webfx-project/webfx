package dev.webfx.platform.shared.util.noreflect;

/**
 * @author Bruno Salmon
 */
public interface WritableIndexedArray extends IndexedArray {

    /**
     * Remove a given index and associated value from the object.
     */
    <V> V remove(int index);

    /**
     * Pushes the given element onto the end of the array. Most consuming call.
     */
    <T extends WritableIndexedArray> T push(Object element);

    /**
     * Set a given index to the given object.
     */
    default <T extends WritableIndexedArray> T push(KeyObject object) { return push((Object) object); }

    /**
     * Set a given index to the given array.
     */
    default <T extends WritableIndexedArray> T push(IndexedArray array) { return push((Object) array); }

    /**
     * Set a given index to the given element.
     */
    default <T extends WritableIndexedArray> T pushScalar(Object scalar) { return push(scalar); }

    /**
     * Pushes the given boolean string onto the end of the array.
     */
    default <T extends WritableIndexedArray> T push(String value) { return pushScalar(value); }

    /**
     * Pushes the given boolean value onto the end of the array.
     */
    default <T extends WritableIndexedArray> T push(boolean value) { return pushScalar(value); }

    /**
     * Pushes the given double value onto the end of the array.
     */
    default <T extends WritableIndexedArray> T push(double value) { return pushScalar(value); }

    /**
     * Set a given index to the given value. Most consuming call.
     */
    <T extends WritableIndexedArray> T set(int index, Object value);

    /**
     * Set a given index to the given object.
     */
    default <T extends WritableIndexedArray> T set(int index, KeyObject object) {
        return set(index, (Object) object);
    }

    /**
     * Set a given index to the given array.
     */
    default <T extends WritableIndexedArray> T set(int index, IndexedArray array) {
        return set(index, (Object) array);
    }

    /**
     * Set a given index to the given scalar.
     */
    default <T extends WritableIndexedArray> T setScalar(int index, Object scalar) {
        return set(index, scalar);
    }

    /**
     * Set a given index to the given string.
     */
    default <T extends WritableIndexedArray> T set(int index, String value) {
        return setScalar(index, value);
    }

    /**
     * Set a given index to the given boolean.
     */
    default <T extends WritableIndexedArray> T set(int index, Boolean value) {
        return setScalar(index, value);
    }

    /**
     * Set a given index to the given double.
     */
    default <T extends WritableIndexedArray> T set(int index, Double value) {
        return setScalar(index, value);
    }


}
