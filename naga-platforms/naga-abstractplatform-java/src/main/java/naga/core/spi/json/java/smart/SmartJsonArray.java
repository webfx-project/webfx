package naga.core.spi.json.java.smart;

import naga.core.composite.WritableCompositeArray;
import naga.core.composite.listmap.ListCompositeArray;

import java.util.List;

/**
 * @author Bruno Salmon
 */
final class SmartJsonArray extends ListCompositeArray implements SmartJsonElement, WritableCompositeArray {

    public SmartJsonArray() {}

    public SmartJsonArray(List list) {
        super(list);
    }

}
