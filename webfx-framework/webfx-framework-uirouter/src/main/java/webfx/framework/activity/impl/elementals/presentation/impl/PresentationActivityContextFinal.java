package webfx.framework.activity.impl.elementals.presentation.impl;

import webfx.framework.activity.impl.elementals.presentation.logic.impl.PresentationLogicActivityContextFinal;
import webfx.framework.activity.impl.elementals.presentation.view.impl.PresentationViewActivityContextFinal;
import webfx.framework.activity.ActivityContext;
import webfx.framework.activity.ActivityContextFactory;

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
