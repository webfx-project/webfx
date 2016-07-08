package naga.commons.util;

import java.time.LocalDateTime;

/**
 * @author Bruno Salmon
 */
public class Dates {

    public static LocalDateTime toLocalDateTime(Object value) {
        if (value == null)
            return null;
        if (value instanceof LocalDateTime)
            return (LocalDateTime) value;
        if (Numbers.isNumber(value))
            return LocalDateTime.ofEpochSecond(Numbers.longValue(value) / 1000, 0, null);
        return LocalDateTime.parse(value.toString());
    }

    /**
     * Formats a date
     * @param date the date to format
     * @param pattern the format pattern, ex: dd/MM/yyyy HH:mm:ss
     * @return the formatted date
     */

    public static String format(LocalDateTime date, String pattern) {
        if (date == null || pattern == null)
            return null;
        String s = pattern;
        if (pattern.contains("dd"))
            s = Strings.replaceAll(s, "dd", twoDigits(date.getDayOfMonth()));
        if (pattern.contains("MM"))
            s = Strings.replaceAll(s, "MM", twoDigits(date.getMonthValue()));
        if (pattern.contains("yyyy"))
            s = Strings.replaceAll(s, "yyyy", twoDigits(date.getYear()));
        if (pattern.contains("HH"))
            s = Strings.replaceAll(s, "HH", twoDigits(date.getHour()));
        if (pattern.contains("mm"))
            s = Strings.replaceAll(s, "mm", twoDigits(date.getMinute()));
        if (pattern.contains("ss"))
            s = Strings.replaceAll(s, "ss", twoDigits(date.getSecond()));
        return s;
    }

    public static String format(Object date, String pattern) {
        return format(toLocalDateTime(date), pattern);
    }

    private static String twoDigits(int i) {
        return i < 10 ? "0" + i : Integer.toString(i);
    }

}
