package dev.webfx.platform.shared.services.json.spi.impl.listmap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
final class ListMapUtil {

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