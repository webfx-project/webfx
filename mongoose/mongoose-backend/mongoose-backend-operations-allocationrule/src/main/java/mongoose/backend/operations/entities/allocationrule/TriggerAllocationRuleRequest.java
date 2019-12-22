package mongoose.backend.operations.entities.allocationrule;

import javafx.scene.layout.Pane;
import mongoose.backend.operations.entities.generic.SetEntityFieldRequest;
import webfx.framework.shared.orm.entity.Entity;

public final class TriggerAllocationRuleRequest extends SetEntityFieldRequest {

    private final static String OPERATION_CODE = "TriggerAllocationRule";

    public TriggerAllocationRuleRequest(Entity allocationRule, Pane parentContainer) {
        super(allocationRule, "triggerAllocate", "true", null, parentContainer);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
