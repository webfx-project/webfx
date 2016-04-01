package naga.core.spi.json.javaplat.smart;

import naga.core.spi.json.javaplat.listmap.ListJsonArray;
import net.minidev.json.JSONArray;

import java.util.List;

/**
 * @author Bruno Salmon
 */
final class SmartJsonArray extends ListJsonArray {

    public SmartJsonArray() {}

    public SmartJsonArray(List list) {
        super(list);
    }

    @Override
    public String toJsonString() {
        return JSONArray.toJSONString(getList());
    }
}
