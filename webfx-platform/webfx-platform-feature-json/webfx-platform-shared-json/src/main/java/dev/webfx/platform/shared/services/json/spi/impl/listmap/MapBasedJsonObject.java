package dev.webfx.platform.shared.services.json.spi.impl.listmap;

import dev.webfx.platform.shared.services.json.WritableJsonObject;
import dev.webfx.platform.shared.services.json.JsonArray;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class MapBasedJsonObject implements WritableJsonObject, ListMapBasedJsonElement {
    protected boolean isShallowCopy;

    protected MapBasedJsonObject() {
        recreateEmptyNativeObject();
    }

    protected MapBasedJsonObject(Map<String, Object> map) {
        setMap(map);
    }

    public abstract Map<String, Object> getMap();

    protected abstract void setMap(Map<String, Object> map);

    protected void recreateEmptyNativeObject() {
        setMap((Map) createNativeObject());
    }

    protected void deepCopyNativeObject() {
        setMap(ListMapUtil.convertMap(getMap()));
    }

    @Override
    public int size() {
        return getMap().size();
    }

    @Override
    public boolean has(String key) {
        return getMap().containsKey(key);
    }

    @Override
    public JsonArray keys() {
        return nativeToJavaJsonArray(new ArrayList(getMap().keySet()));
    }

    public Object getNativeElement(String key) {
        return getMap().get(key);
    }

    @Override
    public <T> T remove(String key) {
        checkCopyBeforeUpdate();
        return (T) getMap().remove(key);
    }

    @Override
    public MapBasedJsonObject setNativeElement(String key, Object element) {
        checkCopyBeforeUpdate();
        getMap().put(key, element);
        return this;
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
        MapBasedJsonObject copy = (MapBasedJsonObject) nativeToJavaJsonObject(getNativeElement());
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