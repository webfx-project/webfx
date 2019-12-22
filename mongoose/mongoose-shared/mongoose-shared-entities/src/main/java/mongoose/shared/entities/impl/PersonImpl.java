package mongoose.shared.entities.impl;

import mongoose.shared.entities.Person;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class PersonImpl extends DynamicEntity implements Person {

    public PersonImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<Person> {
        public ProvidedFactory() {
            super(Person.class, PersonImpl::new);
        }
    }
}
