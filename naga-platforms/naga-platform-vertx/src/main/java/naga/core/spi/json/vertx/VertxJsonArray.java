package naga.core.spi.json.vertx;

import naga.core.spi.json.listmap.ListBasedJsonArray;

import java.util.List;

/**
 * @author Bruno Salmon
 */
final class VertxJsonArray extends ListBasedJsonArray<io.vertx.core.json.JsonArray> {

    private io.vertx.core.json.JsonArray vertxArray;

    VertxJsonArray() {  // super constructor will call recreateEmptyNativeArray() to initialize the array
    }

    VertxJsonArray(io.vertx.core.json.JsonArray vertxArray) {
        this.vertxArray = vertxArray;
    }

    @Override
    public List getList() {
        return vertxArray.getList();
    }

    @Override
    protected io.vertx.core.json.JsonArray getNativeArray() {
        return vertxArray;
    }

    @Override
    protected void recreateEmptyNativeArray() {
        vertxArray = new io.vertx.core.json.JsonArray();
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
