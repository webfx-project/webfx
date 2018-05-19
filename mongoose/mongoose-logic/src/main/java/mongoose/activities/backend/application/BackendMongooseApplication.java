package mongoose.activities.backend.application;

import mongoose.activities.backend.book.event.options.EditableOptionsRouting;
import mongoose.activities.backend.container.BackendContainerViewActivity;
import mongoose.activities.backend.event.bookings.BookingsRouting;
import mongoose.activities.backend.event.clone.CloneEventRouting;
import mongoose.activities.backend.event.letters.LettersRouting;
import mongoose.activities.backend.events.EventsRouting;
import mongoose.activities.backend.letter.LetterRouting;
import mongoose.activities.backend.monitor.MonitorRouting;
import mongoose.activities.backend.organizations.OrganizationsRouting;
import mongoose.activities.backend.tester.TesterRouting;
import mongoose.activities.backend.tester.savetest.SaveTestRooting;
import mongoose.activities.shared.application.SharedMongooseApplication;
import mongoose.activities.shared.auth.LoginRouting;
import mongoose.activities.shared.auth.UnauthorizedRouting;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.Activity;
import naga.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class BackendMongooseApplication extends SharedMongooseApplication {

    public BackendMongooseApplication() {
        super(OrganizationsRouting.getPath());
    }

    @Override
    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return BackendContainerViewActivity::new;
    }

    @Override
    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return super.setupContainedRouter(containedRouter
                // Authentication and authorization settings
                .setRedirectAuthHandler(LoginRouting.getPath(), UnauthorizedRouting.getPath())
                .route(LoginRouting.uiRoute()) // The login page to display when authentication is required before viewing the requested page
                .route(UnauthorizedRouting.uiRoute()) // The unauthorized page to display when the user can't view the requested page
                // Actual pages (listed in alphabetic order)
                .route(BookingsRouting.uiRoute())
                .route(CloneEventRouting.uiRoute())
                .route(EditableOptionsRouting.uiRoute())
                .route(EventsRouting.uiRoute())
                .route(LetterRouting.uiRoute())
                .route(LettersRouting.uiRoute())
                .route(MonitorRouting.uiRoute())
                .route(OrganizationsRouting.uiRoute())
                .route(SaveTestRooting.uiRoute())
                .route(TesterRouting.uiRoute())
        );
    }

    public static void main(String[] args) {
        launchApplication(new BackendMongooseApplication(), args);
    }

}
