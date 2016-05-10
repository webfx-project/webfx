package naga.core.composite.listmap;

import naga.core.composite.WritableCompositeObject;

import java.util.Collection;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class MapBasedCompositeObject implements WritableCompositeObject, ListMapCompositeElement {
    protected boolean isShallowCopy;

    protected MapBasedCompositeObject() {
        recreateEmptyNativeObject();
    }

    protected MapBasedCompositeObject(Map<String, Object> map) {
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
    public Collection<String> keys() {
        return getMap().keySet();
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
    public void setNativeElement(String key, Object element) {
        checkCopyBeforeUpdate();
        getMap().put(key, element);
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
    public MapBasedCompositeObject copy() {
        MapBasedCompositeObject copy = (MapBasedCompositeObject) nativeToCompositeArray(getNativeElement());
        // We actually do the copy lazily if the object is subsequently mutated
        copy.isShallowCopy = isShallowCopy = true;
        return copy;
    }

    /*@Override
    public String toString() {
        return toJsonString();
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MapBasedCompositeObject that = (MapBasedCompositeObject) o;

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