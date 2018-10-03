package webfx.framework.client.activity;

/**
 * Mixin interface that can be used to have a direct access to the ActivityContext methods on any class implementing
 * HasActivityContext. When there are many calls to the ActivityContext, this can be convenient to just write
 * getHistory().push(...) instead of getActivityContext().getHistory().push(...) - for example.
 *
 * @author Bruno Salmon
 */
public interface ActivityContextMixin
        <C extends ActivityContext<C>>

        extends HasActivityContext<C>,
        ActivityContext<C> {

    @Override
    default ActivityContext getParentContext() {
        return getActivityContext().getParentContext();
    }

    @Override
    default ActivityManager<C> getActivityManager() { return getActivityContext().getActivityManager(); }

    @Override
    default ActivityContextFactory<C> getActivityContextFactory() {
        return getActivityContext().getActivityContextFactory();
    }
}
