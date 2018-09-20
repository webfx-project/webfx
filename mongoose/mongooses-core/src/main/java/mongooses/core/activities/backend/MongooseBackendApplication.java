package mongooses.core.activities.backend;

import mongooses.core.activities.backend.authorizations.AuthorizationsRouting;
import mongooses.core.activities.backend.book.options.EditableOptionsRouting;
import mongooses.core.activities.backend.bookings.BookingsRouting;
import mongooses.core.activities.backend.cloneevent.CloneEventRouting;
import mongooses.core.activities.backend.events.EventsRouting;
import mongooses.core.activities.backend.letter.LetterRouting;
import mongooses.core.activities.backend.letters.LettersRouting;
import mongooses.core.activities.backend.monitor.MonitorRouting;
import mongooses.core.activities.backend.operations.OperationsRouting;
import mongooses.core.activities.backend.organizations.OrganizationsRouting;
import mongooses.core.activities.sharedends.MongooseSharedEndsApplication;
import mongooses.core.activities.sharedends.auth.LoginRouting;
import mongooses.core.activities.sharedends.auth.UnauthorizedRouting;
import webfx.framework.activity.Activity;
import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRouter;
import webfx.platforms.core.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendApplication extends MongooseSharedEndsApplication {

    private static final String DEFAULT_START_PATH = OrganizationsRouting.getPath();

    public MongooseBackendApplication() {
        super(DEFAULT_START_PATH);
    }

    @Override
    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return MongooseBackendContainerActivity::new;
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

}
