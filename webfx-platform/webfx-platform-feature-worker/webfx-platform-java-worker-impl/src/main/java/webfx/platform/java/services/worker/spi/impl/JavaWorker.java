package webfx.platform.java.services.worker.spi.impl;

import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.services.scheduler.Scheduler;
import webfx.platform.shared.services.worker.spi.abstrimpl.AbstractPlatformWorker;
import webfx.platform.shared.services.worker.spi.abstrimpl.AbstractWorker;
import webfx.platform.shared.services.worker.spi.abstrimpl.JavaApplicationWorker;

/**
 * @author Bruno Salmon
 */
public class JavaWorker extends AbstractWorker { // this instance represents the proxy the application interacts with

    private final JavaApplicationWorker applicationWorker; // This instance represents the actual running worker coded in Java (instantiated through the passed class)
    private final AbstractPlatformWorker javaPlatformWorker = new AbstractPlatformWorker() { // This instance represents the java platform layer the application worker interacts with
        @Override
        public void log(String message) {
            Logger.log(message);
        }

        @Override
        public void log(Object object) {
            Logger.log(object);
        }

        @Override
        public Object toNativeJsonArray(double[] doubleArray) {
            return doubleArray; // DoubleStream.of(doubleArray).boxed().collect(Collectors.toList());
        }

        @Override
        public int getJsonInt(Object nativeObject, String key) {
            return ((JsonObject) nativeObject).getInteger(key);
        }

        @Override
        public double getJsonDouble(Object nativeObject, String key) {
            return ((JsonObject) nativeObject).getDouble(key);
        }

        @Override
        public void postMessage(Object msg) {
            // When the application worker call this method, we need to pass this to the onMessageHandler of the proxy
            UiScheduler.runInUiThread(() -> JavaWorker.this.getOnMessageHandler().accept(msg));
        }

        @Override
        public void terminate() {
        }
    };

    public JavaWorker(String scriptUrl) {
        throw new UnsupportedOperationException();
    }

    public JavaWorker(Class<? extends JavaApplicationWorker> javaCodedWorkerClass) {
        JavaApplicationWorker javaApplicationWorker;
        try {
            javaApplicationWorker = javaCodedWorkerClass.getDeclaredConstructor().newInstance();
            javaApplicationWorker.setPlatformWorker(javaPlatformWorker);
        } catch (Exception e) {
            e.printStackTrace();
            javaApplicationWorker = null;
        }
        this.applicationWorker = javaApplicationWorker;
        javaApplicationWorker.onLoaded();
    }

    @Override
    public void postMessage(Object msg) {
        // When the application calls this method, we need to pass this to the onMessageHandler of the application
        Scheduler.runInBackground(() -> javaPlatformWorker.getOnMessageHandler().accept(msg));
    }

    @Override
    public void terminate() {
        applicationWorker.terminate();
    }
}
