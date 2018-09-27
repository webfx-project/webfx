package mongoose.client.application;

import mongoose.shared.actions.MongooseActions;
import mongoose.client.jobs.sessionrecorder.ProvidedClientSessionRecorderJob;
import webfx.framework.activity.Activity;
import webfx.framework.activity.ActivityContext;
import webfx.framework.activity.impl.combinations.viewapplication.ViewApplicationContext;
import webfx.framework.activity.impl.combinations.viewdomain.ViewDomainActivityContext;
import webfx.framework.activity.impl.combinations.viewdomain.ViewDomainActivityContextMixin;
import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.operation.action.OperationActionFactoryMixin;
import webfx.framework.operation.action.OperationActionRegistry;
import webfx.framework.orm.entity.Entity;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.ui.action.Action;
import webfx.framework.ui.controls.button.ButtonFactoryMixin;
import webfx.framework.ui.layouts.SceneUtil;
import webfx.framework.ui.uirouter.UiRouter;
import webfx.fxkits.core.util.properties.Properties;
import webfx.platforms.core.services.buscall.PendingBusCall;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.util.function.Factory;

import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public abstract class MongooseClientApplication
        implements Activity<ViewDomainActivityContext>
        , ViewDomainActivityContextMixin
        , OperationActionFactoryMixin
        , ButtonFactoryMixin {

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
        // Also passing the userPrincipal property to the client session recorder so it can react to user changes
        ProvidedClientSessionRecorderJob.setUserPrincipalProperty(getUiSession().userPrincipalProperty());
    }

    protected abstract Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory();

    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return containedRouter
                // Pages shared by both back-end and font-end (listed in alphabetic order)
                .registerProvidedUiRoutes()
                ;
    }

    @Override
    public void onStart() {
        registerActions();
        UiRouter uiRouter = getUiRouter();
        uiRouter.setDefaultInitialHistoryPath(defaultInitialHistoryPath);
        uiRouter.start();
    }

    protected void registerActions() {
        MongooseActions.registerActions();
        EntityStore.create(getDataSourceModel()).executeQuery("select operationCode,i18nCode,public from Operation").setHandler(ar -> {
            if (ar.failed())
                Logger.log("Failed loading operations", ar.cause());
            else {
                OperationActionRegistry registry = getOperationActionRegistry();
                for (Entity operation : ar.result()) {
                    String operationCode = operation.getStringFieldValue("operationCode");
                    String i18nCode = operation.getStringFieldValue("i18nCode");
                    boolean isPublic = operation.getBooleanFieldValue("public");
                    Action action = isPublic ? newAction(i18nCode) : newAuthAction(i18nCode, registry.authorizedOperationActionProperty(operationCode, userPrincipalProperty(), this::isAuthorized));
                    registry.registerOperationAction(operationCode, action);
                }
            }
        });
    }

    static {
        // Activating focus owner auto scroll
        SceneUtil.installPrimarySceneFocusOwnerAutoScroll();
    }

    public static void setLoadingSpinnerVisibleConsumer(Consumer<Boolean> consumer) {
        Properties.consumeInUiThread(Properties.combine(ViewApplicationContext.getViewApplicationContext().windowBoundProperty(), PendingBusCall.pendingCallsCountProperty(),
                (windowBound, pendingCallsCount) -> !windowBound || pendingCallsCount > 0)
                , consumer);
    }

}
