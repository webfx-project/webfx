/*
 * Copyright (c) 2010, 2016, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package javafx.collections;

import com.sun.javafx.collections.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.util.Callback;

import java.util.*;

/**
 * Utility class that consists of static methods that are 1:1 copies of java.util.Collections methods.
 * <br><br>
 * The wrapper methods (like synchronizedObservableList or emptyObservableList) has exactly the same
 * functionality as the methods in Collections, with exception that they return ObservableList and are
 * therefore suitable for methods that require ObservableList on input.
 * <br><br>
 * The utility methods are here mainly for performance reasons. All methods are optimized in a way that
 * they yield only limited number of notifications. On the other hand, java.util.Collections methods
 * might call "modification methods" on an ObservableList multiple times, resulting in a number of notifications.
 *
 * @since JavaFX 2.0
 */
public class FXCollections {
    /** Not to be instantiated. */
    private FXCollections() { }

    /**
     * Constructs an ObservableList that is backed by the specified list.
     * Mutation operations on the ObservableList instance will be reported
     * to observers that have registered on that instance.<br>
     * Note that mutation operations made directly to the underlying list are
     * <em>not</em> reported to observers of any ObservableList that
     * wraps it.
     *
     * @param <E> The type of List to be wrapped
     * @param list a concrete List that backs this ObservableList
     * @return a newly created ObservableList
     */
    public static <E> ObservableList<E> observableList(List<E> list) {
        if (list == null) {
            throw new NullPointerException();
        }
        return list instanceof RandomAccess ? new ObservableListWrapper<E>(list) :
                new ObservableSequentialListWrapper<E>(list);
    }

    /**
     * Constructs an ObservableList that is backed by the specified list.
     * Mutation operations on the ObservableList instance will be reported
     * to observers that have registered on that instance.<br>
     * Note that mutation operations made directly to the underlying list are
     * <em>not</em> reported to observers of any ObservableList that
     * wraps it.
     * <br>
     * This list also reports mutations of the elements in it by using <code>extractor</code>.
     * Observable objects returned by extractor (applied to each list element) are listened for changes
     * and transformed into "update" change of ListChangeListener.
     *
     * @param <E> The type of List to be wrapped
     * @param list a concrete List that backs this ObservableList
     * @param extractor element to Observable[] convertor
     * @since JavaFX 2.1
     * @return a newly created ObservableList
     */
    public static <E> ObservableList<E> observableList(List<E> list, Callback<E, Observable[]> extractor) {
        if (list == null || extractor == null) {
            throw new NullPointerException();
        }
        return list instanceof RandomAccess ? new ObservableListWrapper<E>(list, extractor) :
            new ObservableSequentialListWrapper<E>(list, extractor);
    }

    /**
     * Constructs an ObservableMap that is backed by the specified map.
     * Mutation operations on the ObservableMap instance will be reported
     * to observers that have registered on that instance.<br>
     * Note that mutation operations made directly to the underlying map are <em>not</em>
     * reported to observers of any ObservableMap that wraps it.
     * @param <K> the type of the wrapped key
     * @param <V> the type of the wrapped value
     * @param map a Map that backs this ObservableMap
     * @return a newly created ObservableMap
     */
    public static <K, V> ObservableMap<K, V> observableMap(Map<K, V> map) {
        if (map == null) {
            throw new NullPointerException();
        }
        return new ObservableMapWrapper<K, V>(map);
    }

    /**
     * Constructs an ObservableSet that is backed by the specified set.
     * Mutation operations on the ObservableSet instance will be reported
     * to observers that have registered on that instance.<br>
     * Note that mutation operations made directly to the underlying set are <em>not</em>
     * reported to observers of any ObservableSet that wraps it.
     * @param set a Set that backs this ObservableSet
     * @return a newly created ObservableSet
     * @since JavaFX 2.1
     */
    public static <E> ObservableSet<E> observableSet(Set<E> set) {
        if (set == null) {
            throw new NullPointerException();
        }
        return new ObservableSetWrapper<E>(set);
    }

