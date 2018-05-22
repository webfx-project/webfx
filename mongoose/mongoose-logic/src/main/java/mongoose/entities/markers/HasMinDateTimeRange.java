package mongoose.entities.markers;

import mongoose.activities.bothends.logic.time.DateTimeRange;

/**
 * @author Bruno Salmon
 */
public interface HasMinDateTimeRange {

    void setMinDateTimeRange(String minDateTimeRange);

    String getMinDateTimeRange();

    default DateTimeRange getParsedMinDateTimeRange() { // Should be overridden by implementing class to have a cached value
        return DateTimeRange.parse(getMinDateTimeRange());
    }


}
