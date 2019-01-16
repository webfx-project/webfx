package mongoose.shared.entities.impl;

import mongoose.shared.entities.Site;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class SiteImpl extends DynamicEntity implements Site {

    public SiteImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<Site> {
        public ProvidedFactory() {
            super(Site.class, SiteImpl::new);
        }
    }
}
