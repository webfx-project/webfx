package mongoose.entities.impl;

import mongoose.entities.MoneyTransfer;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class MoneyTransferImpl extends DynamicEntity implements MoneyTransfer {

    public MoneyTransferImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
