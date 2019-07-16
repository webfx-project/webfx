package mongoose.backend.operations.entities.resourceconfiguration;

import javafx.scene.layout.Pane;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.shared.operation.HasOperationExecutor;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.platform.shared.util.async.AsyncFunction;

public final class ChangeResourceConfigurationItemRequest implements HasOperationCode,
        HasOperationExecutor<ChangeResourceConfigurationItemRequest, Void> {

    private final static String OPERATION_CODE = "ChangeResourceConfigurationItem";

    private final Entity resourceConfiguration;
    private final Pane parentContainer;
    private final String itemFamilyCode;
    private final EntityId siteId;

    public ChangeResourceConfigurationItemRequest(Entity resourceConfiguration, Pane parentContainer, String itemFamilyCode, EntityId siteId) {
        this.resourceConfiguration = resourceConfiguration;
        this.parentContainer = parentContainer;
        this.itemFamilyCode = itemFamilyCode;
        this.siteId = siteId;
    }

    Entity getResourceConfiguration() {
        return resourceConfiguration;
    }

    Pane getParentContainer() {
        return parentContainer;
    }

    String getItemFamilyCode() {
        return itemFamilyCode;
    }

    EntityId getSiteId() {
        return siteId;
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    @Override
    public AsyncFunction<ChangeResourceConfigurationItemRequest, Void> getOperationExecutor() {
        return ChangeResourceConfigurationItemExecutor::executeRequest;
    }
}
