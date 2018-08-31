package mongoose.entities.impl;

import mongoose.entities.Cart;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class CartImpl extends DynamicEntity implements Cart {

    public CartImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