    /**
     * Constructs an ObservableSet backed by a HashSet
     * that contains all the specified elements.
     * @param elements elements that will be added into returned ObservableSet
     * @return a newly created ObservableSet
     * @since JavaFX 2.1
     */
    public static <E> ObservableSet<E> observableSet(E... elements) {
        if (elements == null) {
            throw new NullPointerException();
        }
        Set<E> set = new HashSet<E>(elements.length);
        Collections.addAll(set, elements);
        return new ObservableSetWrapper<E>(set);
    }


    // removed

    /**
     * Creates a new empty observable list that is backed by an arraylist.
     * @see #observableList(java.util.List)
     * @param <E> The type of List to be wrapped
     * @return a newly created ObservableList
     */
    @SuppressWarnings("unchecked")
    public static <E> ObservableList<E> observableArrayList() {
        return observableList(new ArrayList());
    }

    /**
     * Creates a new empty observable list backed by an arraylist.
     *
     * This list reports element updates.
     * @param <E> The type of List to be wrapped
     * @param extractor element to Observable[] convertor. Observable objects are listened for changes on the element.
     * @see #observableList(java.util.List, javafx.util.Callback)
     * @since JavaFX 2.1
     * @return a newly created ObservableList
     */
    public static <E> ObservableList<E> observableArrayList(Callback<E, Observable[]> extractor) {
        return observableList(new ArrayList(), extractor);
    }

    /**
     * Creates a new observable array list with {@code items} added to it.
     * @param <E> The type of List to be wrapped
     * @param items the items that will be in the new observable ArrayList
     * @return a newly created observableArrayList
     * @see #observableArrayList()
     */
    public static <E> ObservableList<E> observableArrayList(E... items) {
        ObservableList<E> list = observableArrayList();
        list.addAll(items);
        return list;
    }

    /**
     * Creates a new observable array list and adds a content of collection {@code col}
     * to it.
     * @param <E> The type of List to be wrapped
     * @param col a collection which content should be added to the observableArrayList
     * @return a newly created observableArrayList
     */
    public static <E> ObservableList<E> observableArrayList(Collection<? extends E> col) {
        ObservableList<E> list = observableArrayList();
        list.addAll(col);
        return list;
    }

    /**
     * Creates a new empty observable map that is backed by a HashMap.
     * @param <K> the type of the wrapped key
     * @param <V> the type of the wrapped value
     * @return a newly created observable HashMap
     */
    public static <K,V> ObservableMap<K,V> observableHashMap() {
        return observableMap(new HashMap<K, V>());
    }

    /**
     * Concatenates more observable lists into one. The resulting list
     * would be backed by an arraylist.
     * @param <E> The type of List to be wrapped
     * @param lists lists to concatenate
     * @return new observable array list concatenated from the arguments
     */
    public static <E> ObservableList<E> concat(ObservableList<E>... lists) {
        if (lists.length == 0 ) {
            return observableArrayList();
        }
        if (lists.length == 1) {
            return observableArrayList(lists[0]);
        }
        ArrayList<E> backingList = new ArrayList<E>();
        for (ObservableList<E> s : lists) {
            backingList.addAll(s);
        }

        return observableList(backingList);
    }

    /**
     * Creates and returns unmodifiable wrapper list on top of provided observable list.
     * @param list  an ObservableList that is to be wrapped
     * @param <E> The type of List to be wrapped
     * @return an ObserableList wrapper that is unmodifiable
     * @see Collections#unmodifiableList(java.util.List)
     */
    public static<E> ObservableList<E> unmodifiableObservableList(ObservableList<E> list) {
        if (list == null) {
            throw new NullPointerException();
        }
        return new UnmodifiableObservableListImpl<E>(list);
    }

    /**
     * Creates and returns a typesafe wrapper on top of provided observable list.
     * @param <E> The type of List to be wrapped
     * @param list  an Observable list to be wrapped
     * @param type   the type of element that <tt>list</tt> is permitted to hold
     * @return a dynamically typesafe view of the specified list
     * @see Collections#checkedList(java.util.List, java.lang.Class)
     */
    // removed

