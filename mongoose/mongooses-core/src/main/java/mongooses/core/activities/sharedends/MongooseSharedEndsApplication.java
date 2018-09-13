package mongooses.core.activities.sharedends;

import mongooses.core.actions.MongooseActions;
import mongooses.core.activities.sharedends.book.cart.CartRouting;
import mongooses.core.activities.sharedends.book.fees.FeesRouting;
import mongooses.core.activities.sharedends.book.options.OptionsRouting;
import mongooses.core.activities.sharedends.book.payment.PaymentRouting;
import mongooses.core.activities.sharedends.book.person.PersonRouting;
import mongooses.core.activities.sharedends.book.program.ProgramRouting;
import mongooses.core.activities.sharedends.book.start.StartBookingRouting;
import mongooses.core.activities.sharedends.book.summary.SummaryRouting;
import mongooses.core.activities.sharedends.book.terms.TermsRouting;
import mongooses.core.activities.sharedends.generic.session.ClientSessionRecorder;
import mongooses.core.domainmodel.loader.DomainModelSnapshotLoader;
import mongooses.core.services.authn.MongooseAuthenticationServiceProviderImpl;
import mongooses.core.services.authz.MongooseAuthorizationServiceProviderImpl;
import webfx.framework.activity.Activity;
import webfx.framework.activity.ActivityContext;
import webfx.framework.activity.ActivityManager;
import webfx.framework.activity.base.combinations.viewapplication.ViewApplicationContext;
import webfx.framework.activity.base.combinations.viewdomain.ViewDomainActivityContext;
import webfx.framework.activity.base.combinations.viewdomain.ViewDomainActivityContextMixin;
import webfx.framework.activity.base.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.activity.base.combinations.viewdomainapplication.ViewDomainApplicationContext;
import webfx.framework.operation.action.OperationActionFactoryMixin;
import webfx.framework.operation.action.OperationActionRegistry;
import webfx.framework.orm.entity.Entity;
import webfx.framework.orm.entity.EntityStore;
import webfx.framework.services.authn.spi.AuthenticationServiceProvider;
import webfx.framework.services.authz.spi.AuthorizationServiceProvider;
import webfx.framework.services.i18n.I18n;
import webfx.framework.services.i18n.spi.I18nProvider;
import webfx.framework.ui.action.Action;
import webfx.framework.ui.layouts.SceneUtil;
import webfx.framework.ui.uirouter.UiRouter;
import webfx.fxkits.core.properties.Properties;
import webfx.platforms.core.services.buscall.PendingBusCall;
import webfx.platforms.core.services.bus.spi.BusService;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.util.function.Consumer;
import webfx.platforms.core.util.function.Factory;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public abstract class MongooseSharedEndsApplication
        implements Activity<ViewDomainActivityContext>
        , ViewDomainActivityContextMixin
        , OperationActionFactoryMixin {

    private final String defaultInitialHistoryPath;
    private ViewDomainActivityContext context;

    public MongooseSharedEndsApplication(String defaultInitialHistoryPath) {
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
        ClientSessionRecorder.get().setUserPrincipalProperty(getUiSession().userPrincipalProperty());
    }

    protected abstract Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory();

    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return containedRouter
                // Pages shared by both back-end and font-end (listed in alphabetic order)
                .route(CartRouting.uiRoute())
                .route(FeesRouting.uiRoute())
                .route(OptionsRouting.uiRoute())
                .route(PaymentRouting.uiRoute())
                .route(PersonRouting.uiRoute())
                .route(ProgramRouting.uiRoute())
                .route(StartBookingRouting.uiRoute())
                .route(SummaryRouting.uiRoute())
                .route(TermsRouting.uiRoute())
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
        // Instantiating the bus as soon as possible to open the connection while the application is initializing
        BusService.bus();
        // Registering Mongoose authn/authz services as default services (if not found by the ServiceLoader - which is the case with GWT)
        SingleServiceLoader.registerDefaultServiceFactory(AuthenticationServiceProvider.class, MongooseAuthenticationServiceProviderImpl::new);
        SingleServiceLoader.registerDefaultServiceFactory(AuthorizationServiceProvider.class, MongooseAuthorizationServiceProviderImpl::new);
        // Registering i18n service provider based on json resources
        I18n.registerProvider(I18nProvider.createFromJsonResources("mongooses/core/dictionaries/{lang}.json"));
        // Activating focus owner auto scroll
        SceneUtil.installPrimarySceneFocusOwnerAutoScroll();
    }

    protected static void launchApplication(MongooseSharedEndsApplication mongooseApplication, String[] args) {
        ActivityManager.runActivity(
                mongooseApplication,
                ViewDomainApplicationContext.createViewDomainApplicationContext(
                        DomainModelSnapshotLoader.getDataSourceModel(),
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
