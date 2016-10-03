package mongoose.activities.shared.logic.time;

import naga.commons.util.Dates;
import naga.commons.util.Numbers;
import naga.commons.util.Objects;
import naga.commons.util.Strings;
import naga.commons.util.collection.Collections;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
class TimeConverter {

    static long convertTime(long time, TimeUnit srcTu, TimeUnit dstTu) {
        if (srcTu == dstTu)
            return time;
        return dstTu.convert(time, srcTu);
    }

    static long convertIncludedStart(long start, TimeUnit srcTu, TimeUnit dstTu) {
        return convertTime(start, srcTu, dstTu);
    }

    static long convertExcludedEnd(long end, TimeUnit srcTu, TimeUnit dstTu) {
        if (dstTu == TimeUnit.DAYS && srcTu.compareTo(dstTu) < 0)
            end = end + oneDay(srcTu) - 1;
        return convertTime(end, srcTu, dstTu);
    }

    public static long oneDay(TimeUnit timeUnit) {
        return convertTime(1, TimeUnit.DAYS, timeUnit);
    }

    private static long floorToDay(long time, TimeUnit timeUnit) {
        if (timeUnit == TimeUnit.DAYS)
            return time;
        return convertTime(convertTime(time, timeUnit, TimeUnit.DAYS), TimeUnit.DAYS, timeUnit);
    }

    static TimeInterval convertInterval(TimeInterval src, TimeUnit srcTu, TimeUnit dstTu) {
        if (srcTu == dstTu)
            return src;
        return new TimeInterval(convertIncludedStart(src.getIncludedStart(), srcTu, dstTu), convertExcludedEnd(src.getExcludedEnd(), srcTu, dstTu));
    }

    static long[] convertDaysArray(long[] src, TimeUnit srcTu, TimeUnit dstTu) {
        if (srcTu == dstTu)
            return src;
        long[] dst = new long[src.length];
        for (int i = 0; i < src.length; i++)
            dst[i] = convertIncludedStart(src[i], srcTu, dstTu);
        return dst;
    }

    static TimeInterval[] convertSeries(TimeInterval[] src, TimeUnit srcTu, TimeUnit dstTu) {
        if (srcTu == dstTu)
            return src;
        TimeInterval[] dst = new TimeInterval[src.length];
        for (int i = 0; i < src.length; i++)
            dst[i] = convertInterval(src[i], srcTu, dstTu);
        return dst;
    }

    static TimeInterval[] convertIntervalToSeries(TimeInterval src) {
        return new TimeInterval[]{src};
    }

    static TimeInterval[] convertDaysArrayToSeries(long[] src, TimeUnit srcTu) {
        List<TimeInterval> series = new ArrayList<>();
        long includedStart = 0;
        long excludedEnd = 0;
        long oneDay = oneDay(srcTu);
        for (long day : src) {
            if (includedStart == excludedEnd)
                includedStart = day;
            else if (day != excludedEnd) { // > 1 day => break of the consecutive dates
                series.add(new TimeInterval(includedStart, excludedEnd));
                includedStart = day;
            }
            excludedEnd = day + oneDay;
        }
        if (includedStart != excludedEnd)
            series.add(new TimeInterval(includedStart, excludedEnd));
        return intervalListToArray(series);
    }

    private static TimeInterval[] intervalListToArray(Collection<TimeInterval> series) {
        return Collections.toArray(series, TimeInterval[]::new);
    }

    static TimeInterval convertSeriesToInterval(TimeInterval[] src) {
        int n = src.length;
        if (n >= 2)
            return new TimeInterval(src[0].getIncludedStart(), src[n - 1].getExcludedEnd());
        return n == 1 ? src[0] : TimeInterval.EMPTY_INTERVAL;
    }

    static long[] convertSeriesToDaysArray(TimeInterval[] src, TimeUnit timeUnit) {
        List<Long> days = new ArrayList<>();
        for (TimeInterval aSrc : src)
            appendIntervalToDays(aSrc, days, timeUnit);
        return Collections.toLongArray(days);
    }

    private static List<Long> appendIntervalToDays(TimeInterval interval, List<Long> days, TimeUnit timeUnit) {
        long oneDay = oneDay(timeUnit);
        long extra = oneDay <= 1 ? 0 : oneDay - 1;
        long includedFirstDay = floorToDay(interval.getIncludedStart(), timeUnit);
        long excludedLastDay = floorToDay(interval.getExcludedEnd() + extra, timeUnit);
        for (long day = includedFirstDay; day < excludedLastDay; day += oneDay)
            days.add(day);
        return days;
    }

