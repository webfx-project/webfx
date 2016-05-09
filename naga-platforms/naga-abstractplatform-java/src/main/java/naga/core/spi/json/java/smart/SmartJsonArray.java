package naga.core.spi.json.java.smart;

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.listmap.ListCompositeArray;
import net.minidev.json.JSONArray;

import java.util.List;

/**
 * @author Bruno Salmon
 */
final class SmartJsonArray extends ListCompositeArray implements SmartJsonElement, JsonArray {

    public SmartJsonArray() {}

    public SmartJsonArray(List list) {
        super(list);
    }

    @Override
    public String toJsonString() {
        return JSONArray.toJSONString(getList());
    }
}
