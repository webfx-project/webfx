package naga.core.spi.json.listmap;

import naga.core.spi.json.Json;
import naga.core.spi.json.JsonArray;

import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class ListBasedJsonArray<NA> extends ListMapJsonElement implements JsonArray {
    protected boolean isShallowCopy;

    protected ListBasedJsonArray() {
        recreateEmptyNativeArray();
    }

    public abstract List<Object> getList();

    protected abstract NA getNativeArray();

    protected abstract void recreateEmptyNativeArray();

    protected abstract void deepCopyNativeArray();

    protected void checkCopyBeforeUpdate() {
        if (isShallowCopy) {
            // deep copy the list lazily if the object is mutated
            deepCopyNativeArray();
            isShallowCopy = false;
        }
    }


    @Override
    public Object getRaw(int index) {
        return getList().get(index);
    }

    @Override
    public int indexOfRaw(Object value) {
        return getList().indexOf(value);
    }

    @Override
    public int size() {
        return getList().size();
    }

    @Override
    public void pushRaw(Object value) {
        getList().add(value);
    }

    @Override
    public void setRaw(int index, Object value) {
        getList().set(index, value);
    }

    @Override
    public Collection values() {
        return getList();
    }

    @Override
    public <T> T remove(int index) {
        checkCopyBeforeUpdate();
        return (T) getList().remove(index);
    }

    @Override
    public void clear() {
        if (isShallowCopy) {
            recreateEmptyNativeArray();
            isShallowCopy = false;
        } else
            getList().clear();
    }

    @Override
    public ListBasedJsonArray copy() {
        ListBasedJsonArray copy = (ListBasedJsonArray) Json.createArray(getNativeArray());
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

        ListBasedJsonArray that = (ListBasedJsonArray) o;

        List thisList = this.getList();
        List thatList = that.getList();
        if (thisList.size() != thatList.size())
            return false;

        java.util.Iterator<?> iter = thatList.iterator();
        for (Object entry : thisList) {
            Object other = iter.next();
            if (entry == null) {
                if (other != null)
                    return false;
            } else if (!entry.equals(other))
                return false;
        }
        return true;
    }
}