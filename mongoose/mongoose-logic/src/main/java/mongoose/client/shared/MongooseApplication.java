package mongoose.client.shared;

import mongoose.activities.cart.CartActivity;
import mongoose.activities.container.ContainerActivity;
import mongoose.activities.event.bookings.BookingsActivity;
import mongoose.activities.event.letters.LettersActivity;
import mongoose.activities.events.EventsActivity;
import mongoose.activities.organizations.OrganizationsActivity;
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

    protected UiDomainActivityContext context;

    @Override
    public void onCreate(UiDomainActivityContext context) {
        this.context = context;
        context.getUiRouter().routeAndMount("/", ContainerActivity::new, setupContainedRouter(UiRouter.createSubRouter(context)));
    }

    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return containedRouter
            .route("/organizations", OrganizationsActivity::new)
            .route("/events", EventsActivity::new)
            .route("/organization/:organizationId/events", EventsActivity::new)
            .route("/event/:eventId/bookings", BookingsActivity::new)
            .route("/event/:eventId/letters", LettersActivity::new)
            .route("/cart/:cartUuid", CartActivity::new);
    }

    @Override
    public void onStart() {
        context.getUiRouter().start();
    }

    protected static void launchApplication(MongooseApplication mongooseApplication, String[] args) {
        Platform.bus(); // instantiating the platform bus here to open the connection as soon as possible (before loading the model which takes time)
        ActivityManager.launchApplication(mongooseApplication, UiDomainApplicationContext.create(DomainModelSnapshotLoader.getDataSourceModel(), args));
    }
}
