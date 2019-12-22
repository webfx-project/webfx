package mongoose.backend.operations.entities.resourceconfiguration;

import javafx.scene.layout.Pane;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.shared.operation.HasOperationExecutor;
import webfx.framework.shared.orm.entity.Entity;
import webfx.platform.shared.util.async.AsyncFunction;

public final class EditResourceConfigurationPropertiesRequest implements HasOperationCode,
        HasOperationExecutor<EditResourceConfigurationPropertiesRequest, Void> {

    private final static String OPERATION_CODE = "EditResourceConfigurationProperties";

    private final Entity resourceConfiguration;
    private final Pane parentContainer;

    public EditResourceConfigurationPropertiesRequest(Entity resourceConfiguration, Pane parentContainer) {
        this.resourceConfiguration = resourceConfiguration;
        this.parentContainer = parentContainer;
    }

    Entity getResourceConfiguration() {
        return resourceConfiguration;
    }

    Pane getParentContainer() {
        return parentContainer;
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    @Override
    public AsyncFunction<EditResourceConfigurationPropertiesRequest, Void> getOperationExecutor() {
        return EditResourceConfigurationPropertiesExecutor::executeRequest;
    }
}
