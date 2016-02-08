package naga.core.util;

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

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
}
