package dev.webfx.platform.shared.services.json;

import dev.webfx.platform.shared.util.noreflect.IndexedArray;
import dev.webfx.platform.shared.util.noreflect.KeyObject;
import dev.webfx.platform.shared.util.noreflect.WritableKeyObject;

/**
 * @author Bruno Salmon
 */
public interface WritableJsonObject extends JsonObject, WritableKeyObject {

    /**
     * Set a given key to the given element. Fluent API (return this).
     */
    WritableJsonObject setNativeElement(String key, Object element);

    /**
     * Set a given key to the given value.
     */
    default WritableJsonObject set(String key, Object value) {
        return setNativeElement(key, anyJavaToNative(value));
    }

    /**
     * Set a given key to the given object.
     */
    default WritableJsonObject setObject(String key, KeyObject object) { return setNativeElement(key, javaToNativeJsonObject((JsonObject) object)); }

    /**
     * Set a given key to the given array.
     */
    default WritableJsonObject setArray(String key, IndexedArray array) { return setNativeElement(key, javaToNativeJsonArray((JsonArray) array)); }

    /**
     * Set a given key to the given element.
     */
    default WritableJsonObject setScalar(String key, Object scalar) { return setNativeElement(key, javaToNativeScalar(scalar)); }

}
