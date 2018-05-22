package mongoose.activities.backend.container;

import mongoose.operations.bothends.route.RouteToBookingsRequest;
import mongoose.operations.backend.route.RouteToEventsRequest;
import mongoose.operations.backend.route.RouteToLettersRequest;
import mongoose.operations.backend.route.RouteToMonitorRequest;
import mongoose.operations.backend.route.RouteToOrganizationsRequest;
import mongoose.operations.backend.route.RouteToTesterRequest;
import mongoose.operations.backend.route.RouteToOperationsRequest;
import mongoose.activities.bothends.container.SharedContainerActivity;
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
                , newAction(() -> new RouteToOperationsRequest(getHistory()))
                , englishAction
                , frenchAction
        );
    }
}
