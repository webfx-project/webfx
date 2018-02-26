package mongoose.activities.backend.tester;

import naga.framework.operation.HasOperationExecutor;
import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class TesterRoutingRequest implements HasOperationExecutor<TesterRoutingRequest, Void> {

    private History history;

    public TesterRoutingRequest(History history) {
        this.history = history;
    }

    public History getHistory() {
        return history;
    }

    public TesterRoutingRequest setHistory(History history) {
        this.history = history;
        return this;
    }

    @Override
    public AsyncFunction<TesterRoutingRequest, Void> getOperationExecutor() {
        return TesterRouting.executor();
    }
}
