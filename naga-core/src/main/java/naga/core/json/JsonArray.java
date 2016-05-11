package naga.core.json;

import naga.core.util.Booleans;
import naga.core.util.Numbers;
import naga.core.util.Objects;
import naga.core.util.Strings;

/**
 * @author Bruno Salmon
 */
public interface JsonArray extends JsonElement {

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

    default <T> T getScalar(int index, T defaultValue) {
        return Objects.coalesce(getScalar(index), defaultValue);
    }

    /**
     * Return the ith element of the array as a boolean. If the type is not a boolean, this can result in runtime errors.
     */
    default boolean getBoolean(int index) { return Booleans.booleanValue(getScalar(index)); }

    /**
     * Return the ith element of the array as a boolean. If the type is not a boolean, this can result in runtime errors.
     */
    default boolean getBoolean(int index, boolean defaultValue) { return Booleans.booleanValue(getScalar(index, defaultValue)); }

    /**
     * Return the ith element of the array as a String. If the type is not a String, this can result in runtime errors.
     */
    default String getString(int index) { return Strings.stringValue(getScalar(index)); }

    /**
     * Return the ith element of the array as a String. If the type is not a String, this can result in runtime errors.
     */
    default String getString(int index, String defaultValue) { return Strings.stringValue(getScalar(index, defaultValue)); }

    /**
     * Return the ith element of the array as a int. If the type is not a int, this can result in runtime errors.
     */
    default int getInt(int index) { return Numbers.intValue(getScalar(index)); }

    /**
     * Return the ith element of the array as a int. If the type is not a int, this can result in runtime errors.
     */
    default int getInt(int index, int defaultValue) { return Numbers.intValue(getScalar(index, defaultValue)); }

    /**
     * Return the ith element of the array as a long. If the type is not a long, this can result in runtime errors.
     */
    default long getLong(int index) { return Numbers.longValue(getScalar(index)); }

    /**
     * Return the ith element of the array as a long. If the type is not a long, this can result in runtime errors.
     */
    default long getLong(int index, long defaultValue) { return Numbers.longValue(getScalar(index, defaultValue)); }

    /**
     * Return the ith element of the array as a double. If the type is not a double, this can result in runtime errors.
     */
    default double getDouble(int index) { return Numbers.doubleValue(getScalar(index)); }

    /**
     * Return the ith element of the array as a double. If the type is not a double, this can result in runtime errors.
     */
    default double getDouble(int index, double defaultValue) { return Numbers.doubleValue(getScalar(index, defaultValue)); }

}
