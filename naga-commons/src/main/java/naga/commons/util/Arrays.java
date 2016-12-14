package naga.commons.util;

import naga.commons.util.collection.Collections;
import naga.commons.util.function.Consumer;
import naga.commons.util.function.Converter;
import naga.commons.util.function.IntFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */

public class Arrays {

    private Arrays() {
    }

    public static <T> T[] toArray(T... a) {
        return a;
    }

    public static <T> List<T> asList(T... a) {
        return java.util.Arrays.asList(a);
    }

    public static <T> int length(T[] array) {
        return array == null ? 0 : array.length;
    }

    public static <T> boolean isEmpty(T[] array) {
        return length(array) == 0;
    }

    public static <T> T first(T[] array) {
        return isEmpty(array) ? null : array[0];
    }

    public static <T> T last(T[] list) {
        int length = length(list);
        return length == 0 ? null : list[length - 1];
    }

    public static <T> String toString(T[] array) {
        return Collections.toString(asList(array));
    }

    public static <T> String toStringWithLineFeeds(T[] array) {
        return Collections.toStringWithLineFeeds(asList(array));
    }

    public static <T> void forEach(T[] array, Consumer<T> consumer) {
        for (T element : array)
            consumer.accept(element);
    }

    public static <T> void forEachNonNull(T[] array, Consumer<T> consumer) {
        for (T element : array)
            if (element != null)
                consumer.accept(element);
    }

    public static <T> List<T> collectNonNulls(T[] array, List<T> list) {
        forEachNonNull(array, list::add);
        return list;
    }

    public static <T> T[] nonNulls(IntFunction<T[]> arrayGenerator, T... array) {
        if (allNonNulls(array))
            return array;
        return Collections.toArray(collectNonNulls(array, new ArrayList<>(array.length)), arrayGenerator);
    }

    public static <A, B> B[] convert(A[] aArray, Converter<A, B> aToBConverter, IntFunction<B[]> arrayGenerator) {
        int length = aArray.length;
        B[] bArray = arrayGenerator.apply(length);
        for (int i = 0; i < length; i++)
            bArray[i] = aToBConverter.convert(aArray[i]);
        return bArray;
    }

    public static <A> A[] clone(A[] aArray, IntFunction<A[]> arrayGenerator) {
        return convert(aArray, a -> a, arrayGenerator);
    }

    public static <T> List<T> nonNullsAsList(T[] array) {
        return collectNonNulls(array, new ArrayList<>(length(array)));
    }

    public static <T> int indexOfNull(T[] array) {
        for (int i = 0, n = length(array); i < n; i++)
            if (array[i] == null)
                return i;
        return -1;
    }

    public static <T> boolean allNonNulls(T[] array) {
        return indexOfNull(array) == -1;
    }

    public static <T> boolean hasNulls(T[] array) {
        return !allNonNulls(array);
    }

    public static <T> T getValue(T[] array, int index) {
        return array == null || index >= array.length ? null : array[index];
    }

    public static <T> String getString(T[] array, int index) {
        return Strings.toString(getValue(array, index));
    }
}
