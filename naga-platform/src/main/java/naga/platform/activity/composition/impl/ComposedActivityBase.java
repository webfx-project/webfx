package naga.platform.activity.composition.impl;

import naga.commons.util.async.Future;
import naga.commons.util.function.Factory;
import naga.platform.activity.*;
import naga.platform.activity.composition.ComposedActivity;
import naga.platform.activity.composition.ComposedActivityContext;
import naga.platform.activity.composition.ComposedActivityContextMixin;
import naga.platform.activity.impl.ActivityBase;

/**
 * @author Bruno Salmon
 */
public class ComposedActivityBase
        <C extends ComposedActivityContext<C, C1, C2>,
                C1 extends ActivityContext<C1>,
                C2 extends ActivityContext<C2>>

    extends ActivityBase<C>
    implements ComposedActivity<C, C1, C2>,
        ComposedActivityContextMixin<C, C1, C2> {

    private final Factory<ActivityManager<C1>> activityManagerFactory1;
    private final Factory<ActivityManager<C2>> activityManagerFactory2;

    public ComposedActivityBase(Factory<Activity<C1>> activityFactory1, ActivityContextFactory<C1> contextFactory1, Factory<Activity<C2>> activityFactory2, ActivityContextFactory<C2> contextFactory2) {
        this(ActivityManager.factory(activityFactory1, contextFactory1), ActivityManager.factory(activityFactory2, contextFactory2));
    }

    public ComposedActivityBase(Factory<ActivityManager<C1>> activityManagerFactory1, Factory<ActivityManager<C2>> activityManagerFactory2) {
        this.activityManagerFactory1 = activityManagerFactory1;
        this.activityManagerFactory2 = activityManagerFactory2;
    }

    @Override
    public Future<Void> onCreateAsync(C context) {
        onCreate(context);
        ActivityManager<C1> activityManager1 = activityManagerFactory1.create();
        ActivityManager<C2> activityManager2 = activityManagerFactory2.create();
        C1 context1 = activityManager1.getContextFactory().createContext(context);
        C2 context2 = activityManager2.getContextFactory().createContext(context);
        context.setActivityContext1(context1);
        context.setActivityContext2(context2);
        return Future.executeParallel(activityManager1.create(context1), activityManager2.create(context2));
    }

    @Override
    public Future<Void> onStartAsync() {
        return Future.executeParallel(getActivityManager1().start(), getActivityManager2().start());
    }

    @Override
    public Future<Void> onResumeAsync() {
        return Future.executeParallel(getActivityManager1().resume(), getActivityManager2().resume());
    }

    @Override
    public Future<Void> onPauseAsync() {
        return Future.executeParallel(getActivityManager1().pause(), getActivityManager2().pause());
    }

    @Override
    public Future<Void> onStopAsync() {
        return Future.executeParallel(getActivityManager1().stop(), getActivityManager2().stop());
    }

    @Override
    public Future<Void> onRestartAsync() {
        return Future.executeParallel(getActivityManager1().restart(), getActivityManager2().restart());
    }

    @Override
    public Future<Void> onDestroyAsync() {
        return Future.executeParallel(getActivityManager1().destroy(), getActivityManager2().destroy());
    }
}
