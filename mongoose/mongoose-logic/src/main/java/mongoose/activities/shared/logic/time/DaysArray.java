package mongoose.activities.shared.logic.time;

import java.util.concurrent.TimeUnit;

import static mongoose.activities.shared.logic.time.TimeConverter.convertTime;

/**
 * @author Bruno Salmon
 */
public class DaysArray {

    private final long[] array;
    private final TimeUnit timeUnit;

    public DaysArray(long[] array, TimeUnit timeUnit) {
        this.array = array;
        this.timeUnit = timeUnit;
    }

    public long[] getArray() {
        return array;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public DaysArray changeTimeUnit(TimeUnit newTimeUnit) {
        if (timeUnit == newTimeUnit)
            return this;
        int n = array.length, i = 0;
        long[] newArray = new long[n];
        for (long day : array)
            newArray[i++] = convertTime(day, timeUnit, newTimeUnit);
        return new DaysArray(newArray, newTimeUnit);
    }

    public TimeSeries toSeries() {
        return new TimeSeriesBuilder(timeUnit).addDaysArrays(this).build();
    }
}
