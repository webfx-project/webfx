package dev.webfx.platform.shared.util;

/**
 * @author Bruno Salmon
 */
public final class Numbers {

    private static final Byte ZERO_BYTE = 0;
    private static final Short ZERO_SHORT = 0;
    private static final Integer ZERO_INTEGER = 0;
    private static final Long ZERO_LONG = 0L;
    private static final Float ZERO_FLOAT = 0f;
    private static final Double ZERO_DOUBLE = 0d;

    public static boolean isZero(Object value) {
        return value != null && (
                value == ZERO_BYTE ||
                value == ZERO_SHORT ||
                value == ZERO_INTEGER ||
                value == ZERO_LONG ||
                value.equals(ZERO_FLOAT) ||
                value.equals(ZERO_DOUBLE));
    }

    public static boolean isNotZero(Object value) {
        return !isZero(value);
    }

    public static boolean isPositive(Number number) {
        return number != null && number.intValue() >= 0;
    }

    public static Object negate(Object value) {
        if (value == null)
            return null;
        if (value instanceof Byte)
            return -(Byte) value;
        if (value instanceof Short)
            return -(Short) value;
        if (value instanceof Integer)
            return -(Integer) value;
        if (value instanceof Long)
            return -(Long) value;
        if (value instanceof Float)
            return -(Float) value;
        if (value instanceof Double)
            return -(Double) value;
        return null;
    }

    public static boolean isNumber(Object value) {
        return value instanceof Number;
    }

    public static Integer asInteger(Object value) {
        return value == null ? null : value instanceof Integer ? (Integer) value : value instanceof Number ? ((Number) value).intValue() : null;
    }

    public static Long asLong(Object value) {
        return value == null ? null : value instanceof Long ? (Long) value : value instanceof Number ? ((Number) value).longValue() : null;
    }

    public static Float asFloat(Object value) {
        return value == null ? null : value instanceof Float ? (Float) value : value instanceof Number ? ((Number) value).floatValue() : null;
    }

    public static Double asDouble(Object value) {
        return value == null ? null : value instanceof Double ? (Double) value : value instanceof Number ? ((Number) value).doubleValue() : null;
    }

    public static Byte asByte(Object value){
        return value == null ? null : value instanceof Byte ? (Byte) value : value instanceof Number ? ((Number) value).byteValue() : null;
    }

    public static Short asShort(Object value){
        return value == null ? null : value instanceof Short ? (Short) value : value instanceof Number ? ((Number) value).shortValue() : null;
    }


    public static Integer parseInteger(String s) {
        if (s == null)
            return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long parseLong(String s) {
        if (s == null)
            return null;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Float parseFloat(String s) {
        if (s == null)
            return null;
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double parseDouble(String s) {
        if (s == null)
            return null;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Byte parseByte(String s) {
        if (s == null)
            return null;
        try {
            return Byte.parseByte(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Short parseShort(String s) {
        if (s == null)
            return null;
        try {
            return Short.parseShort(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer toInteger(Object value) {
        Integer number = asInteger(value);
        if (number != null || value == null)
            return number;
        return parseInteger(value.toString());
    }

    public static Long toLong(Object value) {
        Long number = asLong(value);
        if (number != null || value == null)
            return number;
        return parseLong(value.toString());
    }

    public static Float toFloat(Object value) {
        Float number = asFloat(value);
        if (number != null || value == null)
            return number;
        return parseFloat(value.toString());
    }

    public static Double toDouble(Object value) {
        Double number = asDouble(value);
        if (number != null || value == null)
            return number;
        return parseDouble(value.toString());
    }

    public static Byte toByte(Object value) {
        Byte number = asByte(value);
        if (number != null || value == null)
            return number;
        return parseByte(value.toString());
    }

    public static Short toShort(Object value) {
        Short number = asShort(value);
        if (number != null || value == null)
            return number;
        return parseShort(value.toString());
    }

    public static int intValue(Object value) {
        Integer number = toInteger(value);
        return number == null ? 0 : number;
    }

    public static long longValue(Object value) {
        Long number = toLong(value);
        return number == null ? 0 : number;
    }

    public static float floatValue(Object value) {
        Float number = toFloat(value);
        return number == null ? 0 : number;
    }

    public static double doubleValue(Object value) {
        Double number = toDouble(value);
        return number == null ? 0 : number;
    }

    public static byte byteValue(Object value) {
        Byte number = toByte(value);
        return number == null ? 0 : number;
    }

    public static short shortValue(Object value) {
        Short number = toShort(value);
        return number == null ? 0 : number;
    }

    public static String twoDigits(int i) {
        return i < 10 ? "0" + i : Integer.toString(i);
    }
}
