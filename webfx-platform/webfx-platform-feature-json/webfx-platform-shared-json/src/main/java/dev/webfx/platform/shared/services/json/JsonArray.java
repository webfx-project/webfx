package dev.webfx.platform.shared.services.json;

import dev.webfx.platform.shared.util.noreflect.IndexedArray;

/**
 * @author Bruno Salmon
 */
public interface JsonArray extends JsonElement, IndexedArray {

    /**
     * Return true if it is an array.
     */
    default boolean isArray() { return true; }

    /**
     * Return true if it is an object.
     */
    default boolean isObject() { return false; }

    /**
     * Returns the first index of the given element, or -1 if it cannot be found.
     */
    int indexOfNativeElement(Object element);

    /**
     * Returns the first index of the given value, or -1 if it cannot be found.
     */
    default int indexOf(Object value) {
        return indexOfNativeElement(anyJavaToNative(value));
    }

    /**
     * Return the ith element of the array.
     */
    Object getNativeElement(int index);

    /**
     * Return the ith element of the array. Most consuming call.
     */
    default <V> V getElement(int index) {
        return anyNativeToJava(getNativeElement(index));
    }

    /**
     * Return the ith element of the array as a JsonObject. If the type is not an object, this can result in runtime errors.
     */
    default JsonObject getObject(int index) { return nativeToJavaJsonObject(getNativeElement(index)); }

    /**
     * Return the ith element of the array as a JsonArray. If the type is not an array, this can result in runtime errors.
     */
    default JsonArray getArray(int index) { return nativeToJavaJsonArray(getNativeElement(index)); }

    default <T> T getScalar(int index) {
        return nativeToJavaScalar(getNativeElement(index));
    }
}
