package naga.framework.activity.base.combinations.domainpresentation.impl;

import naga.util.function.Factory;
import naga.framework.activity.base.elementals.presentation.impl.PresentationActivityBase;
import naga.framework.activity.base.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityContextFinal;
import naga.framework.activity.base.elementals.presentation.view.impl.PresentationViewActivityContextFinal;
import naga.framework.activity.Activity;

/**
 * @author Bruno Salmon
 */
public abstract class DomainPresentationActivityImpl<PM>
        extends PresentationActivityBase<DomainPresentationActivityContextFinal<PM>, PresentationViewActivityContextFinal<PM>, DomainPresentationLogicActivityContextFinal<PM>, PM> {

    public DomainPresentationActivityImpl(Factory<Activity<PresentationViewActivityContextFinal<PM>>> activityFactory1, Factory<Activity<DomainPresentationLogicActivityContextFinal<PM>>> activityFactory2) {
        super(activityFactory1, PresentationViewActivityContextFinal::new, activityFactory2, DomainPresentationLogicActivityContextFinal::new);
    }

}
