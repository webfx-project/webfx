package mongoose.entities.impl;

import mongoose.entities.Event;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public class EventImpl extends DynamicEntity implements Event {

    public EventImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    @Override
    public void setName(String name) {
        setFieldValue("name", name);
    }

    @Override
    public String getName() {
        return getStringFieldValue("name");
    }

    @Override
    public void setOrganization(Object organization) {
        setForeignField("organization", organization);
    }
}
