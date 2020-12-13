package dev.webfx.platform.shared.util.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Bruno Salmon
 */
public interface ListMixin<E> extends List<E> {

    List<E> getList();

    default void add(int index, E element) {
        getList().add(index, element);
    }

    default boolean add(E o) {
        return getList().add(o);
    }

    default boolean addAll(int index, Collection<? extends E> c) {
        return getList().addAll(index, c);
    }

    default boolean addAll(Collection<? extends E> c) {
        return getList().addAll(c);
    }

    default void clear() {
        getList().clear();
    }

    default boolean contains(Object o) {
        return getList().contains(o);
    }

    default boolean containsAll(Collection c) {
        return getList().containsAll(c);
    }

    default E get(int index) {
        return getList().get(index);
    }

    default int indexOf(Object o) {
        return getList().indexOf(o);
    }

    default boolean isEmpty() {
        return getList() == null || getList().isEmpty();
    }

    default Iterator<E> iterator() {
        return getList().iterator();
    }

    default int lastIndexOf(Object o) {
        return getList().lastIndexOf(o);
    }

    default ListIterator<E> listIterator() {
        return getList().listIterator();
    }

    default ListIterator<E> listIterator(int index) {
        return getList().listIterator(index);
    }

    default E remove(int index) {
        return getList().remove(index);
    }

    default boolean remove(Object o) {
        return getList().remove(o);
    }

    default boolean removeAll(Collection<?> c) {
        return getList().removeAll(c);
    }

    default boolean retainAll(Collection<?> c) {
        return getList().retainAll(c);
    }

    default E set(int index, E element) {
        return getList().set(index, element);
    }

    default int size() {
        return getList().size();
    }

    default List<E> subList(int fromIndex, int toIndex) {
        return getList().subList(fromIndex, toIndex);
    }

    default Object[] toArray() {
        return getList().toArray();
    }

    default <T> T[] toArray(T[] a) {
        return getList().toArray(a);
    }

}
