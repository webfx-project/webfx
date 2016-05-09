package naga.core.spi.json.vertx;

import io.vertx.core.json.JsonArray;
import naga.core.composite.listmap.ListBasedCompositeArray;

import java.util.List;

/**
 * @author Bruno Salmon
 */
final class VertxJsonArray extends ListBasedCompositeArray implements VertxJsonElement, naga.core.spi.json.JsonArray {

    private JsonArray vertxArray;

    VertxJsonArray() {  // super constructor will call recreateEmptyNativeArray() to initialize the array
    }

    VertxJsonArray(List<Object> list) {
        super(list);
    }

    VertxJsonArray(JsonArray vertxArray) {
        this.vertxArray = vertxArray;
    }

    @Override
    public List<Object> getList() {
        return vertxArray.getList();
    }

    @Override
    protected void setList(List<Object> list) {
        vertxArray = new JsonArray(list);
    }

    @Override
    public JsonArray getNativeElement() {
        return vertxArray;
    }

    @Override
    protected void deepCopyNativeArray() {
        vertxArray = vertxArray.copy();
    }

    @Override
    public String toJsonString() {
        return vertxArray.encode();
    }
}
