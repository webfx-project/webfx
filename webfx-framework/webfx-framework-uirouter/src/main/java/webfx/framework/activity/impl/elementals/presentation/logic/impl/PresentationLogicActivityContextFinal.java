package webfx.framework.activity.impl.elementals.presentation.logic.impl;

import webfx.framework.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public class PresentationLogicActivityContextFinal<PM>
        extends PresentationLogicActivityContextBase<PresentationLogicActivityContextFinal<PM>, PM> {

    public PresentationLogicActivityContextFinal(ActivityContext parentContext) {
        super(parentContext, PresentationLogicActivityContextFinal::new);
    }
}
