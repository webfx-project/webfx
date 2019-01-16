package mongoose.shared.entities.impl;

import mongoose.shared.entities.Method;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class MethodImpl extends DynamicEntity implements Method {

    public MethodImpl(EntityId id, EntityStore store) {
        super(id, store);
    }


    public static final class ProvidedFactory extends EntityFactoryProviderImpl<Method> {
        public ProvidedFactory() {
            super(Method.class, MethodImpl::new);
        }
    }
}
