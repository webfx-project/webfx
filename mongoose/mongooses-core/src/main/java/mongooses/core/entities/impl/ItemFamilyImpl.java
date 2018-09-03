package mongooses.core.entities.impl;

import mongooses.core.entities.ItemFamily;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class ItemFamilyImpl extends DynamicEntity implements ItemFamily {

    public ItemFamilyImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
