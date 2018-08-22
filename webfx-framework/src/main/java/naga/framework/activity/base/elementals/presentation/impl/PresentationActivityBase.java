package naga.framework.activity.base.elementals.presentation.impl;

import naga.platform.services.uischeduler.spi.UiSchedulerProvider;
import naga.util.async.Future;
import naga.util.function.Callable;
import naga.util.function.Factory;
import naga.framework.activity.base.elementals.presentation.PresentationActivity;
import naga.framework.activity.base.elementals.presentation.PresentationActivityContext;
import naga.framework.activity.base.elementals.presentation.PresentationActivityContextMixin;
import naga.framework.activity.base.elementals.presentation.logic.PresentationLogicActivityContext;
import naga.framework.activity.base.elementals.presentation.view.PresentationViewActivityContext;
import naga.framework.activity.base.elementals.presentation.view.impl.PresentationViewActivityBase;
import naga.fx.spi.Toolkit;
import naga.framework.activity.Activity;
import naga.framework.activity.ActivityContextFactory;
import naga.framework.activity.ActivityManager;
import naga.framework.activity.base.composition.impl.ComposedActivityBase;

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
    public Future<Void> onCreateAsync(C context) {
        Future<Void> future = super.onCreateAsync(context);
        // Ugly parameter passing
        ((PresentationViewActivityBase) getActivityManager1().getActivity()).setPresentationModel(getActivityContext2().getPresentationModel());
        nodeProperty().bind(getActivityContext1().nodeProperty());
        mountNodeProperty().bind(getActivityContext1().mountNodeProperty());
        return future;
    }

    // Temporary code while naga-common can't access to UiSchedulerProvider - TODO: move this management into naga-platform
    @Override
    protected Future<Void> executeBoth(Callable<Future<Void>> callable1, Callable<Future<Void>> callable2) {
        Future<Void> future2 = Future.future();
        UiSchedulerProvider uiSchedulerProvider = Toolkit.get().scheduler();
        uiSchedulerProvider.runOutUiThread(() -> callable2.call().setHandler(future2.completer()));
        Future<Void> future1 = Future.future();
        uiSchedulerProvider.runInUiThread(() -> callable1.call().setHandler(future1.completer()));
        return Future.allOf(future1, future2);
    }
}
