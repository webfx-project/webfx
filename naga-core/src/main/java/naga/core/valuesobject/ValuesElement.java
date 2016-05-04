package naga.core.valuesobject;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public interface ValuesElement {

    /**
     * Return true if it is an array.
     */
    boolean isArray();

    /**
     * Return true if it is an object.
     */
    boolean isObject();

    /**
     * Length of the array or number of keys of the object
     */
    int size();

    /**
     * Make a copy of this object or array.
     */
    <SC extends ValuesElement> SC copy();

    Collection values();

    RawType getRawType(Object rawValue);

    default <T> T wrapAny(Object rawValue) {
        if (rawValue == null)
            return null;
        switch (getRawType(rawValue)) {
            case RAW_SCALAR: return wrapScalar(rawValue);
            case RAW_VALUES_ARRAY: return (T) wrapValuesArray(rawValue);
            case RAW_VALUES_OBJECT: return (T) wrapValuesObject(rawValue);
        }
        return null;
    }

    default <T> T wrapScalar(Object rawScalar) { return (T) rawScalar; }

    default <T extends ValuesArray> T wrapValuesArray(Object rawArray) { return  (T) rawArray; }

    default <T extends ValuesObject> T wrapValuesObject(Object rawObject) { return (T) rawObject; }

    default Object unwrapAny(Object value) {
        if (value == null)
            return null;
        if (value instanceof ValuesArray)
            return unwrapArray((ValuesArray) value);
        if (value instanceof ValuesObject)
            return unwrapObject((ValuesObject) value);
        return unwrapScalar(value);
    }

    default Object unwrapScalar(Object scalar) { return scalar; }

    default Object unwrapArray(ValuesArray array) { return array; }

    default Object unwrapObject(ValuesObject object) { return object; }

}
