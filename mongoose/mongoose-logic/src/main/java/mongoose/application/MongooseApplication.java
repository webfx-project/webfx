package mongoose.application;

import mongoose.activities.cart.CartActivity;
import mongoose.activities.container.ContainerActivity;
import mongoose.activities.event.bookings.BookingsActivity;
import mongoose.activities.monitor.MonitorActivity;
import mongoose.activities.organizations.OrganizationsActivity;
import mongoose.domainmodel.DomainModelSnapshotLoader;
import mongoose.format.DateFormatter;
import mongoose.format.PriceFormatter;
import naga.core.activity.Activity;
import naga.core.activity.ActivityContext;
import naga.core.activity.ActivityManager;
import naga.core.activity.ActivityRouter;
import naga.core.bus.client.WebSocketBusOptions;
import naga.core.format.FormatterRegistry;
import naga.core.spi.platform.Platform;

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
                        .route("/monitor", MonitorActivity::new)
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
