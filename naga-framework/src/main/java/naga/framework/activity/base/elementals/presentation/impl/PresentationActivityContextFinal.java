package naga.framework.activity.base.elementals.presentation.impl;

import naga.framework.activity.base.elementals.presentation.logic.impl.PresentationLogicActivityContextFinal;
import naga.framework.activity.base.elementals.presentation.view.impl.PresentationViewActivityContextFinal;
import naga.framework.activity.ActivityContext;
import naga.framework.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class PresentationActivityContextFinal
       <PM>

        extends PresentationActivityContextBase<PresentationActivityContextFinal<PM>, PresentationViewActivityContextFinal<PM>, PresentationLogicActivityContextFinal<PM>, PM> {


    public PresentationActivityContextFinal(ActivityContext parentContext) {
        super(parentContext, PresentationActivityContextFinal::new);
    }

    public PresentationActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<PresentationActivityContextFinal<PM>> contextFactory) {
        super(parentContext, contextFactory);
    }
}
