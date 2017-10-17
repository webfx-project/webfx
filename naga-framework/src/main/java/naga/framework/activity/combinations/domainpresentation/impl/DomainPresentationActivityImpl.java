package naga.framework.activity.combinations.domainpresentation.impl;

import naga.util.function.Factory;
import naga.framework.activity.presentation.impl.PresentationActivityBase;
import naga.framework.activity.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityContextFinal;
import naga.framework.activity.presentation.view.impl.PresentationViewActivityContextFinal;
import naga.platform.activity.Activity;

/**
 * @author Bruno Salmon
 */
public abstract class DomainPresentationActivityImpl<PM>
        extends PresentationActivityBase<DomainPresentationActivityContextFinal<PM>, PresentationViewActivityContextFinal<PM>, DomainPresentationLogicActivityContextFinal<PM>, PM> {

    public DomainPresentationActivityImpl(Factory<Activity<PresentationViewActivityContextFinal<PM>>> activityFactory1, Factory<Activity<DomainPresentationLogicActivityContextFinal<PM>>> activityFactory2) {
        super(activityFactory1, PresentationViewActivityContextFinal::new, activityFactory2, DomainPresentationLogicActivityContextFinal::new);
    }

}
