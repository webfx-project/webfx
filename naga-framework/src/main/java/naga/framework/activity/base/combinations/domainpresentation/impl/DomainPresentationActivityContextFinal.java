package naga.framework.activity.base.combinations.domainpresentation.impl;

import naga.framework.activity.base.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityContextFinal;
import naga.framework.activity.base.elementals.presentation.view.impl.PresentationViewActivityContextFinal;
import naga.framework.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public final class DomainPresentationActivityContextFinal<PM>
        extends DomainPresentationActivityContextBase<DomainPresentationActivityContextFinal<PM>, PresentationViewActivityContextFinal<PM>, DomainPresentationLogicActivityContextFinal<PM>, PM> {

    public DomainPresentationActivityContextFinal(ActivityContext parentContext) {
        super(parentContext, DomainPresentationActivityContextFinal::new);
    }
}
