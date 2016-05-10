package naga.core.spi.json.java.smart;

import naga.core.composite.WritableCompositeArray;
import naga.core.composite.listmap.ListCompositeArray;
import net.minidev.json.JSONArray;

import java.util.List;

/**
 * @author Bruno Salmon
 */
final class SmartJsonArray extends ListCompositeArray implements SmartJsonElement, WritableCompositeArray {

    public SmartJsonArray() {}

    public SmartJsonArray(List list) {
        super(list);
    }

    @Override
    public String toJsonString() {
        return JSONArray.toJSONString(getList());
    }
}
