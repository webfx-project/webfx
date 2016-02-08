package naga.core.util;

/**
 * @author Bruno Salmon
 */
public class Strings {

    public static String asString(Object value) {
        return (String) value;
    }

    public static String toString(Object value) {
        return value == null ? null : value.toString();
    }

    public static String stringValue(Object value) {
        return value == null ? null : value.toString();
    }
}
