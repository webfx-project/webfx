package dev.webfx.platform.shared.services.json;

import dev.webfx.platform.shared.util.noreflect.IndexedArray;
import dev.webfx.platform.shared.util.noreflect.KeyObject;
import dev.webfx.platform.shared.util.noreflect.WritableIndexedArray;

/**
 * @author Bruno Salmon
 */
public interface WritableJsonArray extends JsonArray, WritableJsonElement, WritableIndexedArray {

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
    default WritableJsonArray push(KeyObject object) { return pushNativeElement(javaToNativeJsonObject((JsonObject) object)); }

    /**
     * Set a given index to the given array.
     */
    default WritableJsonArray push(IndexedArray array) { return pushNativeElement(javaToNativeJsonArray((JsonArray) array)); }

    /**
     * Set a given index to the given element.
     */
    default WritableJsonArray pushScalar(Object scalar) { return pushNativeElement(javaToNativeScalar(scalar)); }


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
}
