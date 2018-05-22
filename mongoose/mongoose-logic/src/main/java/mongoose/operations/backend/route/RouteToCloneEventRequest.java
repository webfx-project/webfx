package mongoose.operations.backend.route;

import mongoose.activities.backend.cloneevent.CloneEventRouting;
import naga.framework.operation.HasOperationCode;
import naga.framework.operations.route.RoutePushRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToCloneEventRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "CLONE_EVENT";

    public RouteToCloneEventRequest(Object eventId, History history) {
        super(CloneEventRouting.getCloneEventPath(eventId), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
