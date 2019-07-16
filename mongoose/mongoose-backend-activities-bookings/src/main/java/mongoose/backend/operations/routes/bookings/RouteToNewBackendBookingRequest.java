package mongoose.backend.operations.routes.bookings;

import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.client.operations.route.RouteRequestBase;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;
import webfx.platform.shared.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public final class RouteToNewBackendBookingRequest extends RouteRequestBase<RouteToNewBackendBookingRequest>
        implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToNewBackendBooking";

    private Object eventId;

    public RouteToNewBackendBookingRequest(Object eventId, BrowsingHistory history) {
        super(history);
        this.eventId = eventId;
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    public Object getEventId() {
        return eventId;
    }

    public RouteToNewBackendBookingRequest setEventId(Object eventId) {
        this.eventId = eventId;
        return this;
    }

    @Override
    public AsyncFunction<RouteToNewBackendBookingRequest, Void> getOperationExecutor() {
        return RouteToNewBackendBookingExecutor::executeRequest;
    }

}
