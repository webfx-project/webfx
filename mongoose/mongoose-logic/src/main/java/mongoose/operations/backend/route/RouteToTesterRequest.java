package mongoose.operations.backend.route;

import mongoose.activities.backend.tester.TesterRouting;
import naga.framework.operation.HasOperationCode;
import naga.framework.operations.route.RoutePushRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToTesterRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "TESTER_ROUTING";

    public RouteToTesterRequest(History history) {
        super(TesterRouting.getPath(), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
