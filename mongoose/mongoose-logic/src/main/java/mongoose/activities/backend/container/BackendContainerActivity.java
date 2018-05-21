package mongoose.activities.backend.container;

import mongoose.activities.backend.bookings.RouteToBookingsRequest;
import mongoose.activities.backend.events.RouteToEventsRequest;
import mongoose.activities.backend.letters.RouteToLettersRequest;
import mongoose.activities.backend.monitor.RouteToMonitorRequest;
import mongoose.activities.backend.organizations.RouteToOrganizationsRequest;
import mongoose.activities.backend.tester.RouteToTesterRequest;
import mongoose.activities.backend.users.RouteToUsersRequest;
import mongoose.activities.shared.container.SharedContainerActivity;
import naga.framework.ui.action.Action;
import naga.util.collection.Collections;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class BackendContainerActivity extends SharedContainerActivity {

    @Override
    protected Collection<Action> navigationActions() {
        super.navigationActions(); // Ignoring result but this call is required to instantiate parent actions
        return Collections.listOf(
                  backAction
                , forwardAction
                , newAction(() -> new RouteToOrganizationsRequest(getHistory()))
                , newAction(() -> new RouteToEventsRequest(getHistory()))
                , newAction(() -> new RouteToBookingsRequest(getParameter("eventId"), getHistory()))
                , newAction(() -> new RouteToLettersRequest(getParameter("eventId"), getHistory()))
                , newAction(() -> new RouteToMonitorRequest(getHistory()))
                , newAction(() -> new RouteToTesterRequest(getHistory()))
                , newAction(() -> new RouteToUsersRequest(getHistory()))
                , englishAction
                , frenchAction
        );
    }
}
