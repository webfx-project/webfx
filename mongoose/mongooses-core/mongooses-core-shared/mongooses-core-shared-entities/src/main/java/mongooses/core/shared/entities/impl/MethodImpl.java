package mongooses.core.shared.entities.impl;

import mongooses.core.shared.entities.Method;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class MethodImpl extends DynamicEntity implements Method {

    public MethodImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

}
