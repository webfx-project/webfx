package mongooses.core.entities.impl;

import mongooses.core.activities.sharedends.logic.time.DateTimeRange;
import mongooses.core.activities.sharedends.logic.time.DayTimeRange;
import mongooses.core.entities.Option;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class OptionImpl extends DynamicEntity implements Option {

    public OptionImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    @Override
    public void setFieldValue(Object domainFieldId, Object value) {
        super.setFieldValue(domainFieldId, value);
        if ("timeRange".equals(domainFieldId))
            parsedTimeRangeOrParent = null;
        else if ("dateTimeRange".equals(domainFieldId))
            parsedDateTimeRangeOrParent = null;
    }

    private DayTimeRange parsedTimeRangeOrParent;
    @Override
    public DayTimeRange getParsedTimeRangeOrParent() {
        if (parsedTimeRangeOrParent == null)
            parsedTimeRangeOrParent = DayTimeRange.parse(getTimeRangeOrParent());
        return parsedTimeRangeOrParent;
    }

    private DateTimeRange parsedDateTimeRangeOrParent;
    @Override
    public DateTimeRange getParsedDateTimeRangeOrParent() {
        if (parsedDateTimeRangeOrParent == null)
            parsedDateTimeRangeOrParent = DateTimeRange.parse(getDateTimeRangeOrParent());
        return parsedDateTimeRangeOrParent;
    }
}
