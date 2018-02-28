package mongoose.activities.backend.event.clone;

import naga.framework.operation.HasOperationExecutor;
import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class CloneEventRoutingRequest implements HasOperationExecutor<CloneEventRoutingRequest, Void> {

    private Object eventId;
    private History history;

    public CloneEventRoutingRequest(Object eventId, History history) {
        this.eventId = eventId;
        this.history = history;
    }

    public Object getEventId() {
        return eventId;
    }

    public CloneEventRoutingRequest setEventId(Object eventId) {
        this.eventId = eventId;
        return this;
    }

    public History getHistory() {
        return history;
    }

    public CloneEventRoutingRequest setHistory(History history) {
        this.history = history;
        return this;
    }

    @Override
    public AsyncFunction<CloneEventRoutingRequest, Void> getOperationExecutor() {
        return CloneEventRouting.executor();
    }
}
