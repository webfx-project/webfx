package webfx.platforms.core.services.buscall.spi;

import webfx.platforms.core.util.function.Callable;

/**
 * @author Bruno Salmon
 */
public final class CallableBusCallEndpoint<R> extends FunctionBusCallEndpoint<Object, R> {

    public CallableBusCallEndpoint(String address, Callable<R> callable) {
        super(address, ignored -> callable.call());
    }
}
