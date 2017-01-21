package mongoose.activities.shared.application;

import mongoose.activities.frontend.cart.CartActivity;
import mongoose.activities.frontend.event.fees.FeesActivity;
import mongoose.activities.frontend.event.options.OptionsActivity;
import mongoose.activities.frontend.event.program.ProgramActivity;
import mongoose.activities.frontend.event.terms.TermsActivity;
import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.commons.util.function.Consumer;
import naga.commons.util.function.Factory;
import naga.framework.activity.view.ViewApplicationContext;
import naga.framework.activity.view.ViewDomainActivityContext;
import naga.framework.activity.view.ViewDomainActivityContextFinal;
import naga.framework.activity.view.ViewDomainApplicationContext;
import naga.framework.ui.router.UiRouter;
import naga.fx.properties.Properties;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityManager;
import naga.platform.bus.call.PendingBusCall;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public abstract class MongooseApplication implements Activity<ViewDomainActivityContext> {

    protected ViewDomainActivityContext context;

    @Override
    public void onCreate(ViewDomainActivityContext context) {
        this.context = context;
        context.getUiRouter().routeAndMount("/", getContainerActivityFactory(), setupContainedRouter(UiRouter.createSubRouter(context)));
    }

    protected abstract Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory();

    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return containedRouter
            .route("/event/:eventId/fees",    FeesActivity::new)
            .route("/event/:eventId/terms",   TermsActivity::new)
            .route("/event/:eventId/program", ProgramActivity::new)
            .route("/event/:eventId/options", OptionsActivity::new)
            .route("/cart/:cartUuid", CartActivity::new);
    }

    @Override
    public void onStart() {
        context.getUiRouter().start();
    }

    protected static void launchApplication(MongooseApplication mongooseApplication, String[] args) {
        Platform.bus(); // instantiating the platform bus here to open the connection as soon as possible (ex: before loading the model which is time consuming)
        ActivityManager.launchApplication(mongooseApplication, ViewDomainApplicationContext.create(DomainModelSnapshotLoader.getDataSourceModel(), args));
    }

    public static void setLoadingSpinnerVisibleConsumer(Consumer<Boolean> consumer) {
        Properties.consumeInUiThread(Properties.combine(ViewApplicationContext.getViewApplicationContext().windowBoundProperty(), PendingBusCall.pendingCallsCountProperty(),
                (windowBound, pendingCallsCount) -> !windowBound || pendingCallsCount > 0)
                , consumer);
    }

}
