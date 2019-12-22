package mongoose.shared.entities.impl;

import mongoose.shared.entities.Item;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class ItemImpl extends DynamicEntity implements Item {

    public ItemImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<Item> {
        public ProvidedFactory() {
            super(Item.class, ItemImpl::new);
        }
    }
}
