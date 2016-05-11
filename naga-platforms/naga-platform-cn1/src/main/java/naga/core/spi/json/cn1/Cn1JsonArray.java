package naga.core.spi.json.cn1;

import naga.core.json.WritableJsonArray;
import naga.core.json.listmap.ListJsonArray;

import java.util.List;

/**
 * @author Bruno Salmon
 */
final class Cn1JsonArray extends ListJsonArray implements Cn1JsonElement, WritableJsonArray {

    public Cn1JsonArray() {}

    public Cn1JsonArray(List list) {
        super(list);
    }

}
