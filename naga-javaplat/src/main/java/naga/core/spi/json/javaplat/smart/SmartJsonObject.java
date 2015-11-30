package naga.core.spi.json.javaplat.smart;

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonElement;
import naga.core.spi.json.JsonObject;
import naga.core.spi.json.JsonType;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
class SmartJsonObject implements JsonObject {

    private final Map<String, Object> jso;

    public SmartJsonObject() {
        this(new JSONObject());
    }

    public SmartJsonObject(Map jso) {
        this.jso = jso;
    }

    static SmartJsonObject create(Map<String, Object> jso) {
        return jso == null ? null : new SmartJsonObject(jso);
    }

    @Override
    public <T> void forEach(MapIterator<T> handler) {
        jso.forEach((s, o) -> handler.call(s, (T) o));
    }

    @Override
    public <T> T get(String key) {
        return (T) jso.get(key);
    }

    @Override
    public JsonArray getArray(String key) {
        return SmartJsonArray.create((List) jso.get(key)) ;
    }

    @Override
    public boolean getBoolean(String key) {
        return (boolean) jso.get(key);
    }

    @Override
    public double getNumber(String key) {
        return ((Number) jso.get(key)).doubleValue();
    }

    @Override
    public JsonObject getObject(String key) {
        return create((Map<String, Object>) jso.get(key));
    }

    @Override
    public String getString(String key) {
        return (String) jso.get(key);
    }

    @Override
    public JsonType getType(String key) {
        return null;
    }

    @Override
    public boolean has(String key) {
        return jso.containsKey(key);
    }

    @Override
    public JsonArray keys() {
        return SmartJsonArray.create(new ArrayList(jso.keySet()));
    }

    @Override
    public <T> T remove(String key) {
        return (T) jso.remove(key);
    }

    @Override
    public JsonObject set(String key, boolean bool_) {
        jso.put(key, bool_);
        return this;
    }

    @Override
    public JsonObject set(String key, double number) {
        jso.put(key, number);
        return this;
    }

    @Override
    public JsonObject set(String key, Object value) {
        jso.put(key, value);
        return this;
    }

    @Override
    public int size() {
        return jso.size();
    }

    @Override
    public <T extends JsonElement> T clear() {
        jso.clear();
        return (T) this;
    }

    @Override
    public <T extends JsonElement> T copy() {
        return (T) create(new HashMap<>(jso));
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
        return JSONObject.toJSONString(jso);
    }
}
