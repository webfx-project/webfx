package dev.webfx.platform.shared.util.collection;

import java.io.Serializable;
import java.util.*;

/**
 * @author Bruno Salmon
 */
public class ListWrapper<E> implements ListMixin<E>, Serializable {
    protected List<E> wrappedList;

    protected ListWrapper() {
        this(new ArrayList<E>());
    }

    protected ListWrapper(List<E> wrappedList) {
        setWrappedList(wrappedList);
    }

    protected void setWrappedList(List<E> wrappedList) {
        if (wrappedList == null)
            throw new NullPointerException();
        /*while (wrappedList instanceof ListWrapper && !(wrappedList instanceof HashList))
            wrappedList = ((ListWrapper) wrappedList).getWrappedList();*/
        this.wrappedList = wrappedList;
    }

    public List<E> getList() {
        return wrappedList;
    }

    public int hashCode() {
        return wrappedList.hashCode();
    }

    public boolean equals(Object o) {
        return getList().equals(o);
    }

    public String toString() {
        return wrappedList.toString();
    }
}
