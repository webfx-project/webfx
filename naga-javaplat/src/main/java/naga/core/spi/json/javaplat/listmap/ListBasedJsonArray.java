package naga.core.spi.json.javaplat.listmap;

import naga.core.spi.json.Json;
import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonObject;
import naga.core.spi.json.JsonType;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class ListBasedJsonArray<NA> implements JsonArray {
    protected boolean isShallowCopy;

    protected ListBasedJsonArray() {
        recreateEmptyNativeArray();
    }

    public abstract List getList();

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
    public <T> void forEach(ListIterator<T> handler) {
        int i = 0;
        for (Object o : getList())
            handler.call(i++, ListMapUtil.wrap(o));
    }

    protected <T> T getNative(int index) {
        return (T) getList().get(index);
    }

    @Override
    public <T> T get(int index) {
        return ListMapUtil.wrap(getNative(index));
    }

    @Override
    public JsonArray getArray(int index) {
        return Json.createArray(getNative(index));
    }

    @Override
    public boolean getBoolean(int index) {
        return getNative(index);
    }

    @Override
    public double getNumber(int index) {
        return ((Number) getNative(index)).doubleValue();
    }

    @Override
    public JsonObject getObject(int index) {
        return Json.createObject(getNative(index));
    }

    @Override
    public String getString(int index) {
        return getNative(index);
    }

    @Override
    public JsonType getType(int index) {
        return ListMapUtil.getType(getNative(index));
    }

    @Override
    public int indexOf(Object value) {
        return getList().indexOf(value);
    }

    @Override
    public ListBasedJsonArray insert(int index, Object value) {
        checkCopyBeforeUpdate();
        value = ListMapUtil.unwrap(value);
        getList().add(index, value);
        return this;
    }

    @Override
    public int length() {
        return getList().size();
    }

    @Override
    public ListBasedJsonArray push(boolean bool_) {
        checkCopyBeforeUpdate();
        getList().add(bool_);
        return this;
    }

    @Override
    public ListBasedJsonArray push(double number) {
        checkCopyBeforeUpdate();
        getList().add(number);
        return this;
    }

    @Override
    public ListBasedJsonArray push(Object value) {
        checkCopyBeforeUpdate();
        value = ListMapUtil.unwrap(value);
        getList().add(value);
        return this;
    }

    @Override
    public <T> T remove(int index) {
        checkCopyBeforeUpdate();
        return (T) getList().remove(index);
    }

    @Override
    public boolean removeValue(Object value) {
        checkCopyBeforeUpdate();
        return getList().remove(value);
    }

    @Override
    public ListBasedJsonArray clear() {
        if (isShallowCopy) {
            recreateEmptyNativeArray();
            isShallowCopy = false;
        } else
            getList().clear();
        return this;
    }

    @Override
    public ListBasedJsonArray copy() {
        ListBasedJsonArray copy = (ListBasedJsonArray) Json.createArray(getNativeArray());
        // We actually do the copy lazily if the object is subsequently mutated
        copy.isShallowCopy = isShallowCopy = true;
        return copy;
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