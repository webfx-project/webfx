package mongoose.entities.impl;

import mongoose.entities.Rate;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public class RateImpl extends DynamicEntity implements Rate {

    public RateImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
