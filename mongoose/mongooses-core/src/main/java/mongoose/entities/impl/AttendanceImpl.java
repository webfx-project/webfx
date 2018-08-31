package mongoose.entities.impl;

import mongoose.entities.Attendance;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class AttendanceImpl extends DynamicEntity implements Attendance {

    public AttendanceImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