    static String convertSeriesToText(TimeInterval[] series, TimeUnit timeUnit) {
        return convertSeriesToText(series, timeUnit, new StringBuilder()).toString();
    }

    private static StringBuilder convertSeriesToText(TimeInterval[] series, TimeUnit timeUnit, StringBuilder sb) {
        for (TimeInterval interval : series)
            convertIntervalToText(interval, timeUnit, sb.length() > 0 ? sb.append(", ") : sb);
        return sb;
    }

    public static String convertIntervalToText(TimeInterval interval, TimeUnit timeUnit) {
        return convertIntervalToText(interval, timeUnit, new StringBuilder()).toString();
    }

    private static StringBuilder convertIntervalToText(TimeInterval interval, TimeUnit timeUnit, StringBuilder sb) {
        String startText = formatTime(interval.getIncludedStart(), timeUnit, false);
        sb.append(startText);
        String endText = formatTime(interval.getExcludedEnd(), timeUnit, true);
        if (!endText.equals(startText))
            sb.append(" - ").append(endText);
        return sb;
    }
    
    public static String formatTime(long time, TimeUnit timeUnit, boolean excluded) {
        long epochMillis = convertTime(time, timeUnit, TimeUnit.MILLISECONDS);
        LocalDateTime date = Dates.epochMillisUtcToLocalDateTime(epochMillis);
        String format = "dd/MM/yyyy HH:mm";
        if (date.getYear() == 1970)
            format = "HH:mm";
        else if (date.getHour() == 0 && date.getMinute() == 0) {
            format = "dd/MM/yyyy";
            if (excluded)
                date = date.minusDays(1);
        }
        return Dates.format(date, format);
    }

    static TimeInterval[] convertTextToSeries(String text, TimeUnit timeUnit) {
        List<TimeInterval> series = new ArrayList<>();
        String[] commaTokens = Strings.split(text, ",");
        TimeInterval lastInterval = null;
        for (String commaToken : commaTokens) {
            String[] hyphenTokens = Strings.split(commaToken, "-");
            String startText = commaToken, endText = null;
            if (hyphenTokens.length == 2) {
                startText = hyphenTokens[0];
                endText = hyphenTokens[1];
            }
            long includedStart = parseTime(startText, timeUnit, false);
            long excludedEnd = parseTime(Objects.coalesce(endText, startText), timeUnit, true);
            if (lastInterval != null && includedStart == lastInterval.getExcludedEnd())
                series.set(series.size() - 1, lastInterval = new TimeInterval(lastInterval.getIncludedStart(), excludedEnd));
            else
                series.add(lastInterval = new TimeInterval(includedStart, excludedEnd));
        }
        return intervalListToArray(series);
    }

    private static long parseTime(String text, TimeUnit timeUnit, boolean excluded) {
        // Only accepted format is DMY [DD/MM/YYYY] [hh:mm:ss]
        LocalDateTime dateTime = parseDMYLocalDateTime(text, excluded);
        long epochMillis = Dates.localDateTimeToEpochMillisUtc(dateTime);
        return excluded ?
                convertExcludedEnd(epochMillis, TimeUnit.MILLISECONDS, timeUnit) :
                convertTime(epochMillis, TimeUnit.MILLISECONDS, timeUnit);
    }

    private static LocalDateTime parseDMYLocalDateTime(String text, boolean excluded) {
        text = text.trim();
        int day = 1, month = 1, year = 1970, hour = 0, minute = 0, second = 0;
        int p = 0;
        int i = text.indexOf('/', p);
        if (i != -1) {
            day = Numbers.intValue(text.substring(p, i));
            i = text.indexOf('/', p = i + 1);
            if (i != -1) {
                month = Numbers.intValue(text.substring(p, i));
                i = text.indexOf(' ', p = i + 1);
                if (i == -1)
                    i = text.length();
                year = Numbers.intValue(text.substring(p, i));
                p = i + 1;
            }
        }
        i = text.indexOf(':', p);
        if (i == -1)
            i = text.indexOf('h', p);
        if (i != -1) {
            hour = Numbers.intValue(text.substring(p, i));
            i = text.indexOf(':', p = i + 1);
            if (i == -1)
                i = text.length();
            minute = Numbers.intValue(text.substring(p, i));
            if (i < text.length()) {
                p = i + 1;
                i = text.length();
                second = Numbers.intValue(text.substring(p, i));
            }
        } else if (excluded)
            day++;
        return LocalDateTime.of(year, Month.of(month), day, hour, minute, second);
    }
}