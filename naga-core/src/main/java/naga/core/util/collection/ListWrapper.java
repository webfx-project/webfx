package naga.core.util.collection;

import java.io.Serializable;
import java.util.*;

/**
 * @author Bruno Salmon
 */
public class ListWrapper<E> implements List<E>, Serializable {
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

    public List<E> getWrappedList() {
        return wrappedList;
    }

    public void add(int index, E element) {
        wrappedList.add(index, element);
    }

    public boolean add(E o) {
        return wrappedList.add(o);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        return wrappedList.addAll(index, c);
    }

    public boolean addAll(Collection<? extends E> c) {
        return wrappedList.addAll(c);
    }

    public void clear() {
        wrappedList.clear();
    }

    public boolean contains(Object o) {
        return wrappedList.contains(o);
    }

    public boolean containsAll(Collection c) {
        return wrappedList.containsAll(c);
    }

    public boolean equals(Object o) {
        return wrappedList.equals(o);
    }

    public final E get(int index) {
        return wrappedList.get(index);
    }

    public int hashCode() {
        return wrappedList.hashCode();
    }

    public final int indexOf(Object o) {
        return wrappedList.indexOf(o);
    }

    public final boolean isEmpty() {
        return wrappedList == null || wrappedList.isEmpty();
    }

    public final Iterator<E> iterator() {
        return wrappedList.iterator();
    }

    public final int lastIndexOf(Object o) {
        return wrappedList.lastIndexOf(o);
    }

    public final ListIterator<E> listIterator() {
        return wrappedList.listIterator();
    }

    public final ListIterator<E> listIterator(int index) {
        return wrappedList.listIterator(index);
    }

    public E remove(int index) {
        return wrappedList.remove(index);
    }

    public boolean remove(Object o) {
        return wrappedList.remove(o);
    }

    public boolean removeAll(Collection<?> c) {
        return wrappedList.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return wrappedList.retainAll(c);
    }

    public E set(int index, E element) {
        return wrappedList.set(index, element);
    }

    public final int size() {
        if (wrappedList == null)
            return 0;
        return wrappedList.size();
    }

    public final List<E> subList(int fromIndex, int toIndex) {
        return wrappedList.subList(fromIndex, toIndex);
    }

    public final Object[] toArray() {
        return wrappedList.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return wrappedList.toArray(a);
    }

    public String toString() {
        return wrappedList.toString();
    }
}
