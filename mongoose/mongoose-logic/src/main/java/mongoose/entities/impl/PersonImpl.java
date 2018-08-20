package mongoose.entities.impl;

import mongoose.entities.Person;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class PersonImpl extends DynamicEntity implements Person {

    public PersonImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
