package mongooses.core.backend.entities.loadtester.impl;

import mongooses.core.backend.activities.loadtester.drive.listener.EventType;
import mongooses.core.backend.entities.loadtester.LtTestEventEntity;
import mongooses.core.backend.entities.loadtester.LtTestSet;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.orm.entity.impl.DynamicEntity;

import java.time.Instant;

/**
 * @author Jean-Pierre Alonso.
 */
public final class LtTestEventEntityImpl extends DynamicEntity implements LtTestEventEntity {

    public LtTestEventEntityImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    @Override
    public LtTestSet getLtTestSet() {
        return getForeignEntity("ltTestSet");
    }

    @Override
    public void setLtTestSet(Object ltTestSet) {
        setForeignField("ltTestSet", ltTestSet);
    }

    @Override
    public Instant getEventTime() {
        return (Instant) getFieldValue("eventTime");
    }

    @Override
    public void setEventTime(Instant eventTime) {
        setFieldValue("eventTime", eventTime);
    }

    @Override
    public EventType getType () {
        return EventType.values()[getIntegerFieldValue("type")];
    }

    @Override
    public void setType(EventType type) {
        setFieldValue("type", type.ordinal());
    }

    @Override
    public Integer getVal() {
        return getIntegerFieldValue("value");
    }

    @Override
    public void setVal(Integer val){
        setFieldValue("value", val);
    }
}
