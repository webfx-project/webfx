package mongoose.shared.entities.impl;

import mongoose.shared.entities.Country;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class CountryImpl extends DynamicEntity implements Country {

    public CountryImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<Country> {
        public ProvidedFactory() {
            super(Country.class, CountryImpl::new);
        }
    }
}
