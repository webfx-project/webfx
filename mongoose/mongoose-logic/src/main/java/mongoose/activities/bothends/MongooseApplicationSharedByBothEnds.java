package mongoose.activities.bothends;

import mongoose.actions.MongooseActions;
import mongoose.activities.bothends.book.cart.CartRouting;
import mongoose.activities.bothends.book.fees.FeesRouting;
import mongoose.activities.bothends.book.options.OptionsRouting;
import mongoose.activities.bothends.book.payment.PaymentRouting;
import mongoose.activities.bothends.book.person.PersonRouting;
import mongoose.activities.bothends.book.program.ProgramRouting;
import mongoose.activities.bothends.book.start.StartBookingRouting;
import mongoose.activities.bothends.book.summary.SummaryRouting;
import mongoose.activities.bothends.book.terms.TermsRouting;
import mongoose.activities.bothends.generic.session.ClientSessionRecorder;
import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import mongoose.services.authn.MongooseAuthenticationServiceProviderImpl;
import mongoose.services.authz.MongooseAuthorizationServiceProviderImpl;
import naga.framework.activity.Activity;
import naga.framework.activity.ActivityContext;
import naga.framework.activity.ActivityManager;
import naga.framework.activity.base.combinations.viewapplication.ViewApplicationContext;
import naga.framework.activity.base.combinations.viewdomain.ViewDomainActivityContext;
import naga.framework.activity.base.combinations.viewdomain.ViewDomainActivityContextMixin;
import naga.framework.activity.base.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.activity.base.combinations.viewdomainapplication.ViewDomainApplicationContext;
import naga.framework.operation.action.OperationActionFactoryMixin;
import naga.framework.operation.action.OperationActionRegistry;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityStore;
import naga.framework.services.authn.spi.AuthenticationServiceProvider;
import naga.framework.services.authz.spi.AuthorizationServiceProvider;
import naga.framework.services.i18n.I18n;
import naga.framework.services.i18n.spi.I18nProvider;
import naga.framework.ui.action.Action;
import naga.framework.ui.layouts.SceneUtil;
import naga.framework.ui.uirouter.UiRouter;
import naga.fx.properties.Properties;
import naga.platform.bus.call.PendingBusCall;
import naga.platform.services.log.Logger;
import naga.platform.spi.Platform;
import naga.util.function.Consumer;
import naga.util.function.Factory;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public abstract class MongooseApplicationSharedByBothEnds
        implements Activity<ViewDomainActivityContext>
        , ViewDomainActivityContextMixin
        , OperationActionFactoryMixin {

    private final String defaultInitialHistoryPath;
    private ViewDomainActivityContext context;

    public MongooseApplicationSharedByBothEnds(String defaultInitialHistoryPath) {
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
        // Instantiating the platform bus as soon as possible to open the connection while the application is initializing
        Platform.bus();
        // Registering Mongoose authn/authz services as default services (if not found by the ServiceLoader - which is the case with GWT)
        ServiceLoaderHelper.registerDefaultServiceFactory(AuthenticationServiceProvider.class, MongooseAuthenticationServiceProviderImpl::new);
        ServiceLoaderHelper.registerDefaultServiceFactory(AuthorizationServiceProvider.class, MongooseAuthorizationServiceProviderImpl::new);
        // Registering i18n service provider based on json resources
        I18n.registerProvider(I18nProvider.createFromJsonResources("mongoose/dictionaries/{lang}.json"));
        // Activating focus owner auto scroll
        SceneUtil.installPrimarySceneFocusOwnerAutoScroll();
    }

    protected static void launchApplication(MongooseApplicationSharedByBothEnds mongooseApplication, String[] args) {
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
