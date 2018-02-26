package mongoose.activities.backend.events;

import naga.framework.operation.HasOperationExecutor;
import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class EventsRoutingRequest implements HasOperationExecutor<EventsRoutingRequest, Void> {

    private History history;

    public EventsRoutingRequest(History history) {
        this.history = history;
    }

    public History getHistory() {
        return history;
    }

    public EventsRoutingRequest setHistory(History history) {
        this.history = history;
        return this;
    }

    @Override
    public AsyncFunction<EventsRoutingRequest, Void> getOperationExecutor() {
        return EventsRouting.executor();
    }
}
