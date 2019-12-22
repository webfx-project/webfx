package webfx.framework.client.orm.reactive.mapping.dql_to_entities;

import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * A List wrapper class that implements observability.
 *
 * @author Bruno Salmon
 */
class ObservableListWrapper<E> extends ModifiableObservableListBase<E> implements ObservableList<E>, RandomAccess {

    protected final List<E> backingList;

    ObservableListWrapper(List<E> list) {
        backingList = list;
    }

    @Override
    public E get(int index) {
        return backingList.get(index);
    }

    @Override
    public int size() {
        return backingList.size();
    }

    @Override
    protected void doAdd(int index, E element) {
        backingList.add(index, element);
    }

    @Override
    protected E doSet(int index, E element) {
        return backingList.set(index, element);
    }

    @Override
    protected E doRemove(int index) {
        return backingList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return backingList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return backingList.lastIndexOf(o);
    }

    @Override
    public boolean contains(Object o) {
        return backingList.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return backingList.containsAll(c);
    }

    @Override
    public void clear() {
        if (hasListeners()) {
            beginChange();
            nextRemove(0, this);
        }
        backingList.clear();
        ++modCount;
        if (hasListeners())
            endChange();
    }

    @Override
    public void remove(int fromIndex, int toIndex) {
        beginChange();
        for (int i = fromIndex; i < toIndex; ++i)
            remove(fromIndex);
        endChange();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return removeOrRetainAll(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return removeOrRetainAll(c, false);
    }

    private boolean removeOrRetainAll(Collection<?> c, boolean remove) {
        BitSet bs = presenceBitSet(c, remove);
        beginChange();
        if (!bs.isEmpty()) {
            int cur = size();
            while ((cur = bs.previousSetBit(cur - 1)) >= 0)
                remove(cur);
        }
        endChange();
        return !bs.isEmpty();
    }

    private BitSet presenceBitSet(Collection<?> c, boolean present) {
        BitSet bs = new BitSet(c.size());
        int size = size();
        for (int i = 0; i < size; ++i)
            if (c.contains(get(i)) == present)
                bs.set(i);
        return bs;
    }
}