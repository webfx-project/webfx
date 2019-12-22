package webfx.framework.shared.operation;

import webfx.platform.shared.util.async.AsyncFunction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class OperationExecutorRegistry {

    private static OperationExecutorRegistry INSTANCE;

    public static OperationExecutorRegistry getInstance() {
        if (INSTANCE == null)
            INSTANCE = new OperationExecutorRegistry();
        return INSTANCE;
    }

    private final Map<Object, AsyncFunction> operationExecutors = new HashMap<>();
    
    public <Rq> void registerOperationExecutor(Class<Rq> operationRequestClass, AsyncFunction<Rq, ?> operationExecutor) {
        operationExecutors.put(operationRequestClass, operationExecutor);
    }
    
    public void registerOperationExecutor(Object operationCode, AsyncFunction operationExecutor) {
        operationExecutors.put(operationCode, operationExecutor);
    }
    
    public <Rq, Rs> AsyncFunction<Rq, Rs> getOperationExecutorFromClass(Class<Rq> operationRequestClass) {
        return (AsyncFunction<Rq, Rs>) operationExecutors.get(operationRequestClass);
    }

    public <Rq, Rs> AsyncFunction<Rq, Rs> getOperationExecutorFromCode(Object operationCode) {
        return (AsyncFunction<Rq, Rs>) operationExecutors.get(operationCode);
    }

    public <Rq, Rs> AsyncFunction<Rq, Rs> getOperationExecutorFromRequest(Rq operationRequest) {
        AsyncFunction<Rq, Rs> operationExecutor = getOperationExecutorFromClass((Class<Rq>) operationRequest.getClass());
        if (operationExecutor == null && operationRequest instanceof HasOperationCode)
            operationExecutor = getOperationExecutorFromCode(((HasOperationCode) operationRequest).getOperationCode());
        return operationExecutor;
    }

}
