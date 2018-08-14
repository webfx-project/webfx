package naga.framework.operation.action;

import javafx.event.ActionEvent;
import naga.framework.operation.ChainedOperationExecutor;
import naga.framework.operation.OperationExecutorUtil;
import naga.framework.ui.action.impl.WritableAction;
import naga.util.async.AsyncFunction;
import naga.util.function.Factory;
import naga.util.function.Function;

/**
 * @author Bruno Salmon
 */
public class OperationAction<Rq, Rs> extends WritableAction {

    private final Function<ActionEvent, Rq> operationRequestFactory;
    private OperationActionRegistry operationActionRegistry;

    public OperationAction(Factory<Rq> operationRequestFactory, AsyncFunction<Rq, Rs> topOperationExecutor) {
        this(actionEvent -> operationRequestFactory.create(), topOperationExecutor);
    }

    public OperationAction(Function<ActionEvent, Rq> operationRequestFactory, AsyncFunction<Rq, Rs> topOperationExecutor) {
        super(actionEvent -> {
            Rq operationRequest = operationRequestFactory.apply(actionEvent);
            OperationExecutorUtil.executeOperation(operationRequest, topOperationExecutor);
        });
        this.operationRequestFactory = operationRequestFactory;
        OperationActionRegistry registry = getOperationActionRegistry();
        if (registry == null)
            registry = getOperationActionRegistry(topOperationExecutor);
        if (registry == null)
            registry = OperationActionRegistry.getInstance();
        registry.bindOperationAction(this);
    }

    public OperationActionRegistry getOperationActionRegistry(AsyncFunction<Rq, Rs> operationExecutor) {
        if (operationExecutor instanceof ChainedActionOperationExecutor)
            return ((ChainedActionOperationExecutor) operationExecutor).getRegistry();
        if (operationExecutor instanceof ChainedOperationExecutor)
            return getOperationActionRegistry(((ChainedOperationExecutor) operationExecutor).getNextOperationExecutor());
        return null;
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
