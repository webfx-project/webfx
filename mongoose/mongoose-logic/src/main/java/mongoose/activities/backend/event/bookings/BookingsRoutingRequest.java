package mongoose.activities.backend.event.bookings;

import naga.framework.operation.HasOperationExecutor;
import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class BookingsRoutingRequest implements HasOperationExecutor<BookingsRoutingRequest, Void> {

    private Object eventId;
    private History history;

    public BookingsRoutingRequest(Object eventId, History history) {
        this.eventId = eventId;
        this.history = history;
    }

    public Object getEventId() {
        return eventId;
    }

    public BookingsRoutingRequest setEventId(Object eventId) {
        this.eventId = eventId;
        return this;
    }

    public History getHistory() {
        return history;
    }

    public BookingsRoutingRequest setHistory(History history) {
        this.history = history;
        return this;
    }

    @Override
    public AsyncFunction<BookingsRoutingRequest, Void> getOperationExecutor() {
        return BookingsRouting.executor();
    }
}
