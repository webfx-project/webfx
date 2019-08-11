package mongoose.backend.operations.entities.allocationrule;

import javafx.scene.layout.Pane;
import mongoose.shared.entities.Event;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.shared.operation.HasOperationExecutor;
import webfx.platform.shared.util.async.AsyncFunction;

public final class AddNewAllocationRuleRequest implements HasOperationCode,
        HasOperationExecutor<AddNewAllocationRuleRequest, Void> {

    private final static String OPERATION_CODE = "AddNewAllocationRule";

    private final Event event;
    private final Pane parentContainer;

    public AddNewAllocationRuleRequest(Event event, Pane parentContainer) {
        this.event = event;
        this.parentContainer = parentContainer;
    }

    Event getEvent() {
        return event;
    }

    Pane getParentContainer() {
        return parentContainer;
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    @Override
    public AsyncFunction<AddNewAllocationRuleRequest, Void> getOperationExecutor() {
        return AddNewAllocationRuleExecutor::executeRequest;
    }
}
