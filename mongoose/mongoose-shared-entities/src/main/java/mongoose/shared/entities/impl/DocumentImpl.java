package mongoose.shared.entities.impl;

import mongoose.shared.entities.Document;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class DocumentImpl extends DynamicEntity implements Document {

    public DocumentImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<Document> {
        public ProvidedFactory() {
            super(Document.class, DocumentImpl::new);
        }
    }
}
