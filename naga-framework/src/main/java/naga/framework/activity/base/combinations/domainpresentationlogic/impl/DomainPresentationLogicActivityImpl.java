package naga.framework.activity.base.combinations.domainpresentationlogic.impl;

import naga.framework.activity.base.elementals.domain.DomainActivityContextMixin;
import naga.framework.activity.base.elementals.presentation.logic.impl.PresentationLogicActivityBase;
import naga.util.function.Factory;

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
