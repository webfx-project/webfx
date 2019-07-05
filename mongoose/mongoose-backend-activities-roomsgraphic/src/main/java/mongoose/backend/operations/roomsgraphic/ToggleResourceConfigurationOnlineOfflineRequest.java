package mongoose.backend.operations.roomsgraphic;

import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.shared.operation.HasOperationExecutor;
import webfx.framework.shared.orm.entity.Entity;
import webfx.platform.shared.services.update.UpdateResult;
import webfx.platform.shared.util.async.AsyncFunction;
import webfx.platform.shared.util.async.Batch;

public final class ToggleResourceConfigurationOnlineOfflineRequest implements HasOperationCode,
        HasOperationExecutor<ToggleResourceConfigurationOnlineOfflineRequest, Batch<UpdateResult>> {

    private final static String OPERATION_CODE = "ToggleResourceConfigurationOnlineOffline";

    private final Entity resourceConfiguration;

    public ToggleResourceConfigurationOnlineOfflineRequest(Entity resourceConfiguration) {
        this.resourceConfiguration = resourceConfiguration;
    }

    Entity getResourceConfiguration() {
        return resourceConfiguration;
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    @Override
    public AsyncFunction<ToggleResourceConfigurationOnlineOfflineRequest, Batch<UpdateResult>> getOperationExecutor() {
        return ToggleResourceConfigurationOnlineOfflineExecutor::executeRequest;
    }
}
