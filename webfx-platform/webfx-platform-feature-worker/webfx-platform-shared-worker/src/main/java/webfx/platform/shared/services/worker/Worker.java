package webfx.platform.shared.services.worker;

import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public interface Worker {

    void postMessage(Object msg);

    void setOnMessageHandler(Consumer<Object> onMessageHandler);

    void terminate();

}
