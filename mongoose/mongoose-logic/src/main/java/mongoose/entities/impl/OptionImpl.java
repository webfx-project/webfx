package mongoose.entities.impl;

import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.entities.Option;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public class OptionImpl extends DynamicEntity implements Option {

    public OptionImpl(EntityId id, EntityStore store) {
        super(id, store);
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
