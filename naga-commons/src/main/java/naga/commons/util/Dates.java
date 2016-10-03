package naga.commons.util;

import java.time.*;
import java.time.format.DateTimeParseException;

import static java.time.temporal.ChronoField.MILLI_OF_SECOND;

/**
 * @author Bruno Salmon
 */
public class Dates {

    public static Instant asInstant(Object value) {
        return value == null || !(value instanceof Instant) ? null : (Instant) value;
    }

    public static LocalDateTime asLocalDateTime(Object value) {
        return value == null || !(value instanceof LocalDateTime) ? null : (LocalDateTime) value;
    }

    public static Instant parseIsoInstant(String s) {
        if (s == null)
            return null;
        try {
            return Instant.parse(s);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static Instant fastCheckParseIsoInstant(String s) {
        if (s == null || !s.endsWith("Z") || s.length() != 23)
            return null;
        return parseIsoInstant(s);
    }

    public static Object fastToInstantIfIsoString(Object value) {
        if (value instanceof String) {
            Instant instant = fastCheckParseIsoInstant((String) value);
            if (instant != null)
                value = instant;
        }
        return value;
    }

    public static LocalDate parseIsoLocalDate(String s) {
        if (s == null)
            return null;
        try {
            return LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static LocalDateTime parseIsoLocalDateTime(String s) {
        if (s == null)
            return null;
        try {
            return LocalDateTime.parse(s);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static Instant toInstant(Object value) {
        Instant instant = asInstant(value);
        if (instant != null || value == null)
            return instant;
        instant = parseIsoInstant(value.toString());
        if (instant != null)
            return instant;
        Long l = Numbers.toLong(value);
        if (l != null)
            return Instant.ofEpochMilli(l);
        return null;
    }

    public static String formatIso(Instant instant) {
        return formatIso(LocalDateTime.ofInstant(instant, ZoneId.of("Z")));
    }

    public static LocalDateTime toLocalDateTime(Object value) {
        LocalDateTime localDateTime = asLocalDateTime(value);
        if (localDateTime != null || value == null)
            return localDateTime;
        localDateTime = parseIsoLocalDateTime(value.toString());
        if (localDateTime != null)
            return localDateTime;
        Long l = Numbers.toLong(value);
        if (l != null)
            return epochMillisUtcToLocalDateTime(l);
        return null;
    }

    public static LocalDateTime epochMillisUtcToLocalDateTime(long epochMillis) {
        return epochMillisToLocalDateTime(epochMillis, ZoneOffset.UTC);
    }

    public static LocalDateTime epochMillisToLocalDateTime(long epochMillis, ZoneOffset offset) {
        return LocalDateTime.ofEpochSecond(epochMillis / 1000, (int) (epochMillis % 1000), offset);
    }

    public static long localDateTimeToEpochMillisUtc(LocalDateTime date) {
        return localDateTimeToEpochMillis(date, ZoneOffset.UTC);
    }

    public static long localDateTimeToEpochMillis(LocalDateTime date, ZoneOffset offset) {
        return date.toEpochSecond(offset) * 1000 + date.get(MILLI_OF_SECOND);
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

    public static String formatIso(LocalDateTime date) {
        return format(date, "yyyy-MM-ddTHH:mm:ss.00Z");
    }

    public static String format(Object date, String pattern) {
        return format(toLocalDateTime(date), pattern);
    }

    private static String twoDigits(int i) {
        return i < 10 ? "0" + i : Integer.toString(i);
    }

}
