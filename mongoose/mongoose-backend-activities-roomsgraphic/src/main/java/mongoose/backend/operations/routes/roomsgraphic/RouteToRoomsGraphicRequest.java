package mongoose.backend.operations.routes.roomsgraphic;

import mongoose.backend.activities.roomsgraphic.routing.RoomsGraphicRouting;
import webfx.framework.client.operations.route.RoutePushRequest;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToRoomsGraphicRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToRoomsGraphic";

    public RouteToRoomsGraphicRequest(Object eventId, BrowsingHistory history) {
        super(RoomsGraphicRouting.getEventPath(eventId), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

}
