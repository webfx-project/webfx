package mongoose.activities.backend.events;

import naga.framework.operation.HasOperationCode;
import naga.framework.ui.router.PushRouteRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToEventsRequest extends PushRouteRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "EVENTS_ROUTING";

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
