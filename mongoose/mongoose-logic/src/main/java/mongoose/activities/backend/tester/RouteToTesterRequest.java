package mongoose.activities.backend.tester;

import naga.framework.operation.HasOperationCode;
import naga.framework.ui.router.PushRouteRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToTesterRequest extends PushRouteRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "TESTER_ROUTING";

    public RouteToTesterRequest(History history) {
        super(TesterRouting.getPath(), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
