package naga.core.spi.json.javaplat.smart;

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonElement;
import naga.core.spi.json.JsonObject;
import naga.core.spi.json.JsonType;
import net.minidev.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
class SmartJsonArray implements JsonArray {

    private final List jsa;

    public SmartJsonArray() {
        this(new ArrayList<>());
    }

    public SmartJsonArray(List jsa) {
        this.jsa = jsa;
    }

    static SmartJsonArray create(List jsa) {
        return jsa == null ? null : new SmartJsonArray(jsa);
    }

    @Override
    public <T> void forEach(ListIterator<T> handler) {
        int i = 0;
        for (Object o : jsa)
            handler.call(i++, (T) o);
    }

    @Override
    public <T> T get(int index) {
        return (T) jsa.get(index);
    }

    @Override
    public JsonArray getArray(int index) {
        return create((List) jsa.get(index));
    }

    @Override
    public boolean getBoolean(int index) {
        return (Boolean) jsa.get(index);
    }

    @Override
    public double getNumber(int index) {
        return (double) jsa.get(index);
    }

    @Override
    public JsonObject getObject(int index) {
        return SmartJsonObject.create((Map<String, Object>) jsa.get(index));
    }

    @Override
    public String getString(int index) {
        return (String) jsa.get(index);
    }

    @Override
    public JsonType getType(int index) {
        return null;
    }

    @Override
    public int indexOf(Object value) {
        return jsa.indexOf(value);
    }

    @Override
    public JsonArray insert(int index, Object value) {
        jsa.add(index, value);
        return this;
    }

    @Override
    public int length() {
        return jsa.size();
    }

    @Override
    public JsonArray push(boolean bool_) {
        jsa.add(bool_);
        return this;
    }

    @Override
    public JsonArray push(double number) {
        jsa.add(number);
        return this;
    }

    @Override
    public JsonArray push(Object value) {
        jsa.add(value);
        return this;
    }

    @Override
    public <T> T remove(int index) {
        return (T) jsa.remove(index);
    }

    @Override
    public boolean removeValue(Object value) {
        return jsa.remove(value);
    }

    @Override
    public <T extends JsonElement> T clear() {
        jsa.clear();
        return (T) this;
    }

    @Override
    public <T extends JsonElement> T copy() {
        return (T) create(new ArrayList<>(jsa));
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
        return JSONArray.toJSONString(jsa);
    }

    @Override
    public String toString() {
        return toJsonString();
    }
}
