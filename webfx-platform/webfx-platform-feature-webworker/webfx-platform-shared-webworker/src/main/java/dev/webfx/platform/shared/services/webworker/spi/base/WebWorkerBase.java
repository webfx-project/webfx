package dev.webfx.platform.shared.services.webworker.spi.base;

import dev.webfx.platform.shared.services.webworker.WebWorker;

import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public abstract class WebWorkerBase implements WebWorker {

    private Consumer<Object> onMessageHandler;

    public Consumer<Object> getOnMessageHandler() {
        return onMessageHandler;
    }

    @Override
    public void setOnMessageHandler(Consumer<Object> onMessageHandler) {
        this.onMessageHandler = onMessageHandler;
    }
}
