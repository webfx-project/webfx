package naga.core.spi.json.javaplat.listmap;

import naga.core.spi.json.Json;
import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonObject;
import naga.core.spi.json.JsonType;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class MapBasedJsonObject<NO> implements JsonObject {
    protected boolean needsCopy;

    public abstract Map<String, Object> getMap();

    protected abstract NO getNativeObject();

    protected abstract void recreateEmptyNativeObject();

    protected abstract void deepCloneNativeObject();

    protected void checkCopy() {
        if (needsCopy) {
            // deep copy the list lazily if the object is mutated
            deepCloneNativeObject();
            needsCopy = false;
        }
    }

    @Override
    public <T> void forEach(MapIterator<T> handler) {
        getMap().forEach((s, o) -> handler.call(s, ListMapUtil.wrap(o)));
    }

    protected <T> T get0(String key) {
        return (T) getMap().get(key);
    }

    @Override
    public <T> T get(String key) {
        return ListMapUtil.wrap(get0(key));
    }

    @Override
    public JsonArray getArray(String key) {
        return Json.createArray(get0(key)) ;
    }

    @Override
    public boolean getBoolean(String key) {
        return get0(key);
    }

    @Override
    public double getNumber(String key) {
        return ((Number) get0(key)).doubleValue();
    }

    @Override
    public JsonObject getObject(String key) {
        return Json.createObject(get0(key));
    }

    @Override
    public String getString(String key) {
        return get0(key);
    }

    @Override
    public JsonType getType(String key) {
        return ListMapUtil.getType(get0(key));
    }

    @Override
    public boolean has(String key) {
        return getMap().containsKey(key);
    }

    @Override
    public JsonArray keys() {
        return Json.createArray(new ArrayList(getMap().keySet()));
    }

    @Override
    public <T> T remove(String key) {
        checkCopy();
        return (T) getMap().remove(key);
    }

    @Override
    public JsonObject set(String key, boolean bool_) {
        checkCopy();
        getMap().put(key, bool_);
        return this;
    }

    @Override
    public JsonObject set(String key, double number) {
        checkCopy();
        getMap().put(key, number);
        return this;
    }

    @Override
    public JsonObject set(String key, Object value) {
        checkCopy();
        value = ListMapUtil.unwrap(value);
        getMap().put(key, value);
        return this;
    }

    @Override
    public int size() {
        return getMap().size();
    }

    @Override
    public MapBasedJsonObject clear() {
        if (needsCopy) {
            recreateEmptyNativeObject();
            needsCopy = false;
        } else
            getMap().clear();
        return this;
    }

    @Override
    public MapBasedJsonObject copy() {
        MapBasedJsonObject copy = (MapBasedJsonObject) Json.createArray(getNativeObject());
        // We actually do the copy lazily if the object is subsequently mutated
        copy.needsCopy = true;
        needsCopy = true;
        return copy;
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
    public String toString() {
        return toJsonString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MapBasedJsonObject that = (MapBasedJsonObject) o;

        Map<String, Object> thisMap = getMap();
        Map<String, Object> thatMap = that.getMap();
        if (thisMap.size() != thatMap.size())
            return false;

        for (Map.Entry<String, Object> entry : thisMap.entrySet()) {
            Object val = entry.getValue();
            if (val == null) {
                if (thatMap.get(entry.getKey()) != null)
                    return false;
            } else if (!entry.getValue().equals(thatMap.get(entry.getKey())))
                return false;
        }
        return true;
    }
}