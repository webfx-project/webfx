package mongoose.activities.backend.application;

import mongoose.activities.backend.book.event.options.EditableOptionsViewActivity;
import mongoose.activities.backend.container.BackendContainerViewActivity;
import mongoose.activities.backend.event.bookings.BookingsPresentationActivity;
import mongoose.activities.backend.event.clone.CloneEventPresentationActivity;
import mongoose.activities.backend.event.letters.LettersPresentationActivity;
import mongoose.activities.backend.events.EventsPresentationActivity;
import mongoose.activities.backend.monitor.MonitorPresentationActivity;
import mongoose.activities.backend.organizations.OrganizationsPresentationActivity;
import mongoose.activities.backend.tester.TesterPresentationActivity;
import mongoose.activities.backend.tester.savetest.SaveTestPresentationActivity;
import mongoose.activities.shared.application.SharedMongooseApplication;
import naga.commons.util.function.Factory;
import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.Activity;

/**
 * @author Bruno Salmon
 */
public class BackendMongooseApplication extends SharedMongooseApplication {

    @Override
    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return BackendContainerViewActivity::new;
    }

    @Override
    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return super.setupContainedRouter(containedRouter
                .route("/book/event/:eventId/options", EditableOptionsViewActivity::new, ViewDomainActivityContextFinal::new)
                .route("/organizations", OrganizationsPresentationActivity::new, DomainPresentationActivityContextFinal::new)
                .route("/events", EventsPresentationActivity::new, DomainPresentationActivityContextFinal::new)
                .route("/organization/:organizationId/events", EventsPresentationActivity::new, DomainPresentationActivityContextFinal::new)
                .route("/event/:eventId/bookings", BookingsPresentationActivity::new, DomainPresentationActivityContextFinal::new)
                .route("/event/:eventId/letters", LettersPresentationActivity::new, DomainPresentationActivityContextFinal::new)
                .route("/event/:eventId/clone", CloneEventPresentationActivity::new, DomainPresentationActivityContextFinal::new)
                .route("/monitor", MonitorPresentationActivity::new, DomainPresentationActivityContextFinal::new)
                .route("/tester", TesterPresentationActivity::new, DomainPresentationActivityContextFinal::new)
                .route("/saveTest", SaveTestPresentationActivity::new, DomainPresentationActivityContextFinal::new)
        );
    }

    @Override
    public void onStart() {
        context.getUiRouter().setDefaultInitialHistoryPath("/organizations");
        super.onStart();
    }

    public static void main(String[] args) {
        launchApplication(new BackendMongooseApplication(), args);
    }

}
