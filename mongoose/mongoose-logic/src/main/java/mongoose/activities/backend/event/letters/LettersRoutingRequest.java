package mongoose.activities.backend.event.letters;

import mongoose.activities.backend.event.bookings.BookingsRouting;
import naga.framework.operation.HasOperationExecutor;
import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class LettersRoutingRequest implements HasOperationExecutor<LettersRoutingRequest, Void> {

    private Object eventId;
    private History history;

    public LettersRoutingRequest(Object eventId, History history) {
        this.eventId = eventId;
        this.history = history;
    }

    public Object getEventId() {
        return eventId;
    }

    public LettersRoutingRequest setEventId(Object eventId) {
        this.eventId = eventId;
        return this;
    }

    public History getHistory() {
        return history;
    }

    public LettersRoutingRequest setHistory(History history) {
        this.history = history;
        return this;
    }

    @Override
    public AsyncFunction<LettersRoutingRequest, Void> getOperationExecutor() {
        return LettersRouting.executor();
    }
}
