package naga.framework.activity.view.presentationview;

import naga.platform.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public final class PresentationViewActivityContextFinal<PM>
        extends PresentationViewActivityContextBase<PresentationViewActivityContextFinal<PM>, PM> {

    public PresentationViewActivityContextFinal(ActivityContext parentContext) {
        super(parentContext, PresentationViewActivityContextFinal::new);
    }

}
