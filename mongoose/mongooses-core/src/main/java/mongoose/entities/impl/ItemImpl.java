package mongoose.entities.impl;

import mongoose.entities.Item;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class ItemImpl extends DynamicEntity implements Item {

    public ItemImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
