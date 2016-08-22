package mongoose.client.shared;

import mongoose.activities.cart.CartActivity;
import mongoose.activities.container.ContainerActivity;
import mongoose.activities.event.bookings.BookingsActivity;
import mongoose.activities.event.letters.LettersActivity;
import mongoose.activities.events.EventsActivity;
import mongoose.activities.monitor.MonitorActivity;
import mongoose.activities.organizations.OrganizationsActivity;
import mongoose.activities.tester.TesterActivity;
import mongoose.activities.tester.testset.TestSetActivity;
import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.activity.client.UiDomainActivityContext;
import naga.framework.activity.client.UiDomainApplicationContext;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityManager;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public abstract class MongooseApplication implements Activity<UiDomainActivityContext> {

    protected UiRouter uiRouter;

    @Override
    public void onCreate(UiDomainActivityContext context) {

        uiRouter = UiRouter.create(context)
                .routeAndMount("/", ContainerActivity::new, UiRouter.createSubRouter(context)
                        .route("/organizations", OrganizationsActivity::new)
                        .route("/events", EventsActivity::new)
                        .route("/organization/:organizationId/events", EventsActivity::new)
                        .route("/event/:eventId/bookings", BookingsActivity::new)
                        .route("/event/:eventId/letters", LettersActivity::new)
                        .route("/cart/:cartUuid", CartActivity::new)
                        .route("/monitor", MonitorActivity::new)
                        .route("/tester", TesterActivity::new)
                        .route("/testSet", TestSetActivity::new)
                );
    }

    @Override
    public void onStart() {
        uiRouter.start();
    }

    protected static void launchApplication(MongooseApplication mongooseApplication, String[] args) {
        Platform.bus(); // instantiating the platform bus here to open the connection as soon as possible (before loading the model which takes time)
        ActivityManager.launchApplication(mongooseApplication, UiDomainApplicationContext.create(DomainModelSnapshotLoader.getDataSourceModel(), args));
    }
}
