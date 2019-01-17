package mongoose.shared.entities.markers;

import mongoose.shared.businessdata.time.DateTimeRange;

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
