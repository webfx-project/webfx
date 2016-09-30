package mongoose.entities.impl;

import mongoose.entities.Attendance;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public class AttendanceImpl extends DynamicEntity implements Attendance {

    public AttendanceImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
