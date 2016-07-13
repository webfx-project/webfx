package mongoose.client.shared;

import mongoose.activities.cart.CartActivity;
import mongoose.activities.container.ContainerActivity;
import mongoose.activities.event.bookings.BookingsActivity;
import mongoose.activities.organizations.OrganizationsActivity;
import mongoose.activities.tester.TesterActivity;
import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.activity.client.UiDomainActivityContext;
import naga.framework.activity.client.UiDomainApplicationContext;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityManager;

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
                        .route("/event/:eventId/bookings", BookingsActivity::new)
                        .route("/cart/:cartUuid", CartActivity::new)
                        .route("/monitor", TesterActivity::new)
                );
    }

    @Override
    public void onStart() {
        uiRouter.start();
    }

    protected static void launchApplication(MongooseApplication mongooseApplication, String[] args) {
        ActivityManager.launchApplication(mongooseApplication, UiDomainApplicationContext.create(DomainModelSnapshotLoader.getDataSourceModel(), args));
    }
}
