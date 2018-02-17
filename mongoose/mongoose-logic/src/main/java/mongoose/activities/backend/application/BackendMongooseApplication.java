package mongoose.activities.backend.application;

import mongoose.activities.backend.book.event.options.EditableOptionsRouting;
import mongoose.activities.backend.container.BackendContainerViewActivity;
import mongoose.activities.backend.event.bookings.BookingsRouting;
import mongoose.activities.backend.event.clone.CloneEventRouting;
import mongoose.activities.backend.event.letters.LettersRouting;
import mongoose.activities.backend.events.EventsRouting;
import mongoose.activities.backend.letter.edit.EditLetterRouting;
import mongoose.activities.backend.monitor.MonitorRooting;
import mongoose.activities.backend.organizations.OrganizationsRouting;
import mongoose.activities.backend.tester.TesterRooting;
import mongoose.activities.backend.tester.savetest.SaveTestRooting;
import mongoose.activities.shared.application.SharedMongooseApplication;
import mongoose.activities.shared.auth.LoginRouting;
import mongoose.activities.shared.auth.UnauthorizedRouting;
import mongoose.auth.authn.MongooseAuthenticationService;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.Activity;
import naga.util.function.Factory;

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
                .setRedirectAuthHandler(new MongooseAuthenticationService(context.getDataSourceModel()), LoginRouting.getPath(), UnauthorizedRouting.getPath())
                .route(LoginRouting.uiRoute())
                .route(UnauthorizedRouting.uiRoute())
                .route(EditableOptionsRouting.uiRoute())
                .route(OrganizationsRouting.uiRoute())
                .route(EventsRouting.uiRoute())
                .route(BookingsRouting.uiRoute())
                .route(LettersRouting.uiRoute())
                .route(CloneEventRouting.uiRoute())
                .route(EditLetterRouting.uiRoute())
                .route(MonitorRooting.uiRoute())
                .route(TesterRooting.uiRoute())
                .route(SaveTestRooting.uiRoute())
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
