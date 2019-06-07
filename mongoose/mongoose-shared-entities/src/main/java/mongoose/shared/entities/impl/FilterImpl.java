package mongoose.shared.entities.impl;

import mongoose.shared.entities.Filter;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class FilterImpl extends DynamicEntity implements Filter {

    public FilterImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<Filter> {
        public ProvidedFactory() {
            super(Filter.class, FilterImpl::new);
        }
    }
}
