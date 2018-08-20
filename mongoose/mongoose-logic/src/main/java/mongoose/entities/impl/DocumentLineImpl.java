package mongoose.entities.impl;

import mongoose.entities.DocumentLine;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class DocumentLineImpl extends DynamicEntity implements DocumentLine {

    public DocumentLineImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
