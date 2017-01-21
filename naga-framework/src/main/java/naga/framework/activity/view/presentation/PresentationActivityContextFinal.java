package naga.framework.activity.view.presentation;

import naga.framework.activity.presentationlogic.PresentationLogicActivityContextFinal;
import naga.framework.activity.view.presentationview.PresentationViewActivityContextFinal;
import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;

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
