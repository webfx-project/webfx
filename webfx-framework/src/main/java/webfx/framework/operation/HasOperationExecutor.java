package webfx.framework.operation;

import webfx.platforms.core.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public interface HasOperationExecutor<Rq, Rs> {

    AsyncFunction<Rq, Rs> getOperationExecutor();

}
