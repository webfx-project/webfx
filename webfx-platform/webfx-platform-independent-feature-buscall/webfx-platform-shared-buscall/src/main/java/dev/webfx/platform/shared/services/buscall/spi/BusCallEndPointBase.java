package dev.webfx.platform.shared.services.buscall.spi;

import dev.webfx.platform.shared.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class BusCallEndPointBase<A, R> implements BusCallEndpoint<A, R> {

    private final String address;
    private final AsyncFunction<A, R> asyncFunction;

    public BusCallEndPointBase(String address, AsyncFunction<A, R> asyncFunction) {
        this.address = address;
        this.asyncFunction = asyncFunction;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public AsyncFunction<A, R> toAsyncFunction() {
        return asyncFunction;
    }
}
