package mongoose.activities.backend;

import mongoose.activities.backend.authorizations.AuthorizationsRouting;
import mongoose.activities.backend.book.options.EditableOptionsRouting;
import mongoose.activities.backend.bookings.BookingsRouting;
import mongoose.activities.backend.cloneevent.CloneEventRouting;
import mongoose.activities.backend.events.EventsRouting;
import mongoose.activities.backend.letter.LetterRouting;
import mongoose.activities.backend.letters.LettersRouting;
import mongoose.activities.backend.monitor.MonitorRouting;
import mongoose.activities.backend.operations.OperationsRouting;
import mongoose.activities.backend.organizations.OrganizationsRouting;
import mongoose.activities.bothends.MongooseApplicationSharedByBothEnds;
import mongoose.activities.bothends.auth.LoginRouting;
import mongoose.activities.bothends.auth.UnauthorizedRouting;
import webfx.framework.activity.Activity;
import webfx.framework.activity.base.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRouter;
import webfx.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class BackendMongooseApplication extends MongooseApplicationSharedByBothEnds {

    private static final String DEFAULT_START_PATH = OrganizationsRouting.getPath();

    public BackendMongooseApplication() {
        super(DEFAULT_START_PATH);
    }

    @Override
    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return BackendContainerActivity::new;
    }

    @Override
    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return super.setupContainedRouter(containedRouter
                // Authentication and authorization settings
                .setRedirectAuthHandler(LoginRouting.getPath(), UnauthorizedRouting.getPath())
                .route(LoginRouting.uiRoute()) // The login page to display when authentication is required before viewing the requested page
                .route(UnauthorizedRouting.uiRoute()) // The unauthorized page to display when the user can't view the requested page
                // Actual pages (listed in alphabetic order)
                .route(AuthorizationsRouting.uiRoute())
                .route(BookingsRouting.uiRoute())
                .route(CloneEventRouting.uiRoute())
                .route(EditableOptionsRouting.uiRoute())
                .route(EventsRouting.uiRoute())
                .route(LetterRouting.uiRoute())
                .route(LettersRouting.uiRoute())
                .route(MonitorRouting.uiRoute())
                .route(OperationsRouting.uiRoute())
                .route(OrganizationsRouting.uiRoute())
/* Load testing commented for now (to lighten the application)
                .route(LoadTesterRouting.uiRoute())
                .route(SaveLoadTestRooting.uiRoute())
*/
        );
    }

    public static void main(String[] args) {
        launchApplication(new BackendMongooseApplication(), args);
    }

}
