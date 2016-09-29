package mongoose.entities.impl;

import mongoose.entities.Option;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public class OptionImpl extends DynamicEntity implements Option {

    public OptionImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
