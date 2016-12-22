package mongoose.activities.backend.application;

import mongoose.activities.backend.container.BackendContainerActivity;
import mongoose.activities.backend.event.bookings.BookingsActivity;
import mongoose.activities.backend.event.clone.CloneEventActivity;
import mongoose.activities.backend.event.letters.LettersActivity;
import mongoose.activities.backend.events.EventsActivity;
import mongoose.activities.backend.monitor.MonitorActivity;
import mongoose.activities.backend.organizations.OrganizationsActivity;
import mongoose.activities.backend.tester.TesterActivity;
import mongoose.activities.backend.tester.testset.TestSetActivity;
import mongoose.activities.shared.application.MongooseApplication;
import naga.commons.util.function.Factory;
import naga.framework.activity.client.UiDomainActivityContext;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.Activity;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendApplication extends MongooseApplication {

    @Override
    protected Factory<Activity<UiDomainActivityContext>> getContainerActivityFactory() {
        return BackendContainerActivity::new;
    }

    @Override
    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return super.setupContainedRouter(containedRouter)
                .route("/organizations", OrganizationsActivity::new)
                .route("/events", EventsActivity::new)
                .route("/organization/:organizationId/events", EventsActivity::new)
                .route("/event/:eventId/bookings", BookingsActivity::new)
                .route("/event/:eventId/letters", LettersActivity::new)
                .route("/event/:eventId/clone", CloneEventActivity::new)
                .route("/monitor", MonitorActivity::new)
                .route("/tester", TesterActivity::new)
                .route("/testSet", TestSetActivity::new);
    }

    @Override
    public void onStart() {
        context.getUiRouter().setDefaultInitialHistoryPath("/organizations");
        super.onStart();
    }

    public static void main(String[] args) {
        launchApplication(new MongooseBackendApplication(), args);
    }

}
