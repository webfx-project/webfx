package dev.webfx.platform.shared.services.webworker;

import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public interface WebWorker {

    void postMessage(Object msg);

    void setOnMessageHandler(Consumer<Object> onMessageHandler);

    void terminate();

}
