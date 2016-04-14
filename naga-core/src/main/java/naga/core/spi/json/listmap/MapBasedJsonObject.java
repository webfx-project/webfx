package naga.core.spi.json.listmap;

import naga.core.spi.json.Json;
import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonObject;
import naga.core.spi.json.JsonType;
import naga.core.util.Numbers;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class MapBasedJsonObject<NO> implements JsonObject {
    protected boolean isShallowCopy;

    protected MapBasedJsonObject() {
        recreateEmptyNativeObject();
    }

    public abstract Map<String, Object> getMap();

    public abstract NO getNativeObject();

    protected abstract void recreateEmptyNativeObject();

    protected abstract void deepCopyNativeObject();

    protected void checkCopyBeforeUpdate() {
        if (isShallowCopy) {
            // deep copy the list lazily if the object is mutated
            deepCopyNativeObject();
            isShallowCopy = false;
        }
    }

    @Override
    public <T> void forEach(MapIterator<T> handler) {
        //getMap().forEach((s, o) -> handler.call(s, wrap(o))); // J2ME CLDC
        for (Map.Entry<String, Object> entry : getMap().entrySet()) {
            handler.call(entry.getKey(), wrap(entry.getValue()));
        }
    }

    protected <T> T getNative(String key) {
        return (T) getMap().get(key);
    }

    protected <T> T wrap(Object value) {
        return ListMapUtil.wrap(value);
    }

    protected Object unwrap(Object value) {
        return ListMapUtil.unwrap(value);
    }


    @Override
    public <T> T get(String key) {
        return wrap(getNative(key));
    }

    @Override
    public JsonArray getArray(String key) {
        return Json.createArray(getNative(key)) ;
    }

    @Override
    public boolean getBoolean(String key) {
        return getNative(key);
    }

    @Override
    public double getNumber(String key) {
        return Numbers.doubleValue(getNative(key));
    }

    @Override
    public JsonObject getObject(String key) {
        return Json.createObject(getNative(key));
    }

    @Override
    public String getString(String key) {
        return getNative(key);
    }

    @Override
    public JsonType getType(String key) {
        return ListMapUtil.getType(getNative(key));
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
        checkCopyBeforeUpdate();
        return (T) getMap().remove(key);
    }

    @Override
    public JsonObject set(String key, boolean bool_) {
        checkCopyBeforeUpdate();
        getMap().put(key, bool_);
        return this;
    }

    @Override
    public JsonObject set(String key, double number) {
        checkCopyBeforeUpdate();
        getMap().put(key, number);
        return this;
    }

    @Override
    public JsonObject set(String key, Object value) {
        checkCopyBeforeUpdate();
        value = unwrap(value);
        getMap().put(key, value);
        return this;
    }

    @Override
    public int size() {
        return getMap().size();
    }

    @Override
    public MapBasedJsonObject clear() {
        if (isShallowCopy) {
            recreateEmptyNativeObject();
            isShallowCopy = false;
        } else
            getMap().clear();
        return this;
    }

    @Override
    public MapBasedJsonObject copy() {
        MapBasedJsonObject copy = (MapBasedJsonObject) Json.createArray(getNativeObject());
        // We actually do the copy lazily if the object is subsequently mutated
        copy.isShallowCopy = isShallowCopy = true;
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