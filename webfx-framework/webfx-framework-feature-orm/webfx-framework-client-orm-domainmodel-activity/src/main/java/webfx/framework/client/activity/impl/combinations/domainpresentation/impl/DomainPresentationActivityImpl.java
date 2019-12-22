package webfx.framework.client.activity.impl.combinations.domainpresentation.impl;

import webfx.platform.shared.util.function.Factory;
import webfx.framework.client.activity.impl.elementals.presentation.impl.PresentationActivityBase;
import webfx.framework.client.activity.impl.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityContextFinal;
import webfx.framework.client.activity.impl.elementals.presentation.view.impl.PresentationViewActivityContextFinal;
import webfx.framework.client.activity.Activity;

/**
 * @author Bruno Salmon
 */
public abstract class DomainPresentationActivityImpl<PM>
        extends PresentationActivityBase<DomainPresentationActivityContextFinal<PM>, PresentationViewActivityContextFinal<PM>, DomainPresentationLogicActivityContextFinal<PM>, PM> {

    public DomainPresentationActivityImpl(Factory<Activity<PresentationViewActivityContextFinal<PM>>> activityFactory1, Factory<Activity<DomainPresentationLogicActivityContextFinal<PM>>> activityFactory2) {
        super(activityFactory1, PresentationViewActivityContextFinal::new, activityFactory2, DomainPresentationLogicActivityContextFinal::new);
    }

}
