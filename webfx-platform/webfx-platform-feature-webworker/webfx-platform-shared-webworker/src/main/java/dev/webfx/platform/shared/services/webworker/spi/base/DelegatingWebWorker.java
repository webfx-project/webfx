package dev.webfx.platform.shared.services.webworker.spi.base;

import dev.webfx.platform.shared.services.webworker.WebWorker;

import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class DelegatingWebWorker implements WebWorker {

    private WebWorker delegate;

    public DelegatingWebWorker() {
    }

    public DelegatingWebWorker(WebWorker delegate) {
        setDelegate(delegate);
    }

    public void setDelegate(WebWorker delegate) {
        this.delegate = delegate;
    }

    @Override
    public void postMessage(Object msg) {
        delegate.postMessage(msg);
    }

    @Override
    public void setOnMessageHandler(Consumer<Object> onMessageHandler) {
        delegate.setOnMessageHandler(onMessageHandler);
    }

    @Override
    public void terminate() {
        delegate.terminate();
    }
}
