package mongoose.client.application;

import mongoose.client.actions.MongooseActions;
import webfx.framework.client.activity.Activity;
import webfx.framework.client.activity.ActivityContext;
import webfx.framework.client.activity.impl.combinations.viewapplication.ViewApplicationContext;
import webfx.framework.client.activity.impl.combinations.viewdomain.ViewDomainActivityContext;
import webfx.framework.client.activity.impl.combinations.viewdomain.ViewDomainActivityContextMixin;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRouter;
import webfx.fxkits.core.util.properties.Properties;
import webfx.platform.shared.services.buscall.PendingBusCall;
import webfx.platform.shared.util.function.Factory;

import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public abstract class MongooseClientApplication
        implements Activity<ViewDomainActivityContext>
        , ViewDomainActivityContextMixin {

    private final String defaultInitialHistoryPath;
    private ViewDomainActivityContext context;

    public MongooseClientApplication(String defaultInitialHistoryPath) {
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

    protected abstract Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory();

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

    public static void setLoadingSpinnerVisibleConsumer(Consumer<Boolean> consumer) {
        Properties.consumeInUiThread(Properties.combine(ViewApplicationContext.getViewApplicationContext().windowBoundProperty(), PendingBusCall.pendingCallsCountProperty(),
                (windowBound, pendingCallsCount) -> !windowBound || pendingCallsCount > 0)
                , consumer);
    }
}
