package mongoose.activities.shared.logic.time;

import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public class DateTimeRange {

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

    public TimeSeries getSeries() {
        if (series == null) {
            if (text != null)
                series = TimeSeries.parse(text);
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
}