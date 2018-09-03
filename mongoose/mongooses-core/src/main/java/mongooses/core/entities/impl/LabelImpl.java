package mongooses.core.entities.impl;

import mongooses.core.entities.Label;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class LabelImpl extends DynamicEntity implements Label {

    public LabelImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
