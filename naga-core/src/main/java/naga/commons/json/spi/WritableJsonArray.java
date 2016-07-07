package naga.commons.json.spi;

/**
 * @author Bruno Salmon
 */
public interface WritableJsonArray extends JsonArray, WritableJsonElement {

    /**
     * Remove a given index and associated value from the object.
     */
    <V> V remove(int index);

    /**
     * Pushes the given element onto the end of the array. Fluent API (return this).
     */
    WritableJsonArray pushNativeElement(Object element);

    /**
     * Pushes the given element onto the end of the array. Most consuming call.
     */
    default WritableJsonArray push(Object element) {
        return pushNativeElement(anyJavaToNative(element));
    }

    /**
     * Set a given index to the given object.
     */
    default WritableJsonArray push(JsonObject object) { return pushNativeElement(javaToNativeJsonObject(object)); }

    /**
     * Set a given index to the given array.
     */
    default WritableJsonArray push(JsonArray array) { return pushNativeElement(javaToNativeJsonArray(array)); }

    /**
     * Set a given index to the given element.
     */
    default WritableJsonArray pushScalar(Object scalar) { return pushNativeElement(javaToNativeScalar(scalar)); }

    /**
     * Pushes the given boolean string onto the end of the array.
     */
    default WritableJsonArray push(String value) { return pushScalar(value); }

    /**
     * Pushes the given boolean value onto the end of the array.
     */
    default WritableJsonArray push(boolean value) { return pushScalar(value); }

    /**
     * Pushes the given double value onto the end of the array.
     */
    default WritableJsonArray push(double value) { return pushScalar(value); }

    /**
     * Set a given index to the given value. Fluent API (return this).
     */
    WritableJsonArray setNativeElement(int index, Object value);

    /**
     * Set a given index to the given value. Most consuming call.
     */
    default WritableJsonArray set(int index, Object value) {
        return setNativeElement(index, anyJavaToNative(value));
    }

    /**
     * Set a given index to the given object.
     */
    default WritableJsonArray set(int index, JsonObject object) {
        return setNativeElement(index, javaToNativeJsonObject(object));
    }

    /**
     * Set a given index to the given array.
     */
    default WritableJsonArray set(int index, JsonArray array) {
        return setNativeElement(index, javaToNativeJsonArray(array));
    }

    /**
     * Set a given index to the given scalar.
     */
    default WritableJsonArray setScalar(int index, Object scalar) {
        return setNativeElement(index, javaToNativeScalar(scalar));
    }

    /**
     * Set a given index to the given string.
     */
    default WritableJsonArray set(int index, String value) {
        return setScalar(index, value);
    }

    /**
     * Set a given index to the given boolean.
     */
    default WritableJsonArray set(int index, boolean value) {
        return setScalar(index, value);
    }

    /**
     * Set a given index to the given double.
     */
    default WritableJsonArray set(int index, double value) {
        return setScalar(index, value);
    }

}
