package mongoose.activities.backend.event.clone;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.operation.HasOperationCode;
import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class CloneEventRoutingRequest extends PushRoutingRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "CLONE_EVENT";

    public CloneEventRoutingRequest(Object eventId, History history) {
        super(MongooseRoutingUtil.interpolateEventIdInPath(eventId, CloneEventRouting.PATH), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
