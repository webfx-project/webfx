package webfx.framework.client.activity.impl.elementals.presentation.impl;

import webfx.platform.shared.util.function.Factory;
import webfx.framework.client.activity.impl.elementals.presentation.logic.impl.PresentationLogicActivityContextFinal;
import webfx.framework.client.activity.impl.elementals.presentation.view.impl.PresentationViewActivityContextFinal;
import webfx.framework.client.activity.Activity;

/**
 * @author Bruno Salmon
 */
public abstract class PresentationActivityImpl<PM>
        extends PresentationActivityBase<PresentationActivityContextFinal<PM>, PresentationViewActivityContextFinal<PM>, PresentationLogicActivityContextFinal<PM>, PM> {

    public PresentationActivityImpl(Factory<Activity<PresentationViewActivityContextFinal<PM>>> activityFactory1, Factory<Activity<PresentationLogicActivityContextFinal<PM>>> activityFactory2) {
        super(activityFactory1, PresentationViewActivityContextFinal::new, activityFactory2, PresentationLogicActivityContextFinal::new);
    }

}
