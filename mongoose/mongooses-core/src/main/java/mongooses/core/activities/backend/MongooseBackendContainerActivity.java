package mongooses.core.activities.backend;

import mongooses.core.activities.sharedends.MongooseSharedEndsContainerActivity;
import mongooses.core.operations.backend.route.*;
import mongooses.core.operations.bothends.route.RouteToBookingsRequest;
import webfx.framework.ui.action.Action;
import webfx.platforms.core.util.collection.Collections;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
final class MongooseBackendContainerActivity extends MongooseSharedEndsContainerActivity {

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
                //, newAction(() -> new RouteToTesterRequest(getHistory())) // Not used at the moment
                , newAction(() -> new RouteToOperationsRequest(getHistory()))
                , newAction(() -> new RouteToAuthorizationsRequest(getHistory()))
                , englishAction
                , frenchAction
        );
    }
}
