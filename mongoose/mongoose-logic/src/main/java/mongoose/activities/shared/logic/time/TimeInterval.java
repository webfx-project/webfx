package mongoose.activities.shared.logic.time;

/**
 * @author Bruno Salmon
 */
public class TimeInterval {

    public static TimeInterval EMPTY_INTERVAL = new TimeInterval(0, 0);

    private final long includedStart;
    private final long excludedEnd;

    public TimeInterval(long includedStart, long excludedEnd) {
        this.includedStart = includedStart;
        this.excludedEnd = excludedEnd;
    }

    public long getIncludedStart() {
        return includedStart;
    }

    public long getExcludedEnd() {
        return excludedEnd;
    }
}
