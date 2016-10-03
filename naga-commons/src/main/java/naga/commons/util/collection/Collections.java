package naga.commons.util.collection;

import naga.commons.util.function.Consumer;
import naga.commons.util.function.Converter;
import naga.commons.util.function.IntFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class Collections {

    public static <T> void forEach(Collection<T> collection, Consumer<T> consumer) {
        // collection.forEach(consumer); // Not GWT compilable for now
        for (T element : collection)
            consumer.accept(element);
    }

    public static <A, B> List<B> convert(Collection<A> aList, Converter<A, B> aToBConverter) {
        // return aList.stream().map(aToBConverter::convert).collect(Collectors.toList()); // Not GWT compilable for now
        List<B> bList = new ArrayList<>(aList.size());
        forEach(aList, a -> bList.add(aToBConverter.convert(a)));
        return bList;
    }

    public static int size(Collection collection) {
        return collection == null ? 0 : collection.size();
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

    public static String toString(Iterator it) {
        return toString(it, false);
    }

    public static String toStringWithLineFeeds(Iterator it) {
        return toString(it, true);
    }

    private static String toString(Iterator it, boolean lineFeeds) {
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
