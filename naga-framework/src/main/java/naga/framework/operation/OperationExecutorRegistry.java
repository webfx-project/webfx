package naga.framework.operation;

import naga.util.async.AsyncFunction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class OperationExecutorRegistry {

    private static OperationExecutorRegistry INSTANCE;

    public static OperationExecutorRegistry getInstance() {
        if (INSTANCE == null)
            INSTANCE = new OperationExecutorRegistry();
        return INSTANCE;
    }

    private final Map<Object, AsyncFunction> operationExecutors = new HashMap<>();
    
    public <O> void registerOperationExecutor(Class<O> operationRequestClass, AsyncFunction<O, ?> operationExecutor) {
        operationExecutors.put(operationRequestClass, operationExecutor);
    }
    
    public void registerOperationExecutor(Object operationCode, AsyncFunction operationExecutor) {
        operationExecutors.put(operationCode, operationExecutor);
    }
    
    public <O, R> AsyncFunction<O, R> getOperationExecutorFromClass(Class<O> operationRequestClass) {
        return (AsyncFunction<O, R>) operationExecutors.get(operationRequestClass);
    }

    public <O, R> AsyncFunction<O, R> getOperationExecutorFromCode(Object operationCode) {
        return (AsyncFunction<O, R>) operationExecutors.get(operationCode);
    }

    public <O, R> AsyncFunction<O, R> getOperationExecutorFromRequest(O operationRequest) {
        AsyncFunction<O, R> operationExecutor = getOperationExecutorFromClass((Class<O>) operationRequest.getClass());
        if (operationExecutor == null && operationRequest instanceof HasOperationCode)
            operationExecutor = getOperationExecutorFromCode(((HasOperationCode) operationRequest).getOperationCode());
        return operationExecutor;
    }

}
