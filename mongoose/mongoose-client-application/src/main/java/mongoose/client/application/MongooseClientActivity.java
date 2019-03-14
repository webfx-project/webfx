package mongoose.client.application;

import mongoose.client.actions.MongooseActions;
import webfx.framework.client.activity.Activity;
import webfx.framework.client.activity.ActivityContext;
import webfx.framework.client.activity.impl.combinations.viewdomain.ViewDomainActivityContext;
import webfx.framework.client.activity.impl.combinations.viewdomain.ViewDomainActivityContextMixin;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRouter;
import webfx.platform.shared.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public abstract class MongooseClientActivity
        implements Activity<ViewDomainActivityContext>
        , ViewDomainActivityContextMixin {

    private final String defaultInitialHistoryPath;
    private ViewDomainActivityContext context;

    public MongooseClientActivity(String defaultInitialHistoryPath) {
        this.defaultInitialHistoryPath = defaultInitialHistoryPath;
    }

    @Override
    public ActivityContext getActivityContext() {
        return context;
    }

    @Override
    public void onCreate(ViewDomainActivityContext context) {
        this.context = context;
        getUiRouter().routeAndMount("/", getContainerActivityFactory(), setupContainedRouter(UiRouter.createSubRouter(context)));
    }

    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return MongooseClientContainerActivity::new;
    }

    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return containedRouter.registerProvidedUiRoutes();
    }

    @Override
    public void onStart() {
        MongooseActions.registerActions();
        UiRouter uiRouter = getUiRouter();
        uiRouter.setDefaultInitialHistoryPath(defaultInitialHistoryPath);
        uiRouter.start();
    }
}
