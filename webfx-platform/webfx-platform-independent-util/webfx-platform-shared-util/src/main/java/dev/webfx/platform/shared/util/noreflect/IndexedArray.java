package dev.webfx.platform.shared.util.noreflect;

import dev.webfx.platform.shared.util.Booleans;
import dev.webfx.platform.shared.util.Numbers;
import dev.webfx.platform.shared.util.Objects;
import dev.webfx.platform.shared.util.Strings;

/**
 * @author Bruno Salmon
 */
public interface IndexedArray {

    /**
     * Length of the array or number of keys of the object
     */
    int size();

    /**
     * Returns the first index of the given value, or -1 if it cannot be found.
     */
    int indexOf(Object value);

    /**
     * Return the ith element of the array. Most consuming call.
     */
    <V> V getElement(int index);

    /**
     * Return the ith element of the array as a JsonObject. If the type is not an object, this can result in runtime errors.
     */
    default KeyObject getObject(int index) { return getElement(index); }

    /**
     * Return the ith element of the array as a JsonArray. If the type is not an array, this can result in runtime errors.
     */
    default IndexedArray getArray(int index) { return getElement(index); }

    default <T> T getScalar(int index) {
        return getElement(index);
    }

    default <T> T getScalar(int index, T defaultValue) {
        return Objects.coalesce(getScalar(index), defaultValue);
    }

    /**
     * Return the ith element of the array as a boolean. If the type is not a boolean, this can result in runtime errors.
     */
    default Boolean getBoolean(int index) { return Booleans.toBoolean(getScalar(index)); }

    /**
     * Return the ith element of the array as a boolean. If the type is not a boolean, this can result in runtime errors.
     */
    default Boolean getBoolean(int index, Boolean defaultValue) { return Booleans.toBoolean(getScalar(index, defaultValue)); }

    /**
     * Return the ith element of the array as a String. If the type is not a String, this can result in runtime errors.
     */
    default String getString(int index) { return Strings.toString(getScalar(index)); }

    /**
     * Return the ith element of the array as a String. If the type is not a String, this can result in runtime errors.
     */
    default String getString(int index, String defaultValue) { return Strings.toString(getScalar(index, defaultValue)); }

    /**
     * Return the ith element of the array as a int. If the type is not a int, this can result in runtime errors.
     */
    default Integer getInteger(int index) { return Numbers.toInteger(getScalar(index)); }

    /**
     * Return the ith element of the array as a int. If the type is not a int, this can result in runtime errors.
     */
    default Integer getInteger(int index, Integer defaultValue) { return Numbers.toInteger(getScalar(index, defaultValue)); }

    /**
     * Return the ith element of the array as a long. If the type is not a long, this can result in runtime errors.
     */
    default Long getLong(int index) { return Numbers.longValue(getScalar(index)); }

    /**
     * Return the ith element of the array as a long. If the type is not a long, this can result in runtime errors.
     */
    default Long getLong(int index, Long defaultValue) { return Numbers.toLong(getScalar(index, defaultValue)); }

    /**
     * Return the ith element of the array as a double. If the type is not a double, this can result in runtime errors.
     */
    default Double getDouble(int index) { return Numbers.toDouble(getScalar(index)); }

    /**
     * Return the ith element of the array as a double. If the type is not a double, this can result in runtime errors.
     */
    default Double getDouble(int index, Double defaultValue) { return Numbers.toDouble(getScalar(index, defaultValue)); }


}
