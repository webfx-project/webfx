package mongoose.backend.entities.loadtester.impl;

import mongoose.backend.entities.loadtester.LtTestSetEntity;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

import java.time.Instant;

/**
 * @author Jean-Pierre Alonso.
 */
public final class LtTestSetEntityImpl extends DynamicEntity implements LtTestSetEntity {

    public LtTestSetEntityImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    @Override
    public Instant getDate() {
        return getInstantFieldValue("date");
    }

    @Override
    public void setDate(Instant date) {
        setFieldValue("date", date);
    }

    @Override
    public String getName() {
        return getStringFieldValue("name");
    }

    @Override
    public void setName(String name) {
        setFieldValue("name", name);
    }

    @Override
    public String getComment() {
        return getStringFieldValue("comment");
    }

    @Override
    public void setComment(String comment) {
        setFieldValue("comment", comment);
    }

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<LtTestSetEntity> {
        public ProvidedFactory() {
            super(LtTestSetEntity.class, LtTestSetEntityImpl::new);
        }
    }
}
