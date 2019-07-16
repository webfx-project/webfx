package mongoose.backend.operations.routes.statements;

import mongoose.backend.activities.statements.routing.StatementsRouting;
import webfx.framework.client.operations.route.RoutePushRequest;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToStatementsRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToStatements";

    public RouteToStatementsRequest(Object eventId, BrowsingHistory history) {
        super(StatementsRouting.getPaymentsPath(eventId), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

}
