package mongoose.activities.backend.event.bookings;

import mongoose.activities.shared.book.event.fees.FeesRoutingRequest;
import mongoose.services.EventService;
import naga.framework.operation.HasOperationCode;
import naga.framework.operation.HasOperationExecutor;
import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class NewBackendBookingRoutingRequest
        implements HasOperationExecutor<NewBackendBookingRoutingRequest, Void>
        , HasOperationCode {

    private final static String OPERATION_CODE = "NEW_BACKEND_BOOKING";

    private Object eventId;
    private History history;

    public NewBackendBookingRoutingRequest(Object eventId, History history) {
        this.eventId = eventId;
        this.history = history;
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    public Object getEventId() {
        return eventId;
    }

    public NewBackendBookingRoutingRequest setEventId(Object eventId) {
        this.eventId = eventId;
        return this;
    }

    public History getHistory() {
        return history;
    }

    public NewBackendBookingRoutingRequest setHistory(History history) {
        this.history = history;
        return this;
    }

    @Override
    public AsyncFunction<NewBackendBookingRoutingRequest, Void> getOperationExecutor() {
        return executor();
    }

    public static AsyncFunction<NewBackendBookingRoutingRequest, Void> executor() {
        return request -> {
            // When made in the backend, we don't want to add the new booking to the last visited booking cart (as
            // opposed to the frontend), so we clear the reference to the current booking cart (if set) before routing
            EventService eventService = EventService.get(request.getEventId());
            if (eventService != null)
                eventService.setCurrentCart(null);
            // Now that the current cart reference is cleared, we can route to the fees page
            new FeesRoutingRequest(request.getEventId(), request.getHistory()).execute();
            return null;
        };
    }
}
