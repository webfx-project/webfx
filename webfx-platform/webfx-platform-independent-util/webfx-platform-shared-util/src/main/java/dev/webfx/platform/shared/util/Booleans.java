package dev.webfx.platform.shared.util;

/**
 * @author Bruno Salmon
 */
public final class Booleans {

    public static boolean isTrue(Object value) {
        return Boolean.TRUE.equals(value);
    }

    public static boolean isFalse(Object value) {
        return Boolean.FALSE.equals(value);
    }

    public static boolean isNotTrue(Object value) {
        return !Boolean.TRUE.equals(value);
    }

    public static boolean isNotFalse(Object value) {
        return !Boolean.FALSE.equals(value);
    }

    public static Boolean asBoolean(Object value) {
        if (value instanceof Boolean)
            return (Boolean) value;
        return null;
    }

    public static Boolean parseBoolean(String s) {
        if (s == null)
            return null;
        if (s.equalsIgnoreCase("true"))
            return true;
        if (s.equalsIgnoreCase("false"))
            return false;
        return null;
    }

    public static Boolean toBoolean(Object value) {
        Boolean bool = asBoolean(value);
        if (bool != null || value == null)
            return bool;
        return parseBoolean(value.toString());
    }

    public static boolean booleanValue(Object value) {
        Boolean bool = toBoolean(value);
        return bool == null ? false : bool;
    }
}
