package naga.core.spi.json.java.smart;

import naga.core.json.WritableJsonArray;
import naga.core.json.listmap.ListJsonArray;

import java.util.List;

/**
 * @author Bruno Salmon
 */
final class SmartJsonArray extends ListJsonArray implements SmartJsonElement, WritableJsonArray {

    public SmartJsonArray() {}

    public SmartJsonArray(List list) {
        super(list);
    }

}
