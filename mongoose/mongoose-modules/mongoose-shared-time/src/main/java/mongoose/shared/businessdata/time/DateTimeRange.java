package mongoose.shared.businessdata.time;

import webfx.platform.shared.util.Arrays;
import webfx.platform.shared.util.collection.Collections;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public final class DateTimeRange {

    private String text;
    private TimeSeries series;
    private TimeInterval interval;
    private DaysArray daysArray;

    public static DateTimeRange parse(String text) {
        return text == null ? null : new DateTimeRange(text);
    }

    private DateTimeRange(String text) {
        this.text = text;
    }

    public DateTimeRange(TimeSeries series) {
        this.series = series;
    }

    public DateTimeRange(TimeInterval interval) {
        this.interval = interval;
    }

    public DateTimeRange(DaysArray daysArray) {
        this.daysArray = daysArray;
    }

    private DateTimeRange from(TimeSeries series) {
        return series == this.series ? this : new DateTimeRange(series);
    }

    private DateTimeRange from(DaysArray daysArray) {
        return daysArray == this.daysArray ? this : new DateTimeRange(daysArray);
    }

    private DateTimeRange from(TimeInterval interval) {
        return interval == this.interval ? this : new DateTimeRange(interval);
    }

    public TimeInterval getInterval() {
        if (interval == null)
            interval = getSeries().toInterval();
        return interval;
    }

    public DaysArray getDaysArray() {
        if (daysArray == null)
            daysArray = getSeries().toDaysArray();
        return daysArray;
    }

    public DaysArray getDaysArray(DayTimeRange dayTimeRange) {
        return getSeries().toDaysArray(dayTimeRange);
    }

    public TimeSeries getSeries() {
        if (series == null) {
            if (text != null)
                series = TimeSeries.parse(text);
            else if (daysArray != null)
                series = daysArray.toSeries();
            else
                series = interval.toSeries();
        }
        return series;
    }

    public String getText() {
        if (text == null)
            text = getSeries().toText();
        return text;
    }

    public boolean isEmpty() {
        return Arrays.isEmpty(getSeries().getArray());
    }

    public DateTimeRange changeTimeUnit(TimeUnit newTimeUnit) {
        if (series != null)
            return from(series.changeTimeUnit(newTimeUnit));
        if (daysArray != null)
            return from(daysArray.changeTimeUnit(newTimeUnit));
        if (interval != null)
            return from(interval.changeTimeUnit(newTimeUnit));
        return from(getSeries().changeTimeUnit(newTimeUnit));
    }

    public boolean containsInterval(TimeInterval interval) {
        return containsInterval(interval.getIncludedStart(), interval.getExcludedEnd(), interval.getTimeUnit());
    }

    boolean containsInterval(long includedStart, long excludedEnd, TimeUnit timeUnit) {
        TimeInterval[] series = getSeries().changeTimeUnit(timeUnit).getArray();
        for (TimeInterval interval : series) {
            long start = interval.getIncludedStart();
            if (start >= excludedEnd)
                return false;
            long end = interval.getExcludedEnd();
            if (end > includedStart)
                return true;
        }
        return false;
    }

    public DateTimeRange intersect(DateTimeRange dateTimeRange) {
        return dateTimeRange == null ? this : intersect(dateTimeRange.getSeries());
    }

    public DateTimeRange intersect(TimeSeries series) {
        return from(getSeries().intersect(series));
    }

    public DateTimeRange intersect(DayTimeRange dayTimeRange) {
        if (dayTimeRange == null)
            return this;
        return from(getSeries().changeTimeUnit(TimeUnit.MINUTES).intersect(dayTimeRange));
    }

    public boolean overlaps(DateTimeRange dateTimeRange) {
        DateTimeRange intersect = intersect(dateTimeRange);
        return intersect != null && !intersect.isEmpty();
    }

    public static DateTimeRange cropDateTimeRangeWithDayTime(DateTimeRange dateTimeRange, DayTimeRange dayTimeRange) {
        return dateTimeRange == null || dayTimeRange == null ? dateTimeRange : dateTimeRange.intersect(dayTimeRange);
    }

    public static DateTimeRange merge(List<DateTimeRange> dateTimeRanges) {
        switch (Collections.size( dateTimeRanges)) {
            case 0:
                return null;
            case 1:
                return Collections.first(dateTimeRanges);
            default:
                return new DateTimeRange(TimeSeries.merge(Collections.map(dateTimeRanges, DateTimeRange::getSeries)));
        }
    }
}