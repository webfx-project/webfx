package mongoose.activities.shared.application;

import mongoose.actions.MongooseActions;
import mongoose.activities.shared.book.cart.CartViewActivity;
import mongoose.activities.shared.book.cart.payment.PaymentViewActivity;
import mongoose.activities.shared.book.event.fees.FeesPresentationActivity;
import mongoose.activities.shared.book.event.options.OptionsViewActivity;
import mongoose.activities.shared.book.event.person.PersonViewActivity;
import mongoose.activities.shared.book.event.program.ProgramViewActivity;
import mongoose.activities.shared.book.event.summary.SummaryViewActivity;
import mongoose.activities.shared.book.event.terms.TermsPresentationActivity;
import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.commons.util.function.Consumer;
import naga.commons.util.function.Factory;
import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.activity.combinations.viewapplication.ViewApplicationContext;
import naga.framework.activity.combinations.viewdomain.ViewDomainActivityContext;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.activity.combinations.viewdomainapplication.ViewDomainApplicationContext;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.router.UiRouter;
import naga.fx.properties.Properties;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityManager;
import naga.platform.bus.call.PendingBusCall;
import naga.platform.spi.Platform;

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
                .route("/book/event/:eventId/fees", FeesPresentationActivity::new, DomainPresentationActivityContextFinal::new)
                .route("/book/event/:eventId/terms", TermsPresentationActivity::new, DomainPresentationActivityContextFinal::new)
                .route("/book/event/:eventId/program", ProgramViewActivity::new, ViewDomainActivityContextFinal::new)
                .route("/book/event/:eventId/options", OptionsViewActivity::new, ViewDomainActivityContextFinal::new)
                .route("/book/event/:eventId/person", PersonViewActivity::new, ViewDomainActivityContextFinal::new)
                .route("/book/event/:eventId/summary", SummaryViewActivity::new, ViewDomainActivityContextFinal::new)
                .route("/book/cart/:cartUuid", CartViewActivity::new, ViewDomainActivityContextFinal::new)
                .route("/book/cart/:cartUuid/payment", PaymentViewActivity::new, ViewDomainActivityContextFinal::new)
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
    }

    public static void setLoadingSpinnerVisibleConsumer(Consumer<Boolean> consumer) {
        Properties.consumeInUiThread(Properties.combine(ViewApplicationContext.getViewApplicationContext().windowBoundProperty(), PendingBusCall.pendingCallsCountProperty(),
                (windowBound, pendingCallsCount) -> !windowBound || pendingCallsCount > 0)
                , consumer);
    }

}
