package mongoose.entities.impl;

import mongoose.entities.Document;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class DocumentImpl extends DynamicEntity implements Document {

    public DocumentImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

}
