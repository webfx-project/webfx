package mongoose.activities.shared.application;

import mongoose.actions.MongooseActions;
import mongoose.activities.shared.book.cart.CartRouting;
import mongoose.activities.shared.book.cart.payment.PaymentRouting;
import mongoose.activities.shared.book.event.fees.FeesRouting;
import mongoose.activities.shared.book.event.options.OptionsRouting;
import mongoose.activities.shared.book.event.person.PersonRouting;
import mongoose.activities.shared.book.event.program.ProgramRouting;
import mongoose.activities.shared.book.event.start.StartBookingRouting;
import mongoose.activities.shared.book.event.summary.SummaryRouting;
import mongoose.activities.shared.book.event.terms.TermsRouting;
import mongoose.authn.MongooseAuthenticationServiceProvider;
import mongoose.authz.MongooseAuthorizationServiceProvider;
import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.activity.combinations.viewapplication.ViewApplicationContext;
import naga.framework.activity.combinations.viewdomain.ViewDomainActivityContext;
import naga.framework.activity.combinations.viewdomain.ViewDomainActivityContextMixin;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.activity.combinations.viewdomainapplication.ViewDomainApplicationContext;
import naga.framework.operation.action.OperationActionProducer;
import naga.framework.operation.action.OperationActionRegistry;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityStore;
import naga.framework.spi.authn.AuthenticationServiceProvider;
import naga.framework.spi.authz.AuthorizationServiceProvider;
import naga.framework.ui.action.Action;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.layouts.SceneUtil;
import naga.framework.ui.router.UiRouter;
import naga.fx.properties.Properties;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityManager;
import naga.platform.bus.call.PendingBusCall;
import naga.platform.services.log.spi.Logger;
import naga.platform.spi.Platform;
import naga.util.function.Consumer;
import naga.util.function.Factory;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public abstract class SharedMongooseApplication
        implements Activity<ViewDomainActivityContext>
        , ViewDomainActivityContextMixin
        , OperationActionProducer {

    private final String defaultInitialHistoryPath;
    private ViewDomainActivityContext context;

    public SharedMongooseApplication(String defaultInitialHistoryPath) {
        this.defaultInitialHistoryPath = defaultInitialHistoryPath;
    }

    @Override
    public ActivityContext getActivityContext() {
        return context;
    }

    @Override
    public void onCreate(ViewDomainActivityContext context) {
        this.context = context;
        context.getUiRouter().routeAndMount("/", getContainerActivityFactory(), setupContainedRouter(UiRouter.createSubRouter(context)));
    }

    protected abstract Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory();

    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return containedRouter
                .route(StartBookingRouting.uiRoute())
                .route(FeesRouting.uiRoute())
                .route(TermsRouting.uiRoute())
                .route(ProgramRouting.uiRoute())
                .route(OptionsRouting.uiRoute())
                .route(PersonRouting.uiRoute())
                .route(SummaryRouting.uiRoute())
                .route(CartRouting.uiRoute())
                .route(PaymentRouting.uiRoute())
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
        MongooseActions.registerActions(getI18n());
/*
        getOperationActionRegistry()
                .registerOperationAction(BackwardRoutingRequest.class,  newAction("<<"))
                .registerOperationAction(ForwardRoutingRequest.class,   newAction(">>"))
                .registerOperationAction(EnglishLanguageRequest.class,  newAction("English"))
                .registerOperationAction(FrenchLanguageRequest.class,   newAction("Français"))
        ;
*/
        EntityStore.create(getDataSourceModel()).executeQuery("select operationCode,i18nCode,public from Operation").setHandler(ar -> {
            if (ar.failed())
                Logger.log(ar.cause());
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
        Logger.log("build.number = " + System.getProperty("build.number"));
        Platform.bus(); // instantiating the platform bus here to open the connection as soon as possible (ex: before loading the model which is time consuming)
        // Registering Mongoose authn/authz services as default services (if not found by the ServiceLoader - which is the case with GWT)
        ServiceLoaderHelper.registerDefaultServiceFactory(AuthenticationServiceProvider.class, MongooseAuthenticationServiceProvider::new);
        ServiceLoaderHelper.registerDefaultServiceFactory(AuthorizationServiceProvider.class, MongooseAuthorizationServiceProvider::new);
        SceneUtil.installPrimarySceneFocusOwnerAutoScroll();
    }

    protected static void launchApplication(SharedMongooseApplication mongooseApplication, String[] args) {
        ActivityManager.launchApplication(
                mongooseApplication,
                ViewDomainApplicationContext.createViewDomainApplicationContext(
                        DomainModelSnapshotLoader.getDataSourceModel(),
                        I18n.create("mongoose/dictionaries/{lang}.json"),
                        args
                )
        );
    }

    public static void setLoadingSpinnerVisibleConsumer(Consumer<Boolean> consumer) {
        Properties.consumeInUiThread(Properties.combine(ViewApplicationContext.getViewApplicationContext().windowBoundProperty(), PendingBusCall.pendingCallsCountProperty(),
                (windowBound, pendingCallsCount) -> !windowBound || pendingCallsCount > 0)
                , consumer);
    }

}
