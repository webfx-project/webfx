package webfx.framework.activity.base.elementals.presentation.impl;

import webfx.fxkits.core.spi.FxKit;
import webfx.platforms.core.services.uischeduler.spi.UiSchedulerProvider;
import webfx.platforms.core.util.async.Future;
import webfx.platforms.core.util.function.Callable;
import webfx.platforms.core.util.function.Factory;
import webfx.framework.activity.base.elementals.presentation.PresentationActivity;
import webfx.framework.activity.base.elementals.presentation.PresentationActivityContext;
import webfx.framework.activity.base.elementals.presentation.PresentationActivityContextMixin;
import webfx.framework.activity.base.elementals.presentation.logic.PresentationLogicActivityContext;
import webfx.framework.activity.base.elementals.presentation.view.PresentationViewActivityContext;
import webfx.framework.activity.base.elementals.presentation.view.impl.PresentationViewActivityBase;
import webfx.framework.activity.Activity;
import webfx.framework.activity.ActivityContextFactory;
import webfx.framework.activity.ActivityManager;
import webfx.framework.activity.base.composition.impl.ComposedActivityBase;

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

    @Override
    protected Future<Void> executeBoth(Callable<Future<Void>> callable1, Callable<Future<Void>> callable2) {
        Future<Void> future2 = Future.future();
        UiSchedulerProvider uiSchedulerProvider = FxKit.get().scheduler();
        uiSchedulerProvider.runOutUiThread(() -> callable2.call().setHandler(future2.completer()));
        Future<Void> future1 = Future.future();
        uiSchedulerProvider.runInUiThread(() -> callable1.call().setHandler(future1.completer()));
        return Future.allOf(future1, future2);
    }
}
