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

    public DaysArrayBuilder addInterval(TimeInterval interval) {
        long oneDay = TimeConverter.oneDay(timeUnit);
        long extra = oneDay <= 1 ? 0 : oneDay - 1;
        TimeUnit intervalTimeUnit = interval.getTimeUnit();
        long includedFirstDay = convertTime(floorToDay(interval.getIncludedStart(), intervalTimeUnit), intervalTimeUnit, timeUnit);
        long excludedLastDay = convertTime(floorToDay(interval.getExcludedEnd() + extra, intervalTimeUnit), intervalTimeUnit, timeUnit);
        for (long day = includedFirstDay; day < excludedLastDay; day += oneDay)
            days.add(day);
        return this;
    }

    public DaysArrayBuilder addIntervals(TimeInterval[] intervals) {
        for (TimeInterval interval : intervals)
            addInterval(interval);
        return this;
    }

    public DaysArrayBuilder addSeries(TimeSeries series) {
        return addIntervals(series.getArray());
    }

    public DaysArrayBuilder addDate(LocalDate date) {
        days.add(convertTime(date.toEpochDay(), TimeUnit.DAYS, timeUnit));
        return this;
    }

    public DaysArray build() {
        return new DaysArray(Collections.toLongArray(days), timeUnit);
    }
}
