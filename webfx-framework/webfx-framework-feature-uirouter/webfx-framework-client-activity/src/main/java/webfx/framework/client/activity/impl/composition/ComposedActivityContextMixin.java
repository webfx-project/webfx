package webfx.framework.client.activity.impl.composition;

import webfx.framework.client.activity.ActivityContext;
import webfx.framework.client.activity.ActivityContextMixin;
import webfx.framework.client.activity.ActivityManager;

/**
 * @author Bruno Salmon
 */
public interface ComposedActivityContextMixin
        <C extends ComposedActivityContext<C, C1, C2>,
                C1 extends ActivityContext<C1>,
                C2 extends ActivityContext<C2>>

        extends ActivityContextMixin<C>,
        ComposedActivityContext<C, C1, C2> {


    default C1 getActivityContext1() {
        return getActivityContext().getActivityContext1();
    }

    @Override
    default void setActivityContext1(C1 context1) {
        getActivityContext().setActivityContext1(context1);
    }

    default C2 getActivityContext2() {
        return getActivityContext().getActivityContext2();
    }

    @Override
    default void setActivityContext2(C2 context2) {
        getActivityContext().setActivityContext2(context2);
    }

    default ActivityManager<C1> getActivityManager1() {
        return getActivityContext().getActivityManager1();
    }

    default ActivityManager<C2> getActivityManager2() {
        return getActivityContext().getActivityManager2();
    }
}
