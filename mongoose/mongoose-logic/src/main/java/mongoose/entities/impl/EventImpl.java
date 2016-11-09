package mongoose.entities.impl;

import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.entities.Event;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public class EventImpl extends DynamicEntity implements Event {

    public EventImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    private DateTimeRange parsedDateTimeRange;
    @Override
    public DateTimeRange getParsedDateTimeRange() {
        if (parsedDateTimeRange == null)
            parsedDateTimeRange = DateTimeRange.parse(getDateTimeRange());
        return parsedDateTimeRange;
    }

    private DateTimeRange parsedMinDateTimeRange;
    @Override
    public DateTimeRange getParsedMinDateTimeRange() {
        if (parsedMinDateTimeRange == null)
            parsedMinDateTimeRange = DateTimeRange.parse(getMinDateTimeRange());
        return parsedMinDateTimeRange;
    }

    private DateTimeRange parsedMaxDateTimeRange;
    @Override
    public DateTimeRange getParsedMaxDateTimeRange() {
        if (parsedMaxDateTimeRange == null)
            parsedMaxDateTimeRange = DateTimeRange.parse(getMaxDateTimeRange());
        return parsedMaxDateTimeRange;
    }
}
