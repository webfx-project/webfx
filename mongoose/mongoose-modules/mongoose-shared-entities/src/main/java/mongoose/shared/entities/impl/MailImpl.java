package mongoose.shared.entities.impl;

import mongoose.shared.entities.Mail;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class MailImpl extends DynamicEntity implements Mail {

    public MailImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<Mail> {
        public ProvidedFactory() {
            super(Mail.class, MailImpl::new);
        }
    }
}
