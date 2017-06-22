package naga.commons.util.collection;

import naga.commons.util.function.Consumer;
import naga.commons.util.function.Converter;
import naga.commons.util.function.IntFunction;
import naga.commons.util.function.Predicate;

import java.util.*;

/**
 * @author Bruno Salmon
 */
public class Collections {

    public static <T> void forEach(Iterable<T> iterable, Consumer<T> consumer) {
        // iterable.forEach(consumer); // Not GWT compilable for now
        for (T element : iterable)
            consumer.accept(element);
    }

    public static <A, B> List<B> map(Collection<A> aCollection, Converter<A, B> aToBConverter) {
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

    public static <A, B> List<B> mapFilter(Collection<A> aCollection, Converter<A, B> aToBConverter, Predicate<? super B> predicate) {
        // return aCollection.stream().map(aToBConverter::convert).collect(Collectors.toList()); // Not GWT compilable for now
        List<B> bList = new ArrayList<>(aCollection.size());
        forEach(aCollection, a -> {
            B b = aToBConverter.convert(a);
            if (predicate == null || predicate.test(b))
                bList.add(b);
        });
        return bList;
    }

    public static <A> int sum(Collection<A> aCollection, Converter<A, Integer> aToIntConverter) {
        //return aCollection.stream().mapToInt(aToIntConverter).sum(); // Not GWT compilable for now
        int sum = 0;
        for(A a : aCollection) {
            Integer number = aToIntConverter.convert(a);
            if (number != null)
                sum += number;
        }
        return sum;
    }

    public static <T> T findFirst(Iterable<T> iterable, Predicate<? super T> predicate) {
        //return collection.stream().filter(predicate::test).findFirst().get(); // Not GWT compilable for now
        for (T element : iterable) {
            if (predicate.test(element))
                return element;
        }
        return null;
    }

    public static <T> boolean hasNoOneMatching(Iterable<T> iterable, Predicate<? super T> predicate) {
        return findFirst(iterable, predicate) == null;
    }

    public static <T> boolean hasAtLeastOneMatching(Iterable<T> iterable, Predicate<? super T> predicate) {
        return !hasNoOneMatching(iterable, predicate);
    }

    public static <T> int indexOf(Iterable<T> iterable, Predicate<? super T> predicate) {
        if (iterable != null) {
            int index = 0;
            for (T element : iterable) {
                if (predicate.test(element))
                    return index;
                index++;
            }
        }
        return -1;
    }


    public static <T> boolean removeIf(Iterable<T> iterable, Predicate<? super T> predicate) {
        boolean removed = false;
        final Iterator<T> each = iterable.iterator();
        while (each.hasNext()) {
            if (predicate.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
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

    public static <T> T get(List<T> list, int i) {
        if (list == null || i < 0 || i >= list.size())
            return null;
        return list.get(i);
    }

    public static <T> boolean addIfNotNull(T element, Collection<T> collection) {
        if (element == null || collection == null)
            return false;
        collection.add(element);
        return true;
    }

    public static <T> boolean allNonNulls(List<T> list) {
        return list.indexOf(null) == -1;
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
        for (Long value : collection)
            array[i++] = value;
        return array;
    }

    public static float[] toFloatArray(Collection<Float> collection) {
        int n = collection.size(), i = 0;
        float[] array = new float[n];
        for (Float value : collection)
            array[i++] = value;
        return array;
    }

    public static float[] doubleCollectionToFloatArray(Collection<Double> collection) {
        int n = collection.size(), i = 0;
        float[] array = new float[n];
        for (Double value : collection)
            array[i++] = value.floatValue();
        return array;
    }

    public static String toString(Iterable iterable) {
        return toString(iterable, true, false);
    }

    public static String toString(Iterator iterator) {
        return toString(iterator, true, false);
    }

    public static String toStringWithLineFeeds(Iterable iterable) {
        return toString(iterable, true, true);
    }

    public static String toStringWithLineFeeds(Iterator iterator) {
        return toString(iterator, true, true);
    }

    public static String toStringWithNoBrackets(Iterable iterable) {
        return toString(iterable, false, false);
    }

    public static String toStringWithNoBrackets(Iterator iterator) {
        return toString(iterator, false, false);
    }

    private static String toString(Iterable iterable, boolean brackets, boolean lineFeeds) {
        return toString(iterable.iterator(), brackets, lineFeeds);
    }

    private static String toString(Iterator it, boolean brackets, boolean lineFeeds) {
        if (it == null)
            return null;
        if (!it.hasNext())
            return brackets ? "[]" : "";
        StringBuilder sb = new StringBuilder();
        if (brackets)
            sb.append('[');
        int initialLength = sb.length();
        while (it.hasNext()) {
            int length = sb.length();
            if (length > 0) {
                if (length > initialLength)
                    sb.append(", ");
                if (lineFeeds)
                    sb.append('\n');
            }
            sb.append(it.next());
        }
        if (brackets) {
            if (lineFeeds)
                sb.append('\n');
            sb.append(']');
        }
        return sb.toString();
    }
}