    /**
     * Creates and returns a synchronized wrapper on top of provided observable list.
     * @param <E> The type of List to be wrapped
     * @param  list the list to be "wrapped" in a synchronized list.
     * @return A synchronized version of the observable list
     * @see Collections#synchronizedList(java.util.List)
     */
    public static<E> ObservableList<E> synchronizedObservableList(ObservableList<E> list) {
        if (list == null) {
            throw new NullPointerException();
        }
        return new SynchronizedObservableList<E>(list);
    }

    private static ObservableList EMPTY_OBSERVABLE_LIST = new EmptyObservableList();


    /**
     * Creates and empty unmodifiable observable list.
     * @param <E> The type of List to be wrapped
     * @return An empty unmodifiable observable list
     * @see Collections#emptyList()
     */
    @SuppressWarnings("unchecked")
    public static<E> ObservableList<E> emptyObservableList() {
        return EMPTY_OBSERVABLE_LIST;
    }

    /**
     * Creates an unmodifiable observable list with single element.
     * @param <E> The type of List to be wrapped
     * @param e the only elements that will be contained in this singleton observable list
     * @return a singleton observable list
     * @see Collections#singletonList(java.lang.Object)
     */
    public static<E> ObservableList<E> singletonObservableList(E e) {
        return new SingletonObservableList<E>(e);
    }

    // removed

    /**
     * Copies elements from src to dest. Fires only <b>one</b> change notification on dest.
     * @param <T> The type of List to be wrapped
     * @param dest the destination observable list
     * @param src the source list
     * @see Collections#copy(java.util.List, java.util.List)
     */
    @SuppressWarnings("unchecked")
    public static <T> void copy(ObservableList<? super T> dest, List<? extends T> src) {
        final int srcSize = src.size();
        if (srcSize > dest.size()) {
            throw new IndexOutOfBoundsException("Source does not fit in dest");
        }
        T[] destArray = (T[]) dest.toArray();
        System.arraycopy(src.toArray(), 0, destArray, 0, srcSize);
        dest.setAll(destArray);
    }

    /**
     * Fills the provided list with obj. Fires only <b>one</b> change notification on the list.
     * @param <T> The type of List to be wrapped
     * @param list the list to fill
     * @param obj the object to fill the list with
     * @see Collections#fill(java.util.List, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public static <T> void fill(ObservableList<? super T> list, T obj) {
        T[] newContent = (T[]) new Object[list.size()];
        Arrays.fill(newContent, obj);
        list.setAll(newContent);
    }

    /**
     * Replace all oldVal elements in the list with newVal element.
     * Fires only <b>one</b> change notification on the list.
     * @param <T> The type of List to be wrapped
     * @param list the list which will have it's elements replaced
     * @param oldVal the element that is going to be replace
     * @param newVal the replacement
     * @return true if the list was modified
     * @see Collections#replaceAll(java.util.List, java.lang.Object, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean replaceAll(ObservableList<T> list, T oldVal, T newVal) {
        T[] newContent = (T[]) list.toArray();
        boolean modified = false;
        for (int i = 0 ; i < newContent.length; ++i) {
            if (newContent[i].equals(oldVal)) {
                newContent[i] = newVal;
                modified = true;
            }
        }
        if (modified) {
            list.setAll(newContent);
        }
        return modified;
    }

    /**
     * Reverse the order in the list
     * Fires only <b>one</b> change notification on the list.
     * @param list the list to be reversed
     * @see Collections#reverse(java.util.List)
     */
    @SuppressWarnings("unchecked")
    public static void reverse(ObservableList list) {
        Object[] newContent = list.toArray();
        for (int i = 0; i < newContent.length / 2; ++i) {
            Object tmp = newContent[i];
            newContent[i] = newContent[newContent.length - i - 1];
            newContent[newContent.length -i - 1] = tmp;
        }
        list.setAll(newContent);
    }

