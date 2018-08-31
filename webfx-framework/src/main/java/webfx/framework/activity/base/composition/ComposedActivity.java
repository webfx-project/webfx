package webfx.framework.activity.base.composition;

import webfx.framework.activity.Activity;
import webfx.framework.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface ComposedActivity
        <C extends ComposedActivityContext<C, C1, C2>,
                C1 extends ActivityContext<C1>,
                C2 extends ActivityContext<C2>>

        extends Activity<C> {
}
