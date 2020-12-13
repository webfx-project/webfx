package dev.webfx.platform.shared.util;

/**
 * @author Bruno Salmon
 */
public final class Objects {

    public static <T> T coalesce(T... ts) {
        for (T t : ts)
            if (t != null)
                return t;
        return null;
    }

    public static boolean nonNull(Object obj) {
        return obj != null;
    }

    public static <T> boolean allNulls(T... ts) {
        return coalesce(ts) == null;
    }

    public static <T> boolean anyNotNull(T... ts) {
        return !allNulls(ts);
    }

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