    /**
     * Rotates the list by distance.
     * Fires only <b>one</b> change notification on the list.
     * @param list the list to be rotated
     * @param distance the distance of rotation
     * @see Collections#rotate(java.util.List, int)
     */
    @SuppressWarnings("unchecked")
    public static void rotate(ObservableList list, int distance) {
        Object[] newContent = list.toArray();

        int size = list.size();
        distance = distance % size;
        if (distance < 0)
            distance += size;
        if (distance == 0)
            return;

        for (int cycleStart = 0, nMoved = 0; nMoved != size; cycleStart++) {
            Object displaced = newContent[cycleStart];
            Object tmp;
            int i = cycleStart;
            do {
                i += distance;
                if (i >= size)
                    i -= size;
                tmp = newContent[i];
                newContent[i] = displaced;
                displaced = tmp;
                nMoved ++;
            } while(i != cycleStart);
        }
        list.setAll(newContent);
    }

    /**
     * Shuffles all elements in the observable list.
     * Fires only <b>one</b> change notification on the list.
     * @param list the list to shuffle
     * @see Collections#shuffle(java.util.List)
     */
    public static void shuffle(ObservableList<?> list) {
        if (r == null) {
            r = new Random();
        }
        shuffle(list, r);
    }
    private static Random r;

    /**
     * Shuffles all elements in the observable list.
     * Fires only <b>one</b> change notification on the list.
     * @param list the list to be shuffled
     * @param rnd the random generator used for shuffling
     * @see Collections#shuffle(java.util.List, java.util.Random)
     */
    @SuppressWarnings("unchecked")
    public static void shuffle(ObservableList list, Random rnd) {
        Object newContent[] = list.toArray();

        for (int i = list.size(); i > 1; i--) {
            swap(newContent, i - 1, rnd.nextInt(i));
        }

        list.setAll(newContent);
    }

