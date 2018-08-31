package mongoose.operations.bothends.route;

import mongoose.activities.backend.bookings.BookingsRouting;
import webfx.framework.operation.HasOperationCode;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToBookingsRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToBookings";

    public RouteToBookingsRequest(Object eventId, History history) {
        super(BookingsRouting.getEventBookingsPath(eventId), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
