package naga.commons.util;

import naga.commons.util.collection.Collections;

import java.util.List;

/*
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

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> String toString(T[] array) {
        return array == null ? null : Collections.toString(java.util.Arrays.asList(array).iterator());
    }

}
