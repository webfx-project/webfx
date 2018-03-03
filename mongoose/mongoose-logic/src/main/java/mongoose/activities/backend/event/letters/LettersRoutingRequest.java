package mongoose.activities.backend.event.letters;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.operation.HasOperationCode;
import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class LettersRoutingRequest extends PushRoutingRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "LETTERS_ROUTING";

    public LettersRoutingRequest(Object eventId, History history) {
        super(MongooseRoutingUtil.interpolateEventIdInPath(eventId, LettersRouting.EVENT_PATH), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
