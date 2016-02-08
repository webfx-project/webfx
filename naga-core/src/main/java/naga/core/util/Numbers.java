package naga.core.util;

import java.util.Date;

/**
 * @author Bruno Salmon
 */
public class Numbers {

    public static Number asNumber(Object value) {
        return (Number) value;
    }

    public static Number toNumber(Object value) {
        return value == null ? null : (Number) value;
    }

    public static Integer toInteger(Object value) {
        return (Integer) toNumber(value);
    }

    public static int toInt(Object value) {
        return toNumber(value).intValue();
    }

    public static int intValue(Object value) {
        if (value == null)
            return 0;
        if (value instanceof Number)
            return ((Number) value).intValue();
        if (value instanceof Date)
            return (int) ((Date) value).getTime();
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static long longValue(Object value) {
        if (value == null)
            return 0;
        if (value instanceof Number)
            return ((Number) value).longValue();
        if (value instanceof Date)
            return ((Date) value).getTime();
        String s = value.toString();
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            /*
            try {
                return Long.parseLong(Strings.removeWhiteSpaces(s));
            } catch (Exception e2) {
                return 0;
            }
            */
            return 0;
        }
    }

    public static float floatValue(Object value) {
        if (value == null)
            return 0;
        if (value instanceof Number)
            return ((Number) value).floatValue();
        if (value instanceof Date)
            return ((Date) value).getTime();
        try {
            return Float.parseFloat(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static double doubleValue(Object value) {
        if (value == null)
            return 0;
        if (value instanceof Number)
            return ((Number) value).doubleValue();
        if (value instanceof Date)
            return ((Date) value).getTime();
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
