package mongoose.entities.impl;

import mongoose.entities.Site;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public class SiteImpl extends DynamicEntity implements Site {

    public SiteImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
