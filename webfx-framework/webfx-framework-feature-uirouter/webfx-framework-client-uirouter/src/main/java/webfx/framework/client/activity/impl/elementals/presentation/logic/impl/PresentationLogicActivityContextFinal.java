package webfx.framework.client.activity.impl.elementals.presentation.logic.impl;

import webfx.framework.client.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public final class PresentationLogicActivityContextFinal<PM>
        extends PresentationLogicActivityContextBase<PresentationLogicActivityContextFinal<PM>, PM> {

    public PresentationLogicActivityContextFinal(ActivityContext parentContext) {
        super(parentContext, PresentationLogicActivityContextFinal::new);
    }
}
