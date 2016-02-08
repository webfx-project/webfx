package naga.core.util;

/**
 * @author Bruno Salmon
 */
public class Booleans {

    public static boolean isTrue(Object value) {
        return Boolean.TRUE.equals(value);
    }

    public static boolean isFalse(Object value) {
        return Boolean.FALSE.equals(value);
    }

    public static boolean booleanValue(Object value) {
        if (value == null)
            return false;
        if (value instanceof Boolean)
            return (Boolean) value;
        return Boolean.parseBoolean(value.toString());
    }
}
