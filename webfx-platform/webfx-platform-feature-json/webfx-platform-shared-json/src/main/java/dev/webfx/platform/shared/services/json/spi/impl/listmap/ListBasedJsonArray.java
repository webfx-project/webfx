package dev.webfx.platform.shared.services.json.spi.impl.listmap;

import dev.webfx.platform.shared.services.json.WritableJsonArray;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class ListBasedJsonArray implements WritableJsonArray, ListMapBasedJsonElement {
    protected boolean isShallowCopy;

    protected ListBasedJsonArray() {
        recreateEmptyNativeArray();
    }

    protected ListBasedJsonArray(List<Object> list) {
        setList(list);
    }

    public abstract List<Object> getList();

    protected abstract void setList(List<Object> list);

    protected void recreateEmptyNativeArray() {
        setList((List) createNativeArray());
    }

    protected void deepCopyNativeArray() {
        setList(ListMapUtil.convertList(getList()));
    }

    protected void checkCopyBeforeUpdate() {
        if (isShallowCopy) {
            // deep copy the list lazily if the object is mutated
            deepCopyNativeArray();
            isShallowCopy = false;
        }
    }

    @Override
    public Object getNativeElement(int index) {
        return getList().get(index);
    }

    @Override
    public int indexOfNativeElement(Object element) {
        return getList().indexOf(element);
    }

    @Override
    public int size() {
        return getList().size();
    }

    @Override
    public ListBasedJsonArray pushNativeElement(Object element) {
        getList().add(element);
        return this;
    }

    @Override
    public ListBasedJsonArray setNativeElement(int index, Object value) {
        getList().set(index, value);
        return this;
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
        ListBasedJsonArray copy = (ListBasedJsonArray) nativeToJavaJsonArray(getNativeElement());
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