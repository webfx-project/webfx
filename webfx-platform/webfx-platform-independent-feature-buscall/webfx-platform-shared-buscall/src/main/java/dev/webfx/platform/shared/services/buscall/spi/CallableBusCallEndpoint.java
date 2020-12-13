package dev.webfx.platform.shared.services.buscall.spi;

import dev.webfx.platform.shared.util.function.Callable;

/**
 * @author Bruno Salmon
 */
public final class CallableBusCallEndpoint<R> extends FunctionBusCallEndpoint<Object, R> {

    public CallableBusCallEndpoint(String address, Callable<R> callable) {
        super(address, ignored -> callable.call());
    }
}
