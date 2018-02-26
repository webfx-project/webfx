package naga.framework.operation;

import javafx.event.ActionEvent;
import naga.framework.ui.action.impl.BindableAction;
import naga.util.async.AsyncFunction;
import naga.util.function.Factory;
import naga.util.function.Function;

/**
 * @author Bruno Salmon
 */
public class OperationAction<O, R> extends BindableAction {

    private final Function<ActionEvent, O> operationRequestFactory;
    private OperationActionRegistry operationActionRegistry;

    public OperationAction(Factory<O> operationRequestFactory, AsyncFunction<O, R> topOperationExecutor) {
        this(actionEvent -> operationRequestFactory.create(), topOperationExecutor);
    }

    public OperationAction(Function<ActionEvent, O> operationRequestFactory, AsyncFunction<O, R> topOperationExecutor) {
        super(actionEvent -> {
            O operationRequest = operationRequestFactory.apply(actionEvent);
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

    public OperationActionRegistry getOperationActionRegistry(AsyncFunction<O, R> operationExecutor) {
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

    public Function<ActionEvent, O> getOperationRequestFactory() {
        return operationRequestFactory;
    }
}