    private static void swap(Object[] arr, int i, int j) {
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * Sorts the provided observable list.
     * Fires only <b>one</b> change notification on the list.
     * @param <T> The type of List to be wrapped
     * @param list the list to be sorted
     * @see Collections#sort(java.util.List)
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<? super T>> void sort(ObservableList<T> list) {
        if (list instanceof SortableList) {
            ((SortableList<? extends T>)list).sort();
        } else {
            List<T> newContent = new ArrayList<T>(list);
            Collections.sort(newContent);
            list.setAll((Collection<T>)newContent);
        }
    }

    /**
     * Sorts the provided observable list using the c comparator.
     * Fires only <b>one</b> change notification on the list.
     * @param <T> The type of List to be wrapped
     * @param list the list to sort
     * @param c comparator used for sorting. Null if natural ordering is required.
     * @see Collections#sort(java.util.List, java.util.Comparator)
     */
    @SuppressWarnings("unchecked")
    public static <T> void sort(ObservableList<T> list, Comparator<? super T> c) {
        if (list instanceof SortableList) {
            ((SortableList<? extends T>)list).sort(c);
        } else {
            List<T> newContent = new ArrayList<T>(list);
            Collections.sort(newContent, c);
            list.setAll((Collection<T>)newContent);
        }
    }

    private static class EmptyObservableList<E> extends AbstractList<E> implements ObservableList<E> {

        private static final ListIterator iterator = new ListIterator() {

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Object previous() {
                throw new NoSuchElementException();
            }

            @Override
            public int nextIndex() {
                return 0;
            }

            @Override
            public int previousIndex() {
                return -1;
            }

            @Override
            public void set(Object e) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void add(Object e) {
                throw new UnsupportedOperationException();
            }
        };

        public EmptyObservableList() {
        }

        @Override
        public final void addListener(InvalidationListener listener) {
        }

        @Override
        public final void removeListener(InvalidationListener listener) {
        }


        @Override
        public void addListener(ListChangeListener<? super E> o) {
        }

        @Override
        public void removeListener(ListChangeListener<? super E> o) {
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Iterator<E> iterator() {
            return iterator;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return c.isEmpty();
        }

        @Override
        public E get(int index) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public int indexOf(Object o) {
            return -1;
        }

        @Override
        public int lastIndexOf(Object o) {
            return -1;
        }

        @Override
        @SuppressWarnings("unchecked")
        public ListIterator<E> listIterator() {
            return iterator;
        }

        @Override
        @SuppressWarnings("unchecked")
        public ListIterator<E> listIterator(int index) {
            if (index != 0) {
                throw new IndexOutOfBoundsException();
            }
            return iterator;
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            if (fromIndex != 0 || toIndex != 0) {
                throw new IndexOutOfBoundsException();
            }
            return this;
        }

        @Override
        public boolean addAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setAll(Collection<? extends E> col) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(int from, int to) {
            throw new UnsupportedOperationException();
        }
    }

    private static class SingletonObservableList<E> extends AbstractList<E> implements ObservableList<E> {

        private final E element;

        public SingletonObservableList(E element) {
            if (element == null) {
                throw new NullPointerException();
            }
            this.element = element;
        }

        @Override
        public boolean addAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setAll(Collection<? extends E> col) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(E... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addListener(InvalidationListener listener) {
        }

        @Override
        public void removeListener(InvalidationListener listener) {
        }

        @Override
        public void addListener(ListChangeListener<? super E> o) {
        }

        @Override
        public void removeListener(ListChangeListener<? super E> o) {
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return element.equals(o);
        }

        @Override
        public E get(int index) {
            if (index != 0) {
                throw new IndexOutOfBoundsException();
            }
            return element;
        }

    }

    private static class UnmodifiableObservableListImpl<T> extends ObservableListBase<T> implements ObservableList<T> {

        private final ObservableList<T> backingList;
        private final ListChangeListener<T> listener;

        public UnmodifiableObservableListImpl(ObservableList<T> backingList) {
            this.backingList = backingList;
            listener = c -> {
                fireChange(new SourceAdapterChange<T>(UnmodifiableObservableListImpl.this, c));
            };
            this.backingList.addListener(new WeakListChangeListener<T>(listener));
        }

        @Override
        public T get(int index) {
            return backingList.get(index);
        }

        @Override
        public int size() {
            return backingList.size();
        }

        @Override
        public boolean addAll(T... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setAll(T... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setAll(Collection<? extends T> col) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(T... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(T... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(int from, int to) {
            throw new UnsupportedOperationException();
        }

    }

    private static class SynchronizedList<T> implements List<T> {
        final Object mutex;
        private final List<T> backingList;

        SynchronizedList(List<T> list, Object mutex) {
            this.backingList = list;
            this.mutex = mutex;
        }

        @Override
        public int size() {
            synchronized(mutex) {
                return backingList.size();
            }
        }

        @Override
        public boolean isEmpty() {
            synchronized(mutex) {
                return backingList.isEmpty();
            }
        }

        @Override
        public boolean contains(Object o) {
            synchronized(mutex) {
                return backingList.contains(o);
            }
        }

        @Override
        public Iterator<T> iterator() {
            return backingList.iterator();
        }

        @Override
        public Object[] toArray() {
            synchronized(mutex)  {
                return backingList.toArray();
            }
        }

        @Override
        public <T> T[] toArray(T[] a) {
            synchronized(mutex) {
                return backingList.toArray(a);
            }
        }

        @Override
        public boolean add(T e) {
            synchronized(mutex) {
                return backingList.add(e);
            }
        }

        @Override
        public boolean remove(Object o) {
            synchronized(mutex) {
                return backingList.remove(o);
            }
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            synchronized(mutex) {
                return backingList.containsAll(c);
            }
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            synchronized(mutex) {
                return backingList.addAll(c);
            }
        }

        @Override
        public boolean addAll(int index, Collection<? extends T> c) {
            synchronized(mutex) {
                return backingList.addAll(index, c);

            }
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            synchronized(mutex) {
                return backingList.removeAll(c);
            }
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            synchronized(mutex) {
                return backingList.retainAll(c);
            }
        }

        @Override
        public void clear() {
            synchronized(mutex) {
                backingList.clear();
            }
        }

        @Override
        public T get(int index) {
            synchronized(mutex) {
                return backingList.get(index);
            }
        }

        @Override
        public T set(int index, T element) {
            synchronized(mutex) {
                return backingList.set(index, element);
            }
        }

        @Override
        public void add(int index, T element) {
            synchronized(mutex) {
                backingList.add(index, element);
            }
        }

        @Override
        public T remove(int index) {
            synchronized(mutex) {
                return backingList.remove(index);
            }
        }

        @Override
        public int indexOf(Object o) {
            synchronized(mutex) {
                return backingList.indexOf(o);
            }
        }

        @Override
        public int lastIndexOf(Object o) {
            synchronized(mutex) {
                return backingList.lastIndexOf(o);
            }
        }

        @Override
        public ListIterator<T> listIterator() {
            return backingList.listIterator();
        }

        @Override
        public ListIterator<T> listIterator(int index) {
            synchronized(mutex) {
                return backingList.listIterator(index);
            }
        }

        @Override
        public List<T> subList(int fromIndex, int toIndex) {
            synchronized(mutex) {
                return new SynchronizedList<T>(backingList.subList(fromIndex, toIndex),
                        mutex);
            }
        }

        @Override
        public String toString() {
            synchronized(mutex) {
                return backingList.toString();
            }
        }

        @Override
        public int hashCode() {
            synchronized(mutex) {
                return backingList.hashCode();
            }
        }

        @Override
        public boolean equals(Object o) {
            synchronized(mutex) {
                return backingList.equals(o);
            }
        }

    }

    private static class SynchronizedObservableList<T> extends SynchronizedList<T> implements ObservableList<T> {

        private ListListenerHelper helper;

        private final ObservableList<T> backingList;
        private final ListChangeListener<T> listener;

        SynchronizedObservableList(ObservableList<T> seq, Object mutex) {
            super(seq, mutex);
            this.backingList = seq;
            listener = c -> {
                ListListenerHelper.fireValueChangedEvent(helper, new SourceAdapterChange<T>(SynchronizedObservableList.this, c));
            };
            backingList.addListener(new WeakListChangeListener<T>(listener));
        }

        SynchronizedObservableList(ObservableList<T> seq) {
            this(seq, new Object());
        }

        @Override
        public boolean addAll(T... elements) {
            synchronized(mutex) {
                return backingList.addAll(elements);
            }
        }

        @Override
        public boolean setAll(T... elements) {
            synchronized(mutex) {
                return backingList.setAll(elements);
            }
        }

        @Override
        public boolean removeAll(T... elements) {
            synchronized(mutex) {
                return backingList.removeAll(elements);
            }
        }

        @Override
        public boolean retainAll(T... elements) {
            synchronized(mutex) {
                return backingList.retainAll(elements);
            }
        }

        @Override
        public void remove(int from, int to) {
            synchronized(mutex) {
                backingList.remove(from, to);
            }
        }

        @Override
        public boolean setAll(Collection<? extends T> col) {
            synchronized(mutex) {
                return backingList.setAll(col);
            }
        }

        @Override
        public final void addListener(InvalidationListener listener) {
            synchronized (mutex) {
                helper = ListListenerHelper.addListener(helper, listener);
            }
        }

        @Override
        public final void removeListener(InvalidationListener listener) {
            synchronized (mutex) {
                helper = ListListenerHelper.removeListener(helper, listener);
            }
        }

        @Override
        public void addListener(ListChangeListener<? super T> listener) {
            synchronized (mutex) {
                helper = ListListenerHelper.addListener(helper, listener);
            }
        }

        @Override
        public void removeListener(ListChangeListener<? super T> listener) {
            synchronized (mutex) {
                helper = ListListenerHelper.removeListener(helper, listener);
            }
        }


    }

    // removed

    // removed

}
