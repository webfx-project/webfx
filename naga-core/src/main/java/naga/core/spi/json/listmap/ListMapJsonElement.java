package naga.core.spi.json.listmap;

import naga.core.spi.json.Json;
import naga.core.valuesobject.RawType;
import naga.core.valuesobject.ValuesArray;
import naga.core.valuesobject.ValuesObject;
import naga.core.valuesobject.WritableValuesElement;

import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class ListMapJsonElement implements WritableValuesElement {

    @Override
    public RawType getRawType(Object rawValue) {
        if (rawValue instanceof Map || rawValue instanceof MapBasedJsonObject)
            return RawType.RAW_VALUES_OBJECT;
        if (rawValue instanceof List || rawValue instanceof ListBasedJsonArray)
            return RawType.RAW_VALUES_ARRAY;
        return RawType.RAW_SCALAR;
    }

    @Override
    public ListBasedJsonArray wrapValuesArray(Object rawArray) {
        if (rawArray == null || rawArray instanceof ListBasedJsonArray)
            return (ListBasedJsonArray) rawArray;
        return (ListBasedJsonArray) Json.createArray(rawArray);
    }

    @Override
    public MapBasedJsonObject wrapValuesObject(Object rawObject) {
        if (rawObject == null || rawObject instanceof MapBasedJsonObject)
            return (MapBasedJsonObject) rawObject;
        return (MapBasedJsonObject) Json.createObject(rawObject);
    }

    @Override
    public Object unwrapArray(ValuesArray array) {
        if (array instanceof ListBasedJsonArray)
            return ((ListBasedJsonArray) array).getNativeArray();
        return array;
    }

    @Override
    public Object unwrapObject(ValuesObject object) {
        if (object instanceof MapBasedJsonObject)
            return ((MapBasedJsonObject) object).getNativeObject();
        return object;
    }

}
