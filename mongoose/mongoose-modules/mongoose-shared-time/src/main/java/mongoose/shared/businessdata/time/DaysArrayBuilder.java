package mongoose.shared.businessdata.time;

import webfx.platform.shared.util.collection.Collections;
import webfx.platform.shared.util.collection.HashList;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public class DaysArrayBuilder {

    private final List<Long> days = new HashList<>();
    private final TimeUnit timeUnit;

    public DaysArrayBuilder() {
        this(TimeUnit.DAYS);
    }

    public DaysArrayBuilder(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public DaysArrayBuilder addInterval(TimeInterval interval, DayTimeRange dayTimeRange) {
        long oneDay = TimeConverter.oneDay(timeUnit);
        long extra = oneDay <= 1 ? 0 : oneDay - 1;
        TimeUnit intervalTimeUnit = interval.getTimeUnit();
        long includedFirstDay = TimeConverter.floorToDay(interval.getIncludedStart(), intervalTimeUnit);
        long excludedLastDay = TimeConverter.floorToDay(interval.getExcludedEnd() + extra, intervalTimeUnit);
        long firstDayTime = interval.getIncludedStart() - includedFirstDay;
        long lastDayTime = interval.getExcludedEnd() + extra - excludedLastDay;
        includedFirstDay = TimeConverter.convertTime(includedFirstDay, intervalTimeUnit, timeUnit);
        excludedLastDay = TimeConverter.convertTime(excludedLastDay, intervalTimeUnit, timeUnit);
        if (dayTimeRange != null) {
            if (firstDayTime >= dayTimeRange.getDayTimeInterval(includedFirstDay, intervalTimeUnit).getExcludedEnd())
                includedFirstDay += oneDay;
            if (lastDayTime < dayTimeRange.getDayTimeInterval(excludedLastDay, intervalTimeUnit).getIncludedStart())
                excludedLastDay -= oneDay;
        }
        for (long day = includedFirstDay; day < excludedLastDay; day += oneDay)
            days.add(day);
        return this;
    }

    public DaysArrayBuilder addIntervals(TimeInterval[] intervals, DayTimeRange dayTimeRange) {
        for (TimeInterval interval : intervals)
            addInterval(interval, dayTimeRange);
        return this;
    }

    public DaysArrayBuilder addSeries(TimeSeries series, DayTimeRange dayTimeRange) {
        return addIntervals(series.getArray(), dayTimeRange);
    }

    public DaysArrayBuilder addDaysArray(DaysArray daysArray, DayTimeRange dayTimeRange) {
        return addSeries(daysArray.toSeries(), dayTimeRange);
    }

    public DaysArrayBuilder addDate(LocalDate date) {
        days.add(TimeConverter.convertTime(date.toEpochDay(), TimeUnit.DAYS, timeUnit));
        return this;
    }

    public DaysArray build() {
        long[] array = Collections.toLongArray(days);
        Arrays.sort(array);
        return new DaysArray(array, timeUnit);
    }
}
