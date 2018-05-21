package mongoose.operations.backend.route;

import mongoose.activities.backend.letters.LettersRouting;
import naga.framework.operation.HasOperationCode;
import naga.framework.operations.route.PushRouteRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToLettersRequest extends PushRouteRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "LETTERS_ROUTING";

    public RouteToLettersRequest(Object eventId, History history) {
        super(LettersRouting.getEventLettersPath(eventId), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
