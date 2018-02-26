package mongoose.activities.backend.monitor;

import naga.framework.operation.HasOperationExecutor;
import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class MonitorRoutingRequest implements HasOperationExecutor<MonitorRoutingRequest, Void> {

    private History history;

    public MonitorRoutingRequest(History history) {
        this.history = history;
    }

    public History getHistory() {
        return history;
    }

    public MonitorRoutingRequest setHistory(History history) {
        this.history = history;
        return this;
    }

    @Override
    public AsyncFunction<MonitorRoutingRequest, Void> getOperationExecutor() {
        return MonitorRouting.executor();
    }
}
