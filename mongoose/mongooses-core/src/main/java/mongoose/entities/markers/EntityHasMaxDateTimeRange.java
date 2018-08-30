package mongoose.entities.markers;

import mongoose.activities.bothends.logic.time.DateTimeRange;
import naga.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface EntityHasMaxDateTimeRange extends Entity, HasMaxDateTimeRange {

    @Override
    default void setMaxDateTimeRange(String maxDateTimeRange) {
        setFieldValue("maxDateTimeRange", maxDateTimeRange);
    }

    @Override
    default String getMaxDateTimeRange() {
        return getStringFieldValue("maxDateTimeRange");
    }

    default DateTimeRange getParsedMaxDateTimeRange() { // Should be overridden by implementing class to have a cached value
        return DateTimeRange.parse(getMaxDateTimeRange());
    }

}
