package mongooses.core.shared.entities;

import mongooses.core.shared.domainmodel.time.DateTimeRange;
import mongooses.core.shared.entities.markers.*;
import webfx.framework.orm.entity.Entity;
import webfx.framework.orm.entity.EntityId;
import webfx.platforms.core.util.Objects;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
public interface Event extends Entity,
        EntityHasName,
        EntityHasLabel,
        EntityHasOrganization,
        EntityHasDateTimeRange,
        EntityHasMinDateTimeRange,
        EntityHasMaxDateTimeRange {

    default LocalDate getStartDate() {
        return getLocalDateFieldValue("startDate");
    }

    default LocalDate getEndDate() {
        return getLocalDateFieldValue("endDate");
    }

    default void setLive(Boolean live) {
        setFieldValue("live", live);
    }

    default Boolean isLive() {
        return getBooleanFieldValue("live");
    }

    default void setFeesBottomLabel(Object label) {
        setForeignField("feesBottomLabel", label);
    }

    default EntityId getFeesBottomLabelId() {
        return getForeignEntityId("feesBottomLabel");
    }

    default Label getFeesBottomLabel() {
        return getForeignEntity("feesBottomLabel");
    }

    default DateTimeRange computeMaxDateTimeRange() {
        return Objects.coalesce(getParsedMaxDateTimeRange(), getParsedDateTimeRange());
    }


}
