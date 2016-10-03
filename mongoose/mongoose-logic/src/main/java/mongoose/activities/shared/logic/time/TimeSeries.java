package mongoose.activities.shared.logic.time;

import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public class TimeSeries {

    private final TimeInterval[] array;
    private final TimeUnit timeUnit;

    public static TimeSeries parse(String text) {
        return new TimeSeriesBuilder(TimeUnit.MINUTES).addText(text).build();
    }

    public TimeSeries(TimeInterval[] array, TimeUnit timeUnit) {
        this.array = array;
        this.timeUnit = timeUnit;
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

    public TimeInterval toInterval() {
        int n = array.length;
        if (n >= 2)
            return new TimeInterval(array[0].getIncludedStart(), array[n - 1].getExcludedEnd(), timeUnit);
        return n == 1 ? array[0] : TimeInterval.EMPTY_INTERVAL;
    }

    public DaysArray toDaysArray() {
        return new DaysArrayBuilder(timeUnit).addSeries(this).build();
    }

    public String toText() {
        return toText(new StringBuilder()).toString();
    }

    StringBuilder toText(StringBuilder sb) {
        for (TimeInterval interval : array)
            interval.toText(sb.length() > 0 ? sb.append(", ") : sb);
        return sb;
    }

}
