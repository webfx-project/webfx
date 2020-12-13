package dev.webfx.platform.shared.util.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Bruno Salmon
 */
public final class HashList<E> extends ListWrapper<E> {
    private final HashMap<E, E> hashMap;

    public HashList() {
        hashMap = new HashMap<>();
    }

    public HashList(Collection<E> collection) {
        this();
        for (E e : collection)
            add(e);
    }

    public void add(int index, E element) {
        if (hashMap.put(element, element) == null)
            wrappedList.add(index, element);
    }

    public boolean add(E o) {
        if (hashMap.put(o, o) != null)
            return false;
        wrappedList.add(o);
        return true;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        int n = size();
        for (E e : c)
            add(index++, e);
        return n != size();
    }

    public boolean addAll(Collection<? extends E> c) {
        int n = size();
        for (E e : c)
            add(e);
        return n != size();
    }

    public void clear() {
        wrappedList.clear();
        hashMap.clear();
    }

    public boolean contains(Object o) {
        return hashMap.containsKey(o);
    }

    public boolean containsAll(Collection c) {
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }

    public E remove(int index) {
        E removed = wrappedList.remove(index);
        hashMap.remove(removed);
        return removed;
    }

    public boolean remove(Object o) {
        if (hashMap.remove(o) == null)
            return false;
        return wrappedList.remove(o);
    }

    public boolean removeAll(Collection c) {
        boolean modified = false;
        if (size() > c.size()) {
            for (Iterator<?> i = c.iterator(); i.hasNext(); )
                modified |= remove(i.next());
        } else {
            for (Iterator<?> i = iterator(); i.hasNext(); ) {
                if (c.contains(i.next())) {
                    i.remove();
                    modified = true;
                }
            }
        }
        if (modified)
            wrappedList.removeAll(c);
        return modified;
    }

    public boolean retainAll(Collection c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        if (modified)
            wrappedList.retainAll(c);
        return modified;
    }

    public E set(int index, E element) {
        E oldElement = wrappedList.set(index, element);
        hashMap.put(element, element);
        return oldElement;
    }

    public E getExistingElement(E element) {
        return hashMap.get(element);
    }
}
