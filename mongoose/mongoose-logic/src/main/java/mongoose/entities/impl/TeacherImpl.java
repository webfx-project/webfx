package mongoose.entities.impl;

import mongoose.entities.Teacher;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public class TeacherImpl extends DynamicEntity implements Teacher {

    public TeacherImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
