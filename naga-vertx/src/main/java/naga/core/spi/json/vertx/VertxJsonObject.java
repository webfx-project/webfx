package naga.core.spi.json.vertx;

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonElement;
import naga.core.spi.json.JsonObject;
import naga.core.spi.json.JsonType;

import java.util.ArrayList;

/**
 * @author Bruno Salmon
 */
final class VertxJsonObject implements JsonObject {

    private final io.vertx.core.json.JsonObject vjo;

    private VertxJsonObject(io.vertx.core.json.JsonObject vjo) {
        this.vjo = vjo;
    }

    public static VertxJsonObject create() {
        return new VertxJsonObject(new io.vertx.core.json.JsonObject());
    }

    public static VertxJsonObject create(io.vertx.core.json.JsonObject vjo) {
        return vjo == null ? null : new VertxJsonObject(vjo);
    }

    @Override
    public <T> void forEach(MapIterator<T> handler) {
        vjo.forEach(entry -> handler.call(entry.getKey(), (T) entry.getValue()));
    }

    @Override
    public <T> T get(String key) {
        return (T) vjo.getValue(key);
    }

    @Override
    public JsonArray getArray(String key) {
        return VertxJsonArray.create(vjo.getJsonArray(key));
    }

    @Override
    public boolean getBoolean(String key) {
        return vjo.getBoolean(key);
    }

    @Override
    public double getNumber(String key) {
        return vjo.getDouble(key);
    }

    @Override
    public JsonObject getObject(String key) {
        return create(vjo.getJsonObject(key));
    }

    @Override
    public String getString(String key) {
        return vjo.getString(key);
    }

    @Override
    public JsonType getType(String key) {
        return null;
    }

    @Override
    public boolean has(String key) {
        return vjo.containsKey(key);
    }

    @Override
    public JsonArray keys() {
        return VertxJsonArray.create(new io.vertx.core.json.JsonArray(new ArrayList(vjo.fieldNames())));
    }

    @Override
    public <T> T remove(String key) {
        return (T) vjo.remove(key);
    }

    @Override
    public JsonObject set(String key, boolean bool_) {
        vjo.put(key, bool_);
        return this;
    }

    @Override
    public JsonObject set(String key, double number) {
        vjo.put(key, number);
        return this;
    }

    @Override
    public JsonObject set(String key, Object value) {
        vjo.put(key, value);
        return this;
    }

    @Override
    public int size() {
        return vjo.size();
    }

    @Override
    public <T extends JsonElement> T clear() {
        vjo.clear();
        return (T) this;
    }

    @Override
    public <T extends JsonElement> T copy() {
        return (T) VertxJsonObject.create(vjo.copy());
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public String toJsonString() {
        return vjo.encode();
    }
}

