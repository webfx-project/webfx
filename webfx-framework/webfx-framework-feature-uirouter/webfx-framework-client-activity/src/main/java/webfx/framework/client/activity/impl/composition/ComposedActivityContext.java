package webfx.framework.client.activity.impl.composition;

import webfx.framework.client.activity.ActivityContext;
import webfx.framework.client.activity.ActivityManager;

/**
 * @author Bruno Salmon
 */
public interface ComposedActivityContext
        <THIS extends ComposedActivityContext<THIS, C1, C2>,
                C1 extends ActivityContext<C1>,
                C2 extends ActivityContext<C2>>

        extends ActivityContext<THIS> {

    void setActivityContext1(C1 context1);

    C1 getActivityContext1();

    void setActivityContext2(C2 context2);

    C2 getActivityContext2();

    default ActivityManager<C1> getActivityManager1() {
        return getActivityContext1().getActivityManager();
    }

    default ActivityManager<C2> getActivityManager2() {
        return getActivityContext2().getActivityManager();
    }

}
