package mongooses.core.entities.impl;

import mongooses.core.entities.Person;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class PersonImpl extends DynamicEntity implements Person {

    public PersonImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
