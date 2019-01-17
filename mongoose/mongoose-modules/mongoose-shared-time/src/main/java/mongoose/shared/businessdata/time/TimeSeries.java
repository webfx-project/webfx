package mongoose.shared.businessdata.time;

import webfx.platform.shared.util.Arrays;
import webfx.platform.shared.util.collection.Collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public final class TimeSeries {

    private final TimeInterval[] array;
    private final TimeUnit timeUnit;

    public static TimeSeries parse(String text) {
        return new TimeSeriesBuilder(TimeUnit.MINUTES).addText(text).build();
    }

    public TimeSeries(TimeInterval[] array, TimeUnit timeUnit) {
        this.array = array;
        this.timeUnit = timeUnit;
    }

    public TimeSeries(Collection<TimeInterval> intervals, TimeUnit timeUnit) {
        this(Collections.toArray(intervals, TimeInterval[]::new), timeUnit);
    }

    public TimeInterval[] getArray() {
        return array;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public TimeSeries changeTimeUnit(TimeUnit newTimeUnit) {
        if (newTimeUnit == timeUnit)
            return this;
        int n = array.length, i = 0;
        TimeInterval[] newArray = new TimeInterval[n];
        for (TimeInterval interval : array)
            newArray[i++] = interval.changeTimeUnit(newTimeUnit);
        return new TimeSeries(newArray, newTimeUnit);
    }

    public TimeSeries shift(long offset, TimeUnit timeUnit) {
        if (offset == 0)
            return this;
        TimeUnit finalUnit = TimeConverter.smallestUnit(this.timeUnit, timeUnit);
        long finalOffset = TimeConverter.convertTime(offset, timeUnit, finalUnit);
        return new TimeSeries(Arrays.convert(array, interval -> interval.shift(finalOffset, finalUnit), TimeInterval[]::new), finalUnit);
    }

    public TimeInterval toInterval() {
        int n = array.length;
        if (n >= 2)
            return new TimeInterval(array[0].getIncludedStart(), array[n - 1].getExcludedEnd(), timeUnit);
        return n == 1 ? array[0] : TimeInterval.EMPTY_INTERVAL;
    }

    public DaysArray toDaysArray() {
        return toDaysArray(null);
    }

    public DaysArray toDaysArray(DayTimeRange dayTimeRange) {
        return new DaysArrayBuilder(timeUnit).addSeries(this, dayTimeRange).build();
    }

    public String toText() {
        return toText((String) null);
    }

    public String toText(String format) {
        return toText(new StringBuilder(), format).toString();
    }

    public StringBuilder toText(StringBuilder sb) {
        return toText(sb, null);
    }

    StringBuilder toText(StringBuilder sb, String format) {
        int index = 0;
        for (TimeInterval interval : array)
            interval.toText(index++ > 0 ? sb.append(", ") : sb, format);
        return sb;
    }

    TimeSeries intersect(TimeSeries series2) {
        return seriesIntersectSeries(this, series2, new TimeSeriesBuilder(timeUnit)).build();
    }

    public TimeSeries intersect(DayTimeRange dayTimeRange) {
        return seriesIntersectDayTimeRange(this, dayTimeRange, new TimeSeriesBuilder(timeUnit)).build();
    }

    public TimeSeries intersect(TimeInterval interval) {
        return seriesIntersectInterval(this, interval, new TimeSeriesBuilder(timeUnit)).build();
    }

    private static TimeSeriesBuilder seriesIntersectSeries(TimeSeries series1, TimeSeries series2, TimeSeriesBuilder tsb) {
        // loop but could be optimized knowing that intervals are in chronological order
        for (TimeInterval interval : series2.getArray())
            seriesIntersectInterval(series1, interval, tsb);
        return tsb;
    }

    private static TimeSeriesBuilder seriesIntersectInterval(TimeSeries series, TimeInterval interval, TimeSeriesBuilder tsb) {
        // loop but could be optimized knowing that intervals are in chronological order
        for (TimeInterval interval1 : series.getArray())
            intervalIntersectInterval(interval1, interval, tsb);
        return tsb;
    }

    private static TimeSeriesBuilder intervalIntersectInterval(TimeInterval interval1, TimeInterval interval2, TimeSeriesBuilder tsb) {
        TimeUnit timeUnit = interval1.getTimeUnit();
        interval2 = interval2.changeTimeUnit(timeUnit);
        long start1 = interval1.getIncludedStart();
        long end1 = interval1.getExcludedEnd();
        long start2 = interval2.getIncludedStart();
        long end2 = interval2.getExcludedEnd();
        long start = Math.max(start1, start2);
        long end = Math.min(end1, end2);
        if (start < end)
            tsb.addInterval(new TimeInterval(start, end, timeUnit));
        return tsb;
    }

    private static TimeSeriesBuilder seriesIntersectDayTimeRange(TimeSeries series, DayTimeRange dayTimeRange, TimeSeriesBuilder tsb) {
        // loop but could be optimized knowing that intervals are in chronological order
        for (TimeInterval interval1 : series.getArray())
            intervalIntersectDayTimeRange(interval1, dayTimeRange, tsb);
        return tsb;
    }

    private static TimeSeriesBuilder intervalIntersectDayTimeRange(TimeInterval interval, DayTimeRange dayTimeRange, TimeSeriesBuilder tsb) {
        long start = interval.getIncludedStart(TimeUnit.MINUTES);
        long end = interval.getExcludedEnd(TimeUnit.MINUTES);
        long startDay = TimeConverter.floorToDay(start, TimeUnit.MINUTES);
        long endDay = TimeConverter.floorToDay(end, TimeUnit.MINUTES);
        TimeInterval startDayTimeInterval = dayTimeRange.getDayTimeInterval(startDay, TimeUnit.MINUTES).shift(startDay, TimeUnit.MINUTES);
        TimeInterval endDayTimeInterval = dayTimeRange.getDayTimeInterval(endDay, TimeUnit.MINUTES).shift(endDay, TimeUnit.MINUTES);
        if (start < startDayTimeInterval.getIncludedStart())
            start = startDayTimeInterval.getIncludedStart();
        else if (start >= startDayTimeInterval.getExcludedEnd())
            start = startDayTimeInterval.getIncludedStart() + TimeConverter.oneDay(TimeUnit.MINUTES);
        if (end > endDayTimeInterval.getExcludedEnd())
            end = endDayTimeInterval.getExcludedEnd();
        else if (end <= endDayTimeInterval.getIncludedStart())
            end = endDayTimeInterval.getExcludedEnd() - TimeConverter.oneDay(TimeUnit.MINUTES);
        if (start < end)
            intervalIntersectInterval(new TimeInterval(start, end, TimeUnit.MINUTES), interval, tsb);
        return tsb;
    }

    public static TimeSeries merge(Collection<TimeSeries> seriesCollection) {
        TimeUnit timeUnit = seriesCollection.stream().min(Collections.comparing(TimeSeries::getTimeUnit)).get().getTimeUnit();
        List<TimeInterval> intervals = new ArrayList<>();
        Collections.forEach(seriesCollection, series -> java.util.Collections.addAll(intervals, series.changeTimeUnit(timeUnit).getArray()));
        Collections.sort(intervals, Collections.comparingLong(TimeInterval::getIncludedStart));
        TimeSeriesBuilder tsb = new TimeSeriesBuilder(timeUnit);
        for (TimeInterval interval : intervals)
            tsb.addInterval(interval);
        return tsb.build();
    }
}