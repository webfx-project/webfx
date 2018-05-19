package mongoose.activities.backend.events;

import naga.framework.operation.HasOperationCode;
import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class EventsRoutingRequest extends PushRoutingRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "EVENTS_ROUTING";

    public EventsRoutingRequest(History history) {
        super(EventsRouting.getAllEventsPath(), history);
    }

    public EventsRoutingRequest(Object organizationId, History history) {
        super(EventsRouting.getOrganizationEventsPath(organizationId), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
