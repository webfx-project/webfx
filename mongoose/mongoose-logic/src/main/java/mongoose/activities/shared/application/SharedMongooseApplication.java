package mongoose.activities.shared.application;

import mongoose.actions.MongooseActions;
import mongoose.activities.shared.book.cart.CartRooting;
import mongoose.activities.shared.book.cart.payment.PaymentRooting;
import mongoose.activities.shared.book.event.fees.FeesRooting;
import mongoose.activities.shared.book.event.options.OptionsRooting;
import mongoose.activities.shared.book.event.person.PersonRooting;
import mongoose.activities.shared.book.event.program.ProgramRooting;
import mongoose.activities.shared.book.event.start.StartBookingRouting;
import mongoose.activities.shared.book.event.summary.SummaryRooting;
import mongoose.activities.shared.book.event.terms.TermsRooting;
import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.activity.combinations.viewapplication.ViewApplicationContext;
import naga.framework.activity.combinations.viewdomain.ViewDomainActivityContext;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.activity.combinations.viewdomainapplication.ViewDomainApplicationContext;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.layouts.SceneUtil;
import naga.framework.ui.router.UiRouter;
import naga.fx.properties.Properties;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityManager;
import naga.platform.bus.call.PendingBusCall;
import naga.platform.spi.Platform;
import naga.util.function.Consumer;
import naga.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public abstract class SharedMongooseApplication implements Activity<ViewDomainActivityContext> {

    protected ViewDomainActivityContext context;

    @Override
    public void onCreate(ViewDomainActivityContext context) {
        this.context = context;
        context.getUiRouter().routeAndMount("/", getContainerActivityFactory(), setupContainedRouter(UiRouter.createSubRouter(context)));
    }

    protected abstract Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory();

    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return containedRouter
                .route(StartBookingRouting.uiRoute())
                .route(FeesRooting.uiRoute())
                .route(TermsRooting.uiRoute())
                .route(ProgramRooting.uiRoute())
                .route(OptionsRooting.uiRoute())
                .route(PersonRooting.uiRoute())
                .route(SummaryRooting.uiRoute())
                .route(CartRooting.uiRoute())
                .route(PaymentRooting.uiRoute())
                ;
    }

    @Override
    public void onStart() {
        context.getUiRouter().start();
    }

    public static void launchApplication(SharedMongooseApplication mongooseApplication, String[] args) {
        Platform.bus(); // instantiating the platform bus here to open the connection as soon as possible (ex: before loading the model which is time consuming)
        MongooseActions.registerActions();
        ActivityManager.launchApplication(mongooseApplication, ViewDomainApplicationContext.createViewDomainApplicationContext(
                DomainModelSnapshotLoader.getDataSourceModel(),
                I18n.create("mongoose/dictionaries/{lang}.json"),
                args));
        SceneUtil.installPrimarySceneFocusOwnerAutoScroll();
    }

    public static void setLoadingSpinnerVisibleConsumer(Consumer<Boolean> consumer) {
        Properties.consumeInUiThread(Properties.combine(ViewApplicationContext.getViewApplicationContext().windowBoundProperty(), PendingBusCall.pendingCallsCountProperty(),
                (windowBound, pendingCallsCount) -> !windowBound || pendingCallsCount > 0)
                , consumer);
    }

}
