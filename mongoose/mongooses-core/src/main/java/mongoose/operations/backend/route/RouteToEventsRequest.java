package mongoose.operations.backend.route;

import mongoose.activities.backend.events.EventsRouting;
import webfx.framework.operation.HasOperationCode;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToEventsRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToEvents";

    public RouteToEventsRequest(History history) {
        super(EventsRouting.getAllEventsPath(), history);
    }

    public RouteToEventsRequest(Object organizationId, History history) {
        super(EventsRouting.getOrganizationEventsPath(organizationId), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
