package mongoose.shared.entities.impl;

import mongoose.shared.entities.DateInfo;
import mongoose.shared.entities.Label;
import mongoose.shared.businessdata.time.DateTimeRange;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class DateInfoImpl extends DynamicEntity implements DateInfo {

    public DateInfoImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    @Override
    public Label getFeesBottomLabel() {
        return getForeignEntity("feesBottomLabel");
    }

    @Override
    public Label getFeesPopupLabel() {
        return getForeignEntity("feesPopupLabel");
    }

    @Override
    public Boolean isForceSoldout() {
        return getBooleanFieldValue("forceSoldout");
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

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<DateInfo> {
        public ProvidedFactory() {
            super(DateInfo.class, DateInfoImpl::new);
        }
    }
}
