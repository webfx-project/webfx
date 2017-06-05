package mongoose.entities.impl;

import mongoose.entities.Method;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public class MethodImpl extends DynamicEntity implements Method {

    public MethodImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

}
