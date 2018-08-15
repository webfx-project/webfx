package naga.framework.activity.composition;

import naga.framework.activity.Activity;
import naga.framework.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface ComposedActivity
        <C extends ComposedActivityContext<C, C1, C2>,
                C1 extends ActivityContext<C1>,
                C2 extends ActivityContext<C2>>

        extends Activity<C> {
}
