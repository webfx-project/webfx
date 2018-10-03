package webfx.framework.client.activity.impl.composition.impl;

import webfx.framework.client.activity.ActivityContext;
import webfx.framework.client.activity.ActivityContextFactory;

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
