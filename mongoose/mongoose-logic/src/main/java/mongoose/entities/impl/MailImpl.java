package mongoose.entities.impl;

import mongoose.entities.Mail;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class MailImpl extends DynamicEntity implements Mail {

    public MailImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
