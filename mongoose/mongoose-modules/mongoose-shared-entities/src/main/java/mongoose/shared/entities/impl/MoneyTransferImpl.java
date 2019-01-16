package mongoose.shared.entities.impl;

import mongoose.shared.entities.MoneyTransfer;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class MoneyTransferImpl extends DynamicEntity implements MoneyTransfer {

    public MoneyTransferImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<MoneyTransfer> {
        public ProvidedFactory() {
            super(MoneyTransfer.class, MoneyTransferImpl::new);
        }
    }
}
