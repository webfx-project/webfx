package mongoose.entities.impl;

import mongoose.entities.OrganizationType;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public class OrganizationTypeImpl extends DynamicEntity implements OrganizationType {

    public OrganizationTypeImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
