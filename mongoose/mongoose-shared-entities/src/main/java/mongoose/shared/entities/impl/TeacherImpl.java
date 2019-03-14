package mongoose.shared.entities.impl;

import mongoose.shared.entities.Teacher;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class TeacherImpl extends DynamicEntity implements Teacher {

    public TeacherImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<Teacher> {
        public ProvidedFactory() {
            super(Teacher.class, TeacherImpl::new);
        }
    }
}
