package webfx.platform.gwt.services.worker.spi.impl;

import webfx.platform.shared.services.worker.Worker;

import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class GwtWorker implements Worker {

    private final elemental2.dom.Worker jsWorker;

    public GwtWorker(String scriptUrl) {
        jsWorker = new elemental2.dom.Worker(scriptUrl);
    }

    @Override
    public void postMessage(Object msg) {
        jsWorker.postMessage(msg);
    }

    @Override
    public void setOnMessageHandler(Consumer<Object> onMessageHandler) {
        jsWorker.onmessage = messageEvent -> onMessageHandler.accept(messageEvent.data);
    }

    @Override
    public void terminate() {
        jsWorker.terminate();
    }
}
