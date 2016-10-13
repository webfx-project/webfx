package mongoose.activities.shared.logic.time;

import java.util.concurrent.TimeUnit;

import static mongoose.activities.shared.logic.time.TimeConverter.*;

/**
 * @author Bruno Salmon
 */
public class TimeInterval {

    static TimeInterval EMPTY_INTERVAL = new TimeInterval(0, 0, TimeUnit.MINUTES);

    private final long includedStart;
    private final long excludedEnd;
    private final TimeUnit timeUnit;

    public TimeInterval(long includedStart, long excludedEnd, TimeUnit timeUnit) {
        this.includedStart = includedStart;
        this.excludedEnd = excludedEnd;
        this.timeUnit = timeUnit;
    }

    public long getIncludedStart() {
        return includedStart;
    }

    public long getExcludedEnd() {
        return excludedEnd;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public TimeInterval changeTimeUnit(TimeUnit newTimeUnit) {
        if (newTimeUnit == timeUnit)
            return this;
        return new TimeInterval(getIncludedStart(newTimeUnit), getExcludedEnd(newTimeUnit), newTimeUnit);
    }

    public TimeInterval shift(long offset, TimeUnit timeUnit) {
        if (offset == 0)
            return this;
        TimeUnit smallestUnit = TimeConverter.smallestUnit(this.timeUnit, timeUnit);
        offset = TimeConverter.convertTime(offset, timeUnit, smallestUnit);
        return new TimeInterval(getIncludedStart(smallestUnit) + offset, getExcludedEnd(smallestUnit) + offset, smallestUnit);
    }

    public long getIncludedStart(TimeUnit timeUnit) {
        return convertTime(includedStart, this.timeUnit, timeUnit);
    }

    public long getExcludedEnd(TimeUnit timeUnit) {
        return convertExcludedEnd(excludedEnd, this.timeUnit, timeUnit);
    }

    public TimeSeries toSeries() {
        return new TimeSeries(new TimeInterval[]{this}, timeUnit);
    }

    public String toText() {
        return toText(new StringBuilder()).toString();
    }

    StringBuilder toText(StringBuilder sb) {
        String startText = formatTime(getIncludedStart(), timeUnit, false);
        sb.append(startText);
        String endText = formatTime(getExcludedEnd(), timeUnit, true);
        if (!endText.equals(startText))
            sb.append(" - ").append(endText);
        return sb;
    }
}
