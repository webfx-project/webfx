package mongoose.client.shared;

import mongoose.activities.cart.CartActivity;
import mongoose.activities.container.ContainerActivity;
import mongoose.activities.event.bookings.BookingsActivity;
import mongoose.activities.monitor.MonitorActivity;
import mongoose.activities.organizations.OrganizationsActivity;
import mongoose.activities.tester.TesterActivity;
import mongoose.domainmodel.format.DateFormatter;
import mongoose.domainmodel.format.PriceFormatter;
import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.ui.activity.UiDomainActivityContext;
import naga.framework.ui.activity.UiDomainApplicationContext;
import naga.framework.ui.format.FormatterRegistry;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityManager;
import naga.platform.client.bus.WebSocketBusOptions;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public abstract class MongooseApplication implements Activity<UiDomainActivityContext> {

    protected UiRouter uiRouter;

    @Override
    public void onCreate(UiDomainActivityContext context) {
        context.setDataSourceModel(DomainModelSnapshotLoader.getDataSourceModel());
        FormatterRegistry.registerFormatter("price", PriceFormatter.SINGLETON);
        FormatterRegistry.registerFormatter("date", DateFormatter.SINGLETON);

        uiRouter = UiRouter.create(context)
                .routeAndMount("/", ContainerActivity::new, UiRouter.createSubRouter(context)
                        .route("/organizations", OrganizationsActivity::new)
                        .route("/event/:eventId/bookings", BookingsActivity::new)
                        .route("/cart/:cartUuid", CartActivity::new)
                        .route("/monitor", MonitorActivity::new)
                        .route("/tester", TesterActivity::new)
                );
    }

    @Override
    public void onStart() {
        uiRouter.start();
    }

    protected static void launchApplication(MongooseApplication mongooseApplication, String[] args) {
        Platform.setBusOptions(new WebSocketBusOptions().setServerHost("kadampabookings.org").setServerPort("9090"));
        //ActivityManager.launchApplication(mongooseApplication, args);
        ActivityManager.runActivity(mongooseApplication, UiDomainApplicationContext.create(args));
    }
}
