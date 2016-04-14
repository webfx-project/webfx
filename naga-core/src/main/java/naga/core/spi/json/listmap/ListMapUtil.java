package naga.core.spi.json.listmap;

import naga.core.spi.json.Json;
import naga.core.spi.json.JsonType;
import naga.core.util.Numbers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
final class ListMapUtil {

    static JsonType getType(Object value) {
        if (value instanceof Map)
            return JsonType.OBJECT;
        else if (value instanceof List)
            return JsonType.ARRAY;
        else if (value instanceof String)
            return JsonType.STRING;
        else if (Numbers.isNumber(value))
            return JsonType.NUMBER;
        else if (value instanceof Boolean)
            return JsonType.BOOLEAN;
        else if (value == null)
            return JsonType.NULL;
        throw new IllegalArgumentException("Invalid JSON type: " + value.getClass().getName());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static <T> T wrap(Object value) {
        if (value instanceof Map)
            value = Json.createObject(value);
        else if (value instanceof List)
            value = Json.createArray(value);
        return (T) value;
    }

    static Object unwrap(Object value) {
        if (value instanceof MapBasedJsonObject)
            value = ((MapBasedJsonObject) value).getNativeObject();
        else if (value instanceof ListBasedJsonArray)
            value = ((ListBasedJsonArray) value).getNativeArray();
        return value;
    }

    @SuppressWarnings("unchecked")
    static List<Object> convertList(List<?> list) {
        List<Object> arr = new ArrayList<Object>(list.size());
        for (Object obj : list) {
            if (obj instanceof Map)
                arr.add(convertMap((Map<String, Object>) obj));
            else if (obj instanceof List)
                arr.add(convertList((List<?>) obj));
            else
                arr.add(obj);
        }
        return arr;
    }

    @SuppressWarnings("unchecked")
    static Map<String, Object> convertMap(Map<String, Object> map) {
        Map<String, Object> converted = new LinkedHashMap<String, Object>(map.size());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();
            if (obj instanceof Map) {
                Map<String, Object> jm = (Map<String, Object>) obj;
                converted.put(entry.getKey(), convertMap(jm));
            } else if (obj instanceof List) {
                List<Object> list = (List<Object>) obj;
                converted.put(entry.getKey(), convertList(list));
            } else
                converted.put(entry.getKey(), obj);
        }
        return converted;
    }
}