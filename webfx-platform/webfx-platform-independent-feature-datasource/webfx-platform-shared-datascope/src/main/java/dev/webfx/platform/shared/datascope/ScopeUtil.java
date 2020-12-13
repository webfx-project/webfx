package dev.webfx.platform.shared.datascope;

import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public final class ScopeUtil {

    public static <T> boolean arraysIntersect(T[] a1, T[] a2) {
        for (T e1 : a1)
            for (T e2 : a2)
                if (Objects.equals(e1, e2))
                    return true;
        return false;
    }
}
