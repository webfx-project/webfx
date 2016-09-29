package mongoose.entities.impl;

import mongoose.entities.Label;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public class LabelImpl extends DynamicEntity implements Label {

    public LabelImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
