package naga.framework.activity.base.composition.impl;

import naga.framework.activity.ActivityContext;
import naga.framework.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
final class ComposedActivityContextFinal
        <C1 extends ActivityContext<C1>,
                C2 extends ActivityContext<C2>>

        extends ComposedActivityContextBase<ComposedActivityContextFinal<C1, C2>, C1, C2> {

    ComposedActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<ComposedActivityContextFinal<C1, C2>> contextFactory) {
        super(parentContext, contextFactory);
    }
}
