package webfx.framework.client.operation.action;

import javafx.event.ActionEvent;
import webfx.framework.shared.operation.OperationUtil;
import webfx.framework.client.ui.action.impl.WritableAction;
import webfx.platform.shared.util.async.AsyncFunction;
import webfx.platform.shared.util.function.Factory;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public final class OperationAction<Rq, Rs> extends WritableAction {

    private final Function<ActionEvent, Rq> operationRequestFactory;
    private OperationActionRegistry operationActionRegistry;

    public OperationAction(Factory<Rq> operationRequestFactory, AsyncFunction<Rq, Rs> topOperationExecutor) {
        this(actionEvent -> operationRequestFactory.create(), topOperationExecutor);
    }

    public OperationAction(Function<ActionEvent, Rq> operationRequestFactory, AsyncFunction<Rq, Rs> topOperationExecutor) {
        super(actionEvent -> {
            Rq operationRequest = operationRequestFactory.apply(actionEvent);
            OperationUtil.executeOperation(operationRequest, topOperationExecutor);
        });
        this.operationRequestFactory = operationRequestFactory;
        OperationActionRegistry registry = getOperationActionRegistry();
        if (registry == null)
            registry = OperationActionRegistry.getInstance();
        registry.bindOperationAction(this);
    }

    public OperationActionRegistry getOperationActionRegistry() {
        return operationActionRegistry;
    }

    public void setOperationActionRegistry(OperationActionRegistry operationActionRegistry) {
        this.operationActionRegistry = operationActionRegistry;
    }

    public Function<ActionEvent, Rq> getOperationRequestFactory() {
        return operationRequestFactory;
    }
}
