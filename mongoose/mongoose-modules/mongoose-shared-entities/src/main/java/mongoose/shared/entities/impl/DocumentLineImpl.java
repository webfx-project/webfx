package mongoose.shared.entities.impl;

import mongoose.shared.entities.DocumentLine;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class DocumentLineImpl extends DynamicEntity implements DocumentLine {

    public DocumentLineImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
