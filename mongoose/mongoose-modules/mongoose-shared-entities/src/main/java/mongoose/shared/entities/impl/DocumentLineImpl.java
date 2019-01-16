package mongoose.shared.entities.impl;

import mongoose.shared.entities.DocumentLine;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class DocumentLineImpl extends DynamicEntity implements DocumentLine {

    public DocumentLineImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<DocumentLine> {
        public ProvidedFactory() {
            super(DocumentLine.class, DocumentLineImpl::new);
        }
    }
}
