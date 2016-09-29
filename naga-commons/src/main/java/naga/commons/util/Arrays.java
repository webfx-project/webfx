package naga.commons.util;

import naga.commons.util.collection.Collections;
import naga.commons.util.function.Consumer;

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

    public static <T> String toString(T[] array) {
        return array == null ? null : Collections.toString(asList(array).iterator());
    }

    public static <T> String toStringWithLineFeeds(T[] array) {
        return array == null ? null : Collections.toStringWithLineFeeds(asList(array).iterator());
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

    public static <T> List<T> nonNullsAsList(T[] array) {
        return collectNonNulls(array, new ArrayList<>(length(array)));
    }

    public static <T> int indexOfNull(T[] array) {
        int n = length(array);
        for (int i = 0; i < n; i++)
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
        return array == null ? null : array[index];
    }

    public static <T> String getString(T[] array, int index) {
        return Strings.toString(getValue(array, index));
    }
}
