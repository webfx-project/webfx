package dev.webfx.platform.shared.services.buscall.spi;

import dev.webfx.platform.shared.util.async.Future;

import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public class FunctionBusCallEndpoint<A, R> extends BusCallEndPointBase<A, R> {

    public FunctionBusCallEndpoint(String address, Function<A, R> function) {
        super(address, arg -> Future.succeededFuture(function.apply(arg)));
    }
}
