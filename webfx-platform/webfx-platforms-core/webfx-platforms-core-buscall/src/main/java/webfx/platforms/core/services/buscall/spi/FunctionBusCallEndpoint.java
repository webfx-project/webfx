package webfx.platforms.core.services.buscall.spi;

import webfx.platforms.core.util.async.Future;
import webfx.platforms.core.util.function.Function;

/**
 * @author Bruno Salmon
 */
public class FunctionBusCallEndpoint<A, R> extends BusCallEndPointBase<A, R> {

    public FunctionBusCallEndpoint(String address, Function<A, R> function) {
        super(address, arg -> Future.succeededFuture(function.apply(arg)));
    }
}
