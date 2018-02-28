package mongoose.activities.backend.event.bookings;

import mongoose.activities.shared.book.event.fees.FeesRooting;
import mongoose.services.EventService;
import naga.framework.operation.HasOperationExecutor;
import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class NewBackendBookingRoutingRequest implements HasOperationExecutor<NewBackendBookingRoutingRequest, Void> {

    private Object eventId;
    private History history;

    public NewBackendBookingRoutingRequest(Object eventId, History history) {
        this.eventId = eventId;
        this.history = history;
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
            EventService eventService = EventService.get(request.getEventId());
            if (eventService != null)
                eventService.setCurrentCart(null);
            FeesRooting.routeUsingEventId(request.getEventId(), request.getHistory());
            return null;
        };
    }
}
