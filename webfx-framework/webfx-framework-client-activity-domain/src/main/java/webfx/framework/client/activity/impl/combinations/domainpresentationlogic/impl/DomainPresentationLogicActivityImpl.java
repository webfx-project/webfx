package webfx.framework.client.activity.impl.combinations.domainpresentationlogic.impl;

import webfx.framework.client.activity.impl.elementals.domain.DomainActivityContextMixin;
import webfx.framework.client.activity.impl.elementals.presentation.logic.impl.PresentationLogicActivityBase;
import webfx.platform.shared.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public abstract class DomainPresentationLogicActivityImpl<PM>
        extends PresentationLogicActivityBase<DomainPresentationLogicActivityContextFinal<PM>, PM>
        implements DomainActivityContextMixin<DomainPresentationLogicActivityContextFinal<PM>>  {

    public DomainPresentationLogicActivityImpl() {
    }

    public DomainPresentationLogicActivityImpl(Factory<PM> presentationModelFactory) {
        super(presentationModelFactory);
    }
}
