package naga.framework.operation;

import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public interface HasOperationExecutor<O, R> {

    AsyncFunction<O, R> getOperationExecutor();

}
