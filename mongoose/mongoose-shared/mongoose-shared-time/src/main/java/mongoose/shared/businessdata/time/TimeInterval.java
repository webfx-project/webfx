package mongoose.shared.businessdata.time;

import java.util.concurrent.TimeUnit;

import static mongoose.shared.businessdata.time.TimeConverter.*;

/**
 * @author Bruno Salmon
 */
public final class TimeInterval {

    static TimeInterval EMPTY_INTERVAL = new TimeInterval(0, 0, TimeUnit.MINUTES);

    private final long includedStart;
    private final long excludedEnd;
    private final TimeUnit timeUnit;

    public TimeInterval(long includedStart, long excludedEnd, TimeUnit timeUnit) {
        if (excludedEnd < includedStart)
            throw new IllegalArgumentException("TimeInterval application must be before end");
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

    public String toText(String format) {
        return toText(new StringBuilder(), format).toString();
    }

    public String getStartText() {
        return getStartText(null);
    }

    public String getStartText(String format) {
        return formatTime(getIncludedStart(), timeUnit, false, format);
    }

    public String getEndText() {
        return getEndText(null);
    }

    public String getEndText(String format) {
        return formatTime(getExcludedEnd(), timeUnit, true, format);
    }

    StringBuilder toText(StringBuilder sb, String format) {
        String startText = getStartText(format);
        sb.append(startText);
        String endText = getEndText(format);
        if (!endText.equals(startText))
            sb.append(" - ").append(endText);
        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeInterval interval = (TimeInterval) o;

        if (includedStart != interval.includedStart) return false;
        if (excludedEnd != interval.excludedEnd) return false;
        return timeUnit == interval.timeUnit;

    }

    @Override
    public int hashCode() {
        int result = (int) (includedStart ^ (includedStart >>> 32));
        result = 31 * result + (int) (excludedEnd ^ (excludedEnd >>> 32));
        result = 31 * result + timeUnit.hashCode();
        return result;
    }
}
