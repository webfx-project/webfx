package mongooses.core.operations.backend.route;

import mongooses.core.activities.backend.cloneevent.CloneEventRouting;
import webfx.framework.operation.HasOperationCode;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToCloneEventRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToCloneEvent";

    public RouteToCloneEventRequest(Object eventId, BrowsingHistory history) {
        super(CloneEventRouting.getCloneEventPath(eventId), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
