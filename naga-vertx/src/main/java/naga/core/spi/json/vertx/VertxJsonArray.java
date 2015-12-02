package naga.core.spi.json.vertx;

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonElement;
import naga.core.spi.json.JsonObject;
import naga.core.spi.json.JsonType;

/**
 * @author Bruno Salmon
 */
final class VertxJsonArray implements JsonArray {

    private final io.vertx.core.json.JsonArray vja;

    VertxJsonArray(io.vertx.core.json.JsonArray vja) {
        this.vja = vja;
    }

    public static VertxJsonArray create() {
        return new VertxJsonArray(new io.vertx.core.json.JsonArray());
    }

    public static VertxJsonArray create(io.vertx.core.json.JsonArray vja) {
        return vja == null ? null : new VertxJsonArray(vja);
    }

    @Override
    public <T> void forEach(ListIterator<T> handler) {
        for (int i = 0; i < vja.size(); i++)
            handler.call(i, (T) vja.getValue(i));
    }

    @Override
    public <T> T get(int index) {
        return (T) vja.getValue(index);
    }

    @Override
    public JsonArray getArray(int index) {
        return null;
    }

    @Override
    public boolean getBoolean(int index) {
        return vja.getBoolean(index);
    }

    @Override
    public double getNumber(int index) {
        return vja.getDouble(index);
    }

    @Override
    public JsonObject getObject(int index) {
        return VertxJsonObject.create(vja.getJsonObject(index));
    }

    @Override
    public String getString(int index) {
        return vja.getString(index);
    }

    @Override
    public JsonType getType(int index) {
        return null;
    }

    @Override
    public int indexOf(Object value) {
        return vja.getList().indexOf(value);
    }

    @Override
    public JsonArray insert(int index, Object value) {
        vja.getList().add(index, value);
        return this;
    }

    @Override
    public int length() {
        return vja.size();
    }

    @Override
    public JsonArray push(boolean bool_) {
        vja.add(bool_);
        return this;
    }

    @Override
    public JsonArray push(double number) {
        vja.add(number);
        return this;
    }

    @Override
    public JsonArray push(Object value) {
        vja.add(value);
        return this;
    }

    @Override
    public <T> T remove(int index) {
        return (T) vja.remove(index);
    }

    @Override
    public boolean removeValue(Object value) {
        return removeValue(value);
    }

    @Override
    public <T extends JsonElement> T clear() {
        vja.clear();
        return (T) this;
    }

    @Override
    public <T extends JsonElement> T copy() {
        return (T) create(vja.copy());
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public String toJsonString() {
        return vja.encode();
    }
}
