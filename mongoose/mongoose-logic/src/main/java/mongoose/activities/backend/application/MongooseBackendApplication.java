package mongoose.activities.backend.application;

import mongoose.activities.backend.container.BackendContainerActivity;
import mongoose.activities.backend.event.bookings.BookingsActivity;
import mongoose.activities.backend.event.clone.CloneEventPresentationActivity;
import mongoose.activities.backend.event.letters.LettersActivity;
import mongoose.activities.backend.events.EventsActivity;
import mongoose.activities.backend.events.EventsPresentationActivity;
import mongoose.activities.backend.monitor.MonitorActivity;
import mongoose.activities.backend.organizations.OrganizationsPresentationActivity;
import mongoose.activities.backend.tester.TesterActivity;
import mongoose.activities.backend.tester.testset.TestSetActivity;
import mongoose.activities.shared.application.MongooseApplication;
import naga.commons.util.function.Factory;
import naga.framework.activity.view.ViewDomainActivityContextFinal;
import naga.framework.activity.view.presentation.DomainPresentationActivityContextFinal;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.Activity;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendApplication extends MongooseApplication {

    @Override
    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return BackendContainerActivity::new;
    }

    @Override
    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return super.setupContainedRouter(containedRouter)
                .route("/organizations", OrganizationsPresentationActivity::new, DomainPresentationActivityContextFinal::new)
                .route("/events", EventsActivity::new)
                .route("/organization/:organizationId/events", EventsPresentationActivity::new, DomainPresentationActivityContextFinal::new)
                .route("/event/:eventId/bookings", BookingsActivity::new)
                .route("/event/:eventId/letters", LettersActivity::new)
                .route("/event/:eventId/clone", CloneEventPresentationActivity::new, DomainPresentationActivityContextFinal::new)
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
