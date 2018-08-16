package mongoose.activities.backend;

import mongoose.activities.bothends.ContainerActivitySharedByBothEnds;
import mongoose.operations.backend.route.*;
import mongoose.operations.bothends.route.RouteToBookingsRequest;
import naga.framework.ui.action.Action;
import naga.util.collection.Collections;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
class BackendContainerActivity extends ContainerActivitySharedByBothEnds {

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
                , newAction(() -> new RouteToAuthorizationsRequest(getHistory()))
                , englishAction
                , frenchAction
        );
    }
}
