package mongoose.backend.operations.entities.allocationrule;

import javafx.scene.layout.Pane;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.shared.operation.HasOperationExecutor;
import webfx.framework.shared.orm.entity.Entity;
import webfx.platform.shared.util.async.AsyncFunction;

public final class EditAllocationRuleRequest implements HasOperationCode,
        HasOperationExecutor<EditAllocationRuleRequest, Void> {

    private final static String OPERATION_CODE = "EditAllocationRule";

    private final Entity allocationRule;
    private final Pane parentContainer;

    public EditAllocationRuleRequest(Entity allocationRule, Pane parentContainer) {
        this.allocationRule = allocationRule;
        this.parentContainer = parentContainer;
    }

    Entity getAllocationRule() {
        return allocationRule;
    }

    Pane getParentContainer() {
        return parentContainer;
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    @Override
    public AsyncFunction<EditAllocationRuleRequest, Void> getOperationExecutor() {
        return EditAllocationRuleExecutor::executeRequest;
    }
}
