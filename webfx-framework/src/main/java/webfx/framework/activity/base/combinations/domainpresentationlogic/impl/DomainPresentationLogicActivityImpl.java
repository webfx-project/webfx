package webfx.framework.activity.base.combinations.domainpresentationlogic.impl;

import webfx.framework.activity.base.elementals.domain.DomainActivityContextMixin;
import webfx.framework.activity.base.elementals.presentation.logic.impl.PresentationLogicActivityBase;
import webfx.util.function.Factory;

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
