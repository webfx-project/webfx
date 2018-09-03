package mongooses.core.entities.impl;

import mongooses.core.entities.Site;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class SiteImpl extends DynamicEntity implements Site {

    public SiteImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
