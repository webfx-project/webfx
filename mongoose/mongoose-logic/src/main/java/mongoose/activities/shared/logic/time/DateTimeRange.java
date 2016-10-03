package mongoose.activities.shared.logic.time;

import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public class DateTimeRange {

    private final TimeUnit timeUnit;
    private TimeInterval interval;
    private long[] daysArray;
    private TimeInterval[] series;
    private String text;

    public static DateTimeRange parse(String text) {
        return new DateTimeRange(text, TimeUnit.MINUTES);
    }

    public static DateTimeRange parse(String text, TimeUnit timeUnit) {
        return new DateTimeRange(text, timeUnit);
    }

    public DateTimeRange(TimeInterval interval, TimeUnit timeUnit) {
        this.interval = interval;
        this.timeUnit = timeUnit;
    }

    public DateTimeRange(long[] daysArray, TimeUnit timeUnit) {
        this.daysArray = daysArray;
        this.timeUnit = timeUnit;
    }

    public DateTimeRange(TimeInterval[] series, TimeUnit timeUnit) {
        this.series = series;
        this.timeUnit = timeUnit;
    }

    public DateTimeRange(String text, TimeUnit timeUnit) {
        this.text = text;
        this.timeUnit = timeUnit;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public TimeInterval getInterval() {
        if (interval == null)
            interval = TimeConverter.convertSeriesToInterval(getSeries());
        return interval;
    }

    public long[] getDaysArray() {
        if (daysArray == null)
            daysArray = TimeConverter.convertSeriesToDaysArray(getSeries(), timeUnit);
        return daysArray;
    }

    public TimeInterval[] getSeries() {
        if (series == null) {
            if (text != null)
                series = TimeConverter.convertTextToSeries(text, timeUnit);
            else
                series = TimeConverter.convertIntervalToSeries(interval);
        }
        return series;
    }

    public String getText() {
        if (text == null)
            text = TimeConverter.convertSeriesToText(getSeries(), timeUnit);
        return text;
    }

    public TimeInterval getInterval(TimeUnit timeUnit) {
        return TimeConverter.convertInterval(getInterval(), this.timeUnit, timeUnit);
    }

    public long[] getDaysArray(TimeUnit timeUnit) {
        return TimeConverter.convertDaysArray(getDaysArray(), this.timeUnit, timeUnit);
    }

    public TimeInterval[] getSeries(TimeUnit timeUnit) {
        return TimeConverter.convertSeries(getSeries(), this.timeUnit, timeUnit);
    }

    public String getText(TimeUnit timeUnit) {
        if (timeUnit == this.timeUnit)
            return getText();
        return TimeConverter.convertSeriesToText(getSeries(timeUnit), timeUnit);
    }

    public DateTimeRange changeTimeUnit(TimeUnit timeUnit) {
        if (timeUnit == this.timeUnit)
            return this;
        return new DateTimeRange(getSeries(timeUnit), timeUnit);
    }

    public boolean containsInterval(TimeInterval interval, TimeUnit timeUnit) {
        return containsInterval(interval.getIncludedStart(), interval.getExcludedEnd(), timeUnit);
    }

    public boolean containsInterval(long includedStart, long excludedEnd, TimeUnit timeUnit) {
        TimeInterval[] series = getSeries(timeUnit);
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
}