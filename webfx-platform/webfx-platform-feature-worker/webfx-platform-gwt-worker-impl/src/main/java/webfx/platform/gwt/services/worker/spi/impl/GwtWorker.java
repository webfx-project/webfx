package webfx.platform.gwt.services.worker.spi.impl;

import webfx.platform.shared.services.worker.Worker;

import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class GwtWorker implements Worker {

    private final elemental2.dom.Worker jsWorker;

    public GwtWorker(String scriptUrl) {
        //Blob blob = new Blob(JsArray.from("self.importScripts('http://localhost:63342/webfx/webfx-demos-mandelbrot-application-gwt/target/webfx-demos-mandelbrot-application-gwt-0.1.0-SNAPSHOT/webfx_demos_mandelbrot_application_gwt/Worker.js');self.onmessage = function(e) { var embedData = e.data; console.log('Calling " + jsFunctionName + "(' + e.data + ')'); console.log('Result = ' + " + jsFunctionName + "(e.data)); postMessage('Done'); }"), BlobPropertyBag.create());
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
