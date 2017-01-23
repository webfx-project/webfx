package naga.framework.activity.combinations.domainpresentation.impl;

import naga.framework.activity.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityContextFinal;
import naga.framework.activity.presentation.view.impl.PresentationViewActivityContextFinal;
import naga.platform.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public final class DomainPresentationActivityContextFinal<PM>
        extends DomainPresentationActivityContextBase<DomainPresentationActivityContextFinal<PM>, PresentationViewActivityContextFinal<PM>, DomainPresentationLogicActivityContextFinal<PM>, PM> {

    public DomainPresentationActivityContextFinal(ActivityContext parentContext) {
        super(parentContext, DomainPresentationActivityContextFinal::new);
    }
}
