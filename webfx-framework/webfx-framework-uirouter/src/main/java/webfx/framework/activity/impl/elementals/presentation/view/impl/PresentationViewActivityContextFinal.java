package webfx.framework.activity.impl.elementals.presentation.view.impl;

import webfx.framework.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public final class PresentationViewActivityContextFinal<PM>
        extends PresentationViewActivityContextBase<PresentationViewActivityContextFinal<PM>, PM> {

    public PresentationViewActivityContextFinal(ActivityContext parentContext) {
        super(parentContext, PresentationViewActivityContextFinal::new);
    }

}
