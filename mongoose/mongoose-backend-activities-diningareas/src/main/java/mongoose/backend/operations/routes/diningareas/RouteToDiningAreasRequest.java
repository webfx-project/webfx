package mongoose.backend.operations.routes.diningareas;

import mongoose.backend.activities.diningareas.routing.DiningAreasRouting;
import webfx.framework.client.operations.route.RoutePushRequest;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToDiningAreasRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToDiningAreas";

    public RouteToDiningAreasRequest(Object eventId, BrowsingHistory history) {
        super(DiningAreasRouting.getEventPath(eventId), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

}
