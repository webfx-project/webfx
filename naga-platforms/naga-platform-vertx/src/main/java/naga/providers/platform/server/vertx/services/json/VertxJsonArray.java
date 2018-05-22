package naga.providers.platform.server.vertx.services.json;

import io.vertx.core.json.JsonArray;
import naga.platform.services.json.WritableJsonArray;
import naga.platform.services.json.spi.impl.listmap.ListBasedJsonArray;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class VertxJsonArray extends ListBasedJsonArray implements VertxJsonElement, WritableJsonArray {

    private JsonArray vertxArray;

    VertxJsonArray() {  // super constructor will call recreateEmptyNativeArray() to initialize the array
    }

    VertxJsonArray(List<Object> list) {
        setList(list);
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
