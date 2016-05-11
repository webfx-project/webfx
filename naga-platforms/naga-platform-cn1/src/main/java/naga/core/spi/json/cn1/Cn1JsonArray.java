package naga.core.spi.json.cn1;

import naga.core.composite.WritableCompositeArray;
import naga.core.composite.listmap.ListCompositeArray;

import java.util.List;

/**
 * @author Bruno Salmon
 */
final class Cn1JsonArray extends ListCompositeArray implements Cn1JsonElement, WritableCompositeArray {

    public Cn1JsonArray() {}

    public Cn1JsonArray(List list) {
        super(list);
    }

}
