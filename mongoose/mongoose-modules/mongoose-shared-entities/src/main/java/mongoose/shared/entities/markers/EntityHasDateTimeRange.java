package mongoose.shared.entities.markers;

import webfx.framework.shared.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface EntityHasDateTimeRange extends Entity, HasDateTimeRange {

    @Override
    default void setDateTimeRange(String dateTimeRange) {
        setFieldValue("dateTimeRange", dateTimeRange);
    }

    @Override
    default String getDateTimeRange() {
        return getStringFieldValue("dateTimeRange");
    }
}
