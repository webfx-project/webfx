package mongoose.activities.backend.application;

import mongoose.activities.backend.book.event.options.EditableOptionsRouting;
import mongoose.activities.backend.container.BackendContainerViewActivity;
import mongoose.activities.backend.event.bookings.BookingsRouting;
import mongoose.activities.backend.event.clone.CloneEventRouting;
import mongoose.activities.backend.event.letters.LettersRouting;
import mongoose.activities.backend.events.EventsRouting;
import mongoose.activities.backend.letter.edit.EditLetterRouting;
import mongoose.activities.backend.monitor.MonitorRouting;
import mongoose.activities.backend.monitor.MonitorRoutingRequest;
import mongoose.activities.backend.organizations.OrganizationsRouting;
import mongoose.activities.backend.tester.TesterRouting;
import mongoose.activities.backend.tester.TesterRoutingRequest;
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
        super(OrganizationsRouting.PATH);
    }

    @Override
    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return BackendContainerViewActivity::new;
    }

    @Override
    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return super.setupContainedRouter(containedRouter
                .setRedirectAuthHandler(LoginRouting.getPath(), UnauthorizedRouting.getPath())
                .route(LoginRouting.uiRoute())
                .route(UnauthorizedRouting.uiRoute())
                .route(EditableOptionsRouting.uiRoute())
                .route(OrganizationsRouting.uiRoute())
                .route(EventsRouting.uiRoute())
                .route(BookingsRouting.uiRoute())
                .route(LettersRouting.uiRoute())
                .route(CloneEventRouting.uiRoute())
                .route(EditLetterRouting.uiRoute())
                .route(MonitorRouting.uiRoute())
                .route(TesterRouting.uiRoute())
                .route(SaveTestRooting.uiRoute())
        );
    }

    @Override
    protected void registerActions() {
        super.registerActions();
        getOperationActionRegistry()
                //.registerOperationAction(OrganizationsRoutingRequest.class,     newAction("Organizations"))
                //.registerOperationAction(EventsRoutingRequest.class,            newAction("Events"))
                //.registerOperationAction(BookingsRoutingRequest.class,          newAction("Bookings"))
                //.registerOperationAction(LettersRoutingRequest.class,           newAction("Letters"))
                .registerOperationAction(MonitorRoutingRequest.class,           newAuthAction("Monitor", authorizedOperationProperty(MonitorRouting.routingRequest())))
                .registerOperationAction(TesterRoutingRequest.class,            newAuthAction("Tester", authorizedOperationProperty(TesterRouting.routingRequest())))
                //.registerOperationAction(CloneEventRoutingRequest.class,        newAction("CloneEvent"))
                //.registerOperationAction(NewBackendBookingRoutingRequest.class, newAction("NewBooking"))
        ;
    }

    public static void main(String[] args) {
        launchApplication(new BackendMongooseApplication(), args);
    }

}
