package naga.core.spi.json.listmap;

import naga.core.spi.json.Json;
import naga.core.spi.json.JsonObject;

import java.util.Collection;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class MapBasedJsonObject<NO> extends ListMapJsonElement implements JsonObject {
    protected boolean isShallowCopy;

    protected MapBasedJsonObject() {
        recreateEmptyNativeObject();
    }

    public abstract Map<String, Object> getMap();

    public abstract NO getNativeObject();

    protected abstract void recreateEmptyNativeObject();

    protected abstract void deepCopyNativeObject();

    @Override
    public int size() {
        return getMap().size();
    }

    @Override
    public boolean has(String key) {
        return getMap().containsKey(key);
    }

    @Override
    public Collection<String> keys() {
        return getMap().keySet();
    }

    @Override
    public Collection values() {
        return getMap().values();
    }

    public Object getRaw(String key) {
        return getMap().get(key);
    }


    @Override
    public <T> T remove(String key) {
        checkCopyBeforeUpdate();
        return (T) getMap().remove(key);
    }

    @Override
    public void setRaw(String key, Object value) {
        checkCopyBeforeUpdate();
        getMap().put(key, value);
    }

    protected void checkCopyBeforeUpdate() {
        if (isShallowCopy) {
            // deep copy the list lazily if the object is mutated
            deepCopyNativeObject();
            isShallowCopy = false;
        }
    }

    @Override
    public void clear() {
        if (isShallowCopy) {
            recreateEmptyNativeObject();
            isShallowCopy = false;
        } else
            getMap().clear();
    }

    @Override
    public MapBasedJsonObject copy() {
        MapBasedJsonObject copy = (MapBasedJsonObject) Json.createArray(getNativeObject());
        // We actually do the copy lazily if the object is subsequently mutated
        copy.isShallowCopy = isShallowCopy = true;
        return copy;
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