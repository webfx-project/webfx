package mongoose.backend.operations.routes.users;

import mongoose.backend.activities.users.routing.UsersRouting;
import webfx.framework.client.operations.route.RoutePushRequest;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToUsersRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToUsers";

    public RouteToUsersRequest(BrowsingHistory history) {
        super(UsersRouting.getPath(), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

}
