package naga.commons.util.collection;

import naga.commons.util.function.Consumer;
import naga.commons.util.function.Converter;
import naga.commons.util.function.IntFunction;
import naga.commons.util.function.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class Collections {

    public static <T> void forEach(Iterable<T> iterable, Consumer<T> consumer) {
        // iterable.forEach(consumer); // Not GWT compilable for now
        for (T element : iterable)
            consumer.accept(element);
    }

    public static <A, B> List<B> convert(Collection<A> aCollection, Converter<A, B> aToBConverter) {
        // return aCollection.stream().map(aToBConverter::convert).collect(Collectors.toList()); // Not GWT compilable for now
        List<B> bList = new ArrayList<>(aCollection.size());
        forEach(aCollection, a -> bList.add(aToBConverter.convert(a)));
        return bList;
    }

    public static <T> List<T> filter(Iterable<T> iterable, Predicate<? super T> predicate) {
        // return collection.stream().filter(predicate).collect(Collectors.toList()); // Not GWT compilable for now
        List<T> list = new ArrayList<>();
        forEach(iterable, element -> {
            if (predicate.test(element))
                list.add(element);
        });
        return list;
    }

    public static <T> T findFirst(Iterable<T> iterable, Predicate<? super T> predicate) {
        //return collection.stream().filter(predicate::test).findFirst().get(); // Not GWT compilable for now
        for (T element : iterable) {
            if (predicate.test(element))
                return element;
        }
        return null;
    }

    public static int size(Collection collection) {
        return collection == null ? 0 : collection.size();
    }

    public static boolean isEmpty(Collection collection) {
        return size(collection) == 0;
    }

    public static <T> T first(List<T> list) {
        return isEmpty(list) ? null : list.get(0);
    }

    public static <T> T last(List<T> list) {
        int size = size(list);
        return size == 0 ? null : list.get(size - 1);
    }

    public static <T> boolean addIfNotNull(T element, Collection<T> collection) {
        if (element == null || collection == null)
            return false;
        collection.add(element);
        return true;
    }

    public static <T> Iterator<T> iterator(Iterable<T> iterable) {
        return iterable == null ? null : iterable.iterator();
    }

    public static <T> T next(Iterator<T> it) {
        return it == null || !it.hasNext() ? null : it.next();
    }

    public static <T> T[] toArray(Collection<T> collection, IntFunction<T[]> arrayGenerator) {
        return collection.toArray(arrayGenerator.apply(collection.size()));
    }

    public static long[] toLongArray(Collection<Long> collection) {
        int n = collection.size(), i = 0;
        long[] array = new long[n];
        for (Long l : collection)
            array[i++] = l;
        return array;
    }

    public static String toString(Iterable iterable) {
        return toString(iterator(iterable));
    }

    public static String toString(Iterator it) {
        return toString(it, false);
    }

    public static String toStringWithLineFeeds(Iterable iterable) {
        return toStringWithLineFeeds(iterator(iterable));
    }

    public static String toStringWithLineFeeds(Iterator it) {
        return toString(it, true);
    }

    private static String toString(Iterator it, boolean lineFeeds) {
        if (it == null)
            return null;
        if (!it.hasNext())
            return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            if (lineFeeds)
                sb.append('\n');
            Object e = it.next();
            sb.append(e);
            if (!it.hasNext())
                return sb.append(lineFeeds ? "\n]" : "]").toString();
            sb.append(',').append(' ');
        }
    }
}
