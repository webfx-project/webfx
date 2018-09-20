package webfx.framework.activity.impl.combinations.domainpresentation.impl;

import webfx.framework.activity.impl.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityContextFinal;
import webfx.framework.activity.impl.elementals.presentation.view.impl.PresentationViewActivityContextFinal;
import webfx.framework.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public final class DomainPresentationActivityContextFinal<PM>
        extends DomainPresentationActivityContextBase<DomainPresentationActivityContextFinal<PM>, PresentationViewActivityContextFinal<PM>, DomainPresentationLogicActivityContextFinal<PM>, PM> {

    public DomainPresentationActivityContextFinal(ActivityContext parentContext) {
        super(parentContext, DomainPresentationActivityContextFinal::new);
    }
}
