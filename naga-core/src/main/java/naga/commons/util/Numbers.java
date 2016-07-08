package naga.commons.util;

/**
 * @author Bruno Salmon
 */
public class Numbers {

    private static final Byte ZERO_BYTE = 0;
    private static final Short ZERO_SHORT = 0;
    private static final Integer ZERO_INTEGER = 0;
    private static final Long ZERO_LONG = 0L;
    private static final Float ZERO_FLOAT = 0f;
    private static final Double ZERO_DOUBLE = 0d;

    public static boolean isZero(Object number) {
        return number != null && (
                number == ZERO_BYTE ||
                number == ZERO_SHORT ||
                number == ZERO_INTEGER ||
                number == ZERO_LONG ||
                number.equals(ZERO_FLOAT) ||
                number.equals(ZERO_DOUBLE));
    }

    public static boolean isNotZero(Object number) {
        return !isZero(number);
    }

    public static boolean isNumber(Object number) {
        return number != null && (
                number instanceof Byte ||
                number instanceof Short ||
                number instanceof Integer ||
                number instanceof Long ||
                number instanceof Float ||
                number instanceof Double);
    }

    public static Integer toInteger(Object value) {
        if (value == null)
            return null;
        try {
            return (int) value;
        } catch (Exception e) {
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException e1) {
                return (int) doubleValue(value);
            }
        }
    }

    public static Long toLong(Object value) {
        if (value == null)
            return null;
        try {
            return (long) value;
        } catch (Exception e) {
            try {
                return Long.parseLong(value.toString());
            } catch (NumberFormatException e1) {
                return null;
            }
        }
    }

    public static Float toFloat(Object value) {
        if (value == null)
            return null;
        try {
            return (float) value;
        } catch (Exception e) {
            try {
                return Float.parseFloat(value.toString());
            } catch (NumberFormatException e1) {
                return null;
            }
        }
    }

    public static Double toDouble(Object value) {
        if (value == null)
            return null;
        try {
            return (double) value;
        } catch (Exception e) {
            try {
                return Double.parseDouble(value.toString());
            } catch (NumberFormatException e1) {
                return null;
            }
        }
    }

    public static int intValue(Object value) {
        return value == null ? 0 : toInteger(value);
    }

    public static long longValue(Object value) {
        return value == null ? 0 : toLong(value);
    }

    public static float floatValue(Object value) {
        return value == null ? 0 : toFloat(value);
    }

    public static double doubleValue(Object value) {
        return value == null ? 0 : toDouble(value);
    }

    public static Object negate(Object n) {
        if (n instanceof Byte)
            return -(Byte) n;
        if (n instanceof Short)
            return -(Short) n;
        if (n instanceof Integer)
            return -(Integer) n;
        if (n instanceof Long)
            return -(Long) n;
        if (n instanceof Float)
            return -(Float) n;
        if (n instanceof Double)
            return -(Double) n;
        throw new IllegalArgumentException("Not a number: " + n);
    }
}
