package mongoose.shared.entities.impl;

import mongoose.shared.entities.GatewayParameter;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.entity.impl.EntityFactoryProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class GatewayParameterImpl extends DynamicEntity implements GatewayParameter {

    public GatewayParameterImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    public static final class ProvidedFactory extends EntityFactoryProviderImpl<GatewayParameter> {
        public ProvidedFactory() {
            super(GatewayParameter.class, GatewayParameterImpl::new);
        }
    }
}
