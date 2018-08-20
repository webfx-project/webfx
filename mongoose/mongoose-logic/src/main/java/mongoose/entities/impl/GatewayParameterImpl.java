package mongoose.entities.impl;

import mongoose.entities.GatewayParameter;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class GatewayParameterImpl extends DynamicEntity implements GatewayParameter {

    public GatewayParameterImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
