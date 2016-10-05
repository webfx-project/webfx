package mongoose.activities.shared.logic.time;

import naga.commons.util.collection.Collections;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static mongoose.activities.shared.logic.time.TimeConverter.convertTime;
import static mongoose.activities.shared.logic.time.TimeConverter.floorToDay;

/**
 * @author Bruno Salmon
 */
public class DaysArrayBuilder {

    private final List<Long> days = new ArrayList<>();
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
        long includedFirstDay = floorToDay(interval.getIncludedStart(), intervalTimeUnit);
        long excludedLastDay = floorToDay(interval.getExcludedEnd() + extra, intervalTimeUnit);
        long firstDayTime = interval.getIncludedStart() - includedFirstDay;
        long lastDayTime = interval.getExcludedEnd() + extra - excludedLastDay;
        includedFirstDay = convertTime(includedFirstDay, intervalTimeUnit, timeUnit);
        excludedLastDay = convertTime(excludedLastDay, intervalTimeUnit, timeUnit);
        if (dayTimeRange != null) {
            if (firstDayTime > dayTimeRange.getDayTimeInterval(includedFirstDay, intervalTimeUnit).getExcludedEnd())
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

    public DaysArrayBuilder addDate(LocalDate date) {
        days.add(convertTime(date.toEpochDay(), TimeUnit.DAYS, timeUnit));
        return this;
    }

    public DaysArray build() {
        return new DaysArray(Collections.toLongArray(days), timeUnit);
    }
}
