package webfx.platform.shared.services.worker.spi.abstrimpl;

import webfx.platform.shared.services.worker.Worker;

import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public abstract class AbstractWorker implements Worker {

    private Consumer<Object> onMessageHandler;

    public Consumer<Object> getOnMessageHandler() {
        return onMessageHandler;
    }

    @Override
    public void setOnMessageHandler(Consumer<Object> onMessageHandler) {
        this.onMessageHandler = onMessageHandler;
    }
}
