package mongoose.entities.impl;

import mongoose.entities.DateInfo;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public class DateInfoImpl extends DynamicEntity implements DateInfo {

    public DateInfoImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
