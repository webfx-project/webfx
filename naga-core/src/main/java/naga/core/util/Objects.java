package naga.core.util;

/*
 * @author Bruno Salmon
 */

public class Objects {

    private Objects() {
    }

    public static <T> T nonNull(T t, T nonNull) {
        return t != null ? t : nonNull;
    }

    // Casting methods

    // Conversion methods

    // from kbs

    public static boolean areEquals(Object o1, Object o2) {
        if (o1 == o2)
            return true;
        if (o1 == null || o2 == null)
            return false;
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            Object[] a1 = (Object[]) o1;
            Object[] a2 = (Object[]) o2;
            if (a1.length != a2.length)
                return false;
            for (int i = 0; i < a1.length; i++)
                if (!areEquals(a1[i], a2[i]))
                    return false;
            return true;
        }
        return o1.equals(o2);
    }

}
