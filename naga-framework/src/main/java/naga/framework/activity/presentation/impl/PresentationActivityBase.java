package naga.framework.activity.presentation.impl;

import naga.commons.util.async.Future;
import naga.commons.util.function.Factory;
import naga.framework.activity.presentation.logic.PresentationLogicActivityContext;
import naga.framework.activity.presentation.PresentationActivity;
import naga.framework.activity.presentation.PresentationActivityContext;
import naga.framework.activity.presentation.PresentationActivityContextMixin;
import naga.framework.activity.presentation.view.impl.PresentationViewActivityBase;
import naga.framework.activity.presentation.view.PresentationViewActivityContext;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityContextFactory;
import naga.platform.activity.ActivityManager;
import naga.platform.activity.composition.impl.ComposedActivityBase;

/**
 * @author Bruno Salmon
 */
public class PresentationActivityBase
        <C extends PresentationActivityContext<C, C1, C2, PM>,
                C1 extends PresentationViewActivityContext<C1, PM>,
                C2 extends PresentationLogicActivityContext<C2, PM>,
                PM>

        extends ComposedActivityBase<C, C1, C2>
        implements PresentationActivity<C, C1, C2, PM>,
        PresentationActivityContextMixin<C, C1, C2, PM> {

    public PresentationActivityBase(Factory<Activity<C1>> activityFactory1, ActivityContextFactory<C1> contextFactory1, Factory<Activity<C2>> activityFactory2, ActivityContextFactory<C2> contextFactory2) {
        super(activityFactory1, contextFactory1, activityFactory2, contextFactory2);
    }

    public PresentationActivityBase(Factory<ActivityManager<C1>> activityManagerFactory1, Factory<ActivityManager<C2>> activityManagerFactory2) {
        super(activityManagerFactory1, activityManagerFactory2);
    }

    @Override
    public Future<Void> onStartAsync() {
        Future<Void> future = super.onStartAsync();
        // Ugly parameter passing
        ((PresentationViewActivityBase) getActivityManager1().getActivity()).setPresentationModel(getActivityContext2().getPresentationModel());
        nodeProperty().bind(getActivityContext1().nodeProperty());
        mountNodeProperty().bind(getActivityContext1().mountNodeProperty());
        return future;
    }

}
