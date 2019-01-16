package mongoose.shared.entities.impl;

import mongoose.shared.entities.ItemFamily;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class ItemFamilyImpl extends DynamicEntity implements ItemFamily {

    public ItemFamilyImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<ItemFamily> {
        public ProvidedFactory() {
            super(ItemFamily.class, ItemFamilyImpl::new);
        }
    }
}
