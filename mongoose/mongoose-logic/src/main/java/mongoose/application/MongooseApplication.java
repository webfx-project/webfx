package mongoose.application;

import mongoose.activities.cart.CartActivity;
import mongoose.activities.container.ContainerActivity;
import mongoose.activities.event.bookings.BookingsActivity;
import mongoose.activities.tester.TesterActivity;
import mongoose.activities.organizations.OrganizationsActivity;
import mongoose.domainmodel.DomainModelSnapshotLoader;
import mongoose.format.DateFormatter;
import mongoose.format.PriceFormatter;
import naga.commons.activity.Activity;
import naga.commons.activity.ActivityContext;
import naga.commons.activity.ActivityManager;
import naga.framework.ui.router.ActivityRouter;
import naga.commons.bus.websocket.WebSocketBusOptions;
import naga.framework.ui.format.FormatterRegistry;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
abstract class MongooseApplication implements Activity {

    protected ActivityRouter activityRouter;

    @Override
    public void onCreate(ActivityContext context) {
        context.setDataSourceModel(DomainModelSnapshotLoader.getDataSourceModel());
        FormatterRegistry.registerFormatter("price", PriceFormatter.SINGLETON);
        FormatterRegistry.registerFormatter("date", DateFormatter.SINGLETON);

        activityRouter = ActivityRouter.create(context)
                .routeAndMountSubRouter("/", ContainerActivity::new, ActivityRouter.createSubRouter(context)
                        .route("/organizations", OrganizationsActivity::new)
                        .route("/event/:eventId/bookings", BookingsActivity::new)
                        .route("/cart/:cartUuid", CartActivity::new)
                        .route("/monitor", TesterActivity::new)
                );
    }

    @Override
    public void onStart() {
        activityRouter.start();
    }

    protected static void launchApplication(MongooseApplication mongooseApplication, String[] args) {
        Platform.setBusOptions(new WebSocketBusOptions().setServerHost("kadampabookings.org").setServerPort("9090"));
        ActivityManager.launchApplication(mongooseApplication, args);
    }
}
