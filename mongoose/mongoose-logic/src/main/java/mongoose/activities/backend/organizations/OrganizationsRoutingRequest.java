package mongoose.activities.backend.organizations;


import naga.framework.operation.HasOperationExecutor;
import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class OrganizationsRoutingRequest implements HasOperationExecutor<OrganizationsRoutingRequest, Void> {

    private History history;

    public OrganizationsRoutingRequest(History history) {
        this.history = history;
    }

    public History getHistory() {
        return history;
    }

    public OrganizationsRoutingRequest setHistory(History history) {
        this.history = history;
        return this;
    }

    @Override
    public AsyncFunction<OrganizationsRoutingRequest, Void> getOperationExecutor() {
        return OrganizationsRouting.executor();
    }
}
