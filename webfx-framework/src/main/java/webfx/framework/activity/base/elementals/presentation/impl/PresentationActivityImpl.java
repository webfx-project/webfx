package webfx.framework.activity.base.elementals.presentation.impl;

import webfx.platforms.core.util.function.Factory;
import webfx.framework.activity.base.elementals.presentation.logic.impl.PresentationLogicActivityContextFinal;
import webfx.framework.activity.base.elementals.presentation.view.impl.PresentationViewActivityContextFinal;
import webfx.framework.activity.Activity;

/**
 * @author Bruno Salmon
 */
public abstract class PresentationActivityImpl<PM>
        extends PresentationActivityBase<PresentationActivityContextFinal<PM>, PresentationViewActivityContextFinal<PM>, PresentationLogicActivityContextFinal<PM>, PM> {

    public PresentationActivityImpl(Factory<Activity<PresentationViewActivityContextFinal<PM>>> activityFactory1, Factory<Activity<PresentationLogicActivityContextFinal<PM>>> activityFactory2) {
        super(activityFactory1, PresentationViewActivityContextFinal::new, activityFactory2, PresentationLogicActivityContextFinal::new);
    }

}
