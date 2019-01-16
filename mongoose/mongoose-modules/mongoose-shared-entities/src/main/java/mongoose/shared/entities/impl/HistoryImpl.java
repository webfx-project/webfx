package mongoose.shared.entities.impl;

import mongoose.shared.entities.History;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class HistoryImpl extends DynamicEntity implements History {

    public HistoryImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<History> {
        public ProvidedFactory() {
            super(History.class, HistoryImpl::new);
        }
    }
}
