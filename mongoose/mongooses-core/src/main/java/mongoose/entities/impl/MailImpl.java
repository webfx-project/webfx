package mongoose.entities.impl;

import mongoose.entities.Mail;
import webfx.framework.orm.entity.EntityId;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.orm.entity.impl.DynamicEntity;

/**
 * @author Bruno Salmon
 */
public final class MailImpl extends DynamicEntity implements Mail {

    public MailImpl(EntityId id, EntityStore store) {
        super(id, store);
    }
}
