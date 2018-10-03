package mongoose.shared.entities.markers;

import webfx.framework.shared.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface EntityHasMinDateTimeRange extends Entity, HasMinDateTimeRange {

    @Override
    default void setMinDateTimeRange(String minDateTimeRange) {
        setFieldValue("minDateTimeRange", minDateTimeRange);
    }

    @Override
    default String getMinDateTimeRange() {
        return getStringFieldValue("minDateTimeRange");
    }
}
