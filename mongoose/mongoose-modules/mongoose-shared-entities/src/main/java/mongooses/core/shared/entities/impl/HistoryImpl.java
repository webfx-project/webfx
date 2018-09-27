package mongooses.core.shared.entities.impl;

import mongooses.core.shared.entities.History;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class HistoryImpl extends DynamicEntity implements History {

    public HistoryImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
