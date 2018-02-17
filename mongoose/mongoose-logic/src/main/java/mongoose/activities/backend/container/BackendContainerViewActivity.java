package mongoose.activities.backend.container;

import mongoose.activities.backend.event.bookings.BookingsRouting;
import mongoose.activities.backend.event.letters.LettersRouting;
import mongoose.activities.backend.events.EventsRouting;
import mongoose.activities.backend.monitor.MonitorRooting;
import mongoose.activities.backend.organizations.OrganizationsRouting;
import mongoose.activities.backend.tester.TesterRooting;
import mongoose.activities.shared.container.SharedContainerViewActivity;
import naga.framework.ui.action.Action;
import naga.util.collection.Collections;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class BackendContainerViewActivity extends SharedContainerViewActivity {

    @Override
    protected Collection<Action> navigationActions() {
        super.navigationActions();
        return Collections.listOf(
                  backAction
                , forwardAction
                , newAction("Organizations",  () -> OrganizationsRouting.route(getHistory()))
                , newAction("Events",         () -> EventsRouting.route(getHistory()))
                , newAction("Bookings",       () -> BookingsRouting.routeUsingEventId(getParameter("eventId"), getHistory()))
                , newAction("Letters",        () -> LettersRouting.routeUsingEventId(getParameter("eventId"), getHistory()))
                , newAuthAction("Monitor",    () -> MonitorRooting.route(getHistory()), authorizedProperty(MonitorRooting.authorizationRequest()))
                , newAuthAction("Tester",     () -> TesterRooting.route(getHistory()), authorizedProperty(TesterRooting.authorizationRequest()))
                , englishAction
                , frenchAction
        );
    }
}
